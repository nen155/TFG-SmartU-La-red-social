package com.smartu.hebras;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterComentario;
import com.smartu.adaptadores.AdapterComentarioProyecto;
import com.smartu.modelos.Comentario;
import com.smartu.utilidades.ConsultasBBDD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HComentarios extends AsyncTask<Void,Void,Void> {

    private AdapterComentario adapterComentario=null;
    private AdapterComentarioProyecto adapterComentarioProyecto=null;
    private int offset,idProyecto;
    private HComentarios hComentarios;
    private SweetAlertDialog pDialog;
    private Context context;

    public HComentarios(AdapterComentario adapterComentario, int offset, Context context) {
        this.adapterComentario =adapterComentario;
        this.offset = offset;
        this.context = context;
    }
    public HComentarios(AdapterComentarioProyecto adapterComentarioProyecto, int offset, Context context) {
        this.adapterComentarioProyecto =adapterComentarioProyecto;
        this.offset = offset;
        this.context = context;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public void sethComentarios(HComentarios hComentarios) {
        this.hComentarios = hComentarios;
    }
    @Override
    protected void onPreExecute() {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    @Override
    protected Void doInBackground(Void... params) {
        //Recojo el resultado en un String
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        /*String resultado="{\"comentarios\":{" +
                "\"comentarios\":[" +

                "],"
                + "\"totalserver\":\"15\"" +
                "}";*/

        String resultado ="";
        if(adapterComentarioProyecto==null)
          resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaComentarios,"{\"limit\":\"10\",\"offset\":\""+offset+"\"}","POST");
        else
          resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaComentariosProyecto,"{\"limit\":\"10\",\"offset\":\""+offset+"\",\"idProyecto\":\""+idProyecto+"\"}","POST");
        JSONObject res =null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("comentarios")) {
                    //JSONObject comentJSON = res.getJSONObject("comentarios");
                    JSONArray comentariosJSON = res.getJSONArray("comentarios");
                    for(int i=0;i<comentariosJSON.length();++i)
                    {
                        JSONObject comentario = comentariosJSON.getJSONObject(i);
                        Comentario c = mapper.readValue(comentario.toString(), Comentario.class);
                        if(adapterComentarioProyecto==null)
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterComentario.addItem(c);
                                }
                            });
                        else
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterComentarioProyecto.addItem(c);
                                }
                            });
                    }
                    //Digo a los adapter cual es el total de comentarios que tienen
                    if(adapterComentarioProyecto==null)
                        adapterComentario.setTotalElementosServer(res.getInt("totalserver"));
                    else
                        adapterComentarioProyecto.setTotalElementosServer(res.getInt("totalserver"));
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
        hComentarios=null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hComentarios=null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hComentarios=null;
    }
}