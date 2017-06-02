package com.smartu.vistas;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.adaptadores.AdapterAreasInteres;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Area;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;

import java8.util.stream.StreamSupport;

public class AreasActivity extends AppCompatActivity {
    private AdapterAreasInteres adapterAreasInteres;
    private Context context;
    private ArrayList<Area> areasInteresList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);
        context = this;
        //Cargo el menú lateral
        SliderMenu sliderMenu = new SliderMenu(getApplicationContext(),this);
        sliderMenu.inicializateToolbar(getTitle().toString());
        areasInteresList=Sesion.getUsuario(this).getMisAreasInteres();
        if(areasInteresList==null)
            areasInteresList=new ArrayList<>();
        adapterAreasInteres =new AdapterAreasInteres(savedInstanceState,this, areasInteresList);
        final GridView areasInteres = (GridView) findViewById(R.id.areas_interes);
        areasInteres.setAdapter(adapterAreasInteres);

    }

}
