package com.smartu.vistas;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.adaptadores.AdapterAreasInteres;
import com.smartu.hebras.HUsuarioInteresa;
import com.smartu.modelos.Area;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class AreasActivity extends AppCompatActivity {
    private AdapterAreasInteres adapterAreasInteres;
    private Context context;
    private ArrayList<Area> areasInteresList;
    private Set<Integer> posicionAreasInicial = new HashSet<>();
    private ArrayList<Area> areasBack;
    private Usuario usuarioSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);
        context = this;

        //Cargo el menú lateral
        SliderMenu sliderMenu = new SliderMenu(getApplicationContext(),this);
        sliderMenu.inicializateToolbar(getTitle().toString());
        usuarioSesion = Sesion.getUsuario(this);
        //Obtengo las areas del usuario
        areasInteresList=usuarioSesion.getMisAreasInteres();
        if(areasInteresList==null)
            areasInteresList=new ArrayList<>();
        //Copio los elementos de areas seleccionadas por si no llega a guardar no cambiar el array original
        areasBack = new ArrayList<>(areasInteresList);
        final GridView areasInteres = (GridView) findViewById(R.id.areas_interes);
        adapterAreasInteres =new AdapterAreasInteres(savedInstanceState,this, posicionAreasInicial,areasBack,areasInteres,areasInteresList);
        areasInteres.setAdapter(adapterAreasInteres);
        //Guardo las areas que ha seleccionado
        ((FloatingActionButton)findViewById(R.id.guardar_intereses)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Integer> idsIntereses = StreamSupport.stream(areasBack).map(Area::getId).collect(Collectors.toList());
                HUsuarioInteresa hUsuarioInteresa = new HUsuarioInteresa(usuarioSesion.getId(),idsIntereses,AreasActivity.this,adapterAreasInteres, posicionAreasInicial,areasBack);
                hUsuarioInteresa.sethUsuarioInteresa(hUsuarioInteresa);
                hUsuarioInteresa.execute();

            }
        });
    }

}
