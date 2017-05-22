package com.smartu.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.smartu.R;
import com.smartu.modelos.Avance;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Novedad;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Publicacion;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentNovedades.OnNovedadSelectedListener {

    private static ArrayList<Proyecto> proyectos;
    private static ArrayList<Novedad> novedades;
    private static ArrayList<Usuario> usuarios;
    private static ArrayList<Comentario> comentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        //Cojo las publicaciones cargadas durante el splashscreen
        if(bundle!=null) {
            proyectos = bundle.getParcelableArrayList("proyectos");
            novedades = bundle.getParcelableArrayList("novedades");
            usuarios = bundle.getParcelableArrayList("usuarios");
            comentarios = bundle.getParcelableArrayList("comentarios");
        }
        //Cargo el menú lateral
        SliderMenu sliderMenu = new SliderMenu(getApplicationContext(),this);
        sliderMenu.inicializateToolbar(getTitle().toString());
        //Asigno el escuchador al navigationbottom
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Cargo el fragment por defecto
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, FragmentProyectos.newInstance(proyectos));
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment swicthTo=null;
            switch (item.getItemId()) {
                /*case R.id.navigation_inicio:
                    swicthTo = null;
                    break;
                case R.id.navigation_obra:
                    return true;
                case R.id.navigation_puntos:
                    swicthTo = null;
                    break;*/

            }
            if(swicthTo!=null){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame,swicthTo);
                transaction.commit();
            }
            return true;
        }

    };

    @Override
    public void onNovedadSeleccionado(Novedad novedad) {
        Intent intent=null;
       /* if ( publicacion instanceof Proyecto) {
            intent = new Intent(getApplicationContext(),ProyectoActivity.class);
            intent.putExtra("proyecto",(Proyecto)publicacion);
        } else if (publicacion instanceof Comentario){

            intent = new Intent(getApplicationContext(),ComentarioActivity.class);
            intent.putExtra("proyecto",(Comentario)publicacion);
        } else if(publicacion instanceof Avance){

            /*intent = new Intent(getApplicationContext(),AvanceActivity.class);
            intent.putExtra("proyecto",(Avance)publicacion);
        } else if(publicacion instanceof Usuario){

            /*intent = new Intent(getApplicationContext(),UsuarioActivity.class);
            intent.putExtra("proyecto",(Usuario)publicacion);
        }*/
        startActivity(intent);
    }
}
