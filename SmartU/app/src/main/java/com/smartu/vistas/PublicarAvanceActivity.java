package com.smartu.vistas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mvc.imagepicker.ImagePicker;
import com.smartu.R;
import com.smartu.almacenamiento.Almacen;
import com.smartu.hebras.CallBackHebras;
import com.smartu.hebras.CallBackImagen;
import com.smartu.hebras.HCrearAvance;
import com.smartu.hebras.HSubirImagen;
import com.smartu.modelos.Proyecto;
import com.smartu.modelos.Usuario;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.MultipartUtility;
import com.smartu.utilidades.Sesion;
import com.smartu.utilidades.SliderMenu;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PublicarAvanceActivity extends AppCompatActivity implements CallBackImagen,CallBackHebras {

    private Proyecto proyecto;
    private Bitmap imagen = null;
    private Usuario usuarioSesion=null;
    private String descripcion,nombre;
    private EditText editTextDescripcion,editTextNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_avance);
        Bundle bundle = getIntent().getExtras();
        proyecto = new Proyecto();
        editTextDescripcion = (EditText) findViewById(R.id.textoAvance);
        editTextNombre = (EditText) findViewById(R.id.nombreAvance);
        //Obtengo el proyecto que me han pasado
        if (bundle != null) {
            int id = bundle.getInt("idProyecto");
            Almacen.buscar(id, proyecto, this);
        }
        usuarioSesion = Sesion.getUsuario(this);
        if (proyecto != null) {
            //Cargo el menú lateral y pongo el nombre del proyecto a el Toolbar
            SliderMenu sliderMenu = new SliderMenu(this, this);
            sliderMenu.inicializateToolbar(proyecto.getNombre());
            setTitle(proyecto.getNombre());
            ImagePicker.setMinQuality(600, 600);
        }
        FloatingActionButton guardarAvance = (FloatingActionButton) findViewById(R.id.guardar_avance);
        guardarAvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextDescripcion.getText().length()>0 &&  editTextNombre.getText().length()>0) {
                    descripcion = editTextDescripcion.getText().toString();
                    nombre = editTextNombre.getText().toString();
                    if (imagen != null)
                        enviarImagen();
                    else
                        terminada(0);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_adjuntar:
                adjuntar();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void adjuntar() {
        if(imagen==null)
            ImagePicker.pickImage(this, "Selecciona una imagen:");
    }
    //Después de enviar la imagen se creará el avance
    public void enviarImagen() {
        HSubirImagen hSubirImagen = new HSubirImagen(this,imagen,this,proyecto.getNombre());
        hSubirImagen.sethAvances(hSubirImagen);
        hSubirImagen.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imagen = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
    }

    //Termina la hebra de subir una imagen
    @Override
    public void terminada(int id) {
        HCrearAvance hCrearAvance = new HCrearAvance(this,proyecto.getId(),usuarioSesion.getId(),id,descripcion,nombre);
        hCrearAvance.sethAvances(hCrearAvance);
        hCrearAvance.setCallBackHebras(this);
        hCrearAvance.execute();
    }
    //Termina la hebra de crear avance
    @Override
    public void terminada() {
        editTextDescripcion.setText("");
        editTextNombre.setText("");
        imagen=null;
        Toast.makeText(this,"Has creado un nuevo avance en el proyecto!",Toast.LENGTH_SHORT).show();
    }
}
