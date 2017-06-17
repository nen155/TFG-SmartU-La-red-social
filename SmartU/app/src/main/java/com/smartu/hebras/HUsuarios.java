package com.smartu.hebras;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterIntegrante;
import com.smartu.adaptadores.AdapterUsuario;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Usuario;
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

public class HUsuarios extends AsyncTask<Void,Void,Void> {

    private AdapterIntegrante adapterIntegrante =null;
    private AdapterUsuario adapterUsuario =null;
    private int offset,idProyecto;
    private HUsuarios hUsuarios;
    private SweetAlertDialog pDialog;
    private Context context;
    private ArrayList<Usuario> usuarios;
    private CallBackHebras callBackHebras;

    public void setCallBackHebras(CallBackHebras callBackHebras) {
        this.callBackHebras = callBackHebras;
    }

    @Override
    protected void onPreExecute() {
        usuarios=new ArrayList<>();
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    public HUsuarios(AdapterIntegrante adapterIntegrante, int offset,Context context) {
        this.adapterIntegrante = adapterIntegrante;
        this.offset = offset;
        this.context=context;


    }
    public HUsuarios(AdapterUsuario adapterUsuario, int offset,Context context) {
        this.adapterUsuario = adapterUsuario;
        this.offset = offset;
        this.context=context;
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
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        String resultado="";
        if(adapterUsuario==null)
         resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaIntegrantes,"{\"limit\":\"10\",\"offset\":\""+offset+"\",\"idProyecto\":\""+idProyecto+"\"}","POST");
        else
         resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaUsuarios,"{\"limit\":\"10\",\"offset\":\""+offset+"\"}","POST");
        JSONObject res =null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("usuarios")) {
                    JSONArray usuariosJSON = res.getJSONArray("usuarios");
                    for(int i=0;i<usuariosJSON.length();++i)
                    {
                        JSONObject usuario = usuariosJSON.getJSONObject(i);
                        Usuario u = mapper.readValue(usuario.toString(), Usuario.class);
                        usuarios.add(u);
                    }
                    //Digo a los adapter cual es el total de comentarios que tienen
                    if(adapterUsuario ==null)
                        adapterIntegrante.setTotalElementosServer(res.getInt("totalserver"));
                    else
                        adapterUsuario.setTotalElementosServer(res.getInt("totalserver"));
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
        if(adapterUsuario ==null) {
            for (Usuario u : usuarios) {
                adapterIntegrante.addItem(u);
            }
        }else {
            for (Usuario u : usuarios) {
                adapterUsuario.addItem(u);
            }
            callBackHebras.terminada();
        }

        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarios =null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarios =null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarios =null;
    }
}