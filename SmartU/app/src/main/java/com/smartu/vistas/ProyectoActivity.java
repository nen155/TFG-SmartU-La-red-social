package com.smartu.vistas;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.HBuenaIdea;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;

import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import java.util.ArrayList;
import java.util.Arrays;

public class ProyectoActivity extends AppCompatActivity implements FragmentIntegrantes.OnIntegranteSelectedListener {

    private static Proyecto proyecto;
    private Usuario usuarioSesion;
    private FloatingActionButton buenaidea, comentarios;
    private TextView buenaidea_contador;
    private static ArrayList<Usuario> integrantes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto);
        Bundle bundle = getIntent().getExtras();
        //Obtengo el proyecto que me han pasado
        if (bundle != null) {
            int idProyecto = bundle.getInt("idProyecto");
            Almacen.buscar(idProyecto,proyecto,this);
            Almacen.buscarUsuarios(proyecto.getIntegrantes(),integrantes,this);
        }
        //Cargo el menú lateral y pongo el nombre del proyecto a el Toolbar
        SliderMenu sliderMenu = new SliderMenu(getBaseContext(), this);
        sliderMenu.inicializateToolbar(proyecto.getNombre());
        setTitle(proyecto.getNombre());
        //Inicia
        buenaidea_contador = (TextView) findViewById(R.id.buenaidea_text_proyecto);
        buenaidea = (FloatingActionButton) findViewById(R.id.buenaidea_proyecto);

        //Obtengo el usuario que ha iniciado sesión
        usuarioSesion = Sesion.getUsuario(getApplicationContext());
        //Cargo las preferencias del usuario si tuviese sesión
        cargarPreferenciasUsuario();

        buenaidea_contador.setText(String.valueOf(proyecto.getBuenaIdea().size()));
        buenaidea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darBuenaIdea();
            }
        });
        comentarios = (FloatingActionButton) findViewById(R.id.comentar_proyecto_fab);
        comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //Me deshago de iconos innecesarios
                comentarios.setVisibility(View.GONE);
                buenaidea.setVisibility(View.GONE);
                buenaidea_contador.setVisibility(View.GONE);
                // que pertenezcan al proyecto en lugar de guardarlos
                ArrayList<Comentario> comentariosProyecto=null;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    comentariosProyecto = new ArrayList<Comentario>(Arrays.asList(Almacen.getComentarios().stream().filter(comentario -> comentario.getIdProyecto() == proyecto.getId()).toArray(Comentario[]::new)));
                else
                    comentariosProyecto = new ArrayList<Comentario>(StreamSupport.parallelStream(Almacen.getComentarios()).filter(comentario -> comentario.getIdProyecto() == proyecto.getId()).collect(Collectors.toList()));
                transaction.replace(R.id.content_proyecto, FragmentComentariosProyecto.newInstance(comentariosProyecto, proyecto));
                transaction.commit();
            }
        });


        //Cargo el fragment por defecto
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_proyecto, FragmentProyecto.newInstance(proyecto));
        transaction.commit();
        //Inicializo el menú de abajo
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_proyecto);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * Método para dar buena idea a un proyecto
     */
    private void darBuenaIdea() {
        //Compruebo si el usuario ha iniciado sesión
        if (usuarioSesion != null) {
            //Actualizo el botón
            buenaidea.setPressed(!buenaidea.isPressed());
            //Compruebo como ha quedado su estado después de hacer click
            HBuenaIdea hBuenaIdea;
            //Añado buena idea
            if (buenaidea.isPressed()) {
                buenaidea.setImageResource(R.drawable.buenaidea);
                //Añado al contador 1 para decir que es buena idea
                int cont = Integer.parseInt(buenaidea_contador.getText().toString()) + 1;
                buenaidea_contador.setText(String.valueOf(cont));
                Toast.makeText(getApplicationContext(), "Genial!, este proyecto te parece buena idea!", Toast.LENGTH_SHORT).show();
                //Inicializo la hebra con false pues voy a añadir una nueva idea
                hBuenaIdea = new HBuenaIdea(false, getApplicationContext(), proyecto, buenaidea, buenaidea_contador);
                //Para poder poner la referencia a null cuando termine la hebra
                hBuenaIdea.sethBuenaIdea(hBuenaIdea);
            } else {//Elimino buena idea
                buenaidea.setImageResource(R.drawable.idea);
                //Elimino de buena idea 1 usuario.
                int cont = Integer.parseInt(buenaidea_contador.getText().toString()) - 1;
                buenaidea_contador.setText(String.valueOf(cont));
                Toast.makeText(getApplicationContext(), "¿Ya no te parece buena idea?", Toast.LENGTH_SHORT).show();
                //Inicializo la hebra con true para eliminarla
                hBuenaIdea = new HBuenaIdea(true, getApplicationContext(), proyecto, buenaidea, buenaidea_contador);
                //Para poder poner la referencia a null cuando termine la hebra
                hBuenaIdea.sethBuenaIdea(hBuenaIdea);
            }
            hBuenaIdea.execute();
        } else {
            //Lo mando al login
            Intent intent = new Intent(ProyectoActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Comprueba si el usuario ha dado buena idea al proyecto
     */
    private void cargarPreferenciasUsuario() {
        //Cargo las preferencias del usuario
        if (usuarioSesion != null && proyecto.getBuenaIdea()!=null) {
            //Compruebo si el usuario le ha dado antes a buena idea a este proyecto
            boolean usuarioBuenaidea = false;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                usuarioBuenaidea= proyecto.getBuenaIdea().stream().anyMatch(buenaIdea -> buenaIdea.getIdUsuario() == usuarioSesion.getId());
            else
                usuarioBuenaidea= StreamSupport.parallelStream(proyecto.getBuenaIdea()).filter(buenaIdea -> buenaIdea.getIdUsuario() == usuarioSesion.getId()).findAny().isPresent();
            //Si es así lo dejo presionado y le cambio la imagen
            buenaidea.setPressed(usuarioBuenaidea);
            if (usuarioBuenaidea) {
                buenaidea.setImageResource(R.drawable.buenaidea);

            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment swicthTo = null;
            switch (item.getItemId()) {
                case R.id.navigation_proyecto:
                    buenaidea.setVisibility(View.VISIBLE);
                    buenaidea_contador.setVisibility(View.VISIBLE);
                    comentarios.setVisibility(View.VISIBLE);
                    swicthTo = FragmentProyecto.newInstance(proyecto);
                    break;
                case R.id.navigation_integrantes:
                    buenaidea.setVisibility(View.VISIBLE);
                    buenaidea_contador.setVisibility(View.VISIBLE);
                    comentarios.setVisibility(View.VISIBLE);
                    swicthTo = FragmentIntegrantes.newInstance(integrantes, proyecto.getVacantesProyecto(), proyecto);
                    break;
                case R.id.navigation_map_proyecto:
                    buenaidea.setVisibility(View.VISIBLE);
                    buenaidea_contador.setVisibility(View.VISIBLE);
                    comentarios.setVisibility(View.VISIBLE);
                    swicthTo = FragmentMapaProyecto.newInstance(proyecto);
                    break;
                case R.id.navigation_multimedia:
                    buenaidea.setVisibility(View.VISIBLE);
                    buenaidea_contador.setVisibility(View.VISIBLE);
                    comentarios.setVisibility(View.VISIBLE);
                    swicthTo = FragmentMultimedia.newInstance(proyecto.getMisArchivos(), proyecto.getId());
                    break;
            }
            if (swicthTo != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_proyecto, swicthTo);
                transaction.commit();
            }
            return true;
        }

    };

    @Override
    public void onUsuarioSeleccionado(int idUsuario) {
        Intent intent = new Intent(getApplicationContext(), UsuarioActivity.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
    }

}
