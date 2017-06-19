package com.smartu.hebras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.MultipartUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HSubirImagen extends AsyncTask<Void, Void, String> {

    private Bitmap imagenDestacada;
    private HSubirImagen hAvances;
    private SweetAlertDialog pDialog;
    private Context context;
    private CallBackImagen callBackHebras;
    private String nombre;

    public HSubirImagen(Context context, Bitmap imagenDestacada, CallBackImagen callBackHebras,String nombre) {
        this.imagenDestacada = imagenDestacada;
        this.context = context;
        this.callBackHebras =callBackHebras;
        this.nombre = nombre;
    }


    public void sethAvances(HSubirImagen hAvances) {
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
        String idImagenDestacada="";
        try {
            File uploadFile1 = getImageFile(imagenDestacada);
            if (uploadFile1 != null) {
                MultipartUtility multipartUtility = new MultipartUtility(ConsultasBBDD.server+ConsultasBBDD.imagenAvance, "UTF-8");
                multipartUtility.addFilePart("fileUpload", uploadFile1);
                idImagenDestacada = multipartUtility.finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return idImagenDestacada;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        pDialog.dismissWithAnimation();
        JSONObject res=null;
        if(result!=null) {
            try {
                res = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        //Si tengo objeto compruebo el resultado y si es ok cambio el texto al botón
        //Sino muestro mensaje por pantalla
        if (res!=null) {
            try {
                if(res.getInt("id")==0) {
                    Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
                }else{
                    callBackHebras.terminada(res.getInt("id"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
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

    private File getImageFile(Bitmap inImage) {
        File file = null;
        try {
            file = new File(context.getExternalCacheDir(),"imgproject_"+nombre+"_"+new Date().getTime()+".jpg");
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}