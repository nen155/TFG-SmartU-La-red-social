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

        holder.seguirUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuarioSesion = Sesion.getUsuario(context);
                //Si el usuario ha iniciado sesión
                if (usuarioSesion != null) {
                /*Hacer la consulta para el insert en la tabla de seguidores*/
                    hSeguir = new HSeguir();
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
    ////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Hebra para insertar el seguidor
     */
    private class HSeguir extends AsyncTask<Void, Void, String> {

        HSeguir() {

        }

        @Override
        protected String doInBackground(Void... params) {

            String resultado = null;
            //Construyo el JSON
            String seguir = "\"seguir\":{\"idUsuario\":\"" + usuarioSesion.getId() + "\",\"idUsuarioSeguido\":\"" + usuario.getId() + "\"" +
                    ",\"fecha\":\"" + new Date() + "\"}";
            //Cojo el resultado en un String
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
                    if(res.has("resultado") && res.getString("resutlado").compareToIgnoreCase("ok")==0){
                        seguirUsuarioEditable.setText(R.string.no_seguir);
                    }else
                        Toast.makeText(context,"No se puede seguir a este usuario",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(context,"Fallo en la conexión",Toast.LENGTH_SHORT).show();
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
    }
}