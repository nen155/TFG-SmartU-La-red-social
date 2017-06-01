package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.contratos.OperacionesAdapter;
import com.smartu.contratos.Publicacion;
import com.smartu.modelos.Multimedia;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.vistas.Imagen360Activity;
import com.smartu.vistas.ImagenActivity;
import com.smartu.vistas.VideoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterMultimedia extends RecyclerView.Adapter<AdapterMultimedia.ViewHolder>  {
    private Context context;
    private ArrayList<Multimedia> multimediaList;
    private Multimedia multimedia;

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

    public AdapterMultimedia(Context context, ArrayList<Multimedia> items) {
        super();
        this.context = context;
        this.multimediaList = items;
    }



    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder {
        int tipoView;
        TextView nombreMultimedia;
        ImageView imgPreviewMultimedia;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType==VIEW_TYPE_ACTIVITY) {
            nombreMultimedia = (TextView) itemView.findViewById(R.id.nombre_multimedia);
            imgPreviewMultimedia = (ImageView) itemView.findViewById(R.id.img_preview_multimedia);
                tipoView=1;
            }else{
                tipoView=0;
            }
        }

    }

    @Override
    public AdapterMultimedia.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multimedia_recyclerview, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType);

            return vhItem;
        }
    }

    @Override
    public void onBindViewHolder(final AdapterMultimedia.ViewHolder holder, int position) {
        //Sino es el último elemento ni es un progress bar pues muestro el elemento que me toca
        if(holder.tipoView==1) {
            multimedia = (Multimedia) this.multimediaList.get(position);
            if (multimedia.getUrlPreview() != null && multimedia.getUrlPreview().compareTo("") != 0)
                Picasso.with(context).load(ConsultasBBDD.server + multimedia.getUrlPreview()).into(holder.imgPreviewMultimedia);
            else {
                switch (multimedia.getTipo()) {
                    case "video":
                        holder.imgPreviewMultimedia.setImageResource(R.drawable.video_preview);
                        break;
                    case "imagen":
                        holder.imgPreviewMultimedia.setImageResource(R.drawable.image_multiple);
                        break;
                    case "imagen360":
                        holder.imgPreviewMultimedia.setImageResource(R.drawable.imagen360);
                        break;
                }
            }
            holder.nombreMultimedia.setText(multimedia.getNombre());
            holder.nombreMultimedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargaMultimedia();
                }
            });
            holder.imgPreviewMultimedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargaMultimedia();
                }
            });
        }
    }
    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? multimediaList.get(position).getId()
                : -1;
    }
    /**
     * Devuelve el tipo de fila,
     * El ultimo elemento es el de loading
     */
    @Override
    public int getItemViewType(int position) {
        if (position >= multimediaList.size() && position==totalElementosServer && totalElementosServer > 0){
            return VIEW_TYPE_FINAL;
        }else if(position >= multimediaList.size()){
            return VIEW_TYPE_LOADING;
        }else
            return VIEW_TYPE_ACTIVITY;
    }

    /**
     * Carga el multimedia dependiendo del tipo en la Activity correspondiente
     */
    private void cargaMultimedia(){
        Intent intent=null;
        switch(multimedia.getTipo()) {
            case "video":
                intent=new Intent(context, VideoActivity.class);
                break;
            case "imagen":
                intent=new Intent(context, ImagenActivity.class);
                break;
            case "imagen360":
                intent=new Intent(context, Imagen360Activity.class);
                break;
        }
        intent.putExtra("multimedia",(Parcelable) multimedia);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return multimediaList.size()+1;
    }

    public void addItem(Multimedia pushMessage) {
        multimediaList.add(pushMessage);
        notifyItemInserted(0);
    }
}