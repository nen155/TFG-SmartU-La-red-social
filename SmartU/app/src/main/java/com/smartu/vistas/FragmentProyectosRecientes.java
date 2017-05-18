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
import com.smartu.modelos.Proyecto;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnObraSelectedListener} interface
 * to handle interaction events.
 * Use the {@link FragmentProyectosRecientes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentProyectosRecientes extends Fragment {

    //ArrayList de obras para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Proyecto> proyectos = new ArrayList<>();
    private static final String ARG_OBRAS = "recients";
    private RecyclerView recyclerViewProyectos;

    private OnObraSelectedListener mListener;

    public FragmentProyectosRecientes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param proyectos Parameter 1.
     * @return A new instance of fragment Inicio.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentProyectosRecientes newInstance(ArrayList<Proyecto> proyectos) {
        FragmentProyectosRecientes fragment = new FragmentProyectosRecientes();
        Bundle args = new Bundle();
        //args.putParcelableArrayList(ARG_OBRAS, obras);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // obras = getArguments().getParcelableArrayList(ARG_OBRAS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_proyectos_recientes, container, false);

        recyclerViewProyectos = (RecyclerView) fragmen.findViewById(R.id.recyclerProyectosRecientes);


        return fragmen;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Si tengo datos los asigno mediante el adapter
        //recyclerViewProyectos.setAdapter(new AdapterObras(getActivity(),obras,mListener));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Proyecto proyecto) {
        if (mListener != null) {
            mListener.onObraSeleccionada(proyecto);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnObraSelectedListener) {
            mListener = (OnObraSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnObraSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnObraSelectedListener {
        // TODO: Update argument type and name
        void onObraSeleccionada(Proyecto proyecto);
    }
}
