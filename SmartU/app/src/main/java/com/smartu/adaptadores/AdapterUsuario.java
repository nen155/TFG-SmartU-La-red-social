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
import com.smartu.modelos.Usuario;
import com.smartu.vistas.FragmentUsuarios;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ViewHolder> {
	private Context context;
	private ArrayList<Usuario> usuarios;
	private FragmentUsuarios.OnUsuarioSelectedListener onUsuarioSelectedListener;
	private Usuario usuario;


	public AdapterUsuario(Context context, ArrayList<Usuario> items, FragmentUsuarios.OnUsuarioSelectedListener onUsuarioSelectedListener) {
		super();
		this.context = context;
		this.usuarios = items;
		this.onUsuarioSelectedListener = onUsuarioSelectedListener;
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
	public AdapterUsuario.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario_recyclerview,parent,false); //Inflating the layout

		ViewHolder vhItem = new ViewHolder(v,viewType);

		return vhItem;
	}

	@Override
	public void onBindViewHolder(AdapterUsuario.ViewHolder holder, int position) {
		usuario = (Usuario) this.usuarios.get(position);
		Picasso.with(context).load(usuario.getMisArchivos().get(0).getUrl()).into(holder.imgProyecto);
		holder.nombreProyecto.setText(usuario.getNombre());

		holder.imgProyecto.setOnClickListener(cargaProyecto());
		holder.descripcionProyecto.setOnClickListener(cargaProyecto());
		holder.nombreProyecto.setOnClickListener(cargaProyecto());

		holder.nombreUsuario.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(context,UsuarioActivity.class);
				intent.putExtra("usuario",usuario.getUsuario());
				startActivity(intent);*/
			}
		});
	}

	@Override
	public long getItemId(int position) {
		return ((Usuario) usuarios.get(position)).getId();
	}

	@Override
	public int getItemCount() {
		return usuarios.size();
	}


	private View.OnClickListener cargaProyecto(){
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onUsuarioSelectedListener.onUsuarioSeleccionado(usuario);
			}
		};
	}

}