package com.smartu.vistas;

import android.content.Intent;
import android.net.Uri;
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

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.HSeguir;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Constantes;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import java8.util.stream.StreamSupport;

public class UsuarioActivity extends AppCompatActivity implements FragmentProyectos.OnProyectoSelectedListener {

    private FloatingActionButton seguir, mensaje;
    private TextView seguirContador;
    private Usuario usuario = new Usuario();
    private Usuario usuarioSesion;
    private ArrayList<Proyecto> misProyectos = new ArrayList<>();
    private boolean companiero = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Pongo los iconos para los TextView
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_usuario);


        Bundle bundle = getIntent().getExtras();
        //Obtengo el usuario que me han pasado
        if (bundle != null) {
            int idUsuario = bundle.getInt("idUsuario");
            Almacen.buscar(idUsuario, usuario, UsuarioActivity.this);

            //Obtengo el usuario que ha iniciado sesión
            usuarioSesion = Sesion.getUsuario(getApplicationContext());
            //Cargo el menú lateral y pongo el nombre del proyecto a el Toolbar
            SliderMenu sliderMenu = new SliderMenu(getBaseContext(), this);
            sliderMenu.inicializateToolbar(usuario.getUser());
            setTitle(usuario.getUser());

            seguir = (FloatingActionButton) findViewById(R.id.seguir_usuario);
            seguir.setTag(R.drawable.seguir);
            mensaje = (FloatingActionButton) findViewById(R.id.enviar_mensaje_usuario);
            companiero = false;
            //Si tengo sesión
            if (usuarioSesion != null) {
                //Compruebo si son compañeros
                companiero = Almacen.esCompaniero(usuarioSesion, usuario);
                //Si no son compañeros sólo podrá enviarle un email, en lugar de un mensaje
                if (!companiero)
                    mensaje.setImageResource(R.drawable.email);
            }
            seguirContador = (TextView) findViewById(R.id.seguir_contador_usuario);
            //Cargo la imagen de perfil del usuario
            CircleImageView imagenPerfil = (CircleImageView) findViewById(R.id.img_activity_usuario);
            Picasso.with(getApplicationContext()).load(ConsultasBBDD.server + ConsultasBBDD.imagenes +  usuario.getImagenPerfil()).into(imagenPerfil);


            seguir.setOnClickListener(seguirUsuario());
            //Cargo las preferencias del usuario si tuviese sesión
            cargarPreferenciasUsuario();
            //Establezco el contador con el número de seguidores del usuario actual
            if (usuario.getMiStatus() != null)
                seguirContador.setText(String.valueOf(usuario.getMiStatus().getNumSeguidores()));
            //Le envio un mensaje al usuario
            mensaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (usuarioSesion != null && companiero) {
                        Intent intent = new Intent(UsuarioActivity.this, MensajesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("usuario", (Parcelable) usuario);
                        startActivity(intent);
                    } else if (!companiero && usuarioSesion != null) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("plain/text");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { usuario.getEmail() });
                        if (intent.resolveActivity(getPackageManager()) != null)
                            startActivity(intent);
                    } else {
                        Intent intent = new Intent(UsuarioActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });

            Almacen.buscarProyectos(usuario.getMisProyectos(), misProyectos, UsuarioActivity.this);

            //Cargo el perfil por defecto
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_usuario, FragmentUsuario.newInstance(usuario));
            transaction.commit();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationUsuario);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment swicthTo = null;
            switch (item.getItemId()) {
                case R.id.navigation_perfil:
                    swicthTo = FragmentUsuario.newInstance(usuario);
                    break;
                case R.id.navigation_proyectos:
                    swicthTo = FragmentProyectos.newInstance(misProyectos);
                    break;
                /*case R.id.navigation_notifications:
                    swicthTo = null;
                    return true;*/
            }
            if (swicthTo != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_usuario, swicthTo);
                transaction.commit();
            }
            return true;
        }

    };


    /**
     * Comprueba si el usuario ha dado buena idea al proyecto
     */
    private void cargarPreferenciasUsuario() {
        //Cargo las preferencias del usuario
        if (usuarioSesion != null && usuarioSesion.getMisSeguidos() != null) {
            //Compruebo si el usuario le ha dado antes a seguir a este usuario
            boolean usuarioSigue = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                usuarioSigue = usuarioSesion.getMisSeguidos().parallelStream().anyMatch(usuario1 -> usuario1 == usuario.getId());
            else
                usuarioSigue = StreamSupport.stream(usuarioSesion.getMisSeguidos()).filter(usuario1 -> usuario1 == usuario.getId()).findAny().isPresent();
            //Si es así lo dejo presionado
            if(usuarioSigue) {
                seguir.setImageResource(R.drawable.dejarseguir);
                seguir.setTag(R.drawable.dejarseguir);
            }
            else {
                seguir.setImageResource(R.drawable.seguir);
                seguir.setTag(R.drawable.seguir);
            }
        }
    }

    /**
     * Permite seguir al usuario si se ha iniciado sesión
     *
     * @return
     */
    private View.OnClickListener seguirUsuario() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si el usuario ha iniciado sesión
                if (usuarioSesion != null) {
                    HSeguir hSeguir;
                    //Actualizo el botón
                    Integer integer = (Integer) seguir.getTag();
                    if(R.drawable.seguir==integer) {
                        seguir.setImageResource(R.drawable.dejarseguir);
                        seguir.setTag(R.drawable.dejarseguir);
                    }
                    else {
                        seguir.setImageResource(R.drawable.seguir);
                        seguir.setTag(R.drawable.seguir);
                    }
                    //Compruebo como ha quedado su estado después de hacer click
                    if (R.drawable.seguir==integer) {
                        //Añado al contador 1 para decir que eres idProyecto
                        int cont = Integer.parseInt(seguirContador.getText().toString()) + 1;
                        seguirContador.setText(String.valueOf(cont));
                        Toast.makeText(getApplicationContext(), "Genial!,sigues a este usuario!", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra
                        hSeguir = new HSeguir(false, usuario, UsuarioActivity.this, null, seguirContador);
                        hSeguir.setFabButton(seguir);
                        //Para poder poner la referencia a null cuando termine la hebra
                        hSeguir.sethSeguir(hSeguir);
                    } else {
                        //Añado al contador 1 para decir que eres idProyecto
                        int cont = Integer.parseInt(seguirContador.getText().toString()) - 1;
                        seguirContador.setText(String.valueOf(cont));
                        Toast.makeText(getApplicationContext(), "¿Ya no te interesa el usuario?", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra
                        hSeguir = new HSeguir(false, usuario, UsuarioActivity.this, null, seguirContador);
                        hSeguir.setFabButton(seguir);
                        //Para poder poner la referencia a null cuando termine la hebra
                        hSeguir.sethSeguir(hSeguir);
                    }
                    hSeguir.execute();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onProyectoSeleccionado(int idProyecto) {
        Intent intent = new Intent(getApplicationContext(), ProyectoActivity.class);
        intent.putExtra(Constantes.ID_PROYECTO, idProyecto);
        startActivity(intent);
    }
}
