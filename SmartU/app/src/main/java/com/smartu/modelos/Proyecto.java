package com.smartu.modelos;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Proyecto {
    private int id;
    private String nombre;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaFinalizacion;
    private String localizacion;
    //Contenedores de los que es propietario
    private ArrayList<Comentarios> misComentarios;
    private ArrayList<Area> misAreas;
    private ArrayList<Especialidad> especialidadesNecesarias;
    private ArrayList<Multimedia> misArchivos;
    private ArrayList<RedSocial> misRedesSociales;
    private int buenaIdea;

}
