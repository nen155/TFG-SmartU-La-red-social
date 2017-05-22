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
import com.smartu.modelos.Novedad;
import com.smartu.vistas.FragmentNovedades;

import java.util.ArrayList;


public class AdapterNovedad extends RecyclerView.Adapter<AdapterNovedad.ViewHolder> {
	private Context context;
	private ArrayList<Novedad> novedades;
	private FragmentNovedades.OnNovedadSelectedListener onNovedadSelectedListener;
	private Novedad novedad;


	public AdapterNovedad(Context context, ArrayList<Novedad> items, FragmentNovedades.OnNovedadSelectedListener onNovedadSelectedListener) {
		super();
		this.context = context;
		this.novedades = items;
		this.onNovedadSelectedListener = onNovedadSelectedListener;
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
	public AdapterNovedad.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_novedad_recyclerview,parent,false); //Inflating the layout

		ViewHolder vhItem = new ViewHolder(v,viewType);

		return vhItem;
	}

	@Override
	public void onBindViewHolder(AdapterNovedad.ViewHolder holder, int position) {
		novedad = (Novedad)this.novedades.get(position);

		holder.nombreProyecto.setText(novedad.getNombre());
		holder.descripcionProyecto.setText(novedad.getDescripcion());

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
		return ((Novedad) novedades.get(position)).getId();
	}

	@Override
	public int getItemCount() {
		return novedades.size();
	}


	private View.OnClickListener cargaProyecto(){
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onNovedadSelectedListener.onNovedadSeleccionado(novedad);
			}
		};
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