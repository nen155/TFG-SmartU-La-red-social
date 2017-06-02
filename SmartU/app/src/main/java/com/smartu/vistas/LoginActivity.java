package com.smartu.vistas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smartu.R;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    //Para iniciar sesión también en la base de datos de Firebase
    //y poder enviar mensajes
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Hebra que va a comprobar el usuario y contraseña
    private HLogin hLogin = null;

    // Referencias a la UI.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Inicializo los elementos de la interfaz
        inicializaUI();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    iniciarSesion();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_iniciar_sesion);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        //Si el usuario está ya autentificado lo mando al Main
        if (mFirebaseUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    // El usuario no está logueado
                }
            }
        };

    }

    /**
     * Inicializa los componentes de la interfaz
     */
    private void inicializaUI(){
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Intenta iniciar sesión
     * Si hay errores (email no válido, campos no rellenos, etc.),
     * se muestran los errores y no se permite el inicio de sesion
     */
    private void iniciarSesion() {
        if (hLogin != null) {
            return;
        }

        // Reseteo los errores.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Guardo los valores
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Compruebo que el password es válido
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_campo_obligatorio));
            focusView = mPasswordView;
            cancel = true;
        }else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_password_incorrecto));
            focusView = mPasswordView;
            cancel = true;
        }

        // Compruebo que el email es correcto
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_campo_obligatorio));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_email_incorrecto));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            //Hay un error así que devuelvo el foco al campo concreto
            focusView.requestFocus();
        } else {
            //Muestro el progreso
            muestraProgreso(true);
            //Creo un usuario para el inicio de sesión
            Usuario usuario = new Usuario(email,password);
            //Creo la hebra para hacer el inicio de sesión
            hLogin = new HLogin(usuario);
            hLogin.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    /**
     * Muestra el progreso de la hebra y esconde el formulario
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void muestraProgreso(final boolean mostrar) {
        // En Honeycomb MR2 tenemos ViewPropertyAnimator APIs, que nos permiten
        // animaciones de forma facil. Si esta disponible, uso esas APIs para hacer fade-in
        // del contador de progreso.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            //Cojo de los recursos de android un tiempo predefinido corto para la animación
            int tiempoAnimacion = getResources().getInteger(android.R.integer.config_shortAnimTime);
            //Si tengo que mostrar el progreso oculto el Formulario de Login sino lo mantengo visible
            mLoginFormView.setVisibility(mostrar ? View.GONE : View.VISIBLE);
            //Establezco el tiempo y dependiendo de si tengo que mostrarla pongo el alpha o no y le agrego
            //un escuchador para cuando termine la animacion deje de mostrarse
            mLoginFormView.animate().setDuration(tiempoAnimacion).alpha(
                    mostrar ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(mostrar ? View.GONE : View.VISIBLE);
                }
            });
            //Muestro el progreso dependiendo de mostrar
            mProgressView.setVisibility(mostrar ? View.VISIBLE : View.GONE);
            //Hago lo mismo para el progrewso que para el formulario de login
            mProgressView.animate().setDuration(tiempoAnimacion).alpha(
                    mostrar ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(mostrar ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // Si ViewPropertyAnimator APIs no esta disponible, muestro y escondo
            // el progreso y el login respectivamente
            mProgressView.setVisibility(mostrar ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    /**
     * HERBA LOGIN
     * Esta hebra va a quedarse en esta clase porque es intrínseca a ella y sólo se llama aquí
     */
    //////////////////////////////////////////////////////////////////////////////////////////
    private class HLogin  extends AsyncTask<Void, Void, Boolean> {

        private Usuario usuario;

        HLogin(Usuario usuario) {
            this.usuario =usuario;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: COMPLETAR LA TAREA DE LOGIN CUANDO ESTE EL SERVER.
            //Me creo el mapeador de JSON
            ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            String usuarioJSON = "";
            try {
                //Mapeo el usuario que me han pasado
                usuarioJSON = mapper.writeValueAsString(usuario);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // Hacer llamada al servidor
           // String resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.consultaLogin,usuarioJSON,"POST");
            String resultado="";
            if(usuario.getEmail().compareTo("emiliocj@correo.ugr.es")==0) {
                resultado = "{\"usuario\":{\"id\":\"1\",\"nombre\":\"Emilio\",\"apellidos\":\"Chica Jiménez\",\"verificado\":\"true\",\"user\":\"emiliocj\",\"email\":\"emiliocj@correo.ugr.es\",\"nPuntos\":\"100\",\"localizacion\":\"C/Poeta Manuel\",\"biografia\":\"Estudiante universitario de la ETSIIT que vive en Granada y es Graduado en Ingeniería Informática\", \"web\":\"http://coloremoon.com\",\"imagenPerfil\":\"wp-content/uploads/2017/05/foto-buena.jpg\",\n" +
                        "          \"misProyectos\":[\n" +"\"1\"],\n" +
                        "            \"misAreasInteres\":[\n" +
                        "              {\"id\":\"1\",\"nombre\":\"Informática\"}\n" +
                        "              ],\n" +
                        "              \"misEspecialidades\":[\n" +
                        "              {\"id\":\"1\",\"nombre\":\"Informática\"}\n" +
                        "              ],\n" +
                        "              \"miStatus\":{\"id\":\"1\",\"nombre\":\"creador\",\"puntos\":\"100\",\"numSeguidores\":\"1\"},\n" +
                        "              \"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.facebook.com/\"}]\n" +
                        "    }\n" +
                        "}";
            }else if(usuario.getEmail().compareTo("juanji@gmail.com")==0){
                resultado = "{\"usuario\":{\"id\":\"2\",\"nombre\":\"Juanjo\",\"apellidos\":\"Jiménez\",\"verificado\":\"true\",\"user\":\"juanjo\",\"email\":\"juanji@gmail.com\",\"nPuntos\":\"150\",\"localizacion\":\"C/Armilla \",\"biografia\":\"Estudiante universitario de la ETSIIT que vive en Granada, en Armilla y es Graduado en Ingeniería Informática\", \"web\":\"http://juanjo.com\",\"imagenPerfil\":\"wp-content/uploads/2017/05/AtBE7.png\",\n" +
                        "          \"misProyectos\":[\n" +"\"1\"],\n" +
                        "            \"misAreasInteres\":[\n" +
                        "              {\"id\":\"1\",\"nombre\":\"Informática\"}\n" +
                        "              ],\n" +
                        "              \"misEspecialidades\":[\n" +
                        "              {\"id\":\"1\",\"nombre\":\"Informática\"}\n" +
                        "              ],\n" +
                        "              \"miStatus\":{\"id\":\"1\",\"nombre\":\"creador\",\"puntos\":\"100\",\"numSeguidores\":\"2\"},\n" +
                        "              \"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.facebook.com/\"}]\n" +
                        "    }\n" +
                        "}";
            }else if(usuario.getEmail().compareTo("german@gmail.com")==0){
                resultado = "{\"usuario\":{\"id\":\"3\",\"nombre\":\"German\",\"apellidos\":\"Zayas Cabrera\",\"verificado\":\"true\",\"user\":\"german\",\"email\":\"german@gmail.com\",\"nPuntos\":\"150\",\"localizacion\":\"C/Ceballos \",\"biografia\":\"Estudiante universitario de la UGR que vive en Granada, en Peligros con el Grado en Bellas Artes\", \"web\":\"http://german.com\",\"imagenPerfil\":\"wp-content/uploads/2017/05/j5xrbugqkkalex-avatar.png\",\n" +
                        "          \"misProyectos\":[\n" +"\"1\"],\n" +
                        "            \"misAreasInteres\":[\n" +
                        "              {\"id\":\"2\",\"nombre\":\"Bellas artes\"}\n" +
                        "              ],\n" +
                        "              \"misEspecialidades\":[\n" +
                        "              {\"id\":\"2\",\"nombre\":\"Diseño gráfico\"}\n" +
                        "              ],\n" +
                        "              \"miStatus\":{\"id\":\"1\",\"nombre\":\"creador\",\"puntos\":\"100\",\"numSeguidores\":\"2\"},\n" +
                        "              \"misRedesSociales\":[{\"id\":\"1\",\"nombre\":\"facebook\",\"url\":\"https://www.twitter.com/\"}]\n" +
                        "    }\n" +
                        "}";
            }

            try {
                //Convierto el resultado a JSON
                JSONObject res = new JSONObject(resultado);

                //TODO//////////////////////////////////////////////////////////////////////////////

                //////////////////////////PARA PROBAR LOS MENSAJES////////////////////////////


                ////////////////////////////////////////////////////////////////////////////////


                //Si el resultado es null signfica que no coincide usuario y contraseña
                if(res.isNull("usuario"))
                    return false;
                else {
                    JSONObject usuarioJSONServer = res.getJSONObject("usuario");
                    String password = usuario.getPassword();
                    //Mapeo el usuario que me han pasado para mantener la sesión abierta
                    usuario = mapper.readValue(usuarioJSONServer.toString(), Usuario.class);
                    usuario.setPassword(password);
                    //Serializo al usuario para que cuando se requiera esté inicializado
                    Sesion.serializaUsuario(LoginActivity.this,usuario);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //Libero memoria quitando la instancia de la hebra
            hLogin=null;
            //Dejo de mostrar el progreso
            muestraProgreso(false);
            //He conseguido hacer login en mi server
            if (success) {
                //Si he conseguido iniciar sesion voy a iniciar sesión en Firebase
                mFirebaseAuth.signInWithEmailAndPassword(usuario.getEmail(),usuario.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Sino inicio sesion con firebase
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                } else {//Si he conseguido iniciar sesión
                                   Toast toast= Toast.makeText(LoginActivity.this, "Has Iniciado Sesión", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                    //vamos al Main
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                                    //TODO//////////////////////////////////////////////////////////////////////////////

                                    //////////////////////////PARA PROBAR LOS MENSAJES////////////////////////////

                                    Sesion.serializaUsuario(LoginActivity.this,usuario);

                                    ////////////////////////////////////////////////////////////////////////////////

                                    startActivity(intent);
                                }
                            }
                        });
            } else {
                mPasswordView.setError(getString(R.string.error_password_incorrecto));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //Libero memoria quitando la instancia de la hebra
            hLogin=null;
            //Dejo de mostrar el progreso
            muestraProgreso(false);
        }

    }



}

