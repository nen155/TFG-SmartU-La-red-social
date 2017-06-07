package com.smartu.hebras;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.R;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HSeguir extends AsyncTask<Void, Void, String> {

    private boolean eleminar;
    private Usuario seguido,seguidor;
    private Context context;
    private Button seguirUSuario;
    private FloatingActionButton seguirFlotante;
    private TextView seguidoresUsuario;
    private HSeguir hSeguir;
    private SweetAlertDialog pDialog;


    public HSeguir(boolean eleminar, Usuario seguido, Context context, Button seguirUsuario, TextView seguidoresUsuario) {
        this.seguido = seguido;
        this.context=context;
        this.seguirUSuario=seguirUsuario;
        this.seguidoresUsuario=seguidoresUsuario;
        this.seguidor = Sesion.getUsuario(context);
        this.eleminar = eleminar;
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
     * @param hSeguir
     */
    public void sethSeguir(HSeguir hSeguir){
        this.hSeguir = hSeguir;
    }

    public void setFabButton(FloatingActionButton seguirFlotante){
        this.seguirFlotante = seguirFlotante;
    }

    @Override
    protected String doInBackground(Void... params) {

        String resultado = null;
        //Construyo el JSON
        String seguir = "{\"idUsuario\":\"" + seguidor.getId() + "\",\"idUsuarioSeguido\":\"" + seguido.getId() + "\"}";
        //Cojo el resultado en un String
        if(eleminar)
            resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.eliminarSeguidor, seguir, "POST");
        else
            resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaSeguidor, seguir, "POST");

        return resultado;
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        this.hSeguir = null;
        //Obtengo el objeto JSON con el resultado

        JSONObject res=null;
        if(resultado!=null) {
            try {
                res = new JSONObject(resultado);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        //Si tengo objeto compruebo el resultado y si es ok cambio el texto al botón
        //Sino muestro mensaje por pantalla
        if (res!=null) {
            try {
                if(res.getString("resultado").compareToIgnoreCase("ok")!=0)
                    reestablecerEstado();
                else{
                   if(eleminar)
                       seguidor.getMisSeguidos().remove(seguidor.getMisSeguidos().indexOf(seguido.getId()));
                    else
                        seguidor.getMisSeguidos().add(seguido.getId());
                    Sesion.serializaUsuario(context,seguidor);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            reestablecerEstado();
        }
    }

    @Override
    protected void onCancelled(String resultado) {
        super.onCancelled(resultado);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        this.hSeguir = null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        this.hSeguir = null;
    }
    private void reestablecerEstado(){
        Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
        if(seguirUSuario!=null){
            seguirUSuario.setPressed(!seguirUSuario.isPressed());
            //Si no es un botón de tipo fabButton entonces le cambio el texto
            if (!seguirUSuario.isPressed())
                seguirUSuario.setText(R.string.no_seguir);
            else
                seguirUSuario.setText(R.string.seguir);
        }
        else {
            Integer integer = (Integer) seguirFlotante.getTag();
            if(R.drawable.seguir==integer) {
                seguirFlotante.setImageResource(R.drawable.dejarseguir);
                seguirFlotante.setTag(R.drawable.dejarseguir);
            }
            else {
                seguirFlotante.setImageResource(R.drawable.seguir);
                seguirFlotante.setTag(R.drawable.seguir);
            }
        }
        if(eleminar) {
            //Si quería eliminar la buena idea significa que le he restado uno al contador previamente
            int cont = Integer.parseInt(seguidoresUsuario.getText().toString())+1;
            seguidoresUsuario.setText(String.valueOf(cont));
        }else
        {
            //Si quería añadirlo como buena idea significa que le he sumando 1 al contador previamente
            int cont = Integer.parseInt(seguidoresUsuario.getText().toString())-1;
            seguidoresUsuario.setText(String.valueOf(cont));
        }
    }
}
