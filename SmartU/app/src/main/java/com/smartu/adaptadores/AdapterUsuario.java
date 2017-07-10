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

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;
import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Publicacion;
import com.smartu.hebras.HSeguir;
import com.smartu.modelos.Especialidad;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentUsuarios;
import com.smartu.vistas.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java8.util.stream.StreamSupport;


public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ViewHolder> implements OperacionesAdapter {
    private Context context;
    private ArrayList<Usuario> usuarios;
    private FragmentUsuarios.OnUsuarioSelectedListener onUsuarioSelectedListener;
    private Usuario usuario;
    private HSeguir hSeguir;
    private Usuario usuarioSesion;
    private boolean filtro=false;

    //Es el número total de elementos que hay en el server
    //tengo que recogerlo de las hebras de consulta
    private int totalElementosServer = -1;

    // Tres tipos de vistas para saber si es un ProgressBar lo que muestro o la vista normal
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;
    public static final int VIEW_TYPE_FINAL = 2;


    public void setFiltro(boolean filtro) {
        this.filtro = filtro;
    }

    public int getTotalElementosServer() {
        return totalElementosServer;
    }

    public void setTotalElementosServer(int totalElementosServer) {
        this.totalElementosServer = totalElementosServer;
    }

    public AdapterUsuario(Context context, ArrayList<Usuario> items, FragmentUsuarios.OnUsuarioSelectedListener onUsuarioSelectedListener) {
        super();
        this.context = context;
        this.usuarios = items;
        this.onUsuarioSelectedListener = onUsuarioSelectedListener;
        usuarioSesion = Sesion.getUsuario(context);
    }


    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder {
        int tipoView;
        TextView nombreUsuario;
        TagCloudLinkView especialidadUsuario;
        TextView statusUsuario;
        TextView seguidoresUsuario;
        ImageView imgUsuario;
        Button seguirUsuario;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW_TYPE_ACTIVITY) {
                nombreUsuario = (TextView) itemView.findViewById(R.id.nombre_usuario);
                statusUsuario = (TextView) itemView.findViewById(R.id.status_usuario);
                seguidoresUsuario = (TextView) itemView.findViewById(R.id.seguidores_usuario);
                especialidadUsuario = (TagCloudLinkView) itemView.findViewById(R.id.especialidad_usuario);
                imgUsuario = (ImageView) itemView.findViewById(R.id.img_usuario);
                seguirUsuario = (Button) itemView.findViewById(R.id.seguir_usuario);
                tipoView = 1;
            } else {
                tipoView = 0;
            }
        }

    }

    @Override
    public AdapterUsuario.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_recyclerview, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType);

            return vhItem;
        }
    }

    @Override
    public void onBindViewHolder(final AdapterUsuario.ViewHolder holder, int position) {
        //Sino es el último elemento ni es un progress bar pues muestro el elemento que me toca
        if (holder.tipoView == 1) {
            usuario = (Usuario) this.usuarios.get(position);
            Picasso.with(context).load(ConsultasBBDD.server + ConsultasBBDD.imagenes +  usuarios.get(position).getImagenPerfil()).into(holder.imgUsuario);
            holder.nombreUsuario.setText(usuarios.get(position).getNombre());

            if(usuarios.get(position).getMisEspecialidades()!=null) {
                int numEspecialidades = 3;
                //Pongo de previsualización 3 especialidades como mucho
                if (usuarios.get(position).getMisEspecialidades().size() < 3)
                    numEspecialidades = usuarios.get(position).getMisEspecialidades().size();
                //Añado las especialidades al TagCloud
                for (int i = 0; i < numEspecialidades; ++i) {
                    Especialidad e = usuarios.get(position).getMisEspecialidades().get(i);
                    holder.especialidadUsuario.add(new Tag(e.getId(), e.getNombre()));
                }
            }
            if(usuario.getMiStatus()!=null) {
                holder.seguidoresUsuario.setText(String.valueOf(usuarios.get(position).getMiStatus().getNumSeguidores()));
                holder.statusUsuario.setText(usuarios.get(position).getMiStatus().getNombre());
            }
            holder.imgUsuario.setOnClickListener(cargaUsuario(usuarios.get(position).getId()));
            holder.statusUsuario.setOnClickListener(cargaUsuario(usuarios.get(position).getId()));
            holder.nombreUsuario.setOnClickListener(cargaUsuario(usuarios.get(position).getId()));


            //Cargo las preferencias del usuario si tuviese sesión
            cargarPreferenciasUsuario(holder.seguirUsuario, usuarios.get(position));
            holder.seguirUsuario.setOnClickListener(seguirUsuario(usuarios.get(position),holder.seguirUsuario,holder.seguidoresUsuario,position));
        }
    }

    private View.OnClickListener seguirUsuario(Usuario usuario,Button seguirUsuarioEditable,TextView seguidoresUsuarioEditable,int position){
       return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el usuario ha iniciado sesión
                if (usuarioSesion != null) {
                    //Compruebo si el usuario le ha dado antes a seguir a este usuario
                    boolean usuarioSigue = false;
                    usuarioSesion = Sesion.getUsuario(context);
                    usuarioSigue = StreamSupport.stream(usuarioSesion.getMisSeguidos()).filter(usuario1 -> usuario1 == usuario.getId()).findAny().isPresent();

                    //Compruebo como ha quedado su estado después de hacer click
                    if (!usuarioSigue) {
                        seguirUsuarioEditable.setText(context.getResources().getString(R.string.no_seguir));
                        //Añado al contador 1 para decir que eres seguidor
                        int cont = Integer.parseInt(seguidoresUsuarioEditable.getText().toString()) + 1;
                        seguidoresUsuarioEditable.setText(String.valueOf(cont));
                        Toast.makeText(context, "Genial!,sigues a este usuario!", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con 0 pues voy a añadir una nueva idea
                        hSeguir = new HSeguir(false, usuarios.get(position), context, seguirUsuarioEditable, seguidoresUsuarioEditable);
                        //Para poder poner la referencia a null cuando termine la hebra
                        hSeguir.sethSeguir(hSeguir);
                    } else {
                        seguirUsuarioEditable.setText(context.getResources().getString(R.string.seguir));
                        //Añado al contador 1 para decir que eres seguidor
                        int cont = Integer.parseInt(seguidoresUsuarioEditable.getText().toString()) - 1;
                        seguidoresUsuarioEditable.setText(String.valueOf(cont));
                        Toast.makeText(context, "¿Ya no te interesa el usuario?", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con el id de la buena idea que encontré
                        hSeguir = new HSeguir(true, usuarios.get(position), context, seguirUsuarioEditable, seguidoresUsuarioEditable);
                        //Para poder poner la referencia a null cuando termine la hebra
                        hSeguir.sethSeguir(hSeguir);
                    }
                    hSeguir.execute();
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            }
        };
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? usuarios.get(position).getId()
                : -1;
    }

    /**
     * Devuelve el tipo de fila,
     * El ultimo elemento es el de loading
     */
    @Override
    public int getItemViewType(int position) {
        if (position >= usuarios.size() && position >= totalElementosServer && totalElementosServer > 0) {
            return VIEW_TYPE_FINAL;
        } else if (position >= usuarios.size()) {
            return VIEW_TYPE_FINAL;
        } else
            return VIEW_TYPE_ACTIVITY;
    }

    @Override
    public int getItemCount() {
        if(usuarios.size()==0)
            return usuarios.size();
        return usuarios.size() + 1;
    }


    private View.OnClickListener cargaUsuario(int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUsuarioSelectedListener.onUsuarioSeleccionado(id);
            }
        };
    }

    /**
     * Comprueba si el usuario ha dado buena idea al proyecto
     */
    private void cargarPreferenciasUsuario(Button seguirUsuario,Usuario usuario) {
        //Cargo las preferencias del usuario
        if (usuarioSesion != null && usuarioSesion.getMisSeguidos() != null) {
            //Compruebo si el usuario le ha dado antes a seguir a este usuario
            boolean usuarioSigue = false;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                usuarioSigue = usuarioSesion.getMisSeguidos().parallelStream().anyMatch(usuario1 -> usuario1 == usuario.getId());
            else
                usuarioSigue = StreamSupport.stream(usuarioSesion.getMisSeguidos()).filter(usuario1 -> usuario1 == usuario.getId()).findAny().isPresent();
            //Si es así lo dejo presionado
            seguirUsuario.setPressed(usuarioSigue);
            if (usuarioSigue)
                seguirUsuario.setText(R.string.no_seguir);
            else
                seguirUsuario.setText(R.string.seguir);
        }
    }

    @Override
    public void addItem(Publicacion publicacion) {
        Usuario usuario = (Usuario) publicacion;
        boolean esta = StreamSupport.stream(usuarios).filter(usuario1 -> usuario1.getId() == usuario.getId()).findAny().isPresent();
        boolean add=false;
        if (!esta)
        {
            Almacen.add(usuario);

            //Añado las notificaciones filtradas igualmente pero no notifico al adapter
            //Si tengo que mostrarla porque es perteneciente al filtro
            if (Almacen.filtra(usuario, Sesion.getUsuario(context))) {
                //Compruebo sino la he añadido antes
                esta = Almacen.isFiltradaPresent(usuario);
                //Sino la he añadido la añado al ArrayList
                if (!esta) {
                    Almacen.addFiltro(usuario);
                    add=true;
                }
            }
            //Compruebo si tengo que filtrar
            if(filtro) {
                //Si estoy en al adpater con filtro y he añadido una
                //lo notifico
                if(add)
                    notifyItemInserted(usuarios.size()-1);
            }else {
                usuarios.add(usuario);
                notifyItemInserted(usuarios.size() - 1);
            }

        }
    }

}