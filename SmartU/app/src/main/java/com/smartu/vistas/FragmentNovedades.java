package com.smartu.vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.messaging.FirebaseMessaging;
import com.smartu.R;
import com.smartu.adaptadores.AdapterNovedad;
import com.smartu.modelos.Novedad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



/**
 * Una subclase simple {@link Fragment}.
 * Las Activities que lo contentgan deben de implementar
 * {@link OnNovedadSelectedListener} interfaz
 * para manejar la interacción de eventos.
 * Usa el método factoría {@link FragmentNovedades#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentNovedades extends Fragment {
    //Cuando se produzca una novedad utilizo el filtro ACTION_NOTIFY_NEW_NOVEDAD
    public static final String ACTION_NOTIFY_NEW_NOVEDAD = "NOTIFY_NEW_NOVEDAD";
    //ArrayList de obras para cargar y pasar cuando se cambie de Fragment
    private static ArrayList<Novedad> novedades = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_NOVEDADES = "novedades";
    private RecyclerView recyclerViewNovedades;
    //Necesario para recibir el Intent del servicio de FCM
    private BroadcastReceiver mNotificationsReceiver;
    //Cuando selecciono una novedad tengo un escuchador para pasarle la información a la Activity
    private OnNovedadSelectedListener mListener;
    //El adaptador de novedades para el RecyclerView
    private AdapterNovedad adapterNovedad;
    //Muestra el mensaje de que no hay novedades
    private LinearLayout mNoNovedadesView;
    //Cargo la referencia al FCM
    private final FirebaseMessaging mFCMInteractor = FirebaseMessaging.getInstance();

    public FragmentNovedades() {
        // Constructor vacío es necesario
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param novedades Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentNovedades newInstance(ArrayList<Novedad> novedades) {
        FragmentNovedades fragment = new FragmentNovedades();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_NOVEDADES, novedades);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            novedades = getArguments().getParcelableArrayList(ARG_NOVEDADES);
        }
        //Creo el BroadcastReceiver en su método añado al ArrayList del Adapter
        //una nueva novedad
        mNotificationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String nombre = intent.getStringExtra("nombre");
                String description = intent.getStringExtra("descripcion");
                String fecha = intent.getStringExtra("fecha");
                String idelemento = intent.getStringExtra("idelemento");
                String tipo = intent.getStringExtra("tipo");
                String detalle = intent.getStringExtra("detalle");
                String nombreelemento = intent.getStringExtra("nombreelemento");

                Novedad novedad = new Novedad();
                novedad.setNombre(nombre);
                novedad.setDescripcion(description);
                novedad.setDetalle(detalle);
                novedad.setNombreElemento(nombreelemento);
                novedad.setTipo(tipo);

                novedad.setIdElemento(Integer.parseInt(idelemento));

                try {
                    novedad.setFecha(SimpleDateFormat.getInstance().parse(fecha));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                guardaNovedad(novedad);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_novedades, container, false);

        recyclerViewNovedades = (RecyclerView) fragmen.findViewById(R.id.recyclerMuro);
        mNoNovedadesView = (LinearLayout) fragmen.findViewById(R.id.noMessages);

        adapterNovedad = new AdapterNovedad(getContext(), novedades,mListener);

        recyclerViewNovedades.setAdapter(adapterNovedad);

        return fragmen;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        //Me subscribo y cargo las novedades si las hubiese
        start();
        //Escucho los Intents que me llegan con el filtro novedad
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mNotificationsReceiver, new IntentFilter(ACTION_NOTIFY_NEW_NOVEDAD));
    }

    @Override
    public void onPause() {
        super.onPause();
        //Dejo de escuchar los Intents
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mNotificationsReceiver);
    }

    /**
     * Subscribe al tema novedades y las cargo en el RecyclerView
     */
    public void start() {
        subscribirseANovedades();
        cargaNovedades();
    }

    /**
     * Subscribe la aplicación al tema novedades
     */
    public void subscribirseANovedades() {
        mFCMInteractor.subscribeToTopic("novedades");
    }

    /**
     * Muestra las novedades si hubiese sino muestr mensaje sin novedades
     * en el fragment
     */
    public void cargaNovedades() {
        if (novedades.size() > 0) {
            muestraSinNovedades(false);
            muestraNovedades(novedades);
        } else {
            muestraSinNovedades(true);
        }

    }

    /**
     * Añade una novedad al ArrayList y la pone al principio del RecyclerView
     * @param novedad
     */
    public void guardaNovedad(Novedad novedad) {
        novedades.add(novedad);
        muestraSinNovedades(false);
        addNovedadTop(novedad);
    }

    /**
     * Reemplaza las novedades actuales en el RecyclerView por otras
     * @param novedades
     */
    public void muestraNovedades(ArrayList<Novedad> novedades) {
        adapterNovedad.replaceData(novedades);
    }

    /**
     * Cambia la visibilidad del mensaje de novedades y del RecyclerView
     * @param empty
     */
    public void muestraSinNovedades(boolean empty) {
        recyclerViewNovedades.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoNovedadesView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    /**
     * Añade una novedad al principio del RecyclerView
     * @param novedad
     */
    public void addNovedadTop(Novedad novedad) {
        adapterNovedad.addItem(novedad);
    }

    public void onButtonPressed(Novedad novedad) {
        if (mListener != null) {
            mListener.onNovedadSeleccionado(novedad);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNovedadSelectedListener) {
            mListener = (OnNovedadSelectedListener) context;
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
    public interface OnNovedadSelectedListener {
        void onNovedadSeleccionado(Novedad novedad);
    }
}
