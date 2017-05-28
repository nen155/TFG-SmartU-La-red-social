package com.smartu.vistas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.smartu.R;
import com.smartu.adaptadores.AdapterAreasInteres;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

public class AreasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);
        //Cargo el menú lateral
        SliderMenu sliderMenu = new SliderMenu(getApplicationContext(),this);
        sliderMenu.inicializateToolbar(getTitle().toString());
        GridView areasInteres = (GridView) findViewById(R.id.areas_interes);
        areasInteres.setAdapter(new AdapterAreasInteres(this, Sesion.getUsuario(this).getMisAreasInteres()));
    }
}
