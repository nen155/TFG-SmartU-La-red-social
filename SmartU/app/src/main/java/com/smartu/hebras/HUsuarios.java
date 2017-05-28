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
                "\"usuarios\":[" +

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