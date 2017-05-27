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
import com.smartu.adaptadores.AdapterMultimedia;
import com.smartu.modelos.Multimedia;

import java.util.ArrayList;


/**
 * Una subclase simple {@link Fragment}.
 * Usa el método factoría {@link FragmentMultimedia#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentMultimedia extends Fragment {
    //ArrayList de multimedia para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Multimedia> multimedia = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_MULTIMEDIA = "multimedia";
    private RecyclerView recyclerViewMultimedia;


    public FragmentMultimedia() {
        // Constructor vacío es necesario
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param multimedia Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentMultimedia newInstance(ArrayList<Multimedia> multimedia) {
        FragmentMultimedia fragment = new FragmentMultimedia();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MULTIMEDIA, multimedia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            multimedia = getArguments().getParcelableArrayList(ARG_MULTIMEDIA);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_multimedia_proyecto, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMultimedia = (RecyclerView) fragmen.findViewById(R.id.recyclerMultimediaProyecto);
        recyclerViewMultimedia.setLayoutManager(llm);

        return fragmen;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewMultimedia.setAdapter(new AdapterMultimedia(getContext(), multimedia));
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
