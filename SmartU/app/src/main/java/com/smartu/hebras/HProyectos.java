package com.smartu.hebras;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterProyecto;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HProyectos extends AsyncTask<Void,Void,Void> {

    private AdapterProyecto adapterProyecto =null;
    private int offset;
    private HProyectos hProyectos;
    private SweetAlertDialog pDialog;
    private Context context;

    public HProyectos(AdapterProyecto adapterProyecto, int offset, Context context) {
        this.adapterProyecto = adapterProyecto;
        this.offset = offset;
        this.context=context;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);
    }
    @Override
    protected void onPreExecute() {
        pDialog.show();
    }

    public void sethProyectos(HProyectos hProyectos) {
        this.hProyectos = hProyectos;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Recojo el resultado en un String
        String resultado="{\"proyectos\":{" +
                "\"proyectos\":[" +

                "],"
                + "\"totalserver\":\"1\"" +
                "}"+
                "}";
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        //String resultado =resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaProyectos,"{\"cantidad\":{\"limit\":\"10\",\"offset\":\""+offset+"\"}","POST");

        JSONObject res =null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("proyectos")) {
                    JSONObject proyectJSON = res.getJSONObject("proyectos");
                    JSONArray proyectosJSON = proyectJSON.getJSONArray("proyectos");
                    for(int i=0;i<proyectosJSON.length();++i)
                    {
                        JSONObject proyecto = proyectosJSON.getJSONObject(i);
                        Proyecto p = mapper.readValue(proyecto.toString(), Proyecto.class);
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterProyecto.addItem(p);
                            }
                        });
                    }
                    adapterProyecto.setTotalElementosServer(proyectJSON.getInt("totalserver"));
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
        hProyectos =null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hProyectos =null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hProyectos =null;
    }
}