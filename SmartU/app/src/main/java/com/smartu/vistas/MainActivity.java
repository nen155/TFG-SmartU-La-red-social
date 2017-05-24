package com.smartu.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import toan.android.floatingactionmenu.FloatingActionButton;
import toan.android.floatingactionmenu.FloatingActionsMenu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartu.R;
import com.smartu.modelos.Area;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements FragmentProyectos.OnProyectoSelectedListener, FragmentUsuarios.OnUsuarioSelectedListener,FragmentMapa.OnProyectoSeleccionadoMapaListener {

    //Declaro los arrays filtrados por intereses y sin filtrar
    private static ArrayList<Proyecto> proyectos,proyectosFiltrados;
    private static ArrayList<Notificacion> notificaciones,notificacionesFiltradas;
    private static ArrayList<Usuario> usuarios,usuariosFiltrados;
    private static ArrayList<Comentario> comentarios,comentariosFiltrados;
    //Por defecto esta seleccionado el muro porque es lo primero que ve el usuario
    private static int itemMenuSeleccionado=R.id.navigation_muro;
    private FloatingActionsMenu filtros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        //Cojo las publicaciones cargadas durante el splashscreen
        if(bundle!=null) {
            proyectos = bundle.getParcelableArrayList("proyectos");
            notificaciones = bundle.getParcelableArrayList("notificaciones");
            usuarios = bundle.getParcelableArrayList("usuarios");
            comentarios = bundle.getParcelableArrayList("comentarios");
            //Si ha iniciado sesión el usuario filtro los arrays para
            //poder mostrarlos en el fragment Intereses
            if(Sesion.getUsuario(getBaseContext())!=null) {
                proyectosFiltrados = proyectosFiltrados();
                notificacionesFiltradas = notificacionsFiltradas();
                comentariosFiltrados = comentariosFiltrados();
                usuariosFiltrados = usuariosFiltrados();
            }
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

    }

    /**
     * Muestra un Fragment u otro en función del botón flotante que haya seleccionado y
     * en función de si estoy en intereses o en el muro
     * @param v
     */
    private void filtrar(View v){
        Fragment swicthTo=null;
        switch (v.getId()){
            case R.id.filtro_notificaciones:
                //Si estoy en intereses muestro las notificaciones filtradas
                if(itemMenuSeleccionado==R.id.navigation_intereses)
                    swicthTo= FragmentNotificaciones.newInstance(notificacionesFiltradas);
                else
                    swicthTo= FragmentNotificaciones.newInstance(notificaciones);
                Toast.makeText(getApplicationContext(),"Novedades",Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);*/
                break;
            case R.id.filtro_comentarios:
                //Si estoy en intereses muestro las comentarios filtrados
                if(itemMenuSeleccionado==R.id.navigation_intereses)
                    swicthTo=FragmentComentarios.newInstance(comentariosFiltrados);
                else
                    swicthTo=FragmentComentarios.newInstance(comentarios);
                Toast.makeText(getApplicationContext(),"Comentarios",Toast.LENGTH_SHORT).show();
                break;
            case R.id.filtro_proyectos:
                //Si estoy en intereses muestro los proyectos filtrados
                if(itemMenuSeleccionado==R.id.navigation_intereses)
                    swicthTo=FragmentProyectos.newInstance(proyectosFiltrados);
                else
                    swicthTo=FragmentProyectos.newInstance(proyectos);
                Toast.makeText(getApplicationContext(),"Proyectos",Toast.LENGTH_SHORT).show();
                break;
            case R.id.filtro_usuarios:
                //Si estoy en intereses muestro las usuarios filtrados
                if(itemMenuSeleccionado==R.id.navigation_intereses)
                    swicthTo=FragmentUsuarios.newInstance(usuariosFiltrados);
                else
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

    /**
     * Cambia de fragment dependiendo de la opción del menú inferior que haya seleccionado
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment swicthTo=null;
            switch (item.getItemId()) {
                case R.id.navigation_muro:
                    //Muestro los filtros
                    filtros.setVisible(true);
                    //Guardo el item por el que estoy para luego comparar
                    itemMenuSeleccionado =R.id.navigation_muro;
                    //Cargo el fragment con los proyectos
                    swicthTo = FragmentProyectos.newInstance(proyectos);
                    break;
                case R.id.navigation_map:
                    //Oculto los filtros
                    filtros.setVisible(false);
                    //Guardo el item por el que estoy para luego comparar
                    itemMenuSeleccionado=R.id.navigation_map;
                    //Cargo el fragment del mapa con los proyectos
                    swicthTo = FragmentMapa.newInstance(proyectos);
                    break;
                case R.id.navigation_intereses:
                    //Muestro los filtros
                    filtros.setVisible(true);
                    //Guardo el item por el que estoy para luego comparar
                    itemMenuSeleccionado=R.id.navigation_intereses;
                    //Cargo el fragment con los filtros
                    swicthTo = FragmentProyectos.newInstance(proyectosFiltrados);
                    break;

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
    public void onUsuarioSeleccionado( Usuario usuario) {
        /*Intent intent = new Intent(getApplicationContext(),UsuarioActivity.class);
        intent.putExtra("proyecto",usuario);
        startActivity(intent);*/
    }

    @Override
    public void onProyectoSeleccionadoMapa(Proyecto proyecto) {
        /*Intent intent = new Intent(getApplicationContext(),ProyectoActivity.class);
        intent.putExtra("proyecto",proyecto);
        startActivity(intent);*/
    }
    /**
     * Filtra los proyectos por intereses y por usuarios seguidos
     * @return
     */
    private ArrayList<Proyecto> proyectosFiltrados(){
        Usuario usuario = Sesion.getUsuario(getBaseContext());
        //Sino tengo proyectos o gente que siga y areas de interes devuelvo los proyectos como estan
        if(proyectos.isEmpty() || (usuario.getMisSeguidos().isEmpty() && usuario.getMisAreasInteres().isEmpty()))
            return proyectos;

        Stream<Proyecto> proyectoStream = proyectos.stream()
                .filter(
                proyecto -> proyecto.getMisAreas().stream()
                        .anyMatch(area -> usuario.getMisAreasInteres().stream()
                                .anyMatch(areaU -> areaU.getNombre().compareTo(area.getNombre()) == 0))
                        ||
                        usuario.getMisSeguidos().stream().anyMatch(usuario1 -> usuario1.getId() == usuario.getId())
                );
        Proyecto[] proyectos = proyectoStream.toArray(Proyecto[]::new);

        return new ArrayList<>(Arrays.asList(proyectos));
    }

    /**
     * Filtra las notificaciones por intereses y por usuarios a los que sigo
     * @return
     */
    private ArrayList<Notificacion> notificacionsFiltradas(){
        Usuario usuario = Sesion.getUsuario(getBaseContext());
        //Sino tengo proyectos o no tengo notificaciones gente que siga y areas de interes devuelvo los proyectos como estan
        if(proyectosFiltrados.isEmpty() || notificaciones.isEmpty() || usuario.getMisSeguidos().isEmpty())
            return notificaciones;

       return new ArrayList<Notificacion>(Arrays.asList(notificaciones.stream()
               .filter(notificacion -> proyectosFiltrados.stream()
                       .anyMatch(proyecto -> proyecto.getId()== notificacion.getProyecto().getId())
                               ||
                               usuario.getMisSeguidos().stream()
                                       .anyMatch(usuario1 -> usuario1.getId() == notificacion.getUsuario().getId())
                        )
               .toArray(Notificacion[]::new)
            )
       );
    }
    /**
     * Filtra los comentarios por intereses y por usuarios a los que sigo
     * @return
     */
    private ArrayList<Comentario> comentariosFiltrados(){
        Usuario usuario = Sesion.getUsuario(getBaseContext());
        //Sino tengo proyectos o no tengo notificaciones gente que siga y areas de interes devuelvo los proyectos como estan
        if(proyectosFiltrados.isEmpty() || comentarios.isEmpty() || usuario.getMisSeguidos().isEmpty())
            return comentarios;

        return new ArrayList<Comentario>(Arrays.asList(comentarios.stream()
                .filter(comentario -> proyectosFiltrados.stream()
                        .anyMatch(proyecto -> proyecto.getId()== comentario.getProyecto().getId())
                        ||
                        usuario.getMisSeguidos().stream()
                                .anyMatch(usuario1 -> usuario1.getId() == comentario.getUsuario().getId())
                )
                .toArray(Comentario[]::new)));
    }

    private ArrayList<Usuario> usuariosFiltrados(){
        Usuario usuario = Sesion.getUsuario(getBaseContext());
        //Si no tengo gente a la que sigo devuelvo el array como esta
        if(usuario.getMisSeguidos().isEmpty())
            return usuarios;
        else
            return usuario.getMisSeguidos();
    }


}
