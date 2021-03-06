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
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.CallBackHebras;
import com.smartu.hebras.HNotificaciones;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.EndlessRecyclerViewScrollListener;
import com.smartu.utilidades.Sesion;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Una subclase simple {@link Fragment}.
 * Usa el método factoría {@link FragmentNotificaciones#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentNotificaciones extends Fragment implements CallBackHebras {
    //Cuando se produzca una novedad utilizo el filtro ACTION_NOTIFY_NEW_NOTIFICACION
    public static final String ACTION_NOTIFY_NEW_NOTIFICACION = "NOTIFY_NEW_NOVEDAD";
    //ArrayList de notificaciones para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Notificacion> notificaciones = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_NOTIFICACIONES = "notificaciones";
    private static final String ARG_FITRO= "filtro";
    private int count=2;
    private RecyclerView recyclerViewNotificacion;
    private Usuario usuarioSesion;
    private boolean filtro=false;


    /**
    *PUEDE PENSARSE SI ES ÚTIL
    *Creo el BroadcastReceiver en su método añado al ArrayList del Adapter
    *una nueva novedad
    *Necesario para recibir el Intent del servicio de FCM*/
    private BroadcastReceiver mNotificationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String nombre = intent.getStringExtra("nombre");
            String description = intent.getStringExtra("descripcion");
            String fecha = intent.getStringExtra("fecha");
            String idusuario = intent.getStringExtra("idusuario");
            String idproyecto = intent.getStringExtra("idproyecto");
            String usuario = intent.getStringExtra("usuario");
            String proyecto = intent.getStringExtra("proyecto");


            Notificacion notificacion = new Notificacion();
            notificacion.setNombre(nombre);
            notificacion.setDescripcion(description);
            if(idusuario!=null && idusuario.compareTo("0")!=0) {
                notificacion.setIdUsuario(Integer.parseInt(idusuario));
                notificacion.setUsuario(usuario);
            }
            if(idproyecto!=null && idproyecto.compareTo("0")!=0) {
                notificacion.setIdProyecto(Integer.parseInt(idproyecto));
                notificacion.setProyecto(proyecto);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                notificacion.setFecha(sdf.parse(fecha));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            guardaNotificacion(notificacion);
        }
    };


    //El adaptador de notificaciones para el RecyclerView
    private AdapterNotificacion adapterNotificacion;
    //Muestra el mensaje de que no hay notificaciones
    private LinearLayout mNoNotificacionView;
    //Cargo la referencia al FCM
    private final FirebaseMessaging mFCMInteractor = FirebaseMessaging.getInstance();
    //Va hacer de listener para cuando llegue al final del RecyclerView
    //y necesite cargar más elementos
    private EndlessRecyclerViewScrollListener scrollListener;

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
    public static FragmentNotificaciones newInstance(ArrayList<Notificacion> notificaciones,boolean filtro) {
        FragmentNotificaciones fragment = new FragmentNotificaciones();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_NOTIFICACIONES, notificaciones);
        args.putBoolean(ARG_FITRO,filtro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notificaciones = getArguments().getParcelableArrayList(ARG_NOTIFICACIONES);
            filtro =getArguments().getBoolean(ARG_FITRO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_notificaciones, container, false);
        usuarioSesion = Sesion.getUsuario(getContext());
        recyclerViewNotificacion = (RecyclerView) fragmen.findViewById(R.id.recyclerNotificaciones);
        mNoNotificacionView = (LinearLayout) fragmen.findViewById(R.id.noMessages);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewNotificacion.setLayoutManager(llm);
        // Guardo la instancia para poder llamar a `resetState()` para nuevas busquedas
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // El evento sólo se provoca cuando necesito añadir más elementos
                cargarMasNotificaciones(page);
            }
        };


        return fragmen;
    }
    /**
     * Carga hasta 10 comentarios si hubiese a partir del offset
     * que le ofrece el método LoadMore
     * @param offset
     */
    public void cargarMasNotificaciones(int offset) {

        //Establezco el número de elmentos que deberia contener en total
        scrollListener.setTotalServer(adapterNotificacion.getTotalElementosServer());
        //Esto es solamente en el caso de que el usuario no sea anonimo
        //Se hace una precarga porque puede que las primeras notificaciones no pertenezcan
        //a su filtro por lo que vamos cargando hasta que tenga notificaciones
        //de su filtro
        //Para cargar a partir del tamaño del map
        //por si uso filtros
        if(offset< Almacen.sizeNotificaciones())
            offset=Almacen.sizeNotificaciones();

        HNotificaciones hNotificaciones = new HNotificaciones(adapterNotificacion,offset,getActivity());
        hNotificaciones.sethNotificaciones(hNotificaciones);
        //Para reducir el número de cargas que realiza,
        //pido más de una sola vez
        if(Almacen.sizeNotificaciones()==0){
            hNotificaciones.setLimit(count*10);
            count*=2;
        }
        hNotificaciones.setCallback(this);
        hNotificaciones.execute();


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapterNotificacion = new AdapterNotificacion(getContext(), notificaciones);
        adapterNotificacion.setFiltro(filtro);

        recyclerViewNotificacion.setAdapter(adapterNotificacion);

        // Adds the scroll listener to RecyclerView
        recyclerViewNotificacion.addOnScrollListener(scrollListener);
        //Esto es solamente en el caso de que el usuario no sea anonimo
        //Se hace una precarga porque puede que las primeras notificaciones no pertenezcan
        //a su filtro por lo que vamos cargando hasta que tenga notificaciones
        //de su filtro
        if(notificaciones.size()==0 && Sesion.getUsuario(getContext())!=null)
            cargarMasNotificaciones(0);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Me subscribo y cargo las notificaciones si las hubiese
        start();
        //Escucho los Intents que me llegan con el filtro novedad
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mNotificationsReceiver, new IntentFilter(ACTION_NOTIFY_NEW_NOTIFICACION));

    }

    @Override
    public void onPause() {
        super.onPause();
        //Dejo de escuchar los Intents
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mNotificationsReceiver);
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
        mFCMInteractor.subscribeToTopic("/topics/notificaciones");
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
    public void guardaNotificacion(Notificacion notificacion) {
        notificaciones.add(0,notificacion);
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
     * Cambia la visibilidad del mensaje de qeu NO hay notificaciones en el RecyclerView
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
        adapterNotificacion.addItemTop(notificacion);
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
        if(filtro && usuarioSesion!=null && (usuarioSesion.getMisAreasInteres().size()>0 || usuarioSesion.getMisSeguidos().size()>0))
            if(filtro && !scrollListener.isFin() && Almacen.sizeNotificaciones() < adapterNotificacion.getTotalElementosServer()
                    && scrollListener.getLastVisibleItemPosition()+scrollListener.getVisibleThreshold() >adapterNotificacion.getItemCount()){
                cargarMasNotificaciones(0);
                scrollListener.setLoading(true);
            }
        if(!filtro && adapterNotificacion.getItemCount()==0 && Almacen.sizeNotificaciones() < adapterNotificacion.getTotalElementosServer())
            cargarMasNotificaciones(0);
        else
            muestraSinNotificaciones(adapterNotificacion.getItemCount()==0);
    }
}
