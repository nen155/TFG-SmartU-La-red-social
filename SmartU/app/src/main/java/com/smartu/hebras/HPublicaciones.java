package com.smartu.hebras;

/**
 * Created by NeN on 01/06/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.almacenamiento.Almacen;
import com.smartu.contratos.Publicacion;
import com.smartu.modelos.Area;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Constantes;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.ControladorPreferencias;
import com.smartu.vistas.InstruccionesMainActivity;
import com.smartu.vistas.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Cargaría las 10 primeras más actuales ordenadas por fecha
 * Para cargar otras 10 se haría en el evento onScroll del RecyclerView
 * de cada tipo de publicación, para no llenar la memoria del smartphone
 */
public class HPublicaciones extends AsyncTask<Void,Void,Void> {

    private HPublicaciones hPublicaciones;
    private long start;
    private Context context;
    private ArrayList<Integer> idsPublicaciones=null;
    private ArrayList<Publicacion> publicaciones =null;
    private int tipo=-1;
    private SweetAlertDialog pDialog;
    private String notificacion=null;

    public HPublicaciones(Context context) {

        this.context = context;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);

    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setIdsPublicaciones(ArrayList<Integer> idsPublicaciones) {
        this.idsPublicaciones = idsPublicaciones;
        //Lo inicializo aquí pues se que voy a necesitarlo sino lo dejo como null
        this.publicaciones = new ArrayList<>();
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public void sethPublicaciones(HPublicaciones hPublicaciones) {
        this.hPublicaciones = hPublicaciones;
    }
    @Override
    protected void onPreExecute() {
        pDialog.show();
    }
    @Override
    protected Void doInBackground(Void... params) {
        //Miro el momento en el que comienzo a cargar
        start = System.currentTimeMillis();
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        //Recojo el resultado en un String
        /*String resultado="{\"publicaciones\":{\n" +
                "  \"proyectos\":[\n" +
                "    {\n" +
                "      \"id\":\"1\",\"nombre\":\"SmartU\",\"descripcion\":\"La idea general de este proyecto es mediante el uso de herramientas, metodologías y técnicas provenientes de todas las disciplinas integrantes del proyecto se obtenga como resultado un producto final, el cual conecte la Universidad con la ciudad mediante un espacio de coworking de ideas y servicios.\",\"fechaCreacion\":\"2017-01-12\",\"fechaFinalizacion\":\"2018-03-29\",\"imagenDestacada\":\"wp-content/uploads/2017/05/logo_web.png\",\"coordenadas\":\"37.1625378,-3.5964669\",\"localizacion\":\"Calle puertas 10\",\"web\":\"http://coloredmoon.com\",\"idPropietario\":\"1\",\n" +
                "        \"buenaIdea\":[{\"idUsuario\":\"1\"}],\n" +
                "        \"misAreas\":[{\"id\":\"1\",\"nombre\":\"Informatica\"},{\"id\":\"2\",\"nombre\":\"Empresariales\"}],\n" +
                "        \"vacantesProyecto\":[{\"id\":\"1\",\"especialidades\":[\n" +
                "              {\"id\":\"1\",\"nombre\":\"Informática\"}]}],\n" +
                "        \"misArchivos\":[{\"id\":\"1\",\"nombre\":\"logo\",\"url\":\"wp-content/uploads/2017/05/logo_web.png\",\"tipo\":\"imagen\"}],\n" +
                "        \"misAvances\":[{\"id\":\"1\",\"nombre\":\"Casi hemos terminado la app!\",\"fecha\":\"2017-01-12\",\"descripcion\":\"Hemos trabajado duro desde octubre de 2016 para que este proyecto saliese adelante y ahora hemos conseguido casi terminarlo.\",\n" +
                "                \"misArchivos\":[{\"id\":\"1\",\"nombre\":\"logo\",\"url\":\"wp-content/uploads/2017/05/logo_web.png\",\"tipo\":\"imagen\"}]\n" +
                "              }],\n" +
                "        \"integrantes\":[\"1\"],\n" +
                "        \"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.facebook.com/\"}]\n" +
                "    }\n" +
                "    ],\n" +
                "  \"comentarios\":[{\"id\":\"1\",\"descripcion\":\"Es un buen proyecto, esta genial!\",\"fecha\":\"2017-05-29\",\"idUsuario\":\"1\",\"idProyecto\":\"1\",\"usuario\":\"emiliocj\",\"proyecto\":\"SmartU\"}],\n" +
                "  \"notificaciones\":[],\n" +
                "  \"usuarios\":[{\"id\":\"1\",\"nombre\":\"Emilio\",\"apellidos\":\"Chica Jiménez\",\"verificado\":\"true\",\"user\":\"emiliocj\",\"email\":\"emiliocj@correo.ugr.es\",\"nPuntos\":\"100\",\"localizacion\":\"C/Poeta Manuel\",\"biografia\":\"Estudiante universitario de la ETSIIT que vive en Granada y es Graduado en Ingeniería Informática\", \"web\":\"http://coloremoon.com\",\"imagenPerfil\":\"wp-content/uploads/2017/05/foto-buena.jpg\",\n" +
                "          \"misProyectos\":[\"1\"],\n" +
                "            \"misAreasInteres\":[\n" +
                "              {\"id\":\"1\",\"nombre\":\"Informática\"}\n" +
                "              ],\n" +
                "              \"misEspecialidades\":[\n" +
                "              {\"id\":\"1\",\"nombre\":\"Informática\"}\n" +
                "              ],\n" +
                "              \"miStatus\":{\"id\":\"1\",\"nombre\":\"creador\",\"puntos\":\"100\",\"numSeguidores\":\"1\"},\n" +
                "              \"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.facebook.com/\"}],\n" +
                "\"uid\":\"i7OptZwlUQVVQ5w7bHtrgkD5tW43\","+
                "\"firebaseToken\":\"f-R46MZAWX4:APA91bHpMWEfmjmcJQXgtEvDrvacEDReiLhO0ErEvjldGFobof0xbn8LJqqECIPqGzsvtVjkrx6Ew2Koky-u_YX41mBvIThe4glvT-x70s89o2dXvzptsPrMXych6ugQYHGRQ2LtxJ7L\""+
                "    }],\n" +
                "\"areas\":[{\"id\":\"1\",\"nombre\":\"Informática\"},{\"id\":\"2\",\"nombre\":\"Diseño gráfico\"},{\"id\":\"3\",\"nombre\":\"Edificación\"},{\"id\":\"4\",\"nombre\":\"Empresariales\"},{\"id\":\"5\",\"nombre\":\"Audio visuales\"},{\"id\":\"6\",\"nombre\":\"Comunicación\"}]"+
                "}\n" +
                "}";*/

        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        String resultado="";
        if(tipo==-1)
            resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaPublicaciones,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\"0\"}","POST");
        else{
            String idsPubli="[]";
            try {
                 idsPubli = mapper.writeValueAsString(idsPublicaciones);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //Esta consulta coge un Array de IDs para que se traiga esas publicaciones que faltan, NO SE TRAE ASOCIADOS
            resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaPublicaciones,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\"0\", \"publicaciones\":"+idsPubli+", \"tipo\":\""+tipo+"\"}","POST");
        }

        JSONObject res =null;

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
                        Almacen.add(p);
                    }
                    JSONArray comentariosJSON = muroJSON.getJSONArray("comentarios");
                    for(int i=0;i<comentariosJSON.length();++i)
                    {
                        JSONObject comentario = comentariosJSON.getJSONObject(i);
                        Comentario c = mapper.readValue(comentario.toString(), Comentario.class);
                        Almacen.add(c);
                    }
                    JSONArray notificacionesJSON = muroJSON.getJSONArray("notificaciones");
                    for(int i=0;i<notificacionesJSON.length();++i)
                    {
                        JSONObject notificacion = notificacionesJSON.getJSONObject(i);
                        Notificacion n = mapper.readValue(notificacion.toString(), Notificacion.class);
                        Almacen.add(n);
                    }
                    JSONArray usuariosJSON = muroJSON.getJSONArray("usuarios");
                    for(int i=0;i<usuariosJSON.length();++i)
                    {
                        JSONObject usuario = usuariosJSON.getJSONObject(i);
                        Usuario u = mapper.readValue(usuario.toString(), Usuario.class);
                        Almacen.add(u);
                    }
                    JSONArray areasJSON = muroJSON.getJSONArray("areas");
                    for(int i=0;i<areasJSON.length();++i)
                    {
                        JSONObject area = areasJSON.getJSONObject(i);
                        Area a = mapper.readValue(area.toString(), Area.class);
                        Almacen.add(a);
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
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hPublicaciones=null;
        //Significa que estoy en la hebra del SplashScreen
        if(tipo==-1) {
            //Cojo el tiempo en el que he parado
            long stop = System.currentTimeMillis();
            //Calculo el tiempo en milisegundos que he tardado, desde que empecé hasta que terminé la hebra
            long tiempo = stop - start;
            //Cuando va a empezar el Main
            long comienzo = 3000;
            //Si el tiempo que tardo entre que comienzo a descargar y que termino es menor de 3 segundos
            //establezco el tiempo de comienzo a lo que reste para que sean exactamente 3 segundos lo que
            //dure el splashscreen, sino es porque he tardado mucho en descargar por lo tanto lo ejecuto
            //inmediatamente
            if (tiempo < comienzo)
                comienzo -= tiempo;
            else
                comienzo = 0;

            //Creo la ejecución con exactamente 3 segundos o lo que tarde en cargar las cosas del server.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = null;
                    //Compruebo si es la primera vez que abre la aplicación
                    if (ControladorPreferencias.isFirstTime()) {

                        //Sino es así le muestro las instrucciones
                        intent = new Intent(context, InstruccionesMainActivity.class);
                        //Ya no es la primera vez por lo que guardo en las preferencias para que no muestre
                        //otra vez
                        ControladorPreferencias.guardarPreferenciasFirstTime(context, false);
                    }else {
                        //Sino me voy al Main
                        intent = new Intent(context, MainActivity.class);
                        //Significa que me han llamado desde una notificación y tengo que abrir el fragment correspondiente
                        if(notificacion!=null){
                            intent.putExtra("notificacion","notificacion");
                        }
                    }
                    //Establezo que se elimine esta pantalla de la pila y añado los arrays al extra
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            }, comienzo);
        }else {
            //Dependiendo del tipo volveré a buscar en el almacen, pues la referencia ya debe de estar
            //después de buscar en el servidor
            switch (tipo){
                case Constantes.PROYECTO:
                    ArrayList<Proyecto> proyectos = (ArrayList<Proyecto>)(ArrayList<?>) publicaciones;
                    Almacen.buscarProyectos(idsPublicaciones,proyectos,context);
                    break;
                case Constantes.COMENTARIO:
                    ArrayList<Comentario> comentarios = (ArrayList<Comentario>)(ArrayList<?>) publicaciones;
                    Almacen.buscarComentarios(idsPublicaciones,comentarios,context);
                    break;
                case Constantes.NOTIFICACION:
                    ArrayList<Notificacion> notificaciones = (ArrayList<Notificacion>)(ArrayList<?>) publicaciones;
                    Almacen.buscarNotificaciones(idsPublicaciones,notificaciones,context);
                    break;
                case Constantes.USUARIO:
                    ArrayList<Usuario> usuarios = (ArrayList<Usuario>)(ArrayList<?>) publicaciones;
                    Almacen.buscarUsuarios(idsPublicaciones,usuarios,context);
                    break;
            }
        }

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hPublicaciones=null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hPublicaciones=null;
    }
}
