package com.smartu.hebras;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.adaptadores.AdapterIntegrante;
import com.smartu.adaptadores.AdapterUsuario;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Emilio Chica Jiménez on 27/05/2017.
 */

public class HUsuarios extends AsyncTask<Void,Void,Void> {

    private AdapterIntegrante adapterIntegrante =null;
    private AdapterUsuario adapterUsuario =null;
    private int offset,idProyecto;
    private HUsuarios hUsuarios;
    private SweetAlertDialog pDialog;
    private Context context;
    @Override
    protected void onPreExecute() {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Cargando...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    public HUsuarios(AdapterIntegrante adapterIntegrante, int offset,Context context) {
        this.adapterIntegrante = adapterIntegrante;
        this.offset = offset;
        this.context=context;
    }
    public HUsuarios(AdapterUsuario adapterUsuario, int offset,Context context) {
        this.adapterUsuario = adapterUsuario;
        this.offset = offset;
        this.context=context;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public void sethUsuarios(HUsuarios hUsuarios) {
        this.hUsuarios = hUsuarios;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Recojo el resultado en un String
            /*String resultado="{\"usuarios\":{\"usuarios\":[{\"id\":\"1\",\"nombre\":\"Emilio\",\"apellidos\":\"Chica Jiménez\",\"verificado\":\"true\",\"user\":\"emiliocj\",\"email\":\"emiliocj@correo.ugr.es\",\"nPuntos\":\"100\",\"localizacion\":\"C/Poeta Manuel\",\"biografia\":\"Estudiante universitario de la ETSIIT que vive en Granada y es Graduado en Ingeniería Informática\", \"web\":\"http://coloremoon.com\",\"imagenPerfil\":\"wp-content/uploads/2017/05/foto-buena.jpg\",\"misProyectos\":[\"1\"],\"misAreasInteres\":[{\"id\":\"1\",\"nombre\":\"Informática\"}],\n" +
                    "\"misEspecialidades\":[{\"id\":\"1\",\"nombre\":\"Informática\"}], \n" +
                    "\"miStatus\":{\"id\":\"1\",\"nombre\":\"creador\",\"puntos\":\"100\",\"numSeguidores\":\"1\"},\n" +
                    "\"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.facebook.com/\"}]\n" +
                    "}, \n" +
                    "\n" +
                    "{\"id\":\"2\",\"nombre\":\"Juanjo\",\"apellidos\":\"Jiménez\",\"verificado\":\"true\",\"user\":\"juanjo\",\"email\":\"juanji@gmail.com\",\"nPuntos\":\"150\",\"localizacion\":\"C/Armilla \",\"biografia\":\"Estudiante universitario de la ETSIIT que vive en Granada, en Armilla y es Graduado en Ingeniería Informática\", \"web\":\"http://juanjo.com\",\"imagenPerfil\":\"wp-content/uploads/2017/05/AtBE7.png\",\n" +
                    "\"misProyectos\":[\"1\"],\"misAreasInteres\":[{\"id\":\"1\",\"nombre\":\"Informática\"}],\n" +
                    "\"misEspecialidades\":[{\"id\":\"1\",\"nombre\":\"Informática\"}], \n" +
                    "\"miStatus\":{\"id\":\"1\",\"nombre\":\"creador\",\"puntos\":\"100\",\"numSeguidores\":\"2\"},\n" +
                    "\"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.facebook.com/\"}],\n" +
                    "\"uid\":\"qJG5OgYElpajoNcMLMRr1M022g02\","+
                    "\"firebaseToken\":\"f-R46MZAWX4:APA91bHpMWEfmjmcJQXgtEvDrvacEDReiLhO0ErEvjldGFobof0xbn8LJqqECIPqGzsvtVjkrx6Ew2Koky-u_YX41mBvIThe4glvT-x70s89o2dXvzptsPrMXych6ugQYHGRQ2LtxJ7L\""+
                    "},\n" +
                    "{\"id\":\"3\",\"nombre\":\"German\",\"apellidos\":\"Zayas Cabrera\",\"verificado\":\"true\",\"user\":\"german\",\"email\":\"german@gmail.com\",\"nPuntos\":\"150\",\"localizacion\":\"C/Ceballos \",\"biografia\":\"Estudiante universitario de la UGR que vive en Granada, en Peligros con el Grado en Bellas Artes\", \"web\":\"http://german.com\",\"imagenPerfil\":\"wp-content/uploads/2017/05/j5xrbugqkkalex-avatar.png\",\n" +
                    "\"misProyectos\":[\"1\"],\"misAreasInteres\":[{\"id\":\"2\",\"nombre\":\"Bellas artes\"}],\n" +
                    "\"misEspecialidades\":[{\"id\":\"2\",\"nombre\":\"Diseño gráfico\"}], \n" +
                    "\"miStatus\":{\"id\":\"1\",\"nombre\":\"creador\",\"puntos\":\"100\",\"numSeguidores\":\"2\"},\n" +
                    "\"misRedesSociales\":[{\"id\":\"2\",\"nombre\":\"twitter\",\"url\":\"https://www.twitter.com/\"}],\n" +
                    "\"uid\":\"KEKtOZy4mbd42LygAUgOp2ZVr3M2\","+
                    "\"firebaseToken\":\"f-R46MZAWX4:APA91bHpMWEfmjmcJQXgtEvDrvacEDReiLhO0ErEvjldGFobof0xbn8LJqqECIPqGzsvtVjkrx6Ew2Koky-u_YX41mBvIThe4glvT-x70s89o2dXvzptsPrMXych6ugQYHGRQ2LtxJ7L\""+
                    "}\n" +
                    "\n" +
                    "]                      \n" +
                    " \n" +
                    "},\"totalserver\":\"3\"}";*/
        //TODO: PARA CUANDO ESTE EL SERVIDOR ACTIVO LE PASO EL LIMITE(LIMIT) Y EL INICIO(OFFSET)
        String resultado="";
        if(adapterIntegrante==null)
         resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaIntegrantes,"{\"limit\":\"10\",\"offset\":\""+offset+"\",\"idProyecto\":\""+idProyecto+"\"}","POST");
        else
         resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaUsuarios,"{\"limit\":\"10\",\"offset\":\""+offset+"\"}","POST");
        JSONObject res =null;
        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        try {
            if(resultado !=null) {
                res = new JSONObject(resultado);
                if (!res.isNull("usuarios")) {
                    JSONObject usuJSON = res.getJSONObject("usuarios");
                    JSONArray usuariosJSON = usuJSON.getJSONArray("usuarios");
                    for(int i=0;i<usuariosJSON.length();++i)
                    {
                        JSONObject usuario = usuariosJSON.getJSONObject(i);
                        Usuario u = mapper.readValue(usuario.toString(), Usuario.class);
                        if(adapterUsuario ==null)
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterIntegrante.addItem(u);
                                }
                            });
                        else
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapterUsuario.addItem(u);
                                }
                            });

                    }
                    //Digo a los adapter cual es el total de comentarios que tienen
                    if(adapterUsuario ==null)
                        adapterIntegrante.setTotalElementosServer(res.getInt("totalserver"));
                    else
                        adapterUsuario.setTotalElementosServer(res.getInt("totalserver"));
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
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarios =null;

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarios =null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        pDialog.dismissWithAnimation();
        //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
        hUsuarios =null;
    }
}