package com.smartu.vistas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import toan.android.floatingactionmenu.FloatingActionButton;
import toan.android.floatingactionmenu.FloatingActionsMenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements FragmentProyectos.OnProyectoSelectedListener, FragmentUsuarios.OnUsuarioSelectedListener, FragmentMapa.OnProyectoSeleccionadoMapaListener {


    //Por defecto esta seleccionado el muro porque es lo primero que ve el usuario
    private static int itemMenuSeleccionado = R.id.navigation_muro;
    private FloatingActionsMenu filtros;
    private Usuario usuarioSesion = null;
    //Declaro los arrays filtrados por intereses
    private static ArrayList<Proyecto> proyectosFiltrados = new ArrayList<>();
    private static ArrayList<Notificacion> notificacionesFiltradas=new ArrayList<>();
    private static ArrayList<Usuario> usuariosFiltrados=new ArrayList<>();
    private static ArrayList<Comentario> comentariosFiltrados=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuarioSesion = Sesion.getUsuario(getApplicationContext());

        SliderMenu sliderMenu = new SliderMenu(getApplicationContext(), this);
        sliderMenu.inicializateToolbar(getTitle().toString());

        //Si ha iniciado sesión el usuario filtro los arrays para
        //poder mostrarlos en el fragment Intereses
        if (usuarioSesion != null) {
                proyectosFiltrados = Almacen.proyectosFiltrados(usuarioSesion);
                notificacionesFiltradas = Almacen.notificacionsFiltradas(usuarioSesion,proyectosFiltrados);
                comentariosFiltrados = Almacen.comentariosFiltrados(usuarioSesion,proyectosFiltrados);
                Almacen.usuariosFiltrados(usuarioSesion,usuariosFiltrados,this);
        }
        //Cargo el fragment por defecto
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, FragmentProyectos.newInstance(Almacen.getProyectos()));
        transaction.commit();

        //Asigno las acciones de filtros que tiene que tienen que realizar los botones flotantes
        filtros = (FloatingActionsMenu) findViewById(R.id.filtros);
        FloatingActionButton filtro_notificaciones = (FloatingActionButton) findViewById(R.id.filtro_notificaciones);
        filtro_notificaciones.setOnClickListener(new View.OnClickListener() {
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
        //Asigno el escuchador al navigationbottom
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * Muestra un Fragment u otro en función del botón flotante que haya seleccionado y
     * en función de si estoy en intereses o en el muro
     *
     * @param v
     */
    private void filtrar(View v) {
        filtros.collapse();
        Fragment swicthTo = null;
        switch (v.getId()) {
            case R.id.filtro_notificaciones:
                //Si estoy en intereses muestro las notificaciones filtradas
                if (itemMenuSeleccionado == R.id.navigation_intereses)
                    swicthTo = FragmentNotificaciones.newInstance(notificacionesFiltradas);
                else
                    swicthTo = FragmentNotificaciones.newInstance(Almacen.getNotificaciones());
                Toast.makeText(getApplicationContext(), "Novedades", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);*/
                break;
            case R.id.filtro_comentarios:
                //Si estoy en intereses muestro las comentarios filtrados
                if (itemMenuSeleccionado == R.id.navigation_intereses)
                    swicthTo = FragmentComentarios.newInstance(comentariosFiltrados);
                else
                    swicthTo = FragmentComentarios.newInstance(Almacen.getComentarios());
                Toast.makeText(getApplicationContext(), "Comentarios", Toast.LENGTH_SHORT).show();
                break;
            case R.id.filtro_proyectos:
                //Si estoy en intereses muestro los proyectos filtrados
                if (itemMenuSeleccionado == R.id.navigation_intereses)
                    swicthTo = FragmentProyectos.newInstance(proyectosFiltrados);
                else
                    swicthTo = FragmentProyectos.newInstance(Almacen.getProyectos());
                Toast.makeText(getApplicationContext(), "Proyectos", Toast.LENGTH_SHORT).show();
                break;
            case R.id.filtro_usuarios:
                //Si estoy en intereses muestro las usuarios filtrados
                if (itemMenuSeleccionado == R.id.navigation_intereses)
                    swicthTo = FragmentUsuarios.newInstance(usuariosFiltrados);
                else
                    swicthTo = FragmentUsuarios.newInstance(Almacen.getUsuarios());
                Toast.makeText(getApplicationContext(), "Usuarios", Toast.LENGTH_SHORT).show();
                break;
        }
        if (swicthTo != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, swicthTo);
            transaction.commit();
        }
    }

    /**
     * Cambia de fragment dependiendo de la opción del menú inferior que haya seleccionado
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment swicthTo = null;
            switch (item.getItemId()) {
                case R.id.navigation_muro:
                    //Muestro los filtros
                    filtros.setVisible(true);
                    //Guardo el item por el que estoy para luego comparar
                    itemMenuSeleccionado = R.id.navigation_muro;
                    //Cargo el fragment con los proyectos
                    swicthTo = FragmentProyectos.newInstance(Almacen.getProyectos());
                    break;
                case R.id.navigation_map:
                    //Oculto los filtros
                    filtros.setVisible(false);
                    //Guardo el item por el que estoy para luego comparar
                    itemMenuSeleccionado = R.id.navigation_map;
                    //Cargo el fragment del mapa con los proyectos
                    swicthTo = FragmentMapa.newInstance(Almacen.getProyectos());
                    break;
                case R.id.navigation_intereses:
                    //Muestro los filtros
                    filtros.setVisible(true);
                    //Guardo el item por el que estoy para luego comparar
                    itemMenuSeleccionado = R.id.navigation_intereses;
                    //Cargo el fragment con los filtros
                    swicthTo = FragmentProyectos.newInstance(proyectosFiltrados);
                    break;

            }
            if (swicthTo != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, swicthTo);
                transaction.commit();
            }
            return true;
        }

    };

    @Override
    public void onProyectoSeleccionado(int idProyecto) {
        Intent intent = new Intent(getApplicationContext(), ProyectoActivity.class);
        intent.putExtra("idProyecto", idProyecto);
        startActivity(intent);
    }

    @Override
    public void onUsuarioSeleccionado(int idUsuario) {
        Intent intent = new Intent(getApplicationContext(), UsuarioActivity.class);
        intent.putExtra("idUsuario",idUsuario);
        startActivity(intent);
    }

    @Override
    public void onProyectoSeleccionadoMapa(int idProyecto) {
        Intent intent = new Intent(getApplicationContext(), ProyectoActivity.class);
        intent.putExtra("idProyecto",  idProyecto);
        startActivity(intent);
    }




}
