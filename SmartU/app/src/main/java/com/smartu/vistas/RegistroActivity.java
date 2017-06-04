package com.smartu.vistas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smartu.R;
import com.smartu.modelos.Usuario;
import com.smartu.modelos.chat.User;
import com.smartu.utilidades.Constantes;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.ControladorPreferencias;
import com.smartu.utilidades.Encripta;
import com.smartu.utilidades.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistroActivity extends AppCompatActivity {

    private Usuario usuario;
    private HRegistro hRegistro;
    private Context context;
    private EditText email,nombre,password,user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        context=this;

        email = (EditText) findViewById(R.id.email_registro);
        nombre = (EditText) findViewById(R.id.nombre_completo_registro);
        password = (EditText) findViewById(R.id.password_registro);
        user = (EditText) findViewById(R.id.user_registro);


        Button button =(Button) findViewById(R.id.btn_registro);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///FALTAN COMPROBACIONES DE TODO TIPO PERO VOY A OBVIARLAS
                ///POR FALTA DE TIEMPO, SON TRIVIALES: USER EXISTE, EMAIL EXISTE, CONTRASEÑA CORTA,ETC..
                String nombreC= nombre.getText().toString();
                usuario=new Usuario();
                if(nombreC.contains(" ")){
                    String[] nom = nombreC.split(" ");
                    usuario.setNombre(nom[0]);
                    if(nom.length>2){
                        usuario.setApellidos(nom[1]+" "+nom[2]);
                    }else
                        usuario.setApellidos(nom[1]);
                }else
                    usuario.setNombre(nombreC);

                usuario.setEmail(email.getText().toString());
                //Cifro el password aquí para que el POST sea completamente seguro y llevar la clave encriptada
                usuario.setPassword(Encripta.encriptar(password.getText().toString()));
                usuario.setUser(user.getText().toString());

                hRegistro = new HRegistro();
                hRegistro.execute();
            }
        });
    }



    protected class HRegistro extends AsyncTask<Void, Void, String> {


        private SweetAlertDialog pDialog;

        protected HRegistro() {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Cargando...");
            pDialog.setCancelable(false);
        }


        @Override
        protected void onPreExecute() {
            pDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).disable(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS);
            String resultado = null;
            String  usuarioJson="";
            //Construyo el JSON
            try {
                 usuarioJson = mapper.writeValueAsString(usuario);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaUsuario, usuarioJson, "POST");


            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            pDialog.dismissWithAnimation();
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hRegistro = null;
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
                        Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
                    }else{
                        //Me registro en Firebase, no encripto el password porque se encripta en el FCM y la conexión es https
                        performFirebaseRegistration(usuario.getEmail(),password.getText().toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {

            }
        }

        @Override
        protected void onCancelled(String resultado) {
            super.onCancelled(resultado);
            pDialog.dismissWithAnimation();
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hRegistro = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pDialog.dismissWithAnimation();
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hRegistro = null;
        }

        protected void performFirebaseRegistration(final String email, String password) {
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) (context), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser userFB = task.getResult().getUser();
                                // Add the user to users table.
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                User user = new User(userFB.getUid(),
                                        userFB.getEmail(),
                                        ControladorPreferencias.cargarToken(context));

                                database.child(Constantes.ARG_USERS)
                                        .child(userFB.getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context,"Has sido registrado correctamente!",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(context,LoginActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(context,"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                }
                    }
                    });
        }
    }
}
