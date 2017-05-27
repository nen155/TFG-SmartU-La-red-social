package com.smartu.hebras;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.modelos.BuenaIdea;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HBuenaIdea extends AsyncTask<Void, Void, String> {

    private int idIdea = 0;
    private Context context;
    private Proyecto proyecto;
    private ImageView buenaidea;
    private TextView buenaidea_contador;
    private HBuenaIdea hBuenaIdea;
    private Usuario usuarioSesion;


    public HBuenaIdea(int idIdea, Context context, Proyecto proyecto, ImageView buenaidea, TextView buenaidea_contador) {
        this.idIdea = idIdea;
        this.context=context;
        this.proyecto=proyecto;
        this.buenaidea=buenaidea;
        this.buenaidea_contador=buenaidea_contador;
    }

    /**
     * Este método está para poder asignar a null la referencia de la hebra
     * para que el recolector de basura la elimine
     * @param hBuenaIdea
     */
    public void sethBuenaIdea(HBuenaIdea hBuenaIdea){
        this.hBuenaIdea=hBuenaIdea;
    }
    @Override
    protected String doInBackground(Void... params) {
        usuarioSesion= Sesion.getUsuario(context);
        String resultado = null;
        //Construyo el JSON
        String  buenaidea = "\"buenaidea\":{\"idUsuario\":\"" + usuarioSesion.getId() + "\",\"idProyecto\":\"" + proyecto.getId() + "\"}";
        if(idIdea==0) {
            //Recojo el resultado en un String
            resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaBuenaIdea, buenaidea, "POST");
        }else {
            resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.eliminarBuenaIdea, buenaidea, "POST");
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String resultado) {
        super.onPostExecute(resultado);
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        this.hBuenaIdea = null;
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
                if(res.has("resultado") && res.getString("resutlado").compareToIgnoreCase("ok")!=0){
                    reestablecerEstado();
                }else{
                    //Inserto una buena idea en el proyecto
                    if(idIdea==0){
                        proyecto.getBuenaIdea().add(new BuenaIdea(usuarioSesion.getId()));
                    }else{//Elimino las buenas ideas del proyecto

                    }
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
        this.hBuenaIdea = null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        this.hBuenaIdea = null;
    }
    private void reestablecerEstado(){
        Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
        buenaidea.setPressed(!buenaidea.isPressed());
        if(buenaidea.isPressed())
            buenaidea.setImageResource(R.drawable.buenaidea);
        else
            buenaidea.setImageResource(R.drawable.idea);
        if(idIdea!=0) {
            //Si quería eliminar la buena idea significa que le he restado uno al contador previamente
            int cont = Integer.parseInt(buenaidea_contador.getText().toString())+1;
            buenaidea_contador.setText(String.valueOf(cont));
        }else
        {
            //Si quería añadirlo como buena idea significa que le he sumando 1 al contador previamente
            int cont = Integer.parseInt(buenaidea_contador.getText().toString())-1;
            buenaidea_contador.setText(String.valueOf(cont));
        }
    }
}
