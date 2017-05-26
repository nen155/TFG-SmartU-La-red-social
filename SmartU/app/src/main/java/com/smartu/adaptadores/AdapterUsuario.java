package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.smartu.modelos.Especialidad;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentUsuarios;
import com.smartu.vistas.LoginActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ViewHolder> {
    private Context context;
    private ArrayList<Usuario> usuarios;
    private FragmentUsuarios.OnUsuarioSelectedListener onUsuarioSelectedListener;
    private Usuario usuario;
    private HSeguir hSeguir;
    private Usuario usuarioSesion;
    private Button seguirUsuarioEditable;
    private TextView seguidoresUsuarioEditable;

    public AdapterUsuario(Context context, ArrayList<Usuario> items, FragmentUsuarios.OnUsuarioSelectedListener onUsuarioSelectedListener) {
        super();
        this.context = context;
        this.usuarios = items;
        this.onUsuarioSelectedListener = onUsuarioSelectedListener;
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
    public AdapterUsuario.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_recyclerview, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final AdapterUsuario.ViewHolder holder, int position) {
        usuario = (Usuario) this.usuarios.get(position);
        Picasso.with(context).load(usuario.getImagenDestacada()).into(holder.imgUsuario);
        holder.nombreUsuario.setText(usuario.getNombre());
        for (Especialidad e : usuario.getMisEspecialidades()) {
            holder.especialidadUsuario.add(new Tag(e.getId(), e.getNombre()));
        }
        holder.seguidoresUsuario.setText(String.valueOf(usuario.getMisSeguidos().size()));
        holder.statusUsuario.setText(usuario.getMiStatus().getNombre());

        holder.imgUsuario.setOnClickListener(cargaUsuario());
        holder.statusUsuario.setOnClickListener(cargaUsuario());
        holder.nombreUsuario.setOnClickListener(cargaUsuario());

        seguirUsuarioEditable = holder.seguirUsuario;
        seguidoresUsuarioEditable = holder.seguidoresUsuario;

        //Cargo las preferencias del usuario si tuviese sesión
        cargarPreferenciasUsuario();
        holder.seguirUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuarioSesion = Sesion.getUsuario(context);
                //Si el usuario ha iniciado sesión
                if (usuarioSesion != null) {
                    //Actualizo el botón
                    seguirUsuarioEditable.setPressed(!seguirUsuarioEditable.isPressed());
                    //Compruebo como ha quedado su estado después de hacer click
                    if (seguirUsuarioEditable.isPressed()) {
                        seguirUsuarioEditable.setText(R.string.no_seguir);
                        //Añado al contador 1 para decir que eres seguidor
                        int cont = Integer.parseInt(holder.seguidoresUsuario.getText().toString())+1;
                        holder.seguidoresUsuario.setText(String.valueOf(cont));
                        Toast.makeText(context, "Genial!,sigues a este usuario!", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con 0 pues voy a añadir una nueva idea
                        hSeguir = new HSeguir();
                    }
                    else {
                        seguirUsuarioEditable.setText(R.string.seguir);
                        //Añado al contador 1 para decir que eres seguidor
                        int cont = Integer.parseInt(holder.seguidoresUsuario.getText().toString())-1;
                        holder.seguidoresUsuario.setText(String.valueOf(cont));
                        Toast.makeText(context,"¿Ya no te interesa el usuario?",Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con el id de la buena idea que encontré
                        hSeguir = new HSeguir(usuarioSesion.getId(),usuario.getId());
                    }
                    hSeguir.execute();
                }
                else
                {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return ((Usuario) usuarios.get(position)).getId();
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }


    private View.OnClickListener cargaUsuario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUsuarioSelectedListener.onUsuarioSeleccionado(usuario);
            }
        };
    }
    /**
     * Comprueba si el usuario ha dado buena idea al proyecto
     */
    private void cargarPreferenciasUsuario(){
        //Cargo las preferencias del usuario
        if(usuarioSesion!=null) {
            //Compruebo si el usuario le ha dado antes a seguir a este usuario
            boolean usuarioSigue =  usuarioSesion.getMisSeguidos().stream().anyMatch(usuario1 -> usuario1.getId() == usuario.getId());
            //Si es así lo dejo presionado
            seguirUsuarioEditable.setPressed(usuarioSigue);
            if(usuarioSigue)
                seguirUsuarioEditable.setText(R.string.no_seguir);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    /*
     * HEBRAS
     */
    ///////////////////////////////////////////////////////////////////////////////////////
    /**
     * Hebra para insertar el seguidor
     */
    private class HSeguir extends AsyncTask<Void, Void, String> {

        private int seguidor=0,seguido=0;
        HSeguir() {

        }
        HSeguir(int seguidor,int seguido) {
            this.seguidor = seguidor;
            this.seguido = seguido;
        }

        @Override
        protected String doInBackground(Void... params) {

            String resultado = null;
            //Construyo el JSON
            String seguir = "\"seguir\":{\"idUsuario\":\"" + seguidor + "\",\"idUsuarioSeguido\":\"" + seguido + "\"}";
            //Cojo el resultado en un String
            if(seguidor==0 || seguido==0 )
                resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.eliminarSeguidor, seguir, "POST");
            else
                resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaSeguidor, seguir, "POST");

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hSeguir = null;
            //Obtengo el objeto JSON con el resultado
            JSONObject res=null;
            try {
                 res = new JSONObject(resultado);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Si tengo objeto compruebo el resultado y si es ok cambio el texto al botón
            //Sino muestro mensaje por pantalla
            if (res!=null) {
                try {
                    if(res.has("resultado") && res.getString("resutlado").compareToIgnoreCase("ok")!=0)
                        reestablecerEstado();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                reestablecerEstado();
            }
        }

        @Override
        protected void onCancelled(String resultado) {
            super.onCancelled(resultado);
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hSeguir = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hSeguir = null;
        }
        private void reestablecerEstado(){
            Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
            seguirUsuarioEditable.setPressed(!seguirUsuarioEditable.isPressed());
            if(seguirUsuarioEditable.isPressed())
                seguirUsuarioEditable.setText(R.string.no_seguir);
            else
                seguirUsuarioEditable.setText(R.string.seguir);
            if(seguido!=0) {
                //Si quería eliminar la buena idea significa que le he restado uno al contador previamente
                int cont = Integer.parseInt(seguirUsuarioEditable.getText().toString())+1;
                seguirUsuarioEditable.setText(String.valueOf(cont));
            }else
            {
                //Si quería añadirlo como buena idea significa que le he sumando 1 al contador previamente
                int cont = Integer.parseInt(seguirUsuarioEditable.getText().toString())-1;
                seguirUsuarioEditable.setText(String.valueOf(cont));
            }
        }
    }
}