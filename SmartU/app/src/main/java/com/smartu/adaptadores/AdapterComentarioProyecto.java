package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.contratos.OperacionesAdapter;
import com.smartu.contratos.Publicacion;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Proyecto;
import com.smartu.utilidades.Comparador;
import com.smartu.vistas.ProyectoActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;


public class AdapterComentarioProyecto extends RecyclerView.Adapter<AdapterComentarioProyecto.ViewHolder> implements OperacionesAdapter {
    private Context context;
    private ArrayList<Comentario> comentarios;
    private Comentario comentario;
    private Proyecto proyectoOrigen;
    private TextView holderDescripcionComentario;

    //Es el número total de elementos que hay en el server
    //tengo que recogerlo de las hebras de consulta
    private int totalElementosServer = -1;

    // Dos tipos de vistas para saber si es un ProgressBar lo que muestro o la vista normal
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;
    public static final int VIEW_TYPE_FINAL = 2;

    public void setTotalElementosServer(int totalElementosServer) {
        this.totalElementosServer = totalElementosServer;
    }

    public AdapterComentarioProyecto(Context context, ArrayList<Comentario> items,Proyecto proyectoOrigen) {
        super();
        this.context = context;
        this.comentarios = items;
        Collections.sort(comentarios, new Comparador.ComparaComentarios());
        this.proyectoOrigen = proyectoOrigen;
    }

    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int tipoView;
        TextView fechaComentario;
        TextView descripcionComentario;
        TextView nombreUsuario;
        Button btnNombreProyecto;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType==VIEW_TYPE_ACTIVITY) {
                fechaComentario = (TextView) itemView.findViewById(R.id.fecha_comentario);
                descripcionComentario = (TextView) itemView.findViewById(R.id.descripcion_comentario);
                nombreUsuario = (TextView) itemView.findViewById(R.id.nombre_usuario_comentario);
                btnNombreProyecto = (Button) itemView.findViewById(R.id.nombre_proyecto);
                tipoView=1;
            }else{
                tipoView=0;
            }

        }

    }

    @Override
    public AdapterComentarioProyecto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress,parent,false);
            ViewHolder vhBottom = new ViewHolder(v,viewType);

            return vhBottom;
        }else if(viewType ==VIEW_TYPE_FINAL){
            // the ListView has reached the last row
            TextView tvLastRow = new TextView(context);
            tvLastRow.setHint("");
            tvLastRow.setGravity(Gravity.CENTER);
            ViewHolder vhUltimo = new ViewHolder(tvLastRow,viewType);
            return vhUltimo;
        }else
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario_recyclerview, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType);

            return vhItem;
        }
    }

    @Override
    public void onBindViewHolder(AdapterComentarioProyecto.ViewHolder holder, int position) {
        //Sino es el último elemento ni es un progress bar pues muestro el elemento que me toca
        if(holder.tipoView==1) {
            comentario = (Comentario) this.comentarios.get(position);
            Date fecha = comentario.getFecha();
            if (fecha != null) {
                long res = (((new Date().getTime()-fecha.getTime())/1000)/60)/60;
                String hace ="";
                if(res>24) {
                    res = res / 24;
                    if(res>1)
                        hace = "Hace " + res + " dias";
                    else
                        hace = "Hace " + res + " día";
                }else if(res>1)
                    hace = "Hace " + res + " horas";
                else if(res==1){
                    hace = "Hace " + res + " hora";
                }
                else if(res<1) {
                    res = res/60;
                    if(res>1)
                        hace = "Hace " + res + " minutos";
                    else if(res==1){
                        hace = "Hace " + res + " minuto";
                    }
                    else if(res<1) {
                        res=res/60;
                        hace = "Hace " + res + " segundos";
                    }
                }
                holder.fechaComentario.setText(hace);
            }
            holderDescripcionComentario= holder.descripcionComentario;
            holder.descripcionComentario.setText(comentario.getDescripcion());
            holder.btnNombreProyecto.setText(proyectoOrigen.getNombre());
            holder.nombreUsuario.setText(comentario.getUsuario());

            holder.descripcionComentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargaProyecto();
                }
            });
            holder.btnNombreProyecto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargaProyecto();
                }
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? comentarios.get(position).getId()
                : -1;
    }
    /**
     * Devuelve el tipo de fila,
     * El ultimo elemento es el de loading
     */
    @Override
    public int getItemViewType(int position) {
        if (position >= comentarios.size() && position>=totalElementosServer && totalElementosServer > 0){
            return VIEW_TYPE_FINAL;
        }else if(position >= comentarios.size()){
            return VIEW_TYPE_LOADING;
        }else
            return VIEW_TYPE_ACTIVITY;
    }


    @Override
    public int getItemCount() {
        return comentarios.size()+1;
    }


    private void cargaProyecto() {
        Intent intent = new Intent(context, ProyectoActivity.class);
        intent.putExtra("idProyecto",  proyectoOrigen.getId());
        context.startActivity(intent);
    }
    @Override
    public void addItem(Publicacion publicacion) {
        comentarios.add((Comentario) publicacion);
        Almacen.add((Comentario) publicacion);
        notifyItemInserted(comentarios.size()-1);
    }
    public void addItemTop(Comentario pushMessage) {
        Comentario comentario= pushMessage;
        boolean esta = StreamSupport.stream(comentarios).filter(usuario1 -> usuario1.getId() == comentario.getId()).findAny().isPresent();
        if (!esta) {
            holderDescripcionComentario.setText("");
            comentarios.add(0,comentario);
            Almacen.add(comentario);
            notifyItemInserted(0);
        }
    }
}