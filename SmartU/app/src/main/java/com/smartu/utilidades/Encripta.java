package com.smartu.utilidades;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Emilio Chica Jiménez on 03/06/2017.
 */

public class Encripta {
    public static String MD2 = "MD2";

    public static String MD5 = "MD5";

    public static String SHA1 = "SHA-1";

    public static String SHA256 = "SHA-256";

    public static String SHA384 = "SHA-384";

    public static String SHA512 = "SHA-512";


    public static String encriptar(String cadena){
        return cifrar(cadena,SHA1);
    }

    /* Convierte un array de bytes a String usando valores hexadecimales
     * @param digest array de bytes a convertir
     * @return String array a partir de <code>digest</code>
     */
    private static String toHexadecimal(byte[] digest){
        String hash = "";
        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    /***
     * Encripta un mensaje de texto mediante algoritmo de resumen de mensaje.
     * @param message texto a encriptar
     * @param algorithm algoritmo de encriptacion, puede ser: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
     * @return mensaje encriptado
     */
    public static String cifrar(String message, String algorithm){
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest);
    }
}
