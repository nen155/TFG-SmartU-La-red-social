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
import com.smartu.adaptadores.AdapterComentario;
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.CallBackHebras;
import com.smartu.hebras.HComentarios;
import com.smartu.modelos.Comentario;
import com.smartu.utilidades.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;


/**
 * Una subclase simple {@link Fragment}.
 * Usa el método factoría {@link FragmentComentarios#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentComentarios extends Fragment implements CallBackHebras{
    //ArrayList de comentarios para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Comentario> comentarios = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_COMENTARIOS = "comentarios";
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_FILTRO = "filtro";
    private boolean filtro=false;
    //Muestra el mensaje de que no hay notificaciones
    private LinearLayout mNoNotificacionView;
    private RecyclerView recyclerViewComentarios;
    //Va hacer de listener para cuando llegue al final del RecyclerView
    //y necesite cargar más elementos
    private EndlessRecyclerViewScrollListener scrollListener;

    private AdapterComentario adapterComentario;

    public FragmentComentarios() {
        // Constructor vacío es necesario
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param comentarios Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentComentarios newInstance(ArrayList<Comentario> comentarios,boolean filtro) {
        FragmentComentarios fragment = new FragmentComentarios();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_COMENTARIOS, comentarios);
        args.putBoolean(ARG_FILTRO,filtro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comentarios = getArguments().getParcelableArrayList(ARG_COMENTARIOS);
            filtro = getArguments().getBoolean(ARG_FILTRO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_comentarios_recientes, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewComentarios = (RecyclerView) fragmen.findViewById(R.id.recyclerComentarios);
        mNoNotificacionView = (LinearLayout) fragmen.findViewById(R.id.noMessages);
        recyclerViewComentarios.setLayoutManager(llm);
        // Guardo la instancia para poder llamar a `resetState()` para nuevas busquedas
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // El evento sólo se provoca cuando necesito añadir más elementos
                cargarMasComentarios(page);
            }
        };


        return fragmen;
    }
    public void cargarMasComentarios(int offset) {
        //Establezco el número de elmentos que deberia contener en total
        scrollListener.setTotalServer(adapterComentario.getTotalElementosServer());
        //Para cargar a partir del tamaño del map
        //por si uso filtros
        if(offset< Almacen.sizeComentarios())
            offset=Almacen.sizeComentarios();

        HComentarios hComentarios = new HComentarios(adapterComentario,offset,getActivity());
        hComentarios.sethComentarios(hComentarios);
        hComentarios.setCallBackHebras(this);
        hComentarios.execute();
    }
    /**
     * Cambia la visibilidad del mensaje de qeu NO hay notificaciones en el RecyclerView
     * @param empty
     */
    public void muestraSinNotificaciones(boolean empty) {
        recyclerViewComentarios.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoNotificacionView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterComentario= new AdapterComentario(getContext(), comentarios);
        adapterComentario.setFiltro(filtro);
        recyclerViewComentarios.setAdapter(adapterComentario);
        // Adds the scroll listener to RecyclerView
        recyclerViewComentarios.addOnScrollListener(scrollListener);

        if(comentarios.size()==0)
            cargarMasComentarios(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(comentarios!=null)
            muestraSinNotificaciones(comentarios.size()<=0);
        else
            muestraSinNotificaciones(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void terminada() {

        //Establezo el número por el que va el contenedor del Almacen
        scrollListener.setTamAlmacen(Almacen.sizeNotificaciones());
        //Por si lo que cargo durante los filtros no es sufiente continuo cargando
        //para buscar todas las notificaciones posibles que coincidan con el filtro del usuario
        if(filtro && !scrollListener.isFin() && Almacen.sizeNotificaciones() < adapterComentario.getTotalElementosServer()
                && scrollListener.getLastVisibleItemPosition()+scrollListener.getVisibleThreshold() >adapterComentario.getItemCount()){
            cargarMasComentarios(0);
            scrollListener.setLoading(true);
        }

        if(adapterComentario.getItemCount()==0 && Almacen.sizeComentarios() < adapterComentario.getTotalElementosServer())
            cargarMasComentarios(0);
        else
            muestraSinNotificaciones(adapterComentario.getItemCount()==0);

    }
}
