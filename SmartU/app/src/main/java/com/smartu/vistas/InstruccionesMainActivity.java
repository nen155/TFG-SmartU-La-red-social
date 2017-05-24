package com.smartu.vistas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.smartu.R;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;

import java.util.ArrayList;

public class InstruccionesMainActivity extends AppIntro2 {
    private  ArrayList<Proyecto> proyectos;
    private  ArrayList<Notificacion> notificaciones;
    private  ArrayList<Usuario> usuarios;
    private  ArrayList<Comentario> comentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=null;
        bundle = getIntent().getExtras();
        if(bundle!=null) {
            proyectos = bundle.getParcelableArrayList("proyectos");
            notificaciones = bundle.getParcelableArrayList("notificaciones");
            usuarios = bundle.getParcelableArrayList("usuarios");
            comentarios = bundle.getParcelableArrayList("comentarios");
        }
        //Añado los Slide para las instrucciones del Main
        //addSlide(AppIntroFragment.newInstance(getString(R.string.menu_main), getString(R.string.descripcion_instrucciones_main), R.drawable.menu_lateral_instrucciones, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight));
        addSlide(AppIntroFragment.newInstance(getString(R.string.menu_lateral), getString(R.string.descripcion_instrucciones_menu), R.drawable.menu_lateral_instrucciones, Color.parseColor("#2196F3")));
        //Muestro el botón para saltarme las instrucciones
        showSkipButton(true);
        //Muestro un botón para pasar de slide
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putParcelableArrayListExtra("proyectos",proyectos);
        intent.putParcelableArrayListExtra("notificaciones",notificaciones);
        intent.putParcelableArrayListExtra("usuarios",usuarios);
        intent.putParcelableArrayListExtra("comentarios",comentarios);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putParcelableArrayListExtra("proyectos",proyectos);
        intent.putParcelableArrayListExtra("notificaciones",notificaciones);
        intent.putParcelableArrayListExtra("usuarios",usuarios);
        intent.putParcelableArrayListExtra("comentarios",comentarios);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
