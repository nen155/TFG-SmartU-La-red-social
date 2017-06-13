package com.smartu.adaptadores;

import android.content.Context;

import android.content.Intent;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Publicacion;
import com.smartu.hebras.HBuenaIdea;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Comparador;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentProyectos;
import com.smartu.vistas.LoginActivity;
import com.smartu.vistas.UsuarioActivity;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Collections;

import java8.util.stream.StreamSupport;


public class AdapterProyecto extends RecyclerView.Adapter<AdapterProyecto.ViewHolder> implements OperacionesAdapter {
    private Context context;
    private ArrayList<Proyecto> proyectos;
    private FragmentProyectos.OnProyectoSelectedListener onProyectoSelectedListener;
    private Proyecto proyecto;
    private Usuario usuarioSesion;

    //Es el número total de elementos que hay en el server
    //tengo que recogerlo de las hebras de consulta
    private int totalElementosServer = -1;

    // Tres tipos de vistas para saber si es un ProgressBar lo que muestro o la vista normal
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;
    public static final int VIEW_TYPE_FINAL = 2;

    public void setTotalElementosServer(int totalElementosServer) {
        this.totalElementosServer = totalElementosServer;
    }

    public AdapterProyecto(Context context, ArrayList<Proyecto> items, FragmentProyectos.OnProyectoSelectedListener onProyectoSelectedListener) {
        super();
        this.context = context;
        this.proyectos = items;
        this.onProyectoSelectedListener = onProyectoSelectedListener;
        Collections.sort(proyectos, new Comparador.ComparaProyectos());
        usuarioSesion = Sesion.getUsuario(context);
    }


    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int tipoView;
        TextView nombreProyecto;
        TextView descripcionProyecto;
        TextView contadorBuenaIdea;
        ImageView imgProyecto;
        ImageView imgBuenaIdea;
        Button nombreUsuario;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW_TYPE_ACTIVITY) {
                nombreProyecto = (TextView) itemView.findViewById(R.id.nombre_usuario);
                descripcionProyecto = (TextView) itemView.findViewById(R.id.descripcion_proyecto);
                contadorBuenaIdea = (TextView) itemView.findViewById(R.id.buenaidea_contador_proyecto);
                imgProyecto = (ImageView) itemView.findViewById(R.id.img_proyecto);
                nombreUsuario = (Button) itemView.findViewById(R.id.nombre_usuario_proyecto);
                imgBuenaIdea = (ImageView) itemView.findViewById(R.id.img_idea_proyecto);
                imgBuenaIdea.setTag(R.drawable.idea);
                tipoView = 1;
            } else {
                tipoView = 0;
            }
        }

    }

    @Override
    public AdapterProyecto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress, parent, false);
            ViewHolder vhBottom = new ViewHolder(v, viewType);

            return vhBottom;
        } else if (viewType == VIEW_TYPE_FINAL) {
            // the ListView has reached the last row
            TextView tvLastRow = new TextView(context);
            tvLastRow.setHint("");
            tvLastRow.setGravity(Gravity.CENTER);
            ViewHolder vhUltimo = new ViewHolder(tvLastRow, viewType);
            return vhUltimo;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto_recyclerview, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType);

            return vhItem;
        }
    }

    @Override
    public void onBindViewHolder(AdapterProyecto.ViewHolder holder, int position) {
        //Sino es el último elemento ni es un progress bar pues muestro el elemento que me toca
        if (holder.tipoView == 1) {
            proyecto = (Proyecto) this.proyectos.get(position);
            Picasso.with(context).load(ConsultasBBDD.server + ConsultasBBDD.imagenes + proyecto.getImagenDestacada()).into(holder.imgProyecto);
            holder.nombreProyecto.setText(proyecto.getNombre());
            if (proyecto.getDescripcion().length() > 150) {
                holder.descripcionProyecto.setText(proyecto.getDescripcion().substring(0, 150) + "...");
            } else
                holder.descripcionProyecto.setText(proyecto.getDescripcion());
            if (proyecto.getIdPropietario() != 0)
                holder.nombreUsuario.setText(proyecto.getPropietarioUser());

            holder.contadorBuenaIdea.setText(String.valueOf(proyecto.getBuenaIdea().size()));

            //TODO Para cuando cargue usuarios
            //holder.nombreUsuario.setTexto(proyecto.getUsuario());

            holder.imgProyecto.setOnClickListener(cargaProyecto(proyecto.getId()));
            holder.descripcionProyecto.setOnClickListener(cargaProyecto(proyecto.getId()));
            holder.nombreProyecto.setOnClickListener(cargaProyecto(proyecto.getId()));

            //Cargo las preferencias del usuario si tuviese sesión
            cargarPreferenciasUsuario(holder.imgBuenaIdea,proyectos.get(position));

            holder.imgBuenaIdea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HBuenaIdea hBuenaIdea;
                    //Si el usuario ha iniciado sesión
                    if (usuarioSesion != null) {
                        //Actualizo el botón
                        Integer integer = (Integer) holder.imgBuenaIdea.getTag();
                        if (R.drawable.buenaidea == integer) {
                            holder.imgBuenaIdea.setImageResource(R.drawable.idea);
                            holder.imgBuenaIdea.setTag(R.drawable.idea);
                        } else {
                            holder.imgBuenaIdea.setImageResource(R.drawable.buenaidea);
                            holder.imgBuenaIdea.setTag(R.drawable.buenaidea);
                        }
                        //Compruebo como ha quedado su estado después de hacer click
                        if ((Integer) holder.imgBuenaIdea.getTag() == R.drawable.buenaidea) {
                            Toast.makeText(context, "Genial!, este proyecto te parece buena idea!", Toast.LENGTH_SHORT).show();
                            //Inicializo la hebra con false pues voy a añadir una nueva idea
                            hBuenaIdea = new HBuenaIdea(false, context, proyectos.get(position), holder.imgBuenaIdea, holder.contadorBuenaIdea);
                            //Para poder poner la referencia a null cuando termine la hebra
                            hBuenaIdea.sethBuenaIdea(hBuenaIdea);
                        } else {
                            Toast.makeText(context, "¿Ya no te parece buena idea?", Toast.LENGTH_SHORT).show();
                            //Inicializo la hebra con true para eliminar la buena idea de la BD.
                            hBuenaIdea = new HBuenaIdea(true, context, proyectos.get(position), holder.imgBuenaIdea, holder.contadorBuenaIdea);
                            //Para poder poner la referencia a null cuando termine la hebra
                            hBuenaIdea.sethBuenaIdea(hBuenaIdea);
                        }
                        hBuenaIdea.execute();
                    } else {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                }
            });

            holder.nombreUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UsuarioActivity.class);
                    intent.putExtra("idUsuario", proyectos.get(position).getIdPropietario());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? proyectos.get(position).getId()
                : -1;
    }

    /**
     * Devuelve el tipo de fila,
     * El ultimo elemento es el de loading
     */
    @Override
    public int getItemViewType(int position) {
        if (position >= proyectos.size() && position >= totalElementosServer && totalElementosServer > 0) {
            return VIEW_TYPE_FINAL;
        } else if (position >= proyectos.size()) {
            return VIEW_TYPE_FINAL;
        } else
            return VIEW_TYPE_ACTIVITY;
    }

    @Override
    public int getItemCount() {
        if(proyectos.size()==0)
            return proyectos.size();
        return proyectos.size() + 1;
    }


    private View.OnClickListener cargaProyecto(int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProyectoSelectedListener.onProyectoSeleccionado(id);
            }
        };
    }

    /**
     * Comprueba si el usuario ha dado buena idea al proyecto
     */
    private void cargarPreferenciasUsuario(ImageView imgBuenaIdea,Proyecto proyecto) {
        //Cargo las preferencias del usuario
        if (usuarioSesion != null && proyecto.getBuenaIdea() != null) {
            //Compruebo si el usuario le ha dado antes a buena idea a este proyecto
            boolean usuarioBuenaidea = false;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                usuarioBuenaidea = proyecto.getBuenaIdea().parallelStream().anyMatch(buenaIdea -> buenaIdea.getIdUsuario() == usuarioSesion.getId());
            else
                usuarioBuenaidea = StreamSupport.stream(proyecto.getBuenaIdea()).filter(buenaIdea -> buenaIdea.getIdUsuario() == usuarioSesion.getId()).findAny().isPresent();
            //Si es así lo dejo presionado y le cambio la imagen
            if (usuarioBuenaidea) {
                imgBuenaIdea.setImageResource(R.drawable.buenaidea);
                imgBuenaIdea.setTag(R.drawable.buenaidea);
            }else {
                imgBuenaIdea.setImageResource(R.drawable.idea);
                imgBuenaIdea.setTag(R.drawable.idea);
            }
        }
    }

    @Override
    public void addItem(Publicacion publicacion) {
        Proyecto proyecto = (Proyecto) publicacion;
        boolean esta = StreamSupport.stream(proyectos).filter(usuario1 -> usuario1.getId() == proyecto.getId()).findAny().isPresent();
        if (!esta) {
            proyectos.add(proyecto);
            Almacen.add(proyecto);
            notifyItemInserted(proyectos.size() - 1);
        }
    }

}