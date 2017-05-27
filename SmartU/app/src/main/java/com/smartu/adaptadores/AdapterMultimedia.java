package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.modelos.Multimedia;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.vistas.Imagen360Activity;
import com.smartu.vistas.ImagenActivity;
import com.smartu.vistas.VideoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterMultimedia extends RecyclerView.Adapter<AdapterMultimedia.ViewHolder> {
    private Context context;
    private ArrayList<Multimedia> multimediaList;
    private Multimedia multimedia;


    public AdapterMultimedia(Context context, ArrayList<Multimedia> items) {
        super();
        this.context = context;
        this.multimediaList = items;
    }

    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombreMultimedia;
        ImageView imgPreviewMultimedia;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            nombreMultimedia = (TextView) itemView.findViewById(R.id.nombre_multimedia);
            imgPreviewMultimedia = (ImageView) itemView.findViewById(R.id.img_preview_multimedia);
        }

    }

    @Override
    public AdapterMultimedia.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multimedia_recyclerview, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final AdapterMultimedia.ViewHolder holder, int position) {
        multimedia = (Multimedia) this.multimediaList.get(position);
        if(multimedia.getUrlPreview()!=null && multimedia.getUrlPreview().compareTo("")!=0)
            Picasso.with(context).load(ConsultasBBDD.server+multimedia.getUrlPreview()).into(holder.imgPreviewMultimedia);
        else{
            switch(multimedia.getTipo()) {
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
        intent.putExtra("multimedia",multimedia);
        context.startActivity(intent);
    }

    @Override
    public long getItemId(int position) {
        return ((Multimedia) multimediaList.get(position)).getId();
    }

    @Override
    public int getItemCount() {
        return multimediaList.size();
    }

}