package com.smartu.utilidades;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Emilio Chica Jimenez on 27/10/2015.
 */
public class ConsultasBBDD {


    public static String server ="http://smartu.coloredmoon.com/api/public/";

    public static final String imagenes = "imagenes/";

    public static final String grupoUsuarios="user/";
    public static final String grupoProyectos="project/";
    public static final String grupoComentarios="comment/";
    public static final String grupoNotificaciones="notification/";
    public static final String grupoPublicaciones="publish/";

    //Consultas
    public static final String consultaUsuarios =grupoUsuarios+"usuarios";
    public static final String consultaIntegrantes =grupoUsuarios+"integrantes";
    public static final String consultaLogin = grupoUsuarios+"login";
    public static final String consultaProyectos =grupoProyectos+"proyectos";
    public static final String consultaMultimedia =grupoProyectos+"multimedia";
    public static final String consultaComentarios =grupoComentarios+"comentarios";
    public static final String consultaComentariosProyecto =grupoComentarios+"comentariosproyecto";
    public static final String consultaNotificaciones =grupoNotificaciones+"notificaciones";

    public static final String consultaPublicaciones =grupoPublicaciones+"publicaciones";
    public static final String consultaPublicacion =grupoPublicaciones+"publicacion";


    //Inserciones y eliminaciones
    public static final String insertaUsuario =grupoUsuarios+"guardausuario";
    public static final String insertaSeguidor =grupoUsuarios+"seguir";
    public static final String eliminarSeguidor =grupoUsuarios+"dejarseguir";
    public static final String insertaBuenaIdea =grupoUsuarios+"buenaidea";
    public static final String eliminarBuenaIdea =grupoUsuarios+"noesbuenaidea";
    public static final String insertaUnion =grupoUsuarios+"solicitudunion";
    public static final String eliminarUnion =grupoUsuarios+"eliminasolicitudunion";
    public static final String insertaOEliminaInteres =grupoUsuarios+"interes";
    public static final String insertaComentario =grupoComentarios+"guardar";

    public static String hacerConsulta(String urlREST, String parameters, String metodo) {

            HttpURLConnection connection= null;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {

                System.setProperty("http.keepAlive", "false");
            }

            try {

                URL url = new URL(server + urlREST );

                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod(metodo);
                connection.getRequestMethod();
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoInput(true);
                if(metodo.compareTo("POST")==0) {
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(parameters.getBytes());
                    os.flush();
                }
                connection.connect();

                int responseCode = connection.getResponseCode();

                Log.d(" reponseCode", String.valueOf(responseCode));

                if(responseCode == HttpURLConnection.HTTP_OK){

                    StringBuilder sb = new StringBuilder();
                    try{

                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String linea;

                        while ((linea = br.readLine())!= null){
                            sb.append(linea);
                        }

                        return sb.toString();
                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }else{

                    if(responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
                        Log.d(" reponseCode",  connection.getErrorStream().toString());
                    }
                }

            } catch (MalformedURLException e) {

                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            finally {
                    if(connection!=null)
                        connection.disconnect();
            }

            return null;
    }

}
