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

import com.smartu.R;
import com.smartu.adaptadores.AdapterProyecto;
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
public class FragmentProyectos extends Fragment {
    //ArrayList de proyectos para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Proyecto> proyectos = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_PROYECTOS = "proyectos";
    private RecyclerView recyclerViewProyectos;
    private AdapterProyecto adapterProyecto;
    private OnProyectoSelectedListener mListener;
    //Va hacer de listener para cuando llegue al final del RecyclerView
    //y necesite cargar más elementos
    private EndlessRecyclerViewScrollListener scrollListener;

    public FragmentProyectos() {
        // Constructor vacío es necesario
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param proyectos Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentProyectos newInstance(ArrayList<Proyecto> proyectos) {
        FragmentProyectos fragment = new FragmentProyectos();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PROYECTOS, proyectos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            proyectos = getArguments().getParcelableArrayList(ARG_PROYECTOS);
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
        HProyectos hProyectos = new HProyectos(adapterProyecto,offset,getActivity());
        hProyectos.sethProyectos(hProyectos);
        hProyectos.execute();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterProyecto = new AdapterProyecto(getActivity(), proyectos,mListener);
        recyclerViewProyectos.setAdapter(adapterProyecto);
        // Adds the scroll listener to RecyclerView
        recyclerViewProyectos.addOnScrollListener(scrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollListener.resetState();
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollListener.resetState();
    }

    @Override
    public void onStop() {
        super.onStop();
        scrollListener.resetState();
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
