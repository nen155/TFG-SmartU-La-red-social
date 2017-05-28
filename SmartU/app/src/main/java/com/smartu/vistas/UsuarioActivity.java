package com.smartu.vistas;

import android.content.Intent;
import android.os.Bundle;
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
import com.smartu.hebras.HSeguir;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsuarioActivity extends AppCompatActivity {

    private FloatingActionButton seguir,mensaje;
    private TextView seguirContador;
    private Usuario usuario;
    private Usuario usuarioSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Pongo los iconos para los TextView
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_usuario);
        Bundle bundle = getIntent().getExtras();
        //Obtengo el usuario que me han pasado
        if(bundle!=null) {
            usuario = bundle.getParcelable("usuario");

            //Cargo el menú lateral y pongo el nombre del proyecto a el Toolbar
            SliderMenu sliderMenu = new SliderMenu(getBaseContext(), this);
            sliderMenu.inicializateToolbar(usuario.getUser());
            setTitle(usuario.getUser());

            seguir = (FloatingActionButton) findViewById(R.id.seguir_usuario);
            mensaje = (FloatingActionButton) findViewById(R.id.enviar_mensaje_usuario);
            seguirContador = (TextView) findViewById(R.id.seguir_contador_usuario);
            //Cargo la imagen de perfil del usuario
            CircleImageView imagenPerfil = (CircleImageView) findViewById(R.id.img_activity_usuario);
            Picasso.with(getApplicationContext()).load(ConsultasBBDD.server + usuario.getImagenPerfil()).into(imagenPerfil);
            //Obtengo el usuario que ha iniciado sesión
            usuarioSesion = Sesion.getUsuario(getApplicationContext());

            seguir.setOnClickListener(seguirUsuario());
            //Cargo las preferencias del usuario si tuviese sesión
            cargarPreferenciasUsuario();
            //Establezco el contador con el número de seguidores del usuario actual
            seguirContador.setText(String.valueOf(usuario.getMiStatus().getNumSeguidores()));

            mensaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    //transaction.replace(R.id.content_usuario,);
                    transaction.commit();
                }
            });

            //Cargo el perfil por defecto
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_usuario, FragmentUsuario.newInstance(usuario));
            transaction.commit();
        }
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_perfil);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment swicthTo=null;
            switch (item.getItemId()) {
                case R.id.navigation_perfil:
                    swicthTo = FragmentUsuario.newInstance(usuario);
                    break;
                case R.id.navigation_proyectos:
                    swicthTo = FragmentProyectos.newInstance(usuario.getMisProyectos());
                    break;
                /*case R.id.navigation_notifications:
                    swicthTo = null;
                    return true;*/
            }
            if(swicthTo!=null){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_usuario,swicthTo);
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
        if (usuarioSesion != null) {
            //Compruebo si el usuario le ha dado antes a seguir a este usuario
            boolean usuarioSigue = usuarioSesion.getMisSeguidos().stream().anyMatch(usuario1 -> usuario1.getId() == usuario.getId());
            //Si es así lo dejo presionado
            seguir.setPressed(usuarioSigue);
        }
    }

    /**
     * Permite seguir al usuario si se ha iniciado sesión
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
                    seguir.setPressed(!seguir.isPressed());
                    //Compruebo como ha quedado su estado después de hacer click
                    if (seguir.isPressed()) {
                        //Añado al contador 1 para decir que eres idProyecto
                        int cont = Integer.parseInt(seguirContador.getText().toString()) + 1;
                        seguirContador.setText(String.valueOf(cont));
                        Toast.makeText(getApplicationContext(), "Genial!,sigues a este usuario!", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra
                        hSeguir = new HSeguir(false,usuario,getApplicationContext(),null,seguirContador);
                        hSeguir.setFabButton(seguir);
                        //Para poder poner la referencia a null cuando termine la hebra
                        hSeguir.sethSeguir(hSeguir);
                    } else {
                        //Añado al contador 1 para decir que eres idProyecto
                        int cont = Integer.parseInt(seguirContador.getText().toString()) - 1;
                        seguirContador.setText(String.valueOf(cont));
                        Toast.makeText(getApplicationContext(), "¿Ya no te interesa el usuario?", Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra
                        hSeguir = new HSeguir(false,usuario,getApplicationContext(),null,seguirContador);
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
}
