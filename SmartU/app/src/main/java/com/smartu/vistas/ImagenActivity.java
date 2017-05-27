package com.smartu.vistas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.smartu.R;
import com.smartu.modelos.Multimedia;
import com.smartu.utilidades.ConsultasBBDD;
import com.squareup.picasso.Picasso;

public class ImagenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null && bundle.containsKey("multimedia")) {
            Multimedia multimedia = bundle.getParcelable("multimedia");
            ImageView imagen = (ImageView) findViewById(R.id.img_multimedia);
            Picasso.with(getApplicationContext()).load(ConsultasBBDD.server+multimedia.getUrl()).into(imagen);
        }
    }
}
