package com.smartu.vistas;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.smartu.R;
import com.smartu.adaptadores.AdapterSolicitudes;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.SolicitudUnion;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;

import java8.util.Optional;
import java8.util.stream.StreamSupport;

public class SolicitadosActivity extends AppCompatActivity {

    private Usuario usuarioSesion;
    private Context context;
    private AdapterSolicitudes adapterSolicitudes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitados);
        context=this;
        //Cargo el menú lateral y pongo el nombre del proyecto a el Toolbar
        SliderMenu sliderMenu = new SliderMenu(context, this);
        sliderMenu.inicializateToolbar("Solicitudes");
        usuarioSesion = Sesion.getUsuario(context);
        RecyclerView recyclerViewSolicitudes = (RecyclerView) findViewById(R.id.recyclerSolicitudes);
        Optional<ArrayList<SolicitudUnion>> listOptional = StreamSupport.stream(Almacen.getProyectos()).filter(proyecto -> proyecto.getIdPropietario() == usuarioSesion.getId()).map(proyecto -> proyecto.getSolicitudes()).findAny();
        if(listOptional.isPresent())
            adapterSolicitudes = new AdapterSolicitudes(context, listOptional.get());
        else
            adapterSolicitudes = new AdapterSolicitudes(context, new ArrayList<>());
        //Establezco el recyclerview con las redes sociales
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSolicitudes.setLayoutManager(llm);
        recyclerViewSolicitudes.setAdapter(adapterSolicitudes);
        recyclerViewSolicitudes.addItemDecoration(new DividerItemDecoration(recyclerViewSolicitudes.getContext(),
                llm.getOrientation()));
    }
}
