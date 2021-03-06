package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smartu.utilidades.ConversoresJSON;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 26/05/2017.
 */

public class SolicitudUnion implements Parcelable,Serializable {
    @JsonProperty
    @JsonSerialize(using=ConversoresJSON.DateTimeSerializer.class)
    @JsonDeserialize(using=ConversoresJSON.DateTimeDeserializer.class)
    private Date fecha;
    private String proyecto;
    private int idProyecto;
    private String descripcion;
    private int idUsuarioSolicitante;
    private int idVacante;

    public SolicitudUnion(){}
    public SolicitudUnion(Date fecha, String proyecto,int idProyecto,int idVacante) {
        this.fecha = fecha;
        this.proyecto = proyecto;
        this.idProyecto = idProyecto;
        this.idVacante = idVacante;
    }
    public SolicitudUnion(Date fecha, int idUsuarioSolicitante,String descripcion) {
        this.fecha = fecha;
        this.idUsuarioSolicitante=idUsuarioSolicitante;
        this.descripcion = descripcion;
    }
    protected SolicitudUnion(Parcel in) {
        proyecto = in.readString();
        fecha = new Date(in.readLong());
        idProyecto = in.readInt();
        idUsuarioSolicitante=in.readInt();
        descripcion = in.readString();
        idVacante = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(proyecto);
        if(fecha!=null)
            dest.writeLong(fecha.getTime());
        else
            dest.writeLong(new Date().getTime());
        dest.writeInt(idProyecto);
        dest.writeInt(idUsuarioSolicitante);
        dest.writeString(descripcion);
        dest.writeInt(idVacante);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SolicitudUnion> CREATOR = new Creator<SolicitudUnion>() {
        @Override
        public SolicitudUnion createFromParcel(Parcel in) {
            return new SolicitudUnion(in);
        }

        @Override
        public SolicitudUnion[] newArray(int size) {
            return new SolicitudUnion[size];
        }
    };

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public int getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(int idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdVacante() {
        return idVacante;
    }

    public void setIdVacante(int idVacante) {
        this.idVacante = idVacante;
    }
}
