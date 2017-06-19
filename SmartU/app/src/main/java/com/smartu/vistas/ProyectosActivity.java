package com.smartu.vistas;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;

public class ProyectosActivity extends AppCompatActivity implements FragmentProyectos.OnProyectoSelectedListener {
    private Usuario usuarioSesion;
    private static ArrayList<Proyecto> proyectos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyectos);
        usuarioSesion = Sesion.getUsuario(this);

        //Cargo el menú lateral
        SliderMenu sliderMenu = new SliderMenu(getApplicationContext(),this);
        sliderMenu.inicializateToolbar(getTitle().toString());
        //Cargo el fragment por defecto
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Almacen.buscarProyectos(usuarioSesion.getMisProyectos(),proyectos,this);
        FragmentProyectos fragmentProyectos=FragmentProyectos.newInstance(proyectos,false);
        fragmentProyectos.setIdUsuario(usuarioSesion.getId());
        transaction.replace(R.id.content_proyectos, fragmentProyectos);
        transaction.commit();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onProyectoSeleccionado(int idProyecto) {
        Intent intent = new Intent(this,ProyectoActivity.class);
        intent.putExtra("idProyecto", idProyecto);
        startActivity(intent);
    }

}
