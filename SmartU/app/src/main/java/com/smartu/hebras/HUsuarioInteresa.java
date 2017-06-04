package com.smartu.hebras;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterAreasInteres;
import com.smartu.modelos.Area;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HUsuarioInteresa extends AsyncTask<Void, Void, String> {


    private int idUsuario = 0;
    private List<Integer> intereses;
    private Context context;
    private  AdapterAreasInteres back;
    private HUsuarioInteresa hUsuarioInteresa;
    private SweetAlertDialog pDialog;
    private Set<Integer> posicionAreasInicial;
    private ArrayList<Area> areasBack;

    public HUsuarioInteresa(int idUsuario, List<Integer> intereses, Context context, AdapterAreasInteres back, Set<Integer> posicionAreasInicial, ArrayList<Area> areasBack) {

        this.idUsuario = idUsuario;
        this.intereses = intereses;
        this.context = context;
        this.back = back;
        this.areasBack =areasBack;
        this.posicionAreasInicial = posicionAreasInicial;
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
    public void sethUsuarioInteresa(HUsuarioInteresa hUsuarioInteresa) {
        this.hUsuarioInteresa = hUsuarioInteresa;
    }

    @Override
    protected String doInBackground(Void... params) {
        ObjectMapper objectMapper = new ObjectMapper();
        String listaIntereses="";
        try {
            listaIntereses = objectMapper.writeValueAsString(intereses);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String resultado = null;
        //Construyo el JSON
        String interes = "\"intereses\":{\"idUsuario\":\"" + idUsuario + "\",\"idsAreas\":"+listaIntereses+"}";

        resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaOEliminaInteres, interes, "POST");
        //resultado="";
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
                if (res.has("resultado") && res.getString("resutlado").compareToIgnoreCase("ok") != 0)
                    reestablecerEstado();
                else {

                    Toast.makeText(context,"Has guardado tus nuevos intereses!",Toast.LENGTH_SHORT).show();
                    //Cambio las areas que tenía previamente a el total de las que ha seleccionado
                    Usuario usuario = Sesion.getUsuario(context);
                    ArrayList<Area> areasInteresList = usuario.getMisAreasInteres();
                    areasInteresList = new ArrayList<Area>(areasBack);
                    usuario.setMisAreasInteres(areasInteresList);
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
     * Método que invierte los cambios realizados cuando se ha hecho la acción de seguir
     * a un usuario
     */
    private void reestablecerEstado() {
        Toast.makeText(context, "No se ha podido realizar la operacion, problemas de conexión?", Toast.LENGTH_SHORT).show();
        //Deselecciono todos
        back.unCheckAll();
        //Selecciono sólo los que tenía seleccionados previamente
        for(Integer p : posicionAreasInicial){
            back.checkElement(p);
        }
    }
}
