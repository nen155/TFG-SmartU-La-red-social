package com.smartu.adaptadores;

import android.content.Context;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.modelos.BuenaIdea;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentProyectos;
import com.smartu.vistas.LoginActivity;
import com.smartu.vistas.ProyectoActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;


public class AdapterProyecto extends RecyclerView.Adapter<AdapterProyecto.ViewHolder> {
	private Context context;
	private ArrayList<Proyecto> proyectos;
	private FragmentProyectos.OnProyectoSelectedListener onProyectoSelectedListener;
	private Proyecto proyecto;
	private ImageView imgBuenaIdeaEditable;
	private Usuario usuarioSesion;
	private HBuenaIdea hBuenaIdea;
	private Optional<BuenaIdea> buenaIdea1;


	public AdapterProyecto(Context context, ArrayList<Proyecto> items, FragmentProyectos.OnProyectoSelectedListener onProyectoSelectedListener) {
		super();
		this.context = context;
		this.proyectos = items;
		this.onProyectoSelectedListener = onProyectoSelectedListener;
	}

	//Creating a ViewHolder which extends the RecyclerView View Holder
	// ViewHolder are used to to store the inflated views in order to recycle them

	public static class ViewHolder extends RecyclerView.ViewHolder {

		TextView nombreProyecto;
		TextView descripcionProyecto;
		ImageView imgProyecto;
		ImageView imgBuenaIdea;
		Button nombreUsuario;

		public ViewHolder(View itemView, int viewType) {
			super(itemView);
			nombreProyecto = (TextView) itemView.findViewById(R.id.nombre_usuario);
			descripcionProyecto = (TextView) itemView.findViewById(R.id.descripcion_proyecto);
			imgProyecto = (ImageView) itemView.findViewById(R.id.img_proyecto);
			nombreUsuario = (Button) itemView.findViewById(R.id.nombre_usuario_proyecto);
			imgBuenaIdea =(ImageView) itemView.findViewById(R.id.img_idea_proyecto);
		}

	}

	@Override
	public AdapterProyecto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto_recyclerview,parent,false); //Inflating the layout

		ViewHolder vhItem = new ViewHolder(v,viewType);

		return vhItem;
	}

	@Override
	public void onBindViewHolder(AdapterProyecto.ViewHolder holder, int position) {
		proyecto = (Proyecto)this.proyectos.get(position);
		Picasso.with(context).load(proyecto.getImagenDestacada()).into(holder.imgProyecto);
		holder.nombreProyecto.setText(proyecto.getNombre());
		holder.descripcionProyecto.setText(proyecto.getDescripcion());
		//TODO Para cuando cargue usuarios
		//holder.nombreUsuario.setText(proyecto.getPropietario().getNombre());

		holder.imgProyecto.setOnClickListener(cargaProyecto());
		holder.descripcionProyecto.setOnClickListener(cargaProyecto());
		holder.nombreProyecto.setOnClickListener(cargaProyecto());
		//Asigno la refrencia al elemento pues lo voy a necesitar en la hebra
		imgBuenaIdeaEditable=holder.imgBuenaIdea;
		//Cargo las preferencias del usuario si tuviese sesión
		cargarPreferenciasUsuario();

		holder.imgBuenaIdea.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				usuarioSesion = Sesion.getUsuario(context);
				//Si el usuario ha iniciado sesión
				if (usuarioSesion != null) {
					//Actualizo el botón
					imgBuenaIdeaEditable.setPressed(!imgBuenaIdeaEditable.isPressed());
					//Compruebo como ha quedado su estado después de hacer click
					if (imgBuenaIdeaEditable.isPressed()) {
						imgBuenaIdeaEditable.setImageResource(R.drawable.buenaidea);
						Toast.makeText(context,"Genial!, este proyecto te parece buena idea!",Toast.LENGTH_SHORT).show();
						//Inicializo la hebra con 0 pues voy a añadir una nueva idea
						hBuenaIdea = new HBuenaIdea(0);
					}
					else {
						imgBuenaIdeaEditable.setImageResource(R.drawable.idea);
						Toast.makeText(context,"¿Ya no te parece buena idea?",Toast.LENGTH_SHORT).show();
						//Inicializo la hebra con el id de la buena idea que encontré
						hBuenaIdea = new HBuenaIdea(buenaIdea1.get().getId());
					}
					hBuenaIdea.execute();
				}
				else
				{
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);
				}
			}
		});

		holder.nombreUsuario.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(context,UsuarioActivity.class);
				intent.putExtra("usuario",proyecto.getPropietario());
				startActivity(intent);*/
			}
		});
	}

	@Override
	public long getItemId(int position) {
		return ((Proyecto)proyectos.get(position)).getId();
	}

	@Override
	public int getItemCount() {
		return proyectos.size();
	}


	private View.OnClickListener cargaProyecto(){
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onProyectoSelectedListener.onProyectoSeleccionado(proyecto);
			}
		};
	}
	/**
	 * Comprueba si el usuario ha dado buena idea al proyecto
	 */
	private void cargarPreferenciasUsuario(){
		//Cargo las preferencias del usuario
		if(usuarioSesion!=null) {
			//Compruebo si el usuario le ha dado antes a buena idea a este proyecto
			buenaIdea1 = proyecto.getBuenaIdea().stream().filter(buenaIdea -> buenaIdea.getIdUsuario() == usuarioSesion.getId() && buenaIdea.getIdProyecto() == proyecto.getId()).findFirst();
			boolean usuarioBuenaidea = buenaIdea1.isPresent();
			//Si es así lo dejo presionado y le cambio la imagen
			imgBuenaIdeaEditable.setPressed(usuarioBuenaidea);
			if (usuarioBuenaidea)
				imgBuenaIdeaEditable.setImageResource(R.drawable.buenaidea);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Hebra para insertar el seguidor
	 */
	private class HBuenaIdea extends AsyncTask<Void, Void, String> {

		private int idIdea = 0;

		HBuenaIdea(int idIdea) {
			this.idIdea = idIdea;
		}

		@Override
		protected String doInBackground(Void... params) {

			String resultado = null;
			//Construyo el JSON
			String buenaidea="";
			if(idIdea!=0) {
				buenaidea = "\"buenaidea\":{\"idUsuario\":\"" + usuarioSesion.getId() + "\",\"idProyecto\":\"" + proyecto.getId() + "\"" +
						",\"fecha\":\"" + new Date() + "\"}";
				//Recojo el resultado en un String
				resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaBuenaIdea, buenaidea, "POST");
			}
			else
			{
				buenaidea ="\"buenaidea\":{\"idUsuario\":\""+idIdea+ "\"}";
				resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.eliminaBuenaIdea, buenaidea, "POST");
			}
			return resultado;
		}

		@Override
		protected void onPostExecute(String resultado) {
			super.onPostExecute(resultado);
			//Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
			hBuenaIdea = null;
			//Obtengo el objeto JSON con el resultado
			JSONObject res=null;
			try {
				res = new JSONObject(resultado);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//Si tengo objeto compruebo el resultado y si es ok cambio el texto al botón
			//Sino muestro mensaje por pantalla
			if (res!=null) {
				try {
					if(res.has("resultado") && res.getString("resutlado").compareToIgnoreCase("ok")!=0){
						reestablecerEstado();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else {
				reestablecerEstado();
			}
		}

		@Override
		protected void onCancelled(String resultado) {
			super.onCancelled(resultado);
			//Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
			hBuenaIdea = null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
			hBuenaIdea = null;
		}
		private void reestablecerEstado(){
			Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
			imgBuenaIdeaEditable.setPressed(!imgBuenaIdeaEditable.isPressed());
			if(imgBuenaIdeaEditable.isPressed())
				imgBuenaIdeaEditable.setImageResource(R.drawable.buenaidea);
			else
				imgBuenaIdeaEditable.setImageResource(R.drawable.idea);

		}
	}

}