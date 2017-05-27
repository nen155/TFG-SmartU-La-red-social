package com.smartu.adaptadores;

import android.content.Context;

import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.hebras.HBuenaIdea;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentProyectos;
import com.smartu.vistas.LoginActivity;
import com.smartu.vistas.UsuarioActivity;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;



public class AdapterProyecto extends RecyclerView.Adapter<AdapterProyecto.ViewHolder> {
	private Context context;
	private ArrayList<Proyecto> proyectos;
	private FragmentProyectos.OnProyectoSelectedListener onProyectoSelectedListener;
	private Proyecto proyecto;
	private Usuario usuarioSesion;


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
		TextView contadorBuenaIdea;
		ImageView imgProyecto;
		ImageView imgBuenaIdea;
		Button nombreUsuario;

		public ViewHolder(View itemView, int viewType) {
			super(itemView);
			nombreProyecto = (TextView) itemView.findViewById(R.id.nombre_usuario);
			descripcionProyecto = (TextView) itemView.findViewById(R.id.descripcion_proyecto);
			contadorBuenaIdea =  (TextView) itemView.findViewById(R.id.buenaidea_contador_proyecto);
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
		Picasso.with(context).load(ConsultasBBDD.server+proyecto.getImagenDestacada()).into(holder.imgProyecto);
		holder.nombreProyecto.setText(proyecto.getNombre());
		if(proyecto.getDescripcion().length()>150){
			holder.descripcionProyecto.setText(proyecto.getDescripcion().substring(0,150)+"...");
		}else
			holder.descripcionProyecto.setText(proyecto.getDescripcion());

		//TODO Para cuando cargue usuarios
		//holder.nombreUsuario.setText(proyecto.getPropietario().getNombre());

		holder.imgProyecto.setOnClickListener(cargaProyecto());
		holder.descripcionProyecto.setOnClickListener(cargaProyecto());
		holder.nombreProyecto.setOnClickListener(cargaProyecto());

		//Cargo las preferencias del usuario si tuviese sesión
		cargarPreferenciasUsuario(holder.imgBuenaIdea);

		holder.imgBuenaIdea.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HBuenaIdea hBuenaIdea;
				usuarioSesion = Sesion.getUsuario(context);
				//Si el usuario ha iniciado sesión
				if (usuarioSesion != null) {
					//Actualizo el botón
					holder.imgBuenaIdea.setPressed(!holder.imgBuenaIdea.isPressed());
					//Compruebo como ha quedado su estado después de hacer click
					if (holder.imgBuenaIdea.isPressed()) {
						holder.imgBuenaIdea.setImageResource(R.drawable.buenaidea);
						Toast.makeText(context,"Genial!, este proyecto te parece buena idea!",Toast.LENGTH_SHORT).show();
						//Inicializo la hebra con false pues voy a añadir una nueva idea
						hBuenaIdea = new HBuenaIdea(false,context,proyecto,holder.imgBuenaIdea,holder.contadorBuenaIdea);
						//Para poder poner la referencia a null cuando termine la hebra
						hBuenaIdea.sethBuenaIdea(hBuenaIdea);
					}
					else {
						holder.imgBuenaIdea.setImageResource(R.drawable.idea);
						Toast.makeText(context,"¿Ya no te parece buena idea?",Toast.LENGTH_SHORT).show();
						//Inicializo la hebra con true para eliminar la buena idea de la BD.
						hBuenaIdea = new HBuenaIdea(true,context,proyecto,holder.imgBuenaIdea,holder.contadorBuenaIdea);
						//Para poder poner la referencia a null cuando termine la hebra
						hBuenaIdea.sethBuenaIdea(hBuenaIdea);
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
				Intent intent = new Intent(context,UsuarioActivity.class);
				intent.putExtra("usuario",proyecto.getPropietario());
				context.startActivity(intent);
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
	private void cargarPreferenciasUsuario(ImageView imgBuenaIdea){
		//Cargo las preferencias del usuario
		if(usuarioSesion!=null) {
			//Compruebo si el usuario le ha dado antes a buena idea a este proyecto
			boolean usuarioBuenaidea  = proyecto.getBuenaIdea().stream().anyMatch(buenaIdea -> buenaIdea.getIdUsuario() == usuarioSesion.getId());
			//Si es así lo dejo presionado y le cambio la imagen
			imgBuenaIdea.setPressed(usuarioBuenaidea);
			if (usuarioBuenaidea)
				imgBuenaIdea.setImageResource(R.drawable.buenaidea);
		}
	}


}