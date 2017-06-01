package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Especialidad implements Parcelable,Serializable {
    private int id;
    private String nombre;
    private String descripcion;
    private int experiencia;

    public Especialidad(){}
    public Especialidad(int id, String nombre, String descripcion,int experiencia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.experiencia =experiencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    protected Especialidad(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        experiencia = in.readInt();
    }

    public static final Parcelable.Creator<Especialidad> CREATOR = new Parcelable.Creator<Especialidad>() {
        @Override
        public Especialidad createFromParcel(Parcel in) {
            return new Especialidad(in);
        }

        @Override
        public Especialidad[] newArray(int size) {
            return new Especialidad[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeInt(experiencia);
    }

}
