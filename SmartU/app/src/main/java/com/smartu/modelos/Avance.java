package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smartu.utilidades.ConversoresJSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 19/05/2017.
 */

public class Avance implements Parcelable,Serializable{
    private int id;
    @JsonProperty
    @JsonSerialize(using=ConversoresJSON.DateTimeSerializer.class)
    @JsonDeserialize(using=ConversoresJSON.DateTimeDeserializer.class)
    private Date fecha;
    private String nombre;
    private String descripcion;
    private ArrayList<Multimedia> misArchivos;
    private int idUsuario;
    private String nombreUsuario;
    private String imagenDestacada;

    public void clonar(Avance c){
        id=c.getId();
        descripcion=c.getDescripcion();
        nombre=c.getNombre();
        fecha=c.getFecha();
        idUsuario=c.getIdUsuario();
        nombreUsuario =c.getNombreUsuario();
        imagenDestacada =c.getImagenDestacada();
        if(c.getMisArchivos()!=null)
            misArchivos = new ArrayList<>(c.getMisArchivos());
        else
            misArchivos =new ArrayList<>();

    }

    public Avance(){
        this.misArchivos = new ArrayList<>();
    }
    public Avance(int id, Date fecha, String nombre, String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.misArchivos = new ArrayList<>();
    }

    protected Avance(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        misArchivos = in.createTypedArrayList(Multimedia.CREATOR);
        fecha = new Date(in.readLong());
        idUsuario = in.readInt();
        nombreUsuario = in.readString();
        imagenDestacada=in.readString();
    }

    public static final Creator<Avance> CREATOR = new Creator<Avance>() {
        @Override
        public Avance createFromParcel(Parcel in) {
            return new Avance(in);
        }

        @Override
        public Avance[] newArray(int size) {
            return new Avance[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<Multimedia> getMisArchivos() {
        return misArchivos;
    }

    public void setMisArchivos(ArrayList<Multimedia> misArchivos) {
        this.misArchivos = misArchivos;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getImagenDestacada() {
        return imagenDestacada;
    }

    public void setImagenDestacada(String imagenDestacada) {
        this.imagenDestacada = imagenDestacada;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeTypedList(misArchivos);
        dest.writeLong(fecha.getTime());
        dest.writeInt(idUsuario);
        dest.writeString(nombreUsuario);
        dest.writeString(imagenDestacada);
    }
}
