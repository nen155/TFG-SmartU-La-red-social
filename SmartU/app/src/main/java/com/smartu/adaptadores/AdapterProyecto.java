package com.smartu.adaptadores;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.vistas.FragmentProyectos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterProyecto extends RecyclerView.Adapter<AdapterProyecto.ViewHolder> {
	private Context context;
	private ArrayList<Proyecto> proyectos;
	private FragmentProyectos.OnProyectoSelectedListener onProyectoSelectedListener;
	private Proyecto proyecto;


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
		Button nombreUsuario;

		public ViewHolder(View itemView, int viewType) {
			super(itemView);
			nombreProyecto = (TextView) itemView.findViewById(R.id.nombre_usuario);
			descripcionProyecto = (TextView) itemView.findViewById(R.id.descripcion_proyecto);
			imgProyecto = (ImageView) itemView.findViewById(R.id.img_proyecto);
			nombreUsuario = (Button) itemView.findViewById(R.id.nombre_usuario_proyecto);

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

}