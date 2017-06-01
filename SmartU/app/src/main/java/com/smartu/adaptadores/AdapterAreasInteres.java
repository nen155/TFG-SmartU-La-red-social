package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.smartu.R;
import com.smartu.modelos.Area;
import com.smartu.utilidades.ConsultasBBDD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterAreasInteres extends BaseAdapter {
	private Context context;
	private ArrayList<Area> areasInteres;
	private LayoutInflater inflater;


	public AdapterAreasInteres(Context context, ArrayList<Area> items) {
		super();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.areasInteres = items;
	}

	@Override
	public int getCount() {
		return this.areasInteres.size();
	}

	@Override
	public Object getItem(int position) {
		return this.areasInteres.get(position);
	}

	@Override
	public long getItemId(int position) {
		return areasInteres.get(position).getId();
	}

	@Override
	public View getView(int position, final View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.item_area_cuadrada, parent, false);
		}

		final Area area = this.areasInteres.get(position);

		ImageView imgItem = (ImageView) rowView.findViewById(R.id.img_area);
		TextView textBuscar = (TextView) rowView.findViewById(R.id.nombre_area);

		textBuscar.setText(area.getNombre());
		if(area.getUrlImg()!=null &&area.getUrlImg().compareTo("")!=0) {
			Uri imagen = Uri.parse(area.getUrlImg());
			Picasso.with(context).load(ConsultasBBDD.server + imagen).into(imgItem);
		}else{
			imgItem.setImageResource(R.drawable.areas);
		}
		imgItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO PODER CAMBIAR LAS AREAS DE INTERES DEL USUARIO
				//SE QUEDA POR IMPLEMENTAR
			}
		});

		return rowView;
	}

}