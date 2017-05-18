package com.smartu.vistas;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.smartu.R;
import com.smartu.utilidades.ControladorPreferencias;
import com.smartu.utilidades.Sesion;

/**
 * Created by Emilio Chica Jiménez on 17/05/17.
 */

public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Oculto la barra de título para que no se vea en el Splash
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Establezco el contenido de la View al layout correspondiente
        setContentView(R.layout.activity_splash_screen);
        //Cargo las preferencias guardadas por el usuario, tipo de sesión, sesion y si es la primera vez
        ControladorPreferencias.cargarPreferencias(this);
        //Compruebo si es la primera vez que abre la aplicación
        if(ControladorPreferencias.isFirstTime()) {
            //Si es la primera vez, espero 3 ssegundos en el splash y muestro las instrucciones
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, InstruccionesMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }, 3000);
        }
        else
        {
            //Si no es la primera vez, espero 3 ssegundos en el splash
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Si no tengo usuario con sesión iniciada( Usuario anónimo cuenta como sesión)
                    //me voy a la pantalla de Login
                    if(Sesion.getUsuario(getApplicationContext())==null){
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{//Sino voy al Main directamente
                        /*Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                    }
                }
            }, 3000);
        }
    }
}
