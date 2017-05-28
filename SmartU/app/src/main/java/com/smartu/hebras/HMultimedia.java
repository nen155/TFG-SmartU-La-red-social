package com.smartu.hebras;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterMultimedia;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Multimedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HMultimedia extends AsyncTask<Void,Void,Void> {

    private AdapterMultimedia adapterMultimedia =null;
    private int offset,idProyecto;
    private HMultimedia hMultimedia;

    public HMultimedia(AdapterMultimedia adapterMultimedia, int offset, int idProyecto) {
        this.adapterMultimedia = adapterMultimedia;
        this.offset = offset;
        this.idProyecto =idProyecto;
    }

    public void sethMultimedia(HMultimedia hMultimedia) {
        this.hMultimedia = hMultimedia;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Recojo el resultado en un String
        String resultado="{\"multimedia\":{" +
                "\"multimedia\":[" +

                "],"
                + "\"totalserver\":\"15\"" +
                "}";
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        //String resultado =resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaMultimedia,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\""+offset+"\",\"idProyecto\":\""+idProyecto+"\"}","POST");

        JSONObject res =null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("multimedia")) {
                    JSONObject multiJSON = res.getJSONObject("multimedia");
                    JSONArray multimediaJSON = multiJSON.getJSONArray("multimedia");
                    for(int i=0;i<multimediaJSON.length();++i)
                    {
                        JSONObject multimedia = multimediaJSON.getJSONObject(i);
                        Multimedia m = mapper.readValue(multimedia.toString(), Multimedia.class);
                        adapterMultimedia.addItem(m);
                    }
                    adapterMultimedia.setTotalElementosServer(multiJSON.getInt("totalserver"));
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
        hMultimedia =null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hMultimedia =null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hMultimedia =null;
    }
}