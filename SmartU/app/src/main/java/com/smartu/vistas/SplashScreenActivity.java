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
        //Voy a mantener arrays distintos para los elementos del muro
        //para que a la hora de actualizar con FCM actualice el array
        //que necesite
        private ArrayList<Proyecto> proyectos;
        private ArrayList<Notificacion> notificaciones;
        private ArrayList<Usuario> usuarios;
        private ArrayList<Comentario> comentarios;

        private long start;
        HPublicaciones() {
            proyectos = new ArrayList<>();
            notificaciones = new ArrayList<>();
            usuarios = new ArrayList<>();
            comentarios = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Miro el momento en el que comienzo a cargar
            start = System.currentTimeMillis();
            //Recojo el resultado en un String
            String resultado="{\"muro\":{" +
                    "\"proyectos\":[" +
                    "{" +
                    "\"id\":\"1\",\"nombre\":\"SmartU\",\"descripcion\":\"Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas 'Letraset', las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum. That’s the core part of this article. First, the collapsing layout specify how it will behave when the content is scrolled using the flags scroll|exitUntilCollapsed, so it will scroll until it’s completely collapsed. Then we specify the contentScrim, which is the color the toolbar will take when it reaches it’s collapsed state. I’ll be changing this programmatically and use palette to decide its color. We can also specify the margins for the title when it’s expanded. It will create a nice effect over the toolbar title. You can define some other things, such as the statusScrim or the textAppearance for the collapsed and expanded title." +
                    "Al contrario del pensamiento popular, el texto de Lorem Ipsum no es simplemente texto aleatorio. Tiene sus raices en una pieza cl´sica de la literatura del Latin, que data del año 45 antes de Cristo, haciendo que este adquiera mas de 2000 años de antiguedad. Richard McClintock, un profesor de Latin de la Universidad de Hampden-Sydney en Virginia, encontró una de las palabras más oscuras de la lengua del latín.\",\"fechaCreacion\":\"2017-01-22\",\"fechaFinalizacion\":\"2018-12-10\",\"imagenDestacada\":\"https://www.google.es/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png\",\"coordenadas\":\"37.1625378,-3.5964669\",\"localizacion\":\"Calle puertas 10\",\"web\":\"http://coloredmoon.com\"" +
                    "}" +
                    "]," +
                    "\"comentarios\":[]," +
                    "\"notificaciones\":[]," +
                    "\"usuarios\":[]" +
                    "}" +
                    "}";
            //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
            //String resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaPublicaciones,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\"0\"}","POST");
            JSONObject res =null;
            ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
            try {
                if(resultado !=null) {
                    res = new JSONObject(resultado);
                    if (!res.isNull("muro")) {
                        JSONObject muroJSON = res.getJSONObject("muro");
                        JSONArray proyectosJSON = muroJSON.getJSONArray("proyectos");
                        for(int i=0;i<proyectosJSON.length();++i)
                        {
                            JSONObject proyecto = proyectosJSON.getJSONObject(i);
                            Proyecto p = mapper.readValue(proyecto.toString(), Proyecto.class);
                            proyectos.add(p);
                        }
                        JSONArray comentariosJSON = muroJSON.getJSONArray("comentarios");
                        for(int i=0;i<comentariosJSON.length();++i)
                        {
                            JSONObject comentario = comentariosJSON.getJSONObject(i);
                            Comentario c = mapper.readValue(comentario.toString(), Comentario.class);
                            comentarios.add(c);
                        }
                        JSONArray notificacionesJSON = muroJSON.getJSONArray("notificaciones");
                        for(int i=0;i<notificacionesJSON.length();++i)
                        {
                            JSONObject notificacion = notificacionesJSON.getJSONObject(i);
                            Notificacion n = mapper.readValue(notificacion.toString(), Notificacion.class);
                            notificaciones.add(n);
                        }
                        JSONArray usuariosJSON = muroJSON.getJSONArray("usuarios");
                        for(int i=0;i<usuariosJSON.length();++i)
                        {
                            JSONObject usuario = usuariosJSON.getJSONObject(i);
                            Usuario u = mapper.readValue(usuario.toString(), Usuario.class);
                            usuarios.add(u);
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
                    intent.putParcelableArrayListExtra("proyectos",proyectos);
                    intent.putParcelableArrayListExtra("notificaciones",notificaciones);
                    intent.putParcelableArrayListExtra("usuarios",usuarios);
                    intent.putParcelableArrayListExtra("comentarios",comentarios);
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
