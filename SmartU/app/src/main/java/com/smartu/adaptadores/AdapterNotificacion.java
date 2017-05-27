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
import com.smartu.modelos.Notificacion;
import com.smartu.vistas.ProyectoActivity;
import com.smartu.vistas.UsuarioActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AdapterNotificacion extends RecyclerView.Adapter<AdapterNotificacion.ViewHolder> {
	private Context context;
	private ArrayList<Notificacion> notificaciones;
	private Notificacion notificacion;


	public AdapterNotificacion(Context context, ArrayList<Notificacion> items) {
		super();
		this.context = context;
		this.notificaciones = items;
	}

	//Creating a ViewHolder which extends the RecyclerView View Holder
	// ViewHolder are used to to store the inflated views in order to recycle them

	public static class ViewHolder extends RecyclerView.ViewHolder {

		TextView nombreNotificacion;
		TextView descripcionNotificacion;
		TextView fechaNotificacion;
		Button nombreUsuarioOProyecto;

		public ViewHolder(View itemView, int viewType) {
			super(itemView);
			nombreNotificacion = (TextView) itemView.findViewById(R.id.nombre_notificacion);
			descripcionNotificacion = (TextView) itemView.findViewById(R.id.descripcion_notificacion);
			fechaNotificacion = (TextView) itemView.findViewById(R.id.fecha_notificacion);
			nombreUsuarioOProyecto = (Button) itemView.findViewById(R.id.nombre_usuario_o_proyecto);

		}

	}

	@Override
	public AdapterNotificacion.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion_recyclerview,parent,false); //Inflating the layout

		ViewHolder vhItem = new ViewHolder(v,viewType);

		return vhItem;
	}

	@Override
	public void onBindViewHolder(AdapterNotificacion.ViewHolder holder, int position) {
		notificacion = (Notificacion)this.notificaciones.get(position);

		holder.nombreNotificacion.setText(notificacion.getNombre());
		holder.descripcionNotificacion.setText(notificacion.getDescripcion());
		Date fecha = notificacion.getFecha();
		if (fecha != null) {
			Date tiempo = new Date(fecha.getTime() - new Date().getTime());
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(tiempo);
			int horas = calendar.get(Calendar.HOUR);
			String hace = "Hace " + horas + " horas";
			holder.fechaNotificacion.setText(hace);
		}
		String textoBoton="";
		if(notificacion.getUsuario()!=null)
			textoBoton= notificacion.getUsuario().getNombre();
		else if(notificacion.getProyecto()!=null)
			textoBoton= notificacion.getProyecto().getNombre();
		holder.nombreUsuarioOProyecto.setText(textoBoton);


		holder.nombreNotificacion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cargaElemento();
			}
		});
		holder.descripcionNotificacion.setOnClickListener(new View.OnClickListener() {
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
		return ((Notificacion) notificaciones.get(position)).getId();
	}

	@Override
	public int getItemCount() {
		return notificaciones.size();
	}


	private void cargaElemento(){
			if(notificacion.getUsuario()!=null){
				//TODO CUANDO ESTE HECHA LA ACTIVITY USUARIO
				Intent intent = new Intent(context,UsuarioActivity.class);
				intent.putExtra("usuario",notificacion.getUsuario());
				context.startActivity(intent);
			}else if(notificacion.getProyecto()!=null) {
				Intent intent = new Intent(context,ProyectoActivity.class);
				intent.putExtra("proyecto",notificacion.getProyecto());
				context.startActivity(intent);
			}
	}

	public void replaceData(ArrayList<Notificacion> items) {
		setList(items);
		notifyDataSetChanged();
	}

	public void setList(ArrayList<Notificacion> list) {
		this.notificaciones = list;
	}

	public void addItem(Notificacion pushMessage) {
		notificaciones.add(0, pushMessage);
		notifyItemInserted(0);
	}

}