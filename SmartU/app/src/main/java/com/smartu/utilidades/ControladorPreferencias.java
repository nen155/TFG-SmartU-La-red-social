package com.smartu.utilidades;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Emilio Chica Jiménez on 17/05/17.
 */

public class ControladorPreferencias {
    private static ControladorPreferencias instance = new ControladorPreferencias();
    private static boolean firstTime=true;

    public static ControladorPreferencias getInstance() {
        return instance;
    }

    private ControladorPreferencias() {
    }

    public static boolean isFirstTime() {
        return firstTime;
    }

    public static void setFirstTime(boolean firstTime) {
        ControladorPreferencias.firstTime = firstTime;
    }

    public static void  guardarPreferenciasFirstTime(Context t, boolean first){
        SharedPreferences misprefe = t.getSharedPreferences("PrefUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = misprefe.edit();
        editor.putBoolean("first-time",first);
        editor.commit();
        firstTime=first;
    }

    public static void cargarPreferencias(Context t){
        SharedPreferences misprefe = t.getSharedPreferences("PrefUser", Context.MODE_PRIVATE);
        firstTime=misprefe.getBoolean("first-time",true);
    }

}
