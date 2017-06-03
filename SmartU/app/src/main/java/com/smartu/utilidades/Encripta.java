package com.smartu.utilidades;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * Created by Emilio Chica Jiménez on 03/06/2017.
 */

public class Encripta {
    public static String encriptar(String cadena){
        return DigestUtils.sha1Hex(cadena);
    }
}
