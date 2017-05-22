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
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentProyectos;
import com.smartu.vistas.LoginActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class AdapterProyecto extends RecyclerView.Adapter<AdapterProyecto.ViewHolder> {
	private Context context;
	private ArrayList<Proyecto> proyectos;
	private FragmentProyectos.OnProyectoSelectedListener onProyectoSelectedListener;
	private Proyecto proyecto;
	private ImageView imgBuenaIdeaEditable;
	private Usuario usuarioSesion;
	private HBuenaIdea hBuenaIdea;


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
		Picasso.with(context).load(proyecto.getMisArchivos().get(0).getUrl()).into(holder.imgProyecto);
		holder.nombreProyecto.setText(proyecto.getNombre());
		holder.descripcionProyecto.setText(proyecto.getDescripcion());
		holder.nombreUsuario.setText(proyecto.getUsuario().getNombre());

		holder.imgProyecto.setOnClickListener(cargaProyecto());
		holder.descripcionProyecto.setOnClickListener(cargaProyecto());
		holder.nombreProyecto.setOnClickListener(cargaProyecto());
		//Asigno la refrencia al elemento pues lo voy a necesitar en la hebra
		imgBuenaIdeaEditable=holder.imgBuenaIdea;

		holder.imgBuenaIdea.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				usuarioSesion = Sesion.getUsuario(context);
				//Si el usuario ha iniciado sesión
				if (usuarioSesion != null) {
                /*Hacer la consulta para el insert en la tabla de seguidores*/
					hBuenaIdea = new HBuenaIdea();
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
				intent.putExtra("usuario",proyecto.getUsuario());
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
	////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Hebra para insertar el seguidor
	 */
	private class HBuenaIdea extends AsyncTask<Void, Void, String> {

		HBuenaIdea() {

		}

		@Override
		protected String doInBackground(Void... params) {

			String resultado = null;
			//Construyo el JSON
			String seguir = "\"buenaidea\":{\"idUsuario\":\"" + usuarioSesion.getId() + "\",\"idProyecto\":\"" + proyecto.getId() + "\"" +
					",\"fecha\":\"" + new Date() + "\"}";
			//Cojo el resultado en un String
			resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaBuenaIdea, seguir, "POST");

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
					if(res.has("resultado") && res.getString("resutlado").compareToIgnoreCase("ok")==0){
						imgBuenaIdeaEditable.setImageResource(R.drawable.buenaidea);
					}else
						Toast.makeText(context,"No se puede seguir a este usuario",Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else {
				Toast.makeText(context,"Fallo en la conexión",Toast.LENGTH_SHORT).show();
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
	}
}