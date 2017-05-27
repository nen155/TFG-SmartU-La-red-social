package com.smartu.vistas;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartu.R;
import com.smartu.adaptadores.AdapterComentarioProyecto;
import com.smartu.modelos.Comentario;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.Sesion;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Una subclase simple {@link Fragment}.
 * Usa el método factoría {@link FragmentComentariosProyecto#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentComentariosProyecto extends Fragment {
    //ArrayList de comentarios para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Comentario> comentarios = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_COMENTARIOS = "comentarios";
    private static final String ARG_PROYECTO = "proyecto";
    private RecyclerView recyclerViewComentarios;
    private Button enviarComentario;
    private EditText textoComentario;
    private CircleImageView circleImageView;
    private Usuario usuario = null;
    private Proyecto proyectoOrigen=null;
    private AdapterComentarioProyecto adapterComentarioProyecto;
    private HComentar hComentar;



    public FragmentComentariosProyecto() {
        // Constructor vacío es necesario
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param comentarios Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentComentariosProyecto newInstance(ArrayList<Comentario> comentarios, Proyecto proyectoOrigen) {
        FragmentComentariosProyecto fragment = new FragmentComentariosProyecto();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_COMENTARIOS, comentarios);
        args.putParcelable(ARG_PROYECTO,proyectoOrigen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comentarios = getArguments().getParcelableArrayList(ARG_COMENTARIOS);
            proyectoOrigen = getArguments().getParcelable(ARG_PROYECTO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmen =inflater.inflate(R.layout.fragment_comentarios_proyecto, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewComentarios = (RecyclerView) fragmen.findViewById(R.id.recyclerComentariosProyecto);
        recyclerViewComentarios.setLayoutManager(llm);
        enviarComentario = (Button) fragmen.findViewById(R.id.enviar_comentario);
        textoComentario =(EditText) fragmen.findViewById(R.id.text_comentario);
        circleImageView = (CircleImageView) fragmen.findViewById(R.id.img_usuario_comentario);
        usuario =Sesion.getUsuario(getContext());
        return fragmen;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterComentarioProyecto = new AdapterComentarioProyecto(getContext(), comentarios);
        recyclerViewComentarios.setAdapter(adapterComentarioProyecto);
        //Si he iniciado sesión cargo la imagen
        if(usuario!=null)
            Picasso.with(getContext()).load(ConsultasBBDD.server+usuario.getImagenPerfil()).into(circleImageView);

        enviarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si tengo usuario envio el comentario
                if(usuario!=null){
                    if(textoComentario.getText().length()>0) {
                        Comentario comentario = new Comentario();
                        comentario.setId(0);
                        comentario.setDescripcion(textoComentario.getText().toString());
                        comentario.setIdUsuario(usuario.getId());
                        comentario.setIdProyecto(proyectoOrigen.getId());
                        hComentar = new HComentar(comentario);
                    }else
                    {
                        textoComentario.requestFocus();
                    }
                }else { //Sino se va a iniciar sesión
                    startActivity(new Intent(getContext(),LoginActivity.class));
                }
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    ///////////////////////////////////////////////////////////////////////////////////////
    /*
     * HEBRAS
     */
    ///////////////////////////////////////////////////////////////////////////////////////
    /**
     * Hebra para insertar el seguidor
     */
    private class HComentar extends AsyncTask<Void, Void, String> {

        private Comentario comentario;
        HComentar() {

        }
        HComentar(Comentario comentario) {
            this.comentario = comentario;
        }

        @Override
        protected String doInBackground(Void... params) {
            ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES).disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            String comentarioJSON="";
            //Construyo el JSON
            try {
                comentarioJSON = objectMapper.writeValueAsString(comentario);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            String resultado = null;

            //Cojo el resultado en un String
            resultado = ConsultasBBDD.hacerConsulta(ConsultasBBDD.insertaComentario, comentarioJSON, "POST");


            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hComentar = null;
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
                        Toast.makeText(getContext(),"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getContext(),"No se ha podido realizar la operacion, problemas de conexión?",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled(String resultado) {
            super.onCancelled(resultado);
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hComentar = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Elimino la referencia a la hebra para que el recolector de basura la elimine de la memoria
            hComentar = null;
        }
    }

}