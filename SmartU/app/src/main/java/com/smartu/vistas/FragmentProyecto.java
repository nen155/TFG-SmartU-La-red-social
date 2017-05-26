package com.smartu.vistas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;
import com.smartu.R;
import com.smartu.modelos.Area;
import com.smartu.modelos.Proyecto;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentProyecto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProyecto extends Fragment {


    private static final String ARG_PROYECTO = "proyecto";

    private Proyecto proyecto;


    public FragmentProyecto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param proyecto Parameter 1.
     * @return A new instance of fragment FragmentProyecto.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProyecto newInstance(Proyecto proyecto) {
        FragmentProyecto fragment = new FragmentProyecto();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROYECTO, proyecto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            proyecto = getArguments().getParcelable(ARG_PROYECTO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView =inflater.inflate(R.layout.fragment_proyecto, container, false);

        TextView descripcionProyecto = (TextView) inflatedView.findViewById(R.id.descripcion_proyecto);
        TextView fechaCreacion = (TextView) inflatedView.findViewById(R.id.fecha_creacion);
        TextView fechaFinalizacion = (TextView) inflatedView.findViewById(R.id.fecha_finalizacion);
        TagCloudLinkView areasProyecto = (TagCloudLinkView) inflatedView.findViewById(R.id.areas_proyecto);

        descripcionProyecto.setText(proyecto.getDescripcion());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaCreacion.setText(simpleDateFormat.format(proyecto.getFechaCreacion()));
        fechaFinalizacion.setText(simpleDateFormat.format(proyecto.getFechaFinalizacion()));

        for (Area a : proyecto.getMisAreas()) {
            areasProyecto.add(new Tag(a.getId(), a.getNombre()));
        }

        return inflatedView;
    }

}
