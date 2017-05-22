package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Comentario extends Publicacion {
    private int id;
    private String descripcion;
    private Date fecha;
    private Usuario usuario;
    private Proyecto proyecto;


    public Comentario(int id, String descripcion, Date fecha, Usuario usuario, Proyecto proyecto) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.usuario = usuario;
        this.proyecto = proyecto;
    }

    protected Comentario(Parcel in) {
        id = in.readInt();
        descripcion = in.readString();
        usuario = in.readParcelable(Usuario.class.getClassLoader());
        proyecto = in.readParcelable(Proyecto.class.getClassLoader());
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(descripcion);
        dest.writeParcelable(usuario, flags);
        dest.writeParcelable(proyecto, flags);
    }
}
