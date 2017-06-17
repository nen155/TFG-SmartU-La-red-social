package com.smartu.hebras;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.exoplayer2.C;
import com.smartu.adaptadores.AdapterComentario;
import com.smartu.adaptadores.AdapterComentarioProyecto;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
    private CallBackHebras callBackHebras;
    private ArrayList<Comentario> comentarios;

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

    public void setCallBackHebras(CallBackHebras callBackHebras) {
        this.callBackHebras = callBackHebras;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public void sethComentarios(HComentarios hComentarios) {
        this.hComentarios = hComentarios;
    }
    @Override
    protected void onPreExecute() {
        comentarios = new ArrayList<>();
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
                        comentarios.add(c);
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
        if(adapterComentarioProyecto==null) {
            Set<Integer> set = new HashSet<>();
            for (Comentario c : comentarios)
                //Si la notificacion no tiene que ver con proyectos no la añado
                //puede que la notificacion perteneza a un usuario nuevo
                if(c.getIdProyecto()!=0)
                    set.add(c.getIdProyecto());

            ArrayList<Integer> idsProyectos = new ArrayList<Integer>();
            idsProyectos.addAll(set);
            try {
                //Esta carga de proyectos si tiene que ir al server
                //espera hasta que termina de traerse los proyectos
                Almacen.buscarProyectos(idsProyectos,context);

                //Busco los usuarios también por si no han sido cargados
                //Esta hebra espera hasta que termine de traerse los usuarios del server sino estan en el Almacen
                Usuario usuarioSesion = Sesion.getUsuario(context);

                if(usuarioSesion!=null)
                    Almacen.buscarUsuarios(usuarioSesion.getMisSeguidos(),context);
                //Ya que tengo los proyectos que podrían relacionarse
                //con el las notificaciones
                //Añado las notificaciones al adapter
                for (Comentario c : comentarios) {
                    adapterComentario.addItem(c);
                }
                callBackHebras.terminada();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            for (Comentario c : comentarios) {
                adapterComentarioProyecto.addItem(c);
            }

        }

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