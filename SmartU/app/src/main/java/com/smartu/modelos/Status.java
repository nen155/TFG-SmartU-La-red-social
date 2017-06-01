package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Status implements Parcelable,Serializable {
    private int id;
    private String nombre;
    private int puntos;
    //Este campo se consigue a través de contar el número de ocurrencias
    //en la tabla seguidor
    private int numSeguidores;

    public Status(){}
    public Status(int id, String nombre, int puntos, int numSeguidores) {
        this.id = id;
        this.nombre = nombre;
        this.puntos = puntos;
        this.numSeguidores = numSeguidores;
    }

    protected Status(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        puntos = in.readInt();
        numSeguidores = in.readInt();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

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

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getNumSeguidores() {
        return numSeguidores;
    }

    public void setNumSeguidores(int numSeguidores) {
        this.numSeguidores = numSeguidores;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeInt(puntos);
        dest.writeInt(numSeguidores);
    }
}
