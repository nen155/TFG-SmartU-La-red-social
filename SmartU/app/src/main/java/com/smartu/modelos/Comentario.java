package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smartu.contratos.Publicacion;
import com.smartu.utilidades.ConversoresJSON;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Comentario implements Parcelable,Serializable,Publicacion {
    private int id;
    private String descripcion;
    @JsonProperty
    @JsonSerialize(using=ConversoresJSON.DateTimeSerializer.class)
    @JsonDeserialize(using=ConversoresJSON.DateTimeDeserializer.class)
    private Date fecha;
    private int idProyecto;
    private int idUsuario;
    private String usuario;
    private String proyecto;

    public void clonar(Comentario c){
        id=c.getId();
        descripcion=c.getDescripcion();
        fecha=c.getFecha();
        idUsuario=c.getIdUsuario();
        idProyecto=c.getIdProyecto();
        usuario=c.getUsuario();
        proyecto=c.getProyecto();
    }
    public Comentario(){

    }
    public Comentario(int id, String descripcion, Date fecha, String usuario, String proyecto) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.usuario = usuario;
        this.proyecto = proyecto;
    }

    protected Comentario(Parcel in) {
        id = in.readInt();
        descripcion = in.readString();
        usuario = in.readString();
        proyecto = in.readString();
        fecha = new java.sql.Date(in.readLong());
        idProyecto = in.readInt();
        idUsuario = in.readInt();
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(descripcion);
        dest.writeString(usuario);
        dest.writeString(proyecto);
        dest.writeLong(fecha.getTime());
        dest.writeInt(idProyecto);
        dest.writeInt(idUsuario);
    }
}
