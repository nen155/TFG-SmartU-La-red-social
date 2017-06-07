package com.smartu.hebras;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterNotificacion;
import com.smartu.modelos.Multimedia;
import com.smartu.modelos.Notificacion;
import com.smartu.utilidades.ConsultasBBDD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HNotificaciones extends AsyncTask<Void,Void,Void> {

    private AdapterNotificacion adapterNotificacion =null;
    private int offset;
    private HNotificaciones hNotificaciones;
    private SweetAlertDialog pDialog;
    private Context context;

    public HNotificaciones(AdapterNotificacion adapterNotificacion, int offset, Context context) {
        this.adapterNotificacion = adapterNotificacion;
        this.offset = offset;
        this.context = context;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);
    }
    @Override
    protected void onPreExecute() {
        pDialog.show();
    }
    public void sethNotificaciones(HNotificaciones hNotificaciones) {
        this.hNotificaciones = hNotificaciones;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Recojo el resultado en un String
        /*String resultado="{\"notificaciones\":{" +
                "\"notificaciones\":[" +

                "],"
                + "\"totalserver\":\"15\"" +
                "}";*/
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        String resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaNotificaciones,"{\"limit\":\"10\",\"offset\":\""+offset+"\"}","POST");

        JSONObject res =null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("notificaciones")) {
                    JSONArray notificacionesJSON = res.getJSONArray("notificaciones");
                    for(int i=0;i<notificacionesJSON.length();++i)
                    {
                        JSONObject notificacion = notificacionesJSON.getJSONObject(i);
                        Notificacion n = mapper.readValue(notificacion.toString(), Notificacion.class);
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapterNotificacion.addItem(n);
                            }
                        });
                    }
                    adapterNotificacion.setTotalElementosServer(res.getInt("totalserver"));
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
        hNotificaciones =null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hNotificaciones =null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hNotificaciones =null;
    }
}