package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Area;
import com.smartu.utilidades.ConsultasBBDD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java8.util.stream.StreamSupport;


public class AdapterAreasInteres extends BaseAdapter {
	private Context context;
	private ArrayList<Area> areasInteres;
	private LayoutInflater inflater;
	private ArrayList<Area> areas;


	public AdapterAreasInteres(Bundle savedInstanceState, Context context, ArrayList<Area> items) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.areasInteres = items;
		this.areas = Almacen.getAreas();
	}

	@Override
	public int getCount() {
		return this.areas.size();
	}

	@Override
	public Object getItem(int position) {
		return this.areas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return areas.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.item_area_cuadrada, parent, false);
		}

		final Area area = this.areas.get(position);
		boolean esta = StreamSupport.parallelStream(areasInteres).filter(area1 -> area1.getId() == area.getId()).findAny().isPresent();

		if(esta){
			rowView.findViewById(R.id.elemento_area).setBackgroundColor(ContextCompat.getColor(context, R.color.black_semi_transparent));
		}

		ImageView imgItem = (ImageView) rowView.findViewById(R.id.img_area);
		TextView textBuscar = (TextView) rowView.findViewById(R.id.nombre_area);

		textBuscar.setText(area.getNombre());
		if(area.getUrlImg()!=null &&area.getUrlImg().compareTo("")!=0) {
			Uri imagen = Uri.parse(area.getUrlImg());
			Picasso.with(context).load(ConsultasBBDD.server + imagen).into(imgItem);
		}else{
			imgItem.setImageResource(R.drawable.areas);
		}
		return rowView;
	}




}