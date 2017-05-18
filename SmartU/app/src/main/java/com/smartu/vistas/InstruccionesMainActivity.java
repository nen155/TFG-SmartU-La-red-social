package com.smartu.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.smartu.R;

public class InstruccionesMainActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Añado los Slide para las instrucciones del Main
        //addSlide(AppIntroFragment.newInstance(getString(R.string.menu_main), getString(R.string.descripcion_instrucciones_main), R.drawable.menu_lateral_instrucciones, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight));
        addSlide(AppIntroFragment.newInstance(getString(R.string.menu_lateral), getString(R.string.descripcion_instrucciones_menu), R.drawable.menu_lateral_instrucciones,ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
        setSeparatorColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        //Muestro el botón para saltarme las instrucciones
        showSkipButton(true);
        //Muestro un botón para pasar de slide
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent MainIntent = new Intent(this, LoginActivity.class);
        MainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(MainIntent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent MainIntent = new Intent(this, LoginActivity.class);
        MainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(MainIntent);
    }

}
