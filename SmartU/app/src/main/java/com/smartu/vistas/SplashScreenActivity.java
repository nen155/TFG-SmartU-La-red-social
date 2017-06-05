package com.smartu.vistas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.HPublicaciones;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Notificacion;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ControladorPreferencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Emilio Chica Jiménez on 17/05/17.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private HPublicaciones hPublicaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Para cuando es llamado desde una notificacion
        Bundle bundle = getIntent().getExtras();
        // Oculto la barra de título para que no se vea en el Splash
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Establezco el contenido de la View al layout correspondiente
        setContentView(R.layout.activity_splash_screen);
        //Cargo las preferencias guardadas por el usuario, tipo de sesión, sesion y si es la primera vez
        ControladorPreferencias.cargarPreferencias(this);
        hPublicaciones = new HPublicaciones(SplashScreenActivity.this);
        hPublicaciones.sethPublicaciones(hPublicaciones);
        //Esto es porque vengo de una notificacion
        if(bundle!=null){
            hPublicaciones.setNotificacion("notificacion");
        }
        hPublicaciones.execute();
    }


}
