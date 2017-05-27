package com.smartu.hebras;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterComentario;
import com.smartu.adaptadores.AdapterComentarioProyecto;
import com.smartu.modelos.Comentario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HComentarios extends AsyncTask<Void,Void,Void> {

    private AdapterComentario adapterComentario=null;
    private AdapterComentarioProyecto adapterComentarioProyecto=null;
    private int offset;
    private HComentarios hComentarios;

    public HComentarios(AdapterComentario adapterComentario,int offset) {
        this.adapterComentario =adapterComentario;
        this.offset = offset;
    }
    public HComentarios(AdapterComentarioProyecto adapterComentarioProyecto, int offset) {
        this.adapterComentarioProyecto =adapterComentarioProyecto;
        this.offset = offset;
    }

    public void sethComentarios(HComentarios hComentarios) {
        this.hComentarios = hComentarios;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Recojo el resultado en un String
        String resultado="{\"comentarios\":{" +
                "\"comentarios\":[" +

                "],"
                + "\"totalserver\":\"15\"" +
                "}";
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        //String resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaPublicaciones,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\""+offset+"\"}","POST");
        JSONObject res =null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("comentarios")) {
                    JSONObject comentJSON = res.getJSONObject("comentarios");
                    JSONArray comentariosJSON = comentJSON.getJSONArray("comentarios");
                    for(int i=0;i<comentariosJSON.length();++i)
                    {
                        JSONObject comentario = comentariosJSON.getJSONObject(i);
                        Comentario c = mapper.readValue(comentario.toString(), Comentario.class);
                        if(adapterComentarioProyecto==null)
                            adapterComentario.addItem(c);
                        else
                            adapterComentarioProyecto.addItem(c);
                    }
                    //Digo a los adapter cual es el total de comentarios que tienen
                    if(adapterComentarioProyecto==null)
                        adapterComentario.setTotalElementosServer(comentJSON.getInt("totalserver"));
                    else
                        adapterComentario.setTotalElementosServer(comentJSON.getInt("totalserver"));
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
        hComentarios=null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hComentarios=null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hComentarios=null;
    }
}