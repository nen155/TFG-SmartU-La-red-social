package com.smartu.hebras;

/**
 * Created by NeN on 01/06/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Publicacion;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.Constantes;
import com.smartu.utilidades.ConsultasBBDD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Cargaría las 10 primeras más actuales ordenadas por fecha
 * Para cargar otras 10 se haría en el evento onScroll del RecyclerView
 * de cada tipo de publicación, para no llenar la memoria del smartphone
 */
public class HPublicacion extends AsyncTask<Void,Void,Void> {

    private HPublicacion hPublicaciones;
    private Context context;
    private int tipo=-1,id=-1;
    private Publicacion publicacion;
    private SweetAlertDialog pDialog;

    public HPublicacion(Context context, Publicacion publicacion,int tipo,int id) {
        this.context = context;
        this.publicacion = publicacion;
        this.tipo = tipo;
        this.id =id;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);

    }
    @Override
    protected void onPreExecute() {
        pDialog.show();
    }
    public void sethPublicaciones(HPublicacion hPublicaciones) {
        this.hPublicaciones = hPublicaciones;
    }

    @Override
    protected Void doInBackground(Void... params) {

        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        //Recojo el resultado en un String
        /*String resultado="{\"publicaciones\":{\n" +
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
                "}";*/
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        //Trae publicaciones de un tipo /publicaciones pero con un ID y un tipo
        String resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaPublicacion,"{\"idPublicacion\":"+id+", \"tipo\":\""+tipo+"\"}","POST");

        JSONObject res =null;
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("publicacion")) {
                    JSONObject muroJSON = res.getJSONObject("publicacion");

                    if(muroJSON.has("proyecto"))
                    {
                        JSONObject proyecto = muroJSON.getJSONObject("proyecto");
                        Proyecto p = mapper.readValue(proyecto.toString(), Proyecto.class);
                        Almacen.add(p);
                    }
                    if(muroJSON.has("comentario"))
                    {
                        JSONObject comentario = muroJSON.getJSONObject("comentario");
                        Comentario c = mapper.readValue(comentario.toString(), Comentario.class);
                        Almacen.add(c);
                    }
                    if(muroJSON.has("notificacion"))
                    {
                        JSONObject notificacion = muroJSON.getJSONObject("notificacion");
                        Notificacion n = mapper.readValue(notificacion.toString(), Notificacion.class);
                        Almacen.add(n);
                    }
                    if(muroJSON.has("usuario"))
                    {
                        JSONObject usuario = muroJSON.getJSONObject("usuario");
                        Usuario u = mapper.readValue(usuario.toString(), Usuario.class);
                        Almacen.add(u);
                    }
                    //No me traigo las Areas por que se traen TODAS al principio y se mantienen en memoria durante la ejecución
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
        //Dependiendo del tipo volveré a buscar en el almacen, pues la referencia ya debe de estar
        //después de buscar en el servidor
        switch (tipo){
            case Constantes.PROYECTO:
                Proyecto p = (Proyecto) publicacion;
                Almacen.buscar(id,p,context);
                break;
            case Constantes.COMENTARIO:
                Comentario c = (Comentario) publicacion;
                Almacen.buscar(id,c,context);
                break;
            case Constantes.NOTIFICACION:
                Notificacion n = (Notificacion) publicacion;
                Almacen.buscar(id,n,context);
                break;
            case Constantes.USUARIO:
                Usuario u = (Usuario) publicacion;
                Almacen.buscar(id,u,context);
                break;
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
