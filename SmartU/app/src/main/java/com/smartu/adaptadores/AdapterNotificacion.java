package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Publicacion;
import com.smartu.modelos.Notificacion;
import com.smartu.utilidades.Comparador;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.ProyectoActivity;
import com.smartu.vistas.UsuarioActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import java8.util.stream.StreamSupport;


public class AdapterNotificacion extends RecyclerView.Adapter<AdapterNotificacion.ViewHolder> implements OperacionesAdapter {
	private Context context;
	private ArrayList<Notificacion> notificaciones;
	private Notificacion notificacion;
	private boolean filtro;

	//Es el número total de elementos que hay en el server
	//tengo que recogerlo de las hebras de consulta
	private int totalElementosServer = 0;

	// Tres tipos de vistas para saber si es un ProgressBar lo que muestro o la vista normal
	public static final int VIEW_TYPE_LOADING = 0;
	public static final int VIEW_TYPE_ACTIVITY = 1;
	public static final int VIEW_TYPE_FINAL = 2;


	public void setTotalElementosServer(int totalElementosServer) {
		this.totalElementosServer = totalElementosServer;
	}

	public int getTotalElementosServer() {
		return totalElementosServer;
	}

	public void setFiltro(boolean filtro) {
		this.filtro = filtro;
	}

	public AdapterNotificacion(Context context, ArrayList<Notificacion> items) {
		super();
		this.context = context;
		this.notificaciones = items;
		Collections.sort(notificaciones, new Comparador.ComparaNotificaciones());
	}



	//Creating a ViewHolder which extends the RecyclerView View Holder
	// ViewHolder are used to to store the inflated views in order to recycle them

	public static class ViewHolder extends RecyclerView.ViewHolder {
		int tipoView;
		TextView nombreNotificacion;
		TextView descripcionNotificacion;
		TextView fechaNotificacion;
		Button nombreUsuarioOProyecto;

		public ViewHolder(View itemView, int viewType) {
			super(itemView);
			if(viewType==VIEW_TYPE_ACTIVITY) {
			nombreNotificacion = (TextView) itemView.findViewById(R.id.nombre_notificacion);
			descripcionNotificacion = (TextView) itemView.findViewById(R.id.descripcion_notificacion);
			fechaNotificacion = (TextView) itemView.findViewById(R.id.fecha_notificacion);
			nombreUsuarioOProyecto = (Button) itemView.findViewById(R.id.nombre_usuario_o_proyecto);
				tipoView=1;
			}else{
				tipoView=0;
			}
		}

	}

	@Override
	public AdapterNotificacion.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion_recyclerview, parent, false); //Inflating the layout

			ViewHolder vhItem = new ViewHolder(v, viewType);

			return vhItem;
		}
	}

	@Override
	public void onBindViewHolder(AdapterNotificacion.ViewHolder holder, int position) {
		//Sino es el último elemento ni es un progress bar pues muestro el elemento que me toca
		if(holder.tipoView==1) {
			notificacion = (Notificacion) this.notificaciones.get(position);

			holder.nombreNotificacion.setText(notificacion.getNombre());
			holder.descripcionNotificacion.setText(notificacion.getDescripcion());
			Date fecha = notificacion.getFecha();
			if (fecha != null) {
				long res = (((new Date().getTime()-fecha.getTime())/1000)/60)/60;
				String hace ="";
				if(res>24) {
					res = res / 24;
					if(res>1)
						hace = "Hace " + res + " dias";
					else
						hace = "Hace " + res + " día";
				}else if(res>1)
					hace = "Hace " + res + " horas";
				else if(res==1){
					hace = "Hace " + res + " hora";
				}
				else if(res<1) {
					res = res/60;
					if(res>1)
						hace = "Hace " + res + " minutos";
					else if(res==1){
						hace = "Hace " + res + " minuto";
					}
					else if(res<1) {
						res=res/60;
						hace = "Hace " + res + " segundos";
					}
				}

				holder.fechaNotificacion.setText(hace);
			}
			String textoBoton = "";
			if (notificacion.getIdUsuario() != 0)
				textoBoton = notificacion.getUsuario();
			else if (notificacion.getIdProyecto() != 0)
				textoBoton = notificacion.getProyecto();
			holder.nombreUsuarioOProyecto.setText(textoBoton);


			holder.nombreNotificacion.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					cargaElemento(notificaciones.get(position));
				}
			});
			holder.descripcionNotificacion.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					cargaElemento(notificaciones.get(position));
				}
			});
			holder.nombreUsuarioOProyecto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					cargaElemento(notificaciones.get(position));
				}
			});
		}
	}

	@Override
	public long getItemId(int position) {
		return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? notificaciones.get(position).getId()
				: -1;
	}
	/**
	 * Devuelve el tipo de fila,
	 * El ultimo elemento es el de loading
	 */
	@Override
	public int getItemViewType(int position) {
		if (position >= notificaciones.size() && position>=totalElementosServer && totalElementosServer > 0){
			return VIEW_TYPE_FINAL;
		}else if(position >= notificaciones.size()){
			return VIEW_TYPE_FINAL;
		}else
			return VIEW_TYPE_ACTIVITY;
	}

	@Override
	public int getItemCount() {
		if(notificaciones.size()==0)
			return notificaciones.size();
		return notificaciones.size()+1;
	}


	private void cargaElemento(Notificacion notificacion){
			if(notificacion.getIdUsuario()!=0){
				Intent intent = new Intent(context,UsuarioActivity.class);
				intent.putExtra("idUsuario",  notificacion.getIdUsuario());
				context.startActivity(intent);
			}else if(notificacion.getIdProyecto()!=0) {
				Intent intent = new Intent(context,ProyectoActivity.class);
				intent.putExtra("idProyecto", notificacion.getIdProyecto());
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

	public void addItemTop(Publicacion publicacion) {
		Notificacion notificacion =(Notificacion) publicacion;

		boolean esta = StreamSupport.stream(notificaciones).filter(usuario1 -> usuario1.getId() == notificacion.getId()).findAny().isPresent();
		if(!esta) {
			//La añado al almacen
			Almacen.add(notificacion);
			//Compruebo si tengo que filtrar
			if(filtro) {
				//Si tengo que mostrarla porque es perteneciente al filtro
				if (Almacen.filtra(notificacion, Sesion.getUsuario(context))) {
					//Compruebo sino la he añadido antes
					esta = Almacen.isFiltradaPresent(notificacion);
					//Sino la he añadido la añado al ArrayList
					if (!esta)
						Almacen.addFiltro(notificacion);

					notificaciones.add(0, notificacion);
					notifyItemInserted(0);
				}
			}else {
				notificaciones.add(0, notificacion);
				notifyItemInserted(0);
			}
		}
	}
	@Override
	public void addItem(Publicacion publicacion) {
		Notificacion notificacion =(Notificacion) publicacion;
		//Compruebo si esta en el array del adapater
		boolean esta = StreamSupport.stream(notificaciones).filter(usuario1 -> usuario1.getId() == notificacion.getId()).findAny().isPresent();
		boolean add=false;
		//Sino esta
		if(!esta) {
			//La añado al almacen
			Almacen.add(notificacion);
			//Añado las notificaciones filtradas igualmente pero no notifico al adapter
			//Si tengo que mostrarla porque es perteneciente al filtro
			if (Almacen.filtra(notificacion, Sesion.getUsuario(context))) {
				//Compruebo sino la he añadido antes
				esta = Almacen.isFiltradaPresent(notificacion);
				//Sino la he añadido la añado al ArrayList
				if (!esta) {
					Almacen.addFiltro(notificacion);
					add=true;
				}
			}
			//Compruebo si tengo que filtrar
			if(filtro) {
				//Si estoy en al adpater con filtro y he añadido una
				//lo notifico
				if(add)
					notifyItemInserted(notificaciones.size()-1);
			}else {
				notificaciones.add(notificacion);
				notifyItemInserted(notificaciones.size() - 1);
			}
		}
	}
}