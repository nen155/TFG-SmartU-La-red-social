package com.smartu.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.HOcuparVacante;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.RedSocial;
import com.smartu.modelos.SolicitudUnion;
import com.smartu.modelos.Usuario;
import com.smartu.modelos.Vacante;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.AreasActivity;
import com.smartu.vistas.ProyectoActivity;
import com.smartu.vistas.UsuarioActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import java8.util.Optional;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;


public class AdapterSolicitudes extends RecyclerView.Adapter<AdapterSolicitudes.ViewHolder> {
    private Context context;
    private ArrayList<SolicitudUnion> solicitudUnions;
    private SolicitudUnion solicitudUnion;


    public AdapterSolicitudes(Context context, ArrayList<SolicitudUnion> items) {
        super();
        this.context = context;
        this.solicitudUnions = items;
    }

    //Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombreUsuario;
        TextView fecha;
        TextView nombreProyecto;
        TextView descripcion;
        Button button;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            nombreUsuario = (TextView) itemView.findViewById(R.id.nombre_usuario_solicitud);
            fecha = (TextView) itemView.findViewById(R.id.fecha_solicitud);
            nombreProyecto = (TextView) itemView.findViewById(R.id.nombre_proyecto_solicitud);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion_union);
            button = (Button) itemView.findViewById(R.id.boton_aceptar_solicitud);

        }

    }

    @Override
    public AdapterSolicitudes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solicitudes, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType);

        return vhItem;
    }

    @Override
    public void onBindViewHolder(final AdapterSolicitudes.ViewHolder holder, int position) {
        solicitudUnion = (SolicitudUnion) this.solicitudUnions.get(position);
        holder.nombreProyecto.setText(solicitudUnion.getProyecto());

        Usuario u = new Usuario();
        Almacen.buscar(solicitudUnion.getIdUsuarioSolicitante(),u,context);
        holder.nombreUsuario.setText(u.getUser());
        if(solicitudUnion.getDescripcion()!=null)
            holder.descripcion.setText(solicitudUnion.getDescripcion());

        holder.nombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UsuarioActivity.class);
                intent.putExtra("idUsuario",u.getId());
                context.startActivity(intent);
            }
        });
        holder.nombreProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProyectoActivity.class);
                intent.putExtra("idProyecto",solicitudUnion.getIdProyecto());
                context.startActivity(intent);
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if(solicitudUnion.getFecha()!=null)
            holder.fecha.setText(simpleDateFormat.format(solicitudUnion.getFecha()));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Elminar la solicitud de union
                //Buscar vacante y eliminar
                //Añadir a colaboradores
                //Enviar aviso de que es colaborador
                Usuario usuarioSesion = Sesion.getUsuario(context);
                ArrayList<Proyecto> proyectos =new ArrayList<Proyecto>();
                Almacen.buscarProyectos(usuarioSesion.getMisProyectos(),proyectos,context);
                Optional<Proyecto> proyectoOpcional = StreamSupport.stream(proyectos).filter(proyecto -> StreamSupport.stream(proyecto.getVacantesProyecto()).filter(vacante -> vacante.getId() == solicitudUnion.getIdVacante()).findAny().isPresent()).findAny();
                if(proyectoOpcional.isPresent()) {
                    Proyecto p = proyectoOpcional.get();
                    Optional<Vacante> vacanteOptional = StreamSupport.stream(p.getVacantesProyecto()).filter(vacante -> vacante.getId() == solicitudUnion.getIdVacante()).findAny();
                    if(vacanteOptional.isPresent()) {
                        //Añadir sobre todas las especialidades
                        if(vacanteOptional.get().getEspecialidades().size()>0) {
                            HOcuparVacante hOcuparVacante = new HOcuparVacante(context, vacanteOptional.get().getEspecialidades().get(0).getId(), solicitudUnion, AdapterSolicitudes.this);
                            hOcuparVacante.sethUsuarioInteresa(hOcuparVacante);
                            hOcuparVacante.execute();
                        }

                    }
                }
            }
        });

    }
    @Override
    public long getItemId(int position) {
        return ((SolicitudUnion) solicitudUnions.get(position)).getIdVacante();
    }
    public void eliminaSolicitud(){
        //Elimino la solictud de unión y notifico al
        solicitudUnions.remove(solicitudUnion);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return solicitudUnions.size();
    }

}