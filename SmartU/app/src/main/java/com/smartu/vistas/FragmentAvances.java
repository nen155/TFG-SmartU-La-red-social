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
import com.smartu.adaptadores.AdapterAvances;
import com.smartu.modelos.Avance;
import com.smartu.modelos.Proyecto;
import com.smartu.utilidades.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;


/**
 * Una subclase simple {@link Fragment}.
 * Usa el método factoría {@link FragmentAvances#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentAvances extends Fragment {
    //ArrayList de avances para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Avance> avances = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_AVANCES = "avances";
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_PROYECTO = "proyecto";
    private int idProyecto=-1;
    //Muestra el mensaje de que no hay notificaciones
    private LinearLayout mNoNotificacionView;
    private RecyclerView recyclerViewAvances;

    /**
     * Por si en un futuro se quiere cargar de poco en poco
     */
    //Va hacer de listener para cuando llegue al final del RecyclerView
    //y necesite cargar más elementos
    //private EndlessRecyclerViewScrollListener scrollListener;

    private static AdapterAvances adapterAvances;

    public FragmentAvances() {
        // Constructor vacío es necesario
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param avances Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentAvances newInstance(ArrayList<Avance> avances,int idProyecto) {
        FragmentAvances fragment = new FragmentAvances();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_AVANCES, avances);
        args.putInt(ARG_PROYECTO,idProyecto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            avances = getArguments().getParcelableArrayList(ARG_AVANCES);
            idProyecto = getArguments().getInt(ARG_PROYECTO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_avances_recientes, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAvances = (RecyclerView) fragmen.findViewById(R.id.recyclerAvances);
        mNoNotificacionView = (LinearLayout) fragmen.findViewById(R.id.noMessages);
        recyclerViewAvances.setLayoutManager(llm);

        /* Por si en un futuro se quiere cargar de poco en poco
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // El evento sólo se provoca cuando necesito añadir más elementos
                cargarMasAvances(page);
            }
        };*/


        return fragmen;
    }
   /* public void cargarMasAvances(int offset) {
        HAvances hAvances = new HAvances(adapterAvances,offset,getActivity());
        hAvances.sethAvances(hAvances);
        hAvances.setIdProyecto(idProyecto);
        hAvances.execute();
    }*/
    /**
     * Cambia la visibilidad del mensaje de qeu NO hay notificaciones en el RecyclerView
     * @param empty
     */
    public void muestraSinNotificaciones(boolean empty) {
        recyclerViewAvances.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoNotificacionView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterAvances = new AdapterAvances(getContext(), avances);
        recyclerViewAvances.setAdapter(adapterAvances);
        recyclerViewAvances.setNestedScrollingEnabled(false);
        // Adds the scroll listener to RecyclerView
       // recyclerViewAvances.addOnScrollListener(scrollListener);

       /* if(avances.size()==0)
            cargarMasAvances(0);*/
    }
    public static void refrescar(Proyecto p){
        adapterAvances.notifyDataSetChanged();
        adapterAvances.setAvances(p.getMisAvances());
    }
    @Override
    public void onResume() {
        super.onResume();
        if(avances !=null)
            muestraSinNotificaciones(avances.size()<=0);
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

}
