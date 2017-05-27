package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.smartu.hebras.HSeguir;
import com.smartu.hebras.HSolicitud;
import com.smartu.modelos.Especialidad;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentIntegrantes;
import com.smartu.vistas.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterIntegrante extends RecyclerView.Adapter<AdapterIntegrante.ViewHolder> {
    private Context context;
    private ArrayList<Usuario> usuarios;
    private FragmentIntegrantes.OnIntegranteSelectedListener onIntegranteSelectedListener;
    private Usuario usuario;
    private Usuario usuarioSesion;
    private Button seguirUsuarioEditable;
    private TextView seguidoresUsuarioEditable;
    private int idProyecto;


    public AdapterIntegrante(Context context, ArrayList<Usuario> items, FragmentIntegrantes.OnIntegranteSelectedListener onIntegranteSelectedListener,int idProyecto) {
        super();
        this.context = context;
        this.usuarios = items;
        this.onIntegranteSelectedListener = onIntegranteSelectedListener;
        this.idProyecto = idProyecto;
    }

    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombreUsuario;
        TagCloudLinkView especialidadUsuario;
        TextView statusUsuario;
        TextView seguidoresUsuario;
        ImageView imgUsuario;
        Button seguirUsuario;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            nombreUsuario = (TextView) itemView.findViewById(R.id.nombre_usuario);
            statusUsuario = (TextView) itemView.findViewById(R.id.status_usuario);
            seguidoresUsuario = (TextView) itemView.findViewById(R.id.seguidores_usuario);
            especialidadUsuario = (TagCloudLinkView) itemView.findViewById(R.id.especialidad_usuario);
            imgUsuario = (ImageView) itemView.findViewById(R.id.img_usuario);
            seguirUsuario = (Button) itemView.findViewById(R.id.nombre_usuario_o_proyecto);

        }

    }

    @Override
    public AdapterIntegrante.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_integrante_recyclerview, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final AdapterIntegrante.ViewHolder holder, int position) {
        usuario = (Usuario) this.usuarios.get(position);
        //Cargo la sesió del usuario si es que hubiese, para usarla en los demás metodos
        usuarioSesion = Sesion.getUsuario(context);
        //Compruebo que no sean los usuarios que he metido como vacantes
        //para dejar la imagen por defecto.
        if (usuario.getId() != -1)
            Picasso.with(context).load(ConsultasBBDD.server+usuario.getImagenPerfil()).into(holder.imgUsuario);

        holder.nombreUsuario.setText(usuario.getNombre());
        for (Especialidad e : usuario.getMisEspecialidades()) {
            holder.especialidadUsuario.add(new Tag(e.getId(), e.getNombre()));
        }
        //Guardo una referencia para usarlo dentro de los métodos externos a la clase
        seguirUsuarioEditable = holder.seguirUsuario;
        seguidoresUsuarioEditable = holder.seguidoresUsuario;

        //Si es un usuario del proyecto
        if (usuario.getId() != -1) {
            holder.seguidoresUsuario.setText(String.valueOf(usuario.getMiStatus().getNumSeguidores()));
            holder.seguirUsuario.setText(context.getString(R.string.seguir));
        }
        else {//Si es una vacante
            holder.seguidoresUsuario.setText("");
            holder.seguirUsuario.setText(R.string.unirse);
        }
        holder.statusUsuario.setText(usuario.getMiStatus().getNombre());

        //Compruebo que no sean los usuarios que he metido como vacantes
        if (usuario.getId() != -1) {
            holder.imgUsuario.setOnClickListener(cargaUsuario());
            holder.statusUsuario.setOnClickListener(cargaUsuario());
            holder.nombreUsuario.setOnClickListener(cargaUsuario());

            //Cargo las preferencias del usuario si tuviese sesión
            cargarPreferenciasUsuario();
            holder.seguirUsuario.setOnClickListener(seguirUsuario());
        } else {
            //Cargo las posibles solicitudes
            cargarSolicitudesUnion();
            //Todos llaman al mismo método que hace una solicitud de unión al proyecto
            holder.imgUsuario.setOnClickListener(solicitarUnion());
            holder.statusUsuario.setOnClickListener(solicitarUnion());
            holder.nombreUsuario.setOnClickListener(solicitarUnion());
            holder.seguirUsuario.setOnClickListener(solicitarUnion());
        }
    }

    @Override
    public long getItemId(int position) {
        return ((Usuario) usuarios.get(position)).getId();
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    /**
     * Si ya ha solicitado anteriormente la unión a este proyecto el usuario actual
     * pone los botones deshabilitados
     */
    private void cargarSolicitudesUnion(){
        if (usuarioSesion != null) {
            boolean solicitado = usuarioSesion.getMisSolicitudes().stream().anyMatch(solicitudUnion -> solicitudUnion.getProyecto().getId() == idProyecto);
            if(solicitado){
                seguirUsuarioEditable.setText(R.string.solicitado_unio_proyecto);
                seguidoresUsuarioEditable.setPressed(true);
                seguidoresUsuarioEditable.setEnabled(false);
            }
        }
    }

    /**
     * Comprueba si el usuario ha dado buena idea al proyecto
     */
    private void cargarPreferenciasUsuario() {
        //Cargo las preferencias del usuario
        if (usuarioSesion != null) {
            //Compruebo si el usuario le ha dado antes a seguir a este usuario
            boolean usuarioSigue = usuarioSesion.getMisSeguidos().stream().anyMatch(usuario1 -> usuario1.getId() == usuario.getId());
            //Si es así lo dejo presionado
            seguirUsuarioEditable.setPressed(usuarioSigue);
            if (usuarioSigue)
                seguirUsuarioEditable.setText(R.string.no_seguir);
        }
    }

    /**
     * Permite seguir al usuario si se ha iniciado sesión
     * @return
     */
    private View.OnClickListener seguirUsuario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el usuario ha iniciado sesión
                if (usuarioSesion != null) {
                    HSeguir hSeguir;
                    //Actualizo el botón
                    seguirUsuarioEditable.setPressed(!seguirUsuarioEditable.isPressed());
                    //Compruebo como ha quedado su estado después de hacer click
                    if (seguirUsuarioEditable.isPressed()) {
                        seguirUsuarioEditable.setText(R.string.no_seguir);
                        //Añado al contador 1 para decir que eres idProyecto
                        int cont = Integer.parseInt(seguidoresUsuarioEditable.getText().toString()) + 1;
                        seguidoresUsuarioEditable.setText(String.valueOf(cont));
                        Toast.makeText(context, "Genial!,sigues a este usuario!", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con 0 pues voy a añadir una nueva idea
                        hSeguir = new HSeguir(context,seguirUsuarioEditable,seguidoresUsuarioEditable);
                        //Para poder poner la referencia a null cuando termine la hebra
                        hSeguir.sethSeguir(hSeguir);
                    } else {
                        seguirUsuarioEditable.setText(R.string.seguir);
                        //Añado al contador 1 para decir que eres idProyecto
                        int cont = Integer.parseInt(seguidoresUsuarioEditable.getText().toString()) - 1;
                        seguidoresUsuarioEditable.setText(String.valueOf(cont));
                        Toast.makeText(context, "¿Ya no te interesa el usuario?", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con el id de la buena idea que encontré
                        hSeguir = new HSeguir(usuarioSesion.getId(), usuario.getId(),context,seguirUsuarioEditable,seguidoresUsuarioEditable);
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
     * @return
     */
    private View.OnClickListener cargaUsuario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIntegranteSelectedListener.onUsuarioSeleccionado(usuario);
            }
        };
    }

    /**
     * Solicita la unión al proyecto si has iniciado sesión en el sistema
     * @return
     */
    private View.OnClickListener solicitarUnion() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el usuario ha iniciado sesión
                if (usuarioSesion != null) {
                    HSolicitud hSolicitud;
                    seguirUsuarioEditable.setText(R.string.solicitado_unio_proyecto);
                    seguirUsuarioEditable.setPressed(true);
                    seguirUsuarioEditable.setEnabled(false);
                    hSolicitud = new HSolicitud(idProyecto,usuarioSesion.getId(),usuario.getMisEspecialidades(),context,seguirUsuarioEditable);
                    hSolicitud.sethSolicitud(hSolicitud);
                    hSolicitud.execute();
                }
                else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }

            }
        };
    }

}