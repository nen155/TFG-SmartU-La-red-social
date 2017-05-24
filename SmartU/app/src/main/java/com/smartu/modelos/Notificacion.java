package com.smartu.modelos;

import android.os.Parcel;

import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 19/05/2017.
 */

public class Notificacion extends Publicacion {
    private int id;
    private Date fecha;
    private String nombre;
    private String descripcion;
    private Usuario usuario;
    private Proyecto proyecto;

    public Notificacion() {
    }

    public Notificacion(int id, Date fecha, String nombre, String descripcion, Usuario usuario, Proyecto proyecto) {
        this.id = id;
        this.fecha = fecha;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.proyecto = proyecto;
    }

    protected Notificacion(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        usuario = in.readParcelable(Usuario.class.getClassLoader());
        proyecto = in.readParcelable(Proyecto.class.getClassLoader());
        fecha = new Date(in.readLong());
    }

    public static final Creator<Notificacion> CREATOR = new Creator<Notificacion>() {
        @Override
        public Notificacion createFromParcel(Parcel in) {
            return new Notificacion(in);
        }

        @Override
        public Notificacion[] newArray(int size) {
            return new Notificacion[size];
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
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeParcelable(usuario, flags);
        dest.writeParcelable(proyecto, flags);
        dest.writeLong(fecha.getTime());
    }
}
