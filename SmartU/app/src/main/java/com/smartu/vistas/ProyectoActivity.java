package com.smartu.vistas;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartu.R;
import com.smartu.modelos.BuenaIdea;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Optional;

public class ProyectoActivity extends AppCompatActivity {

    private static Proyecto proyecto;
    private Usuario usuarioSesion;
    private FloatingActionButton buenaidea;
    private TextView buenaidea_contador;
    private  HBuenaIdea hBuenaIdea;
    private Optional<BuenaIdea> buenaIdea1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto);
        Bundle bundle = getIntent().getExtras();
        //Obtengo el proyecto que me han pasado
        if(bundle!=null) {
            proyecto = bundle.getParcelable("proyecto");
        }
        //Cargo el menú lateral y pongo el nombre del proyecto a el Toolbar
        SliderMenu sliderMenu = new SliderMenu(getApplicationContext(),this);
        sliderMenu.inicializateToolbar(proyecto.getNombre());
        //Inicia
        buenaidea_contador = (TextView) findViewById(R.id.buenaidea_text_proyecto);
        buenaidea = (FloatingActionButton) findViewById(R.id.buenaidea_proyecto);
        //Obtengo el usuario que ha iniciado sesión
        usuarioSesion =Sesion.getUsuario(getApplicationContext());
        //Cargo las preferencias del usuario si tuviese sesión
        cargarPreferenciasUsuario();

        buenaidea_contador.setText(String.valueOf(proyecto.getBuenaIdea()));
        buenaidea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Compruebo si el usuario ha iniciado sesión
                if(usuarioSesion!=null) {
                    //Actualizo el botón
                    buenaidea.setPressed(!buenaidea.isPressed());
                    //Compruebo como ha quedado su estado después de hacer click
                    if (buenaidea.isPressed()) {
                        buenaidea.setImageResource(R.drawable.buenaidea);
                        //Añado al contador 1 para decir que es buena idea
                        int cont = Integer.parseInt(buenaidea_contador.getText().toString())+1;
                        buenaidea_contador.setText(String.valueOf(cont));
                        Toast.makeText(getApplicationContext(),"Genial!, este proyecto te parece buena idea!",Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con 0 pues voy a añadir una nueva idea
                        hBuenaIdea = new HBuenaIdea(0);
                    }
                    else {
                        buenaidea.setImageResource(R.drawable.idea);
                        //Elimino de buena idea 1 usuario.
                        int cont = Integer.parseInt(buenaidea_contador.getText().toString())-1;
                        buenaidea_contador.setText(String.valueOf(cont));
                        Toast.makeText(getApplicationContext(),"¿Ya no te parece buena idea?",Toast.LENGTH_SHORT).show();
                        //Inicializo la hebra con el id de la buena idea que encontré
                        hBuenaIdea = new HBuenaIdea(buenaIdea1.get().getId());
                    }
                    hBuenaIdea.execute();
                }else
                {
                    //Lo mando al login
                    Intent intent = new Intent(ProyectoActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        //Inicializo el menú de abajo
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_proyecto);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Cargo el fragment por defecto
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, FragmentProyecto.newInstance(proyecto));
        transaction.commit();
    }

    /**
     * Comprueba si el usuario ha dado buena idea al proyecto
     */
    private void cargarPreferenciasUsuario(){
        //Cargo las preferencias del usuario
        if(usuarioSesion!=null) {
            //Compruebo si el usuario le ha dado antes a buena idea a este proyecto
            buenaIdea1 = proyecto.getBuenaIdea().stream().filter(buenaIdea -> buenaIdea.getIdUsuario() == usuarioSesion.getId() && buenaIdea.getIdProyecto() == proyecto.getId()).findFirst();
            boolean usuarioBuenaidea = buenaIdea1.isPresent();
            //Si es así lo dejo presionado y le cambio la imagen
            buenaidea.setPressed(usuarioBuenaidea);
            if (usuarioBuenaidea) {
                buenaidea.setImageResource(R.drawable.buenaidea);

            }
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment swicthTo=null;
            switch (item.getItemId()) {
                case R.id.navigation_proyecto:
                    swicthTo = FragmentProyecto.newInstance(proyecto);
                    break;
                case R.id.navigation_integrantes:
                    swicthTo = null;
                    break;
                case R.id.navigation_map_proyecto:
                    swicthTo = FragmentMapaProyecto.newInstance(proyecto);
                    break;
                case R.id.navigation_multimedia:
                    swicthTo = null;
                    break;
            }
            if(swicthTo!=null){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame,swicthTo);
                transaction.commit();
            }
            return true;
        }

    };
////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Hebra para insertar el seguidor
     */
    private class HBuenaIdea extends AsyncTask<Void, Void, String> {

        private int idIdea = 0;

        HBuenaIdea(int idIdea) {
            this.idIdea = idIdea;
        }

        @Override
        protected String doInBackground(Void... params) {

            String resultado = null;
            //Construyo el JSON
            String buenaidea="";
            if(idIdea!=0) {
                buenaidea = "\"buenaidea\":{\"idUsuario\":\"" + usuarioSesion.getId() + "\",\"idProyecto\":\"" + proyecto.getId() + "\"" +
                        ",\"fecha\":\"" + new Date() + "\"}";
                //Recojo el resultado en un String
                resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaBuenaIdea, buenaidea, "POST");
            }
            else
            {
                buenaidea ="\"buenaidea\":{\"idUsuario\":\""+idIdea+ "\"}";
                resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.eliminaBuenaIdea, buenaidea, "POST");
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hBuenaIdea = null;
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
            hBuenaIdea = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hBuenaIdea = null;
        }
        private void reestablecerEstado(){
            Toast.makeText(getApplicationContext(),"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
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


}
