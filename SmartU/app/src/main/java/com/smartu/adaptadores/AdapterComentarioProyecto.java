package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.modelos.Comentario;
import com.smartu.vistas.ProyectoActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AdapterComentarioProyecto extends RecyclerView.Adapter<AdapterComentarioProyecto.ViewHolder> {
    private Context context;
    private ArrayList<Comentario> comentarios;
    private Comentario comentario;

    //Es el número total de elementos que hay en el server
    //tengo que recogerlo de las hebras de consulta
    private int totalElementosServer = -1;

    // Dos tipos de vistas para saber si es un ProgressBar lo que muestro o la vista normal
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_ACTIVITY = 1;

    public void setTotalElementosServer(int totalElementosServer) {
        this.totalElementosServer = totalElementosServer;
    }

    public AdapterComentarioProyecto(Context context, ArrayList<Comentario> items) {
        super();
        this.context = context;
        this.comentarios = items;
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

            if (vhBottom.getAdapterPosition() >= totalElementosServer && totalElementosServer > 0)
            {
                // the ListView has reached the last row
                TextView tvLastRow = new TextView(context);
                tvLastRow.setHint("No hay más elementos.");
                tvLastRow.setGravity(Gravity.CENTER);
                ViewHolder vhUltimo = new ViewHolder(tvLastRow,viewType);
                return vhUltimo;
            }

            return vhBottom;
        }else {
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
                Date tiempo = new Date(fecha.getTime() - new Date().getTime());
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(tiempo);
                int horas = calendar.get(Calendar.HOUR);
                String hace = "Hace " + horas + " horas";
                holder.fechaComentario.setText(hace);
            }
            holder.descripcionComentario.setText(comentario.getDescripcion());
            holder.btnNombreProyecto.setText(comentario.getProyecto().getNombre());
            holder.nombreUsuario.setText(comentario.getUsuario().getNombre());

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
        return (position >= comentarios.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ACTIVITY;
    }


    @Override
    public int getItemCount() {
        return comentarios.size()+1;
    }


    private void cargaProyecto() {
        Intent intent = new Intent(context, ProyectoActivity.class);
        intent.putExtra("proyecto", comentario.getProyecto());
        context.startActivity(intent);
    }
    public void addItem(Comentario pushMessage) {
        comentarios.add(pushMessage);
        notifyItemInserted(0);
    }
    public void addItemTop(Comentario pushMessage) {
        comentarios.add(0,pushMessage);
        notifyItemInserted(0);
    }
}