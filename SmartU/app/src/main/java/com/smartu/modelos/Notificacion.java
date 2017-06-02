package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import com.smartu.contratos.Publicacion;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 19/05/2017.
 */

public class Notificacion implements Parcelable,Serializable,Publicacion {
    private int id;
    private Date fecha;
    private String nombre;
    private String descripcion;
    private int idUsuario;
    private int idProyecto;
    private String proyecto;
    private String usuario;

    public void clonar(Notificacion n){
        id=n.getId();
        fecha=n.getFecha();
        nombre=n.getNombre();
        descripcion=n.getDescripcion();
        idUsuario=n.getIdUsuario();
        idProyecto=n.getIdProyecto();
        proyecto=n.getProyecto();
        usuario=n.getUsuario();
    }
    public Notificacion() {
    }

    public Notificacion(int id, Date fecha, String nombre, String descripcion, int idUsuario, int idProyecto,String proyecto,String usuario) {
        this.id = id;
        this.fecha = fecha;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idUsuario = idUsuario;
        this.idProyecto = idProyecto;
        this.proyecto = proyecto;
        this.usuario = usuario;
    }

    protected Notificacion(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        idUsuario = in.readInt();
        idProyecto = in.readInt();
        fecha = new Date(in.readLong());
        usuario = in.readString();
        proyecto = in.readString();
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
        dest.writeInt(idUsuario);
        dest.writeInt(idProyecto);
        dest.writeLong(fecha.getTime());
        dest.writeString(usuario);
        dest.writeString(proyecto);
    }
}
