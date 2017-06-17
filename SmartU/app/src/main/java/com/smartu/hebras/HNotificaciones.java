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
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Multimedia;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.vistas.FragmentNotificaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import java8.util.stream.StreamSupport;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HNotificaciones extends AsyncTask<Void,Void,Void> {

    private AdapterNotificacion adapterNotificacion =null;
    private int offset;
    private HNotificaciones hNotificaciones;
    private SweetAlertDialog pDialog;
    private Context context;
    private ArrayList<Notificacion> notificacions;
    private boolean filtro=false;
    private CallBackHebras callback;


    public HNotificaciones(AdapterNotificacion adapterNotificacion, int offset, Context context) {
        this.adapterNotificacion = adapterNotificacion;
        this.offset = offset;
        this.context = context;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);
        notificacions = new ArrayList<>();
    }

    public void setCallback(CallBackHebras callback) {
        this.callback = callback;
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
                        notificacions.add(n);
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
        Set<Integer> set = new HashSet<>();
        for (Notificacion n:notificacions)
            //Si la notificacion no tiene que ver con proyectos no la añado
            //puede que la notificacion perteneza a un usuario nuevo
            if(n.getIdProyecto()!=0)
                set.add(n.getIdProyecto());

        ArrayList<Integer> idsProyectos = new ArrayList<Integer>();
        idsProyectos.addAll(set);

        try {
            //Esta carga de proyectos si tiene que ir al server
            //espera hasta que termina de traerse los proyectos
            Almacen.buscarProyectos(idsProyectos,context);

            //Busco los usuarios también por si no han sido cargados
            //Esta hebra espera hasta que termine de traerse los usuarios del server sino estan en el Almacen
            Usuario usuarioSesion =Sesion.getUsuario(context);

            if(usuarioSesion!=null)
                Almacen.buscarUsuarios(usuarioSesion.getMisSeguidos(),context);
            //Ya que tengo los proyectos que podrían relacionarse
            //con el las notificaciones
            //Añado las notificaciones al adapter
            for (Notificacion n:notificacions) {
                adapterNotificacion.addItem(n);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback.terminada();
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