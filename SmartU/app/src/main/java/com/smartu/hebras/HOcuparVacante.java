package com.smartu.hebras;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterAreasInteres;
import com.smartu.adaptadores.AdapterSolicitudes;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Area;
import com.smartu.modelos.SolicitudUnion;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import java8.util.stream.StreamSupport;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class    HOcuparVacante extends AsyncTask<Void, Void, String> {



    private Context context;
    private int idVacante,idUsuario,idEspecialidad,idProyecto;
    private HOcuparVacante hUsuarioInteresa;
    private SolicitudUnion solicitudUnion;
    private AdapterSolicitudes adapterSolicitudes;
    private SweetAlertDialog pDialog;


    public HOcuparVacante(Context context, int idEspecialidad, SolicitudUnion solicitudUnion, AdapterSolicitudes adapterSolicitudes) {

        this.context = context;
        this.solicitudUnion=solicitudUnion;
        this.idVacante = solicitudUnion.getIdVacante();
        this.idProyecto=solicitudUnion.getIdProyecto();
        this.idUsuario=solicitudUnion.getIdUsuarioSolicitante();
        this.adapterSolicitudes = adapterSolicitudes;
        this.idEspecialidad=idEspecialidad;
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);
    }
    @Override
    protected void onPreExecute() {
        pDialog.show();
    }
    /**
     * Este método está para poder asignar a null la referencia de la hebra
     * para que el recolector de basura la elimine
     *
     * @param hUsuarioInteresa
     */
    public void sethUsuarioInteresa(HOcuparVacante hUsuarioInteresa) {
        this.hUsuarioInteresa = hUsuarioInteresa;
    }

    @Override
    protected String doInBackground(Void... params) {


        String resultado = null;
        //Construyo el JSON
        String ocupar = "{\"id\":\"" + idVacante + "\",\"idUsuario\":\"" + idUsuario + "\",\"idProyecto\":\""+idProyecto+"\",\"idEspecialidad\":\"" + idEspecialidad + "\"}";

        resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.ocuparVacante, ocupar, "POST");
        return resultado;
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarioInteresa = null;
        //Obtengo el objeto JSON con el resultado
        JSONObject res = null;
        if(resultado!=null) {
            try {
                res = new JSONObject(resultado);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        //Si tengo objeto compruebo el resultado y si es ok cambio el texto al botón
        //Sino muestro mensaje por pantalla
        if (res != null) {
            try {
                if (res.getString("resultado").compareToIgnoreCase("ok") != 0)
                    reestablecerEstado();
                else {

                    Toast.makeText(context,"Ahora colabora en el proyecto!",Toast.LENGTH_SHORT).show();
                    //Elimino las solicitudes para esa vacante
                    Usuario usuario = Sesion.getUsuario(context);
                    StreamSupport.stream(usuario.getMisSolicitudes())
                            .filter(solicitudUnion -> solicitudUnion.getIdVacante()==idVacante).forEach( solicitudUnion ->
                            usuario.getMisSolicitudes().remove(solicitudUnion)
                    );
                    adapterSolicitudes.eliminaSolicitud();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            reestablecerEstado();
        }
    }

    @Override
    protected void onCancelled(String resultado) {
        super.onCancelled(resultado);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarioInteresa = null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarioInteresa = null;
    }

    /**
     * Método que invierte los cambios realizados
     */
    private void reestablecerEstado() {
        Toast.makeText(context, "No se ha podido realizar la operacion, problemas de conexión?", Toast.LENGTH_SHORT).show();

    }
}
