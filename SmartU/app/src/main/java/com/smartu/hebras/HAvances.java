package com.smartu.hebras;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterAvances;
import com.smartu.almacenamiento.Almacen;
import com.smartu.modelos.Avance;
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
 * No esta en uso porque se cargan con el proyecto
 * pero se puede cambiar en un futuro para mejorar la carga
 *
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HAvances extends AsyncTask<Void, Void, Void> {

    private AdapterAvances adapterAvances = null;
    private int offset, idProyecto = -1;
    private HAvances hAvances;
    private SweetAlertDialog pDialog;
    private Context context;
    private ArrayList<Avance> avances;

    public HAvances(AdapterAvances adapterAvances, int offset, Context context) {
        this.adapterAvances = adapterAvances;
        this.offset = offset;
        this.context = context;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public void sethAvances(HAvances hAvances) {
        this.hAvances = hAvances;
    }

    @Override
    protected void onPreExecute() {
        avances = new ArrayList<>();
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Recojo el resultado en un String
        String resultado = "";
        resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaAvances, "{\"limit\":\"10\",\"offset\":\"" + offset + "\",\"idProyecto\":\"" + idProyecto + "\"}", "POST");
        JSONObject res = null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if (resultado != null) {
                res = new JSONObject(resultado);
                if (!res.isNull("avances")) {
                    JSONArray avancesJSON = res.getJSONArray("avances");
                    for (int i = 0; i < avancesJSON.length(); ++i) {
                        JSONObject avance = avancesJSON.getJSONObject(i);
                        Avance c = mapper.readValue(avance.toString(), Avance.class);
                        avances.add(c);
                    }
                    adapterAvances.setTotalElementosServer(res.getInt("totalserver"));
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

        for (Avance c : avances) {
            adapterAvances.addItem(c);
        }


        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hAvances = null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
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