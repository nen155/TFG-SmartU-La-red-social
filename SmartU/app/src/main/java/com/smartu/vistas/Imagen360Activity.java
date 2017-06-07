package com.smartu.vistas;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.texture.MD360BitmapTexture;
import com.smartu.R;
import com.smartu.modelos.Multimedia;
import com.smartu.utilidades.ConsultasBBDD;
import com.smartu.utilidades.SpinnerHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

public class Imagen360Activity extends AppCompatActivity {
    private static final SparseArray<String> sInteractiveMode = new SparseArray<>();
    static {
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_TOUCH,"TOCANDO");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_CARDBORAD_MOTION, "MOVIMIENTO");
    }
    private MDVRLibrary mVRLibrary;
    private Target mTarget;// keep the reference for picasso.
    private Multimedia multimedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sin titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // A pantalla completa
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_imagen360);

        cargandoImagen();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null && bundle.containsKey("multimedia")) {
            multimedia = bundle.getParcelable("multimedia");
            // Inicializo VR Library
            initVRLibrary();
            //Cargo el SPINNER con las opciones
            SpinnerHelper.with(this)
                    .setData(sInteractiveMode)
                    .setDefault(mVRLibrary.getInteractiveMode())
                    .setClickHandler(new SpinnerHelper.ClickHandler() {
                        @Override
                        public void onSpinnerClicked(int index, int key, String value) {
                            mVRLibrary.switchInteractiveMode(Imagen360Activity.this, key);
                        }
                    })
                    .init(R.id.spinner_interactive);
        }
    }
    private void initVRLibrary(){
        // new instance
        mVRLibrary = MDVRLibrary.with(this)
                .asBitmap(new MDVRLibrary.IBitmapProvider() {
                    @Override
                    public void onProvideBitmap(final MD360BitmapTexture.Callback callback) {
                        /*TODO Esto es para cuando funcione el server Uri.parse(ConsultasBBDD.server+multimedia.getUrl()*/
                        loadImage(Uri.parse(ConsultasBBDD.server+ConsultasBBDD.imagenes+multimedia.getUrl()), callback);
                    }
                })
                .pinchEnabled(true)
                .build(R.id.gl_view);
    }
    private void loadImage(Uri uri, final MD360BitmapTexture.Callback callback){
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // notify if size changed
                mVRLibrary.onTextureResize(bitmap.getWidth(), bitmap.getHeight());
                // texture
                callback.texture(bitmap);
                imagenCargada();
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };
        //Cargo la imagen con Picasso, me aseguro de que no la guarde en cache
        //que la reescale para ser más pequeña, que la centre y que la ponga
        //en el target
        Picasso.with(getApplicationContext())
                .load(uri)
                .resize(callback.getMaxTextureSize(),callback.getMaxTextureSize())
                .onlyScaleDown()
                .centerInside()
                .memoryPolicy(NO_CACHE, NO_STORE)
                .into(mTarget);
    }

    /**
     * Metodo para quitar el Spinner de carga
     */
    private void imagenCargada(){findViewById(R.id.progress).setVisibility(View.GONE);}
    /**
     * Metodo para poner el Spinner de carga
     */
    public void cargandoImagen(){
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVRLibrary.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mVRLibrary.onOrientationChanged(this);
    }

    /**
     * Otiene la URI de un Drawable
     * Para probar la imagen en 360
     */
    private Uri getDrawableUri(@DrawableRes int resId){
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }
}
