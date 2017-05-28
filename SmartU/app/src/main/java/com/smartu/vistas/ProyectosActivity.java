package com.smartu.vistas;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.smartu.R;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Sesion;

public class ProyectosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyectos);
        Usuario usuarioSesion = Sesion.getUsuario(this);
        //Cargo el fragment por defecto
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_proyectos, FragmentProyectos.newInstance(usuarioSesion.getMisProyectos()));
        transaction.commit();
    }
}
