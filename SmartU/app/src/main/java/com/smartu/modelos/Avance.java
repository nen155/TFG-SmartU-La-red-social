package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 19/05/2017.
 */

public class Avance implements Parcelable,Serializable{
    private int id;
    private Date fecha;
    private String nombre;
    private String descripcion;
    private ArrayList<Multimedia> misArchivos;

    public Avance(){}
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
    }
}
