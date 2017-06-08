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
import com.smartu.contratos.OperacionesAdapter;
import com.smartu.contratos.Publicacion;
import com.smartu.hebras.HSeguir;
import com.smartu.hebras.HSolicitud;
import com.smartu.modelos.Especialidad;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentIntegrantes;
import com.smartu.vistas.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java8.util.stream.StreamSupport;


public class AdapterIntegrante extends RecyclerView.Adapter<AdapterIntegrante.ViewHolder> implements OperacionesAdapter {
    private Context context;
    private ArrayList<Usuario> usuarios;
    private FragmentIntegrantes.OnIntegranteSelectedListener onIntegranteSelectedListener;
    private Usuario usuario;
    private Usuario usuarioSesion;
    private Button seguirUsuarioEditable;
    private TextView seguidoresUsuarioEditable;
    private Proyecto proyecto;
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

    public AdapterIntegrante(Context context, ArrayList<Usuario> items, FragmentIntegrantes.OnIntegranteSelectedListener onIntegranteSelectedListener, Proyecto proyecto) {
        super();
        this.context = context;
        this.usuarios = items;
        this.onIntegranteSelectedListener = onIntegranteSelectedListener;
        this.proyecto = proyecto;
        //Cargo la sesió del usuario si es que hubiese, para usarla en los demás metodos
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
    public AdapterIntegrante.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_integrante_recyclerview, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType);

            return vhItem;
        }
    }

    @Override
    public void onBindViewHolder(final AdapterIntegrante.ViewHolder holder, int position) {
        //Sino es el último elemento ni es un progress bar pues muestro el elemento que me toca
        if (holder.tipoView == 1) {
            usuario = (Usuario) this.usuarios.get(position);

            //Compruebo que no sean los usuarios que he metido como vacantes
            //para dejar la imagen por defecto.
            if (usuario.getId() != -1)
                Picasso.with(context).load(ConsultasBBDD.server + ConsultasBBDD.imagenes + usuario.getImagenPerfil()).into(holder.imgUsuario);

            holder.nombreUsuario.setText(usuario.getNombre());

            if (usuario.getMisEspecialidades() != null) {
                int numEspecialidades = 3;
                //Pongo de previsualización 3 especialidades como mucho
                if (usuario.getMisEspecialidades().size() < 3)
                    numEspecialidades = usuario.getMisEspecialidades().size();
                //Añado las especialidades al TagCloud
                for (int i = 0; i < numEspecialidades; ++i) {
                    Especialidad e = usuario.getMisEspecialidades().get(i);
                    holder.especialidadUsuario.add(new Tag(e.getId(), e.getNombre()));
                }
            }

            //Guardo una referencia para usarlo dentro de los métodos externos a la clase
            seguirUsuarioEditable = holder.seguirUsuario;
            seguidoresUsuarioEditable = holder.seguidoresUsuario;

            //Si es un usuario del proyecto
            if (usuario.getId() != -1) {
                if (usuario.getMiStatus() != null)
                    holder.seguidoresUsuario.setText(String.valueOf(usuario.getMiStatus().getNumSeguidores()));
                holder.seguirUsuario.setText(context.getString(R.string.seguir));
            } else {//Si es una vacante
                holder.seguidoresUsuario.setText("");
                holder.seguirUsuario.setText(R.string.unirse);
            }
            if (usuario.getMiStatus() != null)
                holder.statusUsuario.setText(usuario.getMiStatus().getNombre());

            //Compruebo que no sean los usuarios que he metido como vacantes
            if (usuario.getId() != -1) {
                holder.imgUsuario.setOnClickListener(cargaUsuario(usuarios.get(position).getId()));
                holder.statusUsuario.setOnClickListener(cargaUsuario(usuarios.get(position).getId()));
                holder.nombreUsuario.setOnClickListener(cargaUsuario(usuarios.get(position).getId()));

                //Cargo las preferencias del usuario si tuviese sesión
                cargarPreferenciasUsuario( usuarios.get(position));
                holder.seguirUsuario.setOnClickListener(seguirUsuario(usuarios.get(position),holder.seguirUsuario,holder.seguidoresUsuario));
            } else {
                //Cargo las posibles solicitudes
                cargarSolicitudesUnion();
                //Todos llaman al mismo método que hace una solicitud de unión al proyecto
                holder.imgUsuario.setOnClickListener(solicitarUnion(usuarios.get(position)));
                holder.statusUsuario.setOnClickListener(solicitarUnion(usuarios.get(position)));
                holder.nombreUsuario.setOnClickListener(solicitarUnion(usuarios.get(position)));
                holder.seguirUsuario.setOnClickListener(solicitarUnion(usuarios.get(position)));
            }
        }
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

    /**
     * Si ya ha solicitado anteriormente la unión a este proyecto el usuario actual
     * pone los botones deshabilitados
     */
    private void cargarSolicitudesUnion() {

        boolean soyPropietario =StreamSupport.stream(usuarioSesion.getMisProyectos()).filter(proyect-> proyect == proyecto.getId()).findAny().isPresent();
        if(soyPropietario) {
            seguirUsuarioEditable.setText(R.string.eres_colaborador);
            seguirUsuarioEditable.setPressed(true);
            seguirUsuarioEditable.setEnabled(false);
        }else
        if (usuarioSesion != null && usuarioSesion.getMisSolicitudes() != null) {
            boolean solicitado = false;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                solicitado = usuarioSesion.getMisSolicitudes().parallelStream().anyMatch(solicitudUnion -> solicitudUnion.getIdProyecto() == proyecto.getId());
            else
                solicitado = StreamSupport.stream(usuarioSesion.getMisSolicitudes()).filter(solicitudUnion -> solicitudUnion.getIdProyecto() == proyecto.getId()).findAny().isPresent();
            if (solicitado) {
                seguirUsuarioEditable.setText(R.string.solicitado_unio_proyecto);
                seguirUsuarioEditable.setPressed(true);
                seguirUsuarioEditable.setEnabled(false);
            }else {
                seguirUsuarioEditable.setText(R.string.unirse);
                seguirUsuarioEditable.setPressed(false);
                seguirUsuarioEditable.setEnabled(true);
            }
        }
    }

    /**
     * Comprueba si el usuario ha dado buena idea al proyecto
     */
    private void cargarPreferenciasUsuario(Usuario usuario) {
        //Cargo las preferencias del usuario
        if (usuarioSesion != null && usuarioSesion.getMisSeguidos() != null) {
            //Compruebo si el usuario le ha dado antes a seguir a este usuario
            boolean usuarioSigue = false;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                usuarioSigue = usuarioSesion.getMisSeguidos().stream().anyMatch(usuario1 -> usuario1 == usuario.getId());
            else
                usuarioSigue = StreamSupport.stream(usuarioSesion.getMisSeguidos()).filter(usuario1 -> usuario1 == usuario.getId()).findAny().isPresent();
            //Si es así lo dejo presionado
            seguirUsuarioEditable.setPressed(usuarioSigue);
            if (usuarioSigue)
                seguirUsuarioEditable.setText(R.string.no_seguir);
            else
                seguirUsuarioEditable.setText(R.string.seguir);
        }
    }

    /**
     * Permite seguir al usuario si se ha iniciado sesión
     *
     * @return
     */
    private View.OnClickListener seguirUsuario(Usuario usuario,Button seguirUsuarioEditable,TextView seguidoresUsuarioEditable) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el usuario ha iniciado sesión
                if (usuarioSesion != null) {
                    HSeguir hSeguir;
                    usuarioSesion = Sesion.getUsuario(context);
                    //Compruebo si el usuario le ha dado antes a seguir a este usuario
                    boolean usuarioSigue = false;
                    usuarioSigue = StreamSupport.stream(usuarioSesion.getMisSeguidos()).filter(usuario1 -> usuario1 == usuario.getId()).findAny().isPresent();
                    //Sigo al usuario
                    if (!usuarioSigue) {
                        seguirUsuarioEditable.setText(R.string.no_seguir);
                        //Añado al contador 1 para decir que eres idProyecto
                        int cont = Integer.parseInt(seguidoresUsuarioEditable.getText().toString()) + 1;
                        seguidoresUsuarioEditable.setText(String.valueOf(cont));
                        Toast.makeText(context, "Genial!,sigues a este usuario!", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con 0 pues voy a añadir una nueva idea
                        hSeguir = new HSeguir(false, usuario, context, seguirUsuarioEditable, seguidoresUsuarioEditable);
                        //Para poder poner la referencia a null cuando termine la hebra
                        hSeguir.sethSeguir(hSeguir);
                    } else {//Dejo de seguir al usuario
                        seguirUsuarioEditable.setText(R.string.seguir);
                        //Añado al contador 1 para decir que eres idProyecto
                        int cont = Integer.parseInt(seguidoresUsuarioEditable.getText().toString()) - 1;
                        seguidoresUsuarioEditable.setText(String.valueOf(cont));
                        Toast.makeText(context, "¿Ya no te interesa el usuario?", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con el id de la buena idea que encontré
                        hSeguir = new HSeguir(true, usuario, context, seguirUsuarioEditable, seguidoresUsuarioEditable);
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

    /**
     * Abre la Activity del usuario al que se le ha hecho click
     *
     * @return
     */
    private View.OnClickListener cargaUsuario(int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIntegranteSelectedListener.onUsuarioSeleccionado(id);
            }
        };
    }

    /**
     * Solicita la unión al proyecto si has iniciado sesión en el sistema
     *
     * @return
     */
    private View.OnClickListener solicitarUnion(Usuario usuario) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el usuario ha iniciado sesión
                if (usuarioSesion != null) {
                    HSolicitud hSolicitud;
                    seguirUsuarioEditable.setText(R.string.solicitado_unio_proyecto);
                    seguirUsuarioEditable.setPressed(true);
                    seguirUsuarioEditable.setEnabled(false);
                    hSolicitud = new HSolicitud(false, proyecto, usuarioSesion.getId(), usuario.getMisEspecialidades(), context, seguirUsuarioEditable);
                    hSolicitud.sethSolicitud(hSolicitud);
                    hSolicitud.execute();
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }

            }
        };
    }

    @Override
    public void addItem(Publicacion publicacion) {
        Usuario u = (Usuario) publicacion;
        boolean esta = StreamSupport.stream(usuarios).filter(usuario1 -> usuario1.getId() == u.getId()).findAny().isPresent();
        if (!esta) {
            usuarios.add(u);
            if (u.getId() != -1)
                Almacen.add(u);
            notifyItemInserted(usuarios.size() - 1);
        }
    }

}