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
import com.smartu.adaptadores.AdapterUsuario;
import com.smartu.hebras.HUsuarios;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;


/**
 * Una subclase simple {@link Fragment}.
 * Las Activities que lo contentgan deben de implementar
 * {@link OnUsuarioSelectedListener} interfaz
 * para manejar la interacción de eventos.
 * Usa el método factoría {@link FragmentUsuarios#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentUsuarios extends Fragment {
    //ArrayList de obras para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_USUARIOS = "usuarios";
    private RecyclerView recyclerViewUsuarios;

    private OnUsuarioSelectedListener mListener;
    //Va hacer de listener para cuando llegue al final del RecyclerView
    //y necesite cargar más elementos
    private EndlessRecyclerViewScrollListener scrollListener;

    private AdapterUsuario adapterUsuario;
    public FragmentUsuarios() {
        // Constructor vacío es necesario
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param usuarios Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentUsuarios newInstance(ArrayList<Usuario> usuarios) {
        FragmentUsuarios fragment = new FragmentUsuarios();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_USUARIOS, usuarios);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuarios = getArguments().getParcelableArrayList(ARG_USUARIOS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_usuarios_recientes, container, false);

        recyclerViewUsuarios = (RecyclerView) fragmen.findViewById(R.id.recyclerUsuarios);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewUsuarios.setLayoutManager(llm);

        // Guardo la instancia para poder llamar a `resetState()` para nuevas busquedas
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // El evento sólo se provoca cuando necesito añadir más elementos
                cargarMasUsuarios(page);
            }
        };


        return fragmen;
    }

    /**
     * Carga hasta 10 comentarios si hubiese a partir del offset
     * que le ofrece el método LoadMore
     * @param offset
     */
    public void cargarMasUsuarios(int offset) {
        HUsuarios hUsuarios = new HUsuarios(adapterUsuario,offset);
        hUsuarios.sethUsuarios(hUsuarios);
        hUsuarios.execute();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterUsuario = new AdapterUsuario(getContext(), usuarios,mListener);
        recyclerViewUsuarios.setAdapter(adapterUsuario);
        // Adds the scroll listener to RecyclerView
        recyclerViewUsuarios.addOnScrollListener(scrollListener);
        //La primera vez le pongo el tamaño del Array por si no son más de 10
        //que son lo que me traigo
        adapterUsuario.setTotalElementosServer(usuarios.size());

    }


    public void onButtonPressed(int idUsuario) {
        if (mListener != null) {
            mListener.onUsuarioSeleccionado(idUsuario);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUsuarioSelectedListener) {
            mListener = (OnUsuarioSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar OnNovedadSelectedListener");
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
    public interface OnUsuarioSelectedListener {
        void onUsuarioSeleccionado(int idUsuario);
    }
}
