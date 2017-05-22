package com.smartu.modelos;

import android.os.Parcel;

import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Comentario extends Publicacion {
    private int id;
    private String descripcion;
    private Date fecha;

    public Comentario(int id, String descripcion, Date fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    protected Comentario(Parcel in) {
        id = in.readInt();
        descripcion = in.readString();
        fecha = new Date(in.readLong());
    }

    public static final Creator<Comentario> CREATOR = new Creator<Comentario>() {
        @Override
        public Comentario createFromParcel(Parcel in) {
            return new Comentario(in);
        }

        @Override
        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(descripcion);
        dest.writeLong(fecha.getTime());
    }
}
