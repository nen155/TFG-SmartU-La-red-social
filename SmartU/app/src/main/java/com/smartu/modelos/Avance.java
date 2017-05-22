package com.smartu.modelos;

import android.os.Parcel;

import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 19/05/2017.
 */

public class Avance extends Publicacion {
    private int id;
    private Date fecha;
    private String nombre;
    private String descripcion;

    public Avance(int id, Date fecha, String nombre, String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

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

    protected Avance(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeLong(fecha.getTime());
    }
}
