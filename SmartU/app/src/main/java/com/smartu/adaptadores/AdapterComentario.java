package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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


public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.ViewHolder> {
    private Context context;
    private ArrayList<Comentario> comentarios;
    private Comentario comentario;


    public AdapterComentario(Context context, ArrayList<Comentario> items) {
        super();
        this.context = context;
        this.comentarios = items;
    }

    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fechaComentario;
        TextView descripcionComentario;
        TextView nombreUsuario;
        Button btnNombreProyecto;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            fechaComentario = (TextView) itemView.findViewById(R.id.fecha_comentario);
            descripcionComentario = (TextView) itemView.findViewById(R.id.descripcion_comentario);
            nombreUsuario = (TextView) itemView.findViewById(R.id.nombre_usuario_comentario);
            btnNombreProyecto = (Button) itemView.findViewById(R.id.nombre_proyecto);

        }

    }

    @Override
    public AdapterComentario.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario_recyclerview, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(AdapterComentario.ViewHolder holder, int position) {
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
        if (comentario.getDescripcion().length() > 150) {
            holder.descripcionComentario.setText(comentario.getDescripcion().substring(0, 150) + "...");
        } else
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

    @Override
    public long getItemId(int position) {
        return ((Comentario) comentarios.get(position)).getId();
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }


    private void cargaProyecto() {
        Intent intent = new Intent(context, ProyectoActivity.class);
        intent.putExtra("proyecto", comentario.getProyecto());
        context.startActivity(intent);
    }

}