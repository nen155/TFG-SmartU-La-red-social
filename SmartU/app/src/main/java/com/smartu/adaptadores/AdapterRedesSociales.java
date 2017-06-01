package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartu.R;

import com.smartu.modelos.RedSocial;


import java.util.ArrayList;


public class AdapterRedesSociales extends RecyclerView.Adapter<AdapterRedesSociales.ViewHolder> {
    private Context context;
    private ArrayList<RedSocial> redesSociales;
    private RedSocial redSocial;


    public AdapterRedesSociales(Context context, ArrayList<RedSocial> items) {
        super();
        this.context = context;
        this.redesSociales = items;
    }

    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView imgRedSocial;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            imgRedSocial = (TextView) itemView.findViewById(R.id.img_redsocial);

        }

    }

    @Override
    public AdapterRedesSociales.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_redsocial_recyclerview, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final AdapterRedesSociales.ViewHolder holder, int position) {
        redSocial = (RedSocial) this.redesSociales.get(position);
        String[] sinHTTPS=null;
        //Le quito el https a la redsocial
        if(redSocial.getUrl().startsWith("https://www."))
            sinHTTPS= redSocial.getUrl().split("https://www.");
        else
            sinHTTPS= redSocial.getUrl().split("https://");
        //Divido el resultado por los puntos por lo que me quedaría algo así
        // facebook  .com/assdasdasd
        String[] sinURLCOMPLETA = sinHTTPS[1].split("\\.");
        //Lo paso a minúsculas por si acaso
        String nombreRed = sinURLCOMPLETA[0].toLowerCase();
        //Establezco el icono que quiero mostrar en el TextView
        String iconoFa="{fa-"+nombreRed+"-square}";
        holder.imgRedSocial.setText(iconoFa);
        holder.imgRedSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(redSocial.getUrl()));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public long getItemId(int position) {
        return ((RedSocial) redesSociales.get(position)).getId();
    }

    @Override
    public int getItemCount() {
        return redesSociales.size();
    }

}