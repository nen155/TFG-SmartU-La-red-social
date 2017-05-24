package com.smartu.vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.messaging.FirebaseMessaging;
import com.smartu.R;
import com.smartu.adaptadores.AdapterNotificacion;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



/**
 * Una subclase simple {@link Fragment}.
 * Usa el método factoría {@link FragmentNotificaciones#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentNotificaciones extends Fragment {
    //Cuando se produzca una novedad utilizo el filtro ACTION_NOTIFY_NEW_NOTIFICACION
    public static final String ACTION_NOTIFY_NEW_NOTIFICACION = "NOTIFY_NEW_NOVEDAD";
    //ArrayList de obras para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Notificacion> notificaciones = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_NOTIFICACIONES = "notificaciones";
    private RecyclerView recyclerViewNotificacion;
    //Necesario para recibir el Intent del servicio de FCM
    private BroadcastReceiver mNotificationsReceiver;
    //El adaptador de notificaciones para el RecyclerView
    private AdapterNotificacion adapterNotificacion;
    //Muestra el mensaje de que no hay notificaciones
    private LinearLayout mNoNotificacionView;
    //Cargo la referencia al FCM
    private final FirebaseMessaging mFCMInteractor = FirebaseMessaging.getInstance();

    public FragmentNotificaciones() {
        // Constructor vacío es necesario
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param notificaciones Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentNotificaciones newInstance(ArrayList<Notificacion> notificaciones) {
        FragmentNotificaciones fragment = new FragmentNotificaciones();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_NOTIFICACIONES, notificaciones);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notificaciones = getArguments().getParcelableArrayList(ARG_NOTIFICACIONES);
        }
        //Creo el BroadcastReceiver en su método añado al ArrayList del Adapter
        //una nueva novedad
        mNotificationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String nombre = intent.getStringExtra("nombre");
                String description = intent.getStringExtra("descripcion");
                String fecha = intent.getStringExtra("fecha");
                String idusuario = intent.getStringExtra("idusuario");
                String idproyecto = intent.getStringExtra("idproyecto");


                Notificacion notificacion = new Notificacion();
                notificacion.setNombre(nombre);
                notificacion.setDescripcion(description);
                if(idusuario.compareTo("0")!=0) {
                    Usuario u = new Usuario();
                    u.setId(Integer.parseInt(idusuario));
                    notificacion.setUsuario(u);
                }
                if(idproyecto.compareTo("0")!=0) {
                    Proyecto p = new Proyecto();
                    p.setId(Integer.parseInt(idproyecto));
                    notificacion.setProyecto(p);
                }

                try {
                    notificacion.setFecha(SimpleDateFormat.getInstance().parse(fecha));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                guardaNovedad(notificacion);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_notificaciones, container, false);

        recyclerViewNotificacion = (RecyclerView) fragmen.findViewById(R.id.recyclerNotificaciones);
        mNoNotificacionView = (LinearLayout) fragmen.findViewById(R.id.noMessages);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewNotificacion.setLayoutManager(llm);


        return fragmen;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterNotificacion = new AdapterNotificacion(getContext(), notificaciones);
        recyclerViewNotificacion.setAdapter(adapterNotificacion);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Me subscribo y cargo las notificaciones si las hubiese
        start();
        //Escucho los Intents que me llegan con el filtro novedad
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mNotificationsReceiver, new IntentFilter(ACTION_NOTIFY_NEW_NOTIFICACION));
    }

    @Override
    public void onPause() {
        super.onPause();
        //Dejo de escuchar los Intents
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mNotificationsReceiver);
    }

    /**
     * Subscribe al tema notificaciones y las cargo en el RecyclerView
     */
    public void start() {
        subscribirseANotificaciones();
        cargaNotificaiones();
    }

    /**
     * Subscribe la aplicación al tema notificaciones
     */
    public void subscribirseANotificaciones() {
        mFCMInteractor.subscribeToTopic("notificaciones");
    }

    /**
     * Muestra las notificaciones si hubiese sino muestr mensaje sin notificaciones
     * en el fragment
     */
    public void cargaNotificaiones() {
        if(notificaciones !=null)
        if (notificaciones.size() > 0) {
            muestraSinNotificaciones(false);
            muestraNotificaciones(notificaciones);
        } else {
            muestraSinNotificaciones(true);
        }

    }

    /**
     * Añade una notificacion al ArrayList y la pone al principio del RecyclerView
     * @param notificacion
     */
    public void guardaNovedad(Notificacion notificacion) {
        notificaciones.add(notificacion);
        muestraSinNotificaciones(false);
        addNotificacionTop(notificacion);
    }

    /**
     * Reemplaza las notificaciones actuales en el RecyclerView por otras
     * @param notificaciones
     */
    public void muestraNotificaciones(ArrayList<Notificacion> notificaciones) {
        adapterNotificacion.replaceData(notificaciones);
    }

    /**
     * Cambia la visibilidad del mensaje de notificaciones y del RecyclerView
     * @param empty
     */
    public void muestraSinNotificaciones(boolean empty) {
        recyclerViewNotificacion.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoNotificacionView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    /**
     * Añade una notificacion al principio del RecyclerView
     * @param notificacion
     */
    public void addNotificacionTop(Notificacion notificacion) {
        adapterNotificacion.addItem(notificacion);
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