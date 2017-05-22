package com.smartu.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.modelos.Novedad;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AdapterNovedad extends RecyclerView.Adapter<AdapterNovedad.ViewHolder> {
	private Context context;
	private ArrayList<Novedad> novedades;
	private Novedad novedad;


	public AdapterNovedad(Context context, ArrayList<Novedad> items) {
		super();
		this.context = context;
		this.novedades = items;
	}

	//Creating a ViewHolder which extends the RecyclerView View Holder
	// ViewHolder are used to to store the inflated views in order to recycle them

	public static class ViewHolder extends RecyclerView.ViewHolder {

		TextView nombreNovedad;
		TextView descripcionNovedad;
		TextView fechaNovedad;
		Button nombreUsuarioOProyecto;

		public ViewHolder(View itemView, int viewType) {
			super(itemView);
			nombreNovedad = (TextView) itemView.findViewById(R.id.nombre_usuario);
			descripcionNovedad = (TextView) itemView.findViewById(R.id.descripcion_novedad);
			fechaNovedad = (TextView) itemView.findViewById(R.id.fecha_novedad);
			nombreUsuarioOProyecto = (Button) itemView.findViewById(R.id.nombre_usuario_o_proyecto);

		}

	}

	@Override
	public AdapterNovedad.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_novedad_recyclerview,parent,false); //Inflating the layout

		ViewHolder vhItem = new ViewHolder(v,viewType);

		return vhItem;
	}

	@Override
	public void onBindViewHolder(AdapterNovedad.ViewHolder holder, int position) {
		novedad = (Novedad)this.novedades.get(position);

		holder.nombreNovedad.setText(novedad.getNombre());
		holder.descripcionNovedad.setText(novedad.getDescripcion());
		Date fecha = novedad.getFecha();
		if (fecha != null) {
			Date tiempo = new Date(fecha.getTime() - new Date().getTime());
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(tiempo);
			int horas = calendar.get(Calendar.HOUR);
			String hace = "Hace " + horas + " horas";
			holder.fechaNovedad.setText(hace);
		}
		String textoBoton="";
		if(novedad.getUsuario()!=null)
			textoBoton=novedad.getUsuario().getNombre();
		else if(novedad.getProyecto()!=null)
			textoBoton=novedad.getProyecto().getNombre();
		holder.nombreUsuarioOProyecto.setText(textoBoton);


		holder.nombreNovedad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cargaElemento();
			}
		});
		holder.descripcionNovedad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cargaElemento();
			}
		});
		holder.nombreUsuarioOProyecto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cargaElemento();
			}
		});
	}

	@Override
	public long getItemId(int position) {
		return ((Novedad) novedades.get(position)).getId();
	}

	@Override
	public int getItemCount() {
		return novedades.size();
	}


	private void cargaElemento(){
			if(novedad.getUsuario()!=null){
				/*Intent intent = new Intent(context,UsuarioActivity.class);
				intent.putExtra("usuario",novedad.getUsuario());
				startActivity(intent);*/
			}else if(novedad.getProyecto()!=null) {
				/*Intent intent = new Intent(context,ProyectoActivity.class);
				intent.putExtra("proyecto",novedad.getProyecto());
				startActivity(intent);*/
			}
	}

	public void replaceData(ArrayList<Novedad> items) {
		setList(items);
		notifyDataSetChanged();
	}

	public void setList(ArrayList<Novedad> list) {
		this.novedades = list;
	}

	public void addItem(Novedad pushMessage) {
		novedades.add(0, pushMessage);
		notifyItemInserted(0);
	}

}