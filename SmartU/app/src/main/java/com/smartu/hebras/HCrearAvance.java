package com.smartu.hebras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterAvances;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Avance;
import com.smartu.modelos.Proyecto;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.MultipartUtility;
import com.smartu.utilidades.Sesion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import java8.util.Optional;
import java8.util.stream.StreamSupport;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HCrearAvance extends AsyncTask<Void, Void, String> {

    private int idProyecto, idUsuario;
    private int idImagenDestacada;
    private String descripcion, nombre;
    private HCrearAvance hAvances;
    private SweetAlertDialog pDialog;
    private Context context;

    public HCrearAvance(Context context, int idProyecto, int idUsuario, int idImagenDestacada, String descripcion, String nombre) {
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.idImagenDestacada = idImagenDestacada;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.context = context;
    }


    public void sethAvances(HCrearAvance hAvances) {
        this.hAvances = hAvances;
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
    protected String doInBackground(Void... params) {

        //Recojo el resultado en un String
        String resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.crearAvance, "{\"idProyecto\":\"" + idProyecto + "\",\"idUsuario\":\"" + idUsuario + "\",\"nombre\":\"" + nombre + "\",\"descripcion\":\"" + descripcion + "\",\"idImagenDestacada\":\"" + idImagenDestacada + "\"}", "POST");

        return resultado;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        pDialog.dismissWithAnimation();
        JSONObject res = null;
        if (result != null) {
            try {
                res = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        //Si tengo objeto compruebo el resultado y si es ok cambio el texto al botón
        //Sino muestro mensaje por pantalla
        if (res != null) {
            try {

                ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
                //Añadir avance al proyecto
                Optional<Proyecto> proyectoOptional = StreamSupport.stream(Almacen.getProyectos()).filter(proyecto -> proyecto.getId() == idProyecto).findAny();
                if(proyectoOptional.isPresent())
                {
                    Avance avance = mapper.readValue(result, Avance.class);
                    proyectoOptional.get().getMisAvances().add(avance);
                }

            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No se ha podido realizar la operacion, problemas de conexión?", Toast.LENGTH_SHORT).show();
        }
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hAvances = null;

    }

    @Override
    protected void onCancelled(String aVoid) {
        super.onCancelled(aVoid);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hAvances = null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hAvances = null;
    }

}