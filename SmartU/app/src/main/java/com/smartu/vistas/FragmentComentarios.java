package com.smartu.vistas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartu.R;
import com.smartu.adaptadores.AdapterComentario;
import com.smartu.adaptadores.AdapterUsuario;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Usuario;

import java.util.ArrayList;


/**
 * Una subclase simple {@link Fragment}.
 * Usa el método factoría {@link FragmentComentarios#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentComentarios extends Fragment {
    //ArrayList de obras para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Comentario> comentarios = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_COMENTARIOS = "comentarios";
    private RecyclerView recyclerViewComentarios;


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
    public static FragmentComentarios newInstance(ArrayList<Comentario> comentarios) {
        FragmentComentarios fragment = new FragmentComentarios();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_COMENTARIOS, comentarios);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comentarios = getArguments().getParcelableArrayList(ARG_COMENTARIOS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_comentarios_recientes, container, false);

        recyclerViewComentarios = (RecyclerView) fragmen.findViewById(R.id.recyclerMuro);


        return fragmen;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Si tengo datos los asigno mediante el adapter
        if(comentarios.size()>0) {
                recyclerViewComentarios.setAdapter(new AdapterComentario(getContext(), comentarios));
        }
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
