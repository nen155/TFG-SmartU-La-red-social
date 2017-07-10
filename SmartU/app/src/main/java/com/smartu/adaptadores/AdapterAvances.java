package com.smartu.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.modelos.Avance;
import com.smartu.utilidades.Comparador;
import com.smartu.utilidades.ConsultasBBDD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import java8.util.stream.StreamSupport;


public class AdapterAvances extends RecyclerView.Adapter<AdapterAvances.ViewHolder> {

    private Context context;
    private ArrayList<Avance> avances;

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

    public void setAvances(ArrayList<Avance> avances) {
        this.avances = avances;
        Collections.sort(this.avances , new Comparador.ComparaAvances());
    }

    public AdapterAvances(Context context, ArrayList<Avance> items) {
        super();
        this.context = context;
        this.avances = items;
        Collections.sort(avances, new Comparador.ComparaAvances());
    }




    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int tipoView;
        TextView fechaAvance;
        TextView nombreAvance;
        TextView descripcionAvance;
        TextView nombreUsuario;
        ImageView imagenDestacada;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType==VIEW_TYPE_ACTIVITY) {
                nombreAvance = (TextView) itemView.findViewById(R.id.nombre_avance);
                fechaAvance = (TextView) itemView.findViewById(R.id.fecha_avance);
                descripcionAvance = (TextView) itemView.findViewById(R.id.descripcion_avance);
                nombreUsuario = (TextView) itemView.findViewById(R.id.nombre_usuario_avance);
                imagenDestacada = (ImageView) itemView.findViewById(R.id.img_avance);
                tipoView=1;
            }else{
                tipoView=0;
            }


        }


    }


    @Override
    public AdapterAvances.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avance_recyclerview, parent, false); //Inflating the layout
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        }

    }

    @Override
    public void onBindViewHolder(AdapterAvances.ViewHolder holder, int position) {
        //Sino es el último elemento ni es un progress bar pues muestro el elemento que me toca
        if(holder.tipoView==1) {
            Avance avance = (Avance) this.avances.get(position);

            Date fecha = avance.getFecha();

            if (fecha != null) {
                long res = (((new Date().getTime()-fecha.getTime())/1000)/60)/60;
                String hace ="";
                if(res>24) {
                    res = res / 24;
                    if(res>1)
                        hace = "Hace " + res + " dias";
                    else
                        hace = "Hace " + res + " día";
                }else
                    if(res>1)
                        hace = "Hace " + res + " horas";
                    else
                        hace = "Hace " + res + " hora";
                holder.fechaAvance.setText(hace);
            }
            Picasso.with(context).load(ConsultasBBDD.server+ConsultasBBDD.imagenes+ avance.getImagenDestacada()).into(holder.imagenDestacada);
            holder.nombreAvance.setText(avance.getNombre());
            holder.descripcionAvance.setText(avance.getDescripcion());
            holder.nombreUsuario.setText(avance.getNombreUsuario());
        }
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? avances.get(position).getId()
                : -1;
    }

    /**
     * El +1 es por el loading
     * @return
     */
    @Override
    public int getItemCount() {
        if(avances.size()==0)
            return avances.size();
        return avances.size()+1;
    }

    /**
     * Devuelve el tipo de fila,
     * El ultimo elemento es el de loading
     */
    @Override
    public int getItemViewType(int position) {
        if (position >= avances.size() && position>=totalElementosServer && totalElementosServer > 0){
            return VIEW_TYPE_FINAL;
        }else if(position >= avances.size()){
            return VIEW_TYPE_FINAL;
        }else
            return VIEW_TYPE_ACTIVITY;
    }

    /**
     * Actualmente no se usa, ya que se cargan los avances
     * con el proyecto
     * @param avance
     */
    public void addItem(Avance avance) {
        boolean esta = StreamSupport.stream(avances).filter(avance1 -> avance1.getId() == avance.getId()).findAny().isPresent();
        if (!esta) {
            avances.add(avance);
            notifyItemInserted(avances.size() - 1);
        }
    }
}