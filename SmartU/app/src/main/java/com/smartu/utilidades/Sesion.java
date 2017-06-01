package com.smartu.utilidades;

import android.content.Context;

import com.smartu.modelos.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Emilio Chica Jimenez on 19/04/2016.
 */
public class Sesion {
    private static FileOutputStream fos;
    private static FileInputStream fis;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static Usuario usuario=null;


    private Sesion() {

    }
    /**
     * Implementa el patrón Singleton
     *
     * @return
     */
    public static synchronized Usuario getUsuario(Context context){
        if(usuario==null)
            usuario =deserializaUsuario(context);
        return usuario;
    }


    public static synchronized void logOut(Context context){
        usuario=null;
        serializaUsuario(context,usuario);
    }
    /**
     * Serializa el usuario para mantener la sesión
     *
     * @param context
     * @param usuario
     */
    public static void serializaUsuario(Context context, Usuario usuario) {
        try {
            fos = context.openFileOutput("usu.bin", Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(usuario);
            out.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializa al usuario para obtener la sesión, sino hay sesión
     * devuelve null
     * @param context
     * @return
     */
    public static Usuario deserializaUsuario(Context context) {
        Usuario aux = null;
            try {
                fis = context.openFileInput("usu.bin");
                if (fis.available() > 0) {
                    in = new ObjectInputStream(fis);
                }
                if (in != null) {
                    aux = (Usuario) in.readObject();
                    in.close();
                    fis.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return aux;
    }

}
