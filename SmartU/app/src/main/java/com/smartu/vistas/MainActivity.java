package com.smartu.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Novedad;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentProyectos.OnProyectoSelectedListener, FragmentUsuarios.OnUsuarioSelectedListener {

    private static ArrayList<Proyecto> proyectos;
    private static ArrayList<Novedad> novedades;
    private static ArrayList<Usuario> usuarios;
    private static ArrayList<Comentario> comentarios;
    private FloatingActionsMenu filtros;

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


        filtros = (FloatingActionsMenu) findViewById(R.id.filtros);
        FloatingActionButton filtro_novedades = (FloatingActionButton) findViewById(R.id.filtro_novedades);
        filtro_novedades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrar(v);
            }
        });
        FloatingActionButton filtro_comentarios = (FloatingActionButton) findViewById(R.id.filtro_comentarios);
        filtro_comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrar(v);
            }
        });
        FloatingActionButton filtro_proyectos = (FloatingActionButton) findViewById(R.id.filtro_proyectos);
        filtro_proyectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrar(v);
            }
        });
        FloatingActionButton filtro_usuarios = (FloatingActionButton) findViewById(R.id.filtro_usuarios);
        filtro_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrar(v);
            }
        });

    }

    private void filtrar(View v){
        Fragment swicthTo=null;
        switch (v.getId()){
            case R.id.filtro_novedades:
                swicthTo=FragmentNovedades.newInstance(novedades);
                Toast.makeText(getApplicationContext(),"Novedades",Toast.LENGTH_SHORT).show();
                break;
            case R.id.filtro_comentarios:
                swicthTo=FragmentComentarios.newInstance(comentarios);
                Toast.makeText(getApplicationContext(),"Comentarios",Toast.LENGTH_SHORT).show();
                break;
            case R.id.filtro_proyectos:
                swicthTo=FragmentProyectos.newInstance(proyectos);
                Toast.makeText(getApplicationContext(),"Proyectos",Toast.LENGTH_SHORT).show();
                break;
            case R.id.filtro_usuarios:
                swicthTo=FragmentUsuarios.newInstance(usuarios);
                Toast.makeText(getApplicationContext(),"Usuarios",Toast.LENGTH_SHORT).show();
                break;
        }
        if(swicthTo!=null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame,swicthTo);
            transaction.commit();
        }
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
    public void onProyectoSeleccionado(Proyecto proyecto) {
        /*Intent intent = new Intent(getApplicationContext(),ProyectoActivity.class);
        intent.putExtra("proyecto",proyecto);
        startActivity(intent);*/
    }

    @Override
    public void onUsuarioSeleccionado(Usuario usuario) {
        /*Intent intent = new Intent(getApplicationContext(),UsuarioActivity.class);
        intent.putExtra("proyecto",usuario);
        startActivity(intent);*/
    }
}
