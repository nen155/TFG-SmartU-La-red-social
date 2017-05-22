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
import com.smartu.modelos.Comentario;
import com.smartu.vistas.FragmentComentarios;

import java.util.ArrayList;


public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.ViewHolder> {
	private Context context;
	private ArrayList<Comentario> comentarios;
	private FragmentComentarios.OnComentarioSelectedListener onComentarioSelectedListener;
	private Comentario comentario;


	public AdapterComentario(Context context, ArrayList<Comentario> items, FragmentComentarios.OnComentarioSelectedListener onComentarioSelectedListener) {
		super();
		this.context = context;
		this.comentarios = items;
		this.onComentarioSelectedListener = onComentarioSelectedListener;
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
	public AdapterComentario.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario_recyclerview,parent,false); //Inflating the layout

		ViewHolder vhItem = new ViewHolder(v,viewType);

		return vhItem;
	}

	@Override
	public void onBindViewHolder(AdapterComentario.ViewHolder holder, int position) {
		comentario = (Comentario) this.comentarios.get(position);

		holder.descripcionProyecto.setOnClickListener(cargaProyecto());
		holder.nombreProyecto.setOnClickListener(cargaProyecto());

		holder.nombreUsuario.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(context,UsuarioActivity.class);
				intent.putExtra("comentario",comentario.getUsuario());
				startActivity(intent);*/
			}
		});
	}

	@Override
	public long getItemId(int position) {
		return ((Comentario) comentarios.get(position)).getId();
	}

	@Override
	public int getItemCount() {
		return comentarios.size();
	}


	private View.OnClickListener cargaProyecto(){
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onComentarioSelectedListener.onComentarioSeleccionado(comentario);
			}
		};
	}

}