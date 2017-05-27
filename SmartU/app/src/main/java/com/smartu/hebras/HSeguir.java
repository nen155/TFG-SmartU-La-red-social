package com.smartu.hebras;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

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


    public HSeguir(boolean eleminar, Usuario seguido, Context context, Button seguirUsuario, TextView seguidoresUsuario) {
        this.seguido = seguido;
        this.context=context;
        this.seguirUSuario=seguirUsuario;
        this.seguidoresUsuario=seguidoresUsuario;
        this.seguidor = Sesion.getUsuario(context);
        this.eleminar = eleminar;
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
        String seguir = "\"seguir\":{\"idUsuario\":\"" + seguidor.getId() + "\",\"idUsuarioSeguido\":\"" + seguido.getId() + "\"}";
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
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        this.hSeguir = null;
        //Obtengo el objeto JSON con el resultado
        JSONObject res=null;
        try {
            res = new JSONObject(resultado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Si tengo objeto compruebo el resultado y si es ok cambio el texto al botón
        //Sino muestro mensaje por pantalla
        if (res!=null) {
            try {
                if(res.has("resultado") && res.getString("resutlado").compareToIgnoreCase("ok")!=0)
                    reestablecerEstado();
                else{
                   if(eleminar)
                       seguidor.getMisSeguidos().remove(seguido);
                    else
                        seguidor.getMisSeguidos().add(seguido);
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
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        this.hSeguir = null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        this.hSeguir = null;
    }
    private void reestablecerEstado(){
        Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
        if(seguirUSuario!=null){
            seguirUSuario.setPressed(!seguirUSuario.isPressed());
            //Si no es un botón de tipo fabButton entonces le cambio el texto
            if (seguirUSuario.isPressed())
                seguirUSuario.setText(R.string.no_seguir);
            else
                seguirUSuario.setText(R.string.seguir);
        }
        else
            seguirFlotante.setPressed(!seguirFlotante.isPressed());

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
