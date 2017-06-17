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
import com.smartu.adaptadores.AdapterUsuario;
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.CallBackHebras;
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
public class FragmentUsuarios extends Fragment implements CallBackHebras {
    //ArrayList de obras para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_USUARIOS = "usuarios";
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_FILTRO = "filtro";
    private boolean filtro=false;
    private RecyclerView recyclerViewUsuarios;
    //Muestra el mensaje de que no hay notificaciones
    private LinearLayout mNoNotificacionView;

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
    public static FragmentUsuarios newInstance(ArrayList<Usuario> usuarios,boolean filtro) {
        FragmentUsuarios fragment = new FragmentUsuarios();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_USUARIOS, usuarios);
        args.putBoolean(ARG_FILTRO,filtro);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuarios = getArguments().getParcelableArrayList(ARG_USUARIOS);
            filtro = getArguments().getBoolean(ARG_FILTRO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_usuarios_recientes, container, false);

        recyclerViewUsuarios = (RecyclerView) fragmen.findViewById(R.id.recyclerUsuarios);
        mNoNotificacionView = (LinearLayout) fragmen.findViewById(R.id.noMessages);
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
     * Cambia la visibilidad del mensaje de qeu NO hay notificaciones en el RecyclerView
     * @param empty
     */
    public void muestraSinNotificaciones(boolean empty) {
        recyclerViewUsuarios.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoNotificacionView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    /**
     * Carga hasta 10 comentarios si hubiese a partir del offset
     * que le ofrece el método LoadMore
     * @param offset
     */
    public void cargarMasUsuarios(int offset) {
        //Establezco el número de elmentos que deberia contener en total
        scrollListener.setTotalServer(adapterUsuario.getTotalElementosServer());
        //Para cargar a partir del tamaño del map
        //por si uso filtros
        if(offset< Almacen.sizeUsuarios())
            offset=Almacen.sizeUsuarios();

        HUsuarios hUsuarios = new HUsuarios(adapterUsuario,offset,getActivity());
        hUsuarios.sethUsuarios(hUsuarios);
        hUsuarios.setCallBackHebras(this);
        hUsuarios.execute();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterUsuario = new AdapterUsuario(getContext(), usuarios,mListener);
        recyclerViewUsuarios.setAdapter(adapterUsuario);
        // Adds the scroll listener to RecyclerView
        recyclerViewUsuarios.addOnScrollListener(scrollListener);

        if(usuarios.size()==0)
            cargarMasUsuarios(0);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(usuarios!=null)
            muestraSinNotificaciones(usuarios.size()<=0);
        else
            muestraSinNotificaciones(true);
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

    @Override
    public void terminada() {
        //Establezo el número por el que va el contenedor del Almacen
        scrollListener.setTamAlmacen(Almacen.sizeNotificaciones());
        //Por si lo que cargo durante los filtros no es sufiente continuo cargando
        //para buscar todas las notificaciones posibles que coincidan con el filtro del usuario
        if(filtro && !scrollListener.isFin() && Almacen.sizeNotificaciones() < adapterUsuario.getTotalElementosServer()
                && scrollListener.getLastVisibleItemPosition()+scrollListener.getVisibleThreshold() >adapterUsuario.getItemCount()){
            cargarMasUsuarios(0);
            scrollListener.setLoading(true);
        }


        if(adapterUsuario.getItemCount()==0 && Almacen.sizeUsuarios() < adapterUsuario.getTotalElementosServer())
            cargarMasUsuarios(0);
        else
            muestraSinNotificaciones(adapterUsuario.getItemCount()==0);
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
