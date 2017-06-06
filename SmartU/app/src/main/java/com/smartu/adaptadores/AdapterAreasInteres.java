package com.smartu.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Area;
import com.smartu.utilidades.ConsultasBBDD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Set;

import java8.util.stream.StreamSupport;


public class AdapterAreasInteres extends BaseAdapter {
	private Context context;
	private ArrayList<Area> areasBack;
	private ArrayList<Area> areasInicales;
	private Set<Integer> posicionAreasInicial;
	private LayoutInflater inflater;
	private ArrayList<Area> areas;
	private GridView gridView;


	public AdapterAreasInteres(Bundle savedInstanceState, Context context, Set<Integer> posicionAreasInicial, ArrayList<Area> areasBack, GridView gridView, ArrayList<Area> areasIniciales) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.areas = Almacen.getAreas();
		this.areasBack = areasBack;
		this.gridView=gridView;
		this.areasInicales=areasIniciales;
		this.posicionAreasInicial = posicionAreasInicial;
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
		ImageView seleccionado = (ImageView) rowView.findViewById(R.id.elemento_area);
		final Area area = this.areas.get(position);
		//Si esta en el arrayList la selecciono
		boolean esta = StreamSupport.stream(areasBack).filter(area1 -> area1.getId() == area.getId()).findAny().isPresent();
		if(esta)
			seleccionado.setVisibility(View.VISIBLE);
		//Esta comprobación la hago para que cuando areasBack sea modificado no modifique el SET inicial que es el que quiero mantener
		boolean estaAreaInicial = StreamSupport.stream(areasInicales).filter(area1 -> area1.getId() == area.getId()).findAny().isPresent();
		if(estaAreaInicial)
			posicionAreasInicial.add(position);


		ImageView imgItem = (ImageView) rowView.findViewById(R.id.img_area);
		TextView textBuscar = (TextView) rowView.findViewById(R.id.nombre_area);

		textBuscar.setText(area.getNombre());
		if(area.getUrlImg()!=null &&area.getUrlImg().compareTo("")!=0) {
			Uri imagen = Uri.parse(area.getUrlImg());
			Picasso.with(context).load(ConsultasBBDD.server+ ConsultasBBDD.imagenes + imagen).into(imgItem);
		}else{
			imgItem.setImageResource(R.drawable.areas);
		}

		imgItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Compruebo si estaba para quitarlo y sino para añadirlo como seleccionado
				boolean esta = StreamSupport.stream(areasBack).filter(area1 -> area1.getId() == area.getId()).findAny().isPresent();
				if(esta)
					uncheck(seleccionado,position);
				else
					check(seleccionado,position);
			}
		});
		return rowView;
	}

	/**
	 * Marca como seleccionado el item del Gridview
	 * @param view
	 * @param position
	 */
	private void check(View view,int position){
		view.setVisibility(View.VISIBLE);
		areasBack.add((Area) getItem(position));
	}

	public void unCheckAll(){
		for(int i=0;i<areas.size();++i){
			unCheckElement(i);
		}
	}
	private void unCheckElement(int position){
		View view =gridView.getChildAt(position);
		ImageView seleccionado = (ImageView) view.findViewById(R.id.elemento_area);
		seleccionado.setVisibility(View.GONE);
		areasBack.remove((Area) getItem(position));
	}
	/**
	 * Selecciona el elemento sólo por la posicion del mismo
	 * @param position
	 */
	public void checkElement(int position){
		View view =gridView.getChildAt(position);
		ImageView seleccionado = (ImageView) view.findViewById(R.id.elemento_area);
		seleccionado.setVisibility(View.VISIBLE);
		areasBack.add((Area) getItem(position));
	}
	/**
	 * Deselecciona el item del Gridview
	 * @param view
	 * @param position
	 */
	private void uncheck(View view,int position){
		view.setVisibility(View.GONE);
		areasBack.remove((Area) getItem(position));
	}


}