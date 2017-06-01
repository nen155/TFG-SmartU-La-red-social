package com.smartu.hebras;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterIntegrante;
import com.smartu.adaptadores.AdapterUsuario;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HUsuarios extends AsyncTask<Void,Void,Void> {

    private AdapterIntegrante adapterIntegrante =null;
    private AdapterUsuario adapterUsuario =null;
    private int offset,idProyecto;
    private HUsuarios hUsuarios;

    public HUsuarios(AdapterIntegrante adapterIntegrante, int offset) {
        this.adapterIntegrante = adapterIntegrante;
        this.offset = offset;
    }
    public HUsuarios(AdapterUsuario adapterUsuario, int offset) {
        this.adapterUsuario = adapterUsuario;
        this.offset = offset;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public void sethUsuarios(HUsuarios hUsuarios) {
        this.hUsuarios = hUsuarios;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Recojo el resultado en un String
        String resultado="{\"usuarios\":{" +
                "\"usuarios\":[" +"{\"id\":\"1\",\"nombre\":\"Emilio\",\"apellidos\":\"Chica Jiménez\",\"verificado\":\"true\",\"user\":\"emiliocj\",\"email\":\"emiliocj@correo.ugr.es\",\"nPuntos\":\"100\",\"localizacion\":\"C/Poeta Manuel\",\"biografia\":\"Estudiante universitario de la ETSIIT que vive en Granada y es Graduado en Ingeniería Informática\", \"web\":\"http://coloremoon.com\",\"imagenPerfil\":\"wp-content/uploads/2017/05/foto-buena.jpg\",\n" +
                "          \"misProyectos\":[\n" +
                "            {\"id\":\"1\",\"nombre\":\"SmartU\",\"descripcion\":\"Es el primer proyecto\",\"fechaCreacion\":\"2017-01-22\",\"fechaFinalizacion\":\"2018-12-10\",\"imagenDestacada\":\"wp-content/uploads/2017/05/logo_web.png\",\"coordenadas\":\"37.1625378,-3.5964669\",\"localizacion\":\"Calle puertas 10\",\"buenaIdea\":[{\"idUsuario\":\"1\"}],\"web\":\"http://coloredmoon.com\"}\n" +
                "            ],\n" +
                "            \"misAreasInteres\":[\n" +
                "              {\"id\":\"1\",\"nombre\":\"Informática\"}\n" +
                "              ],\n" +
                "              \"misEspecialidades\":[\n" +
                "              {\"id\":\"1\",\"nombre\":\"Informática\"}\n" +
                "              ],\n" +
                "              \"miStatus\":{\"id\":\"1\",\"nombre\":\"creador\",\"puntos\":\"100\",\"numSeguidores\":\"1\"},\n" +
                "              \"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.facebook.com/\"}]\n" +
                "    }\n" +
                "}"+

                "],"
                + "\"totalserver\":\"15\"" +
                "}";
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        //String resultado="";
        //if(adapterIntegrante==null)
        // resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaIntegrantes,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\""+offset+"\",\"idProyecto\":\""+idPoryecto+"\"}","POST");
        //else
        // resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaUsuarios,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\""+offset+"\}","POST");
        JSONObject res =null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("usuarios")) {
                    JSONObject usuJSON = res.getJSONObject("usuarios");
                    JSONArray usuariosJSON = usuJSON.getJSONArray("usuarios");
                    for(int i=0;i<usuariosJSON.length();++i)
                    {
                        JSONObject usuario = usuariosJSON.getJSONObject(i);
                        Usuario u = mapper.readValue(usuario.toString(), Usuario.class);
                        if(adapterUsuario ==null)
                            adapterIntegrante.addItem(u);
                        else
                            adapterUsuario.addItem(u);
                    }
                    //Digo a los adapter cual es el total de comentarios que tienen
                    if(adapterUsuario ==null)
                        adapterIntegrante.setTotalElementosServer(usuJSON.getInt("totalserver"));
                    else
                        adapterIntegrante.setTotalElementosServer(usuJSON.getInt("totalserver"));
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
        hUsuarios =null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarios =null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarios =null;
    }
}