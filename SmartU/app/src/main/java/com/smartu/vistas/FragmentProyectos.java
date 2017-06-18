package com.smartu.vistas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smartu.R;
import com.smartu.adaptadores.AdapterProyecto;
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.CallBackHebras;
import com.smartu.hebras.HProyectos;
import com.smartu.modelos.Proyecto;
import com.smartu.utilidades.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;


/**
 * Una subclase simple {@link Fragment}.
 * Las Activities que lo contentgan deben de implementar
 * {@link OnProyectoSelectedListener} interfaz
 * para manejar la interacción de eventos.
 * Usa el método factoría {@link FragmentProyectos#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentProyectos extends Fragment implements CallBackHebras {
    //ArrayList de proyectos para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Proyecto> proyectos = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_PROYECTOS = "proyectos";
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_FILTRO = "filtro";
    private RecyclerView recyclerViewProyectos;
    private AdapterProyecto adapterProyecto;
    private OnProyectoSelectedListener mListener;
    private boolean filtro=false;
    //Solo se utiliza para los proyectos de un usuario
    //Es static por que no se recarga cuando se destruye el fragment
    //por lo que necesita ser static
    private static int idUsuario=-1;
    //Muestra el mensaje de que no hay notificaciones
    private LinearLayout mNoNotificacionView;
    //Va hacer de listener para cuando llegue al final del RecyclerView
    //y necesite cargar más elementos
    private EndlessRecyclerViewScrollListener scrollListener;

    public FragmentProyectos() {
        // Constructor vacío es necesario
    }

    public void setIdUsuario(int idUsuario) {
        FragmentProyectos.idUsuario = idUsuario;
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param proyectos Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentProyectos newInstance(ArrayList<Proyecto> proyectos,boolean filtro) {
        FragmentProyectos fragment = new FragmentProyectos();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PROYECTOS, proyectos);
        args.putBoolean(ARG_FILTRO,filtro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            proyectos = getArguments().getParcelableArrayList(ARG_PROYECTOS);
            filtro = getArguments().getBoolean(ARG_FILTRO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_proyectos_recientes, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProyectos = (RecyclerView) fragmen.findViewById(R.id.recyclerProyectos);
        mNoNotificacionView = (LinearLayout) fragmen.findViewById(R.id.noMessages);
        recyclerViewProyectos.setLayoutManager(llm);
        // Guardo la instancia para poder llamar a `resetState()` para nuevas busquedas
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // El evento sólo se provoca cuando necesito añadir más elementos
                cargarMasProyectos(page);
            }
        };


        return fragmen;
    }
    /**
     * Carga hasta 10 comentarios si hubiese a partir del offset
     * que le ofrece el método LoadMore
     * @param offset
     */
    public void cargarMasProyectos(int offset) {
        //Establezco el número de elmentos que deberia contener en total
        scrollListener.setTotalServer(adapterProyecto.getTotalElementosServer());
        //Para cargar a partir del tamaño del map
        //por si uso filtros
        if(offset< Almacen.sizeProyectos())
            offset=Almacen.sizeProyectos();

        HProyectos hProyectos = new HProyectos(adapterProyecto,offset,getActivity());
        hProyectos.sethProyectos(hProyectos);
        hProyectos.setIdUsuario(idUsuario);
        hProyectos.setCallback(this);
        hProyectos.execute();
    }


    /**
     * Cambia la visibilidad del mensaje de qeu NO hay notificaciones en el RecyclerView
     * @param empty
     */
    public void muestraSinNotificaciones(boolean empty) {
        recyclerViewProyectos.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoNotificacionView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterProyecto = new AdapterProyecto(getActivity(), proyectos,mListener);
        adapterProyecto.setFiltro(filtro);
        recyclerViewProyectos.setAdapter(adapterProyecto);
        // Adds the scroll listener to RecyclerView
        recyclerViewProyectos.addOnScrollListener(scrollListener);

        if(proyectos.size()==0)
            cargarMasProyectos(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(proyectos!=null)
            muestraSinNotificaciones(proyectos.size()<=0);
        else
            muestraSinNotificaciones(true);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onButtonPressed(int idProyecto) {
        if (mListener != null) {
            mListener.onProyectoSeleccionado(idProyecto);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProyectoSelectedListener) {
            mListener = (OnProyectoSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar OnProyectoSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void terminada() {
        //Establezo el número por el que va el contenedor del Almacen
        scrollListener.setTamAlmacen(Almacen.sizeProyectos());
        //Por si lo que cargo durante los filtros no es sufiente continuo cargando
        //para buscar todas las notificaciones posibles que coincidan con el filtro del usuario
        if(filtro && !scrollListener.isFin() && Almacen.sizeProyectos() < adapterProyecto.getTotalElementosServer()
                && scrollListener.getLastVisibleItemPosition()+scrollListener.getVisibleThreshold() >adapterProyecto.getItemCount()){
            cargarMasProyectos(0);
            scrollListener.setLoading(true);
        }


        if(adapterProyecto.getItemCount()==0 && Almacen.sizeProyectos() < adapterProyecto.getTotalElementosServer())
            cargarMasProyectos(0);
        else
            muestraSinNotificaciones(adapterProyecto.getItemCount()==0 );
    }

    /**
     * Esta interfaz debe ser implementada por las activities que contienen este
     * fragment para permitir una interacción con este fragment y comunicar a
     * a la activity y potencialmente otros fragments contenidos en esta
     * activity.
     * <p>
     * Referencias a la lección Android Training <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnProyectoSelectedListener {
        void onProyectoSeleccionado(int idProyecto);
    }
}
