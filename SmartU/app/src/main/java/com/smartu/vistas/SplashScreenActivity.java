package com.smartu.vistas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.smartu.R;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Novedad;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.ControladorPreferencias;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Emilio Chica Jiménez on 17/05/17.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private HPublicaciones hPublicaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Oculto la barra de título para que no se vea en el Splash
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Establezco el contenido de la View al layout correspondiente
        setContentView(R.layout.activity_splash_screen);
        //Cargo las preferencias guardadas por el usuario, tipo de sesión, sesion y si es la primera vez
        ControladorPreferencias.cargarPreferencias(this);
        hPublicaciones = new HPublicaciones();
        hPublicaciones.execute();
    }

    private class HPublicaciones extends AsyncTask<Void,Void,Void>{
        //Voy a mantener arrays distintos para los elementos del muro
        //para que a la hora de actualizar con FCM actualice el array
        //que necesite
        private ArrayList<Proyecto> proyectos;
        private ArrayList<Novedad> novedades;
        private ArrayList<Usuario> usuarios;
        private ArrayList<Comentario> comentarios;

        private long start;
        HPublicaciones() {
            proyectos = new ArrayList<>();
            novedades = new ArrayList<>();
            usuarios = new ArrayList<>();
            comentarios = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Miro el momento en el que comienzo a cargar
            start = System.currentTimeMillis();
            //Cojo el resultado en un String
            String resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaPublicaciones,"","GET");
            JSONObject res =null;
            try {
                if(resultado !=null) {
                    res = new JSONObject(resultado);
                    if (!res.isNull("muro")) {
                        JSONObject proyectosJSON = res.getJSONObject("proyectos");
                        JSONObject comentariosJSON = res.getJSONObject("comentarios");
                        JSONObject novedadesJSON = res.getJSONObject("novedades");
                        JSONObject usuariosJSON = res.getJSONObject("usuarios");


                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hPublicaciones=null;
            //Cojo el tiempo en el que he parado
            long stop=  System.currentTimeMillis();
            //Calculo el tiempo en milisegundos que he tardado, desde que empecé hasta que terminé la hebra
            long tiempo = stop-start;
            //Cuando va a empezar el Main
            long comienzo=3000;
            //Si el tiempo que tardo entre que comienzo a descargar y que termino es menor de 3 segundos
            //establezco el tiempo de comienzo a lo que reste para que sean exactamente 3 segundos lo que
            //dure el splashscreen, sino es porque he tardado mucho en descargar por lo tanto lo ejecuto
            //inmediatamente
            if(tiempo<comienzo)
                comienzo -=tiempo;
            else
                comienzo=0;
            //Creo la ejecución con exactamente 3 segundos o lo que tarde en cargar las cosas del server.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=null;
                    //Compruebo si es la primera vez que abre la aplicación
                    if (ControladorPreferencias.isFirstTime())
                        //Sino es así le muestro las instrucciones
                        intent= new Intent(SplashScreenActivity.this, InstruccionesMainActivity.class);
                    else
                        //Sino me voy al Main
                        intent = new Intent(SplashScreenActivity.this, MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("proyectos",proyectos);
                    intent.putExtra("novedades",novedades);
                    intent.putExtra("usuarios",usuarios);
                    intent.putExtra("comentarios",comentarios);
                    startActivity(intent);
                }
            }, comienzo);

        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hPublicaciones=null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hPublicaciones=null;
        }
    }
}
