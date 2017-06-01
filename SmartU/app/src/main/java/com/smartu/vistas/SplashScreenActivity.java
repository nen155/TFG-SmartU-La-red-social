package com.smartu.vistas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ControladorPreferencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    /**
     * Cargaría las 10 primeras más actuales ordenadas por fecha
     * Para cargar otras 10 se haría en el evento onScroll del RecyclerView
     * de cada tipo de publicación, para no llenar la memoria del smartphone
     */
    private class HPublicaciones extends AsyncTask<Void,Void,Void>{


        private long start;
        HPublicaciones() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Miro el momento en el que comienzo a cargar
            start = System.currentTimeMillis();
            //Recojo el resultado en un String
            String resultado="{\"publicaciones\":{\n" +
                    "  \"proyectos\":[\n" +
                    "    {\n" +
                    "      \"id\":\"1\",\"nombre\":\"SmartU\",\"descripcion\":\"Es el primer proyecto\",\"fechaCreacion\":\"2017-01-12\",\"fechaFinalizacion\":\"2018-03-29\",\"imagenDestacada\":\"wp-content/uploads/2017/05/logo_web.png\",\"coordenadas\":\"37.1625378,-3.5964669\",\"localizacion\":\"Calle puertas 10\",\"web\":\"http://coloredmoon.com\",\"idPropietario\":\"1\",\n" +
                    "        \"buenaIdea\":[{\"idUsuario\":\"1\"}],\n" +
                    "        \"misComentarios\":[{\"id\":\"1\",\"descripcion\":\"Es un buen proyecto, esta genial!\",\"fecha\":\"2017-05-29\",\"idUsuario\":\"1\",\"idProyecto\":\"1\",\"usuario\":\"Emilio\",\"proyecto\":\"SmartU\"}],\n" +
                    "        \"misAreas\":[{\"id\":\"1\",\"nombre\":\"Informatica\"},{\"id\":\"2\",\"nombre\":\"Empresariales\"}],\n" +
                    "        \"vacantesProyecto\":[{\"id\":\"1\",\"especialidades\":[\n" +
                    "              {\"id\":\"1\",\"nombre\":\"Informática\"}]}],\n" +
                    "              \"misArchivos\":[{\"id\":\"1\",\"nombre\":\"logo\",\"url\":\"wp-content/uploads/2017/05/logo_web.png\",\"tipo\":\"imagen\"}],\n" +
                    "              \"misAvances\":[{\"id\":\"1\",\"nombre\":\"Casi hemos terminado la app!\",\"fecha\":\"2017-01-12\",\"descripcion\":\"Hemos trabajado duro desde octubre de 2016 para que este proyecto saliese adelante y ahora hemos conseguido casi terminarlo.\",\n" +
                    "                \"misArchivos\":[{\"id\":\"1\",\"nombre\":\"logo\",\"url\":\"wp-content/uploads/2017/05/logo_web.png\",\"tipo\":\"imagen\"}]\n" +
                    "              }],\n" +
                    "              \"integrantes\":[\"1\",\"2\"]\n" +
                    "              ,\n" +
                    "              \"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.facebook.com/\"}]\n" +
                    "    }\n" +
                    "    ],\n" +
                    "  \"comentarios\":[],\n" +
                    "  \"notificaciones\":[],\n" +
                    "  \"usuarios\":[]\n" +
                    "}\n" +
                    "}";
            //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
            //String resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaPublicaciones,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\"0\"}","POST");
            //Voy a mantener arrays distintos para los elementos del muro
            //para que a la hora de actualizar con FCM actualice el array
            //que necesite
            JSONObject res =null;
            ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
            try {
                if(resultado !=null) {
                    res = new JSONObject(resultado);
                    if (!res.isNull("publicaciones")) {
                        JSONObject muroJSON = res.getJSONObject("publicaciones");
                        JSONArray proyectosJSON = muroJSON.getJSONArray("proyectos");
                        for(int i=0;i<proyectosJSON.length();++i)
                        {
                            JSONObject proyecto = proyectosJSON.getJSONObject(i);
                            Proyecto p = mapper.readValue(proyecto.toString(), Proyecto.class);
                            Almacen.proyectos.add(p);
                        }
                        JSONArray comentariosJSON = muroJSON.getJSONArray("comentarios");
                        for(int i=0;i<comentariosJSON.length();++i)
                        {
                            JSONObject comentario = comentariosJSON.getJSONObject(i);
                            Comentario c = mapper.readValue(comentario.toString(), Comentario.class);
                            Almacen.comentarios.add(c);
                        }
                        JSONArray notificacionesJSON = muroJSON.getJSONArray("notificaciones");
                        for(int i=0;i<notificacionesJSON.length();++i)
                        {
                            JSONObject notificacion = notificacionesJSON.getJSONObject(i);
                            Notificacion n = mapper.readValue(notificacion.toString(), Notificacion.class);
                            Almacen.notificaciones.add(n);
                        }
                        JSONArray usuariosJSON = muroJSON.getJSONArray("usuarios");
                        for(int i=0;i<usuariosJSON.length();++i)
                        {
                            JSONObject usuario = usuariosJSON.getJSONObject(i);
                            Usuario u = mapper.readValue(usuario.toString(), Usuario.class);
                            Almacen.usuarios.add(u);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
                    //Establezo que se elimine esta pantalla de la pila y añado los arrays al extra
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
