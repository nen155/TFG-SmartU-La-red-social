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

    public SolicitudUnion(){}
    public SolicitudUnion(Date fecha, String proyecto,int idProyecto) {
        this.fecha = fecha;
        this.proyecto = proyecto;
        this.idProyecto = idProyecto;
    }

    protected SolicitudUnion(Parcel in) {
        proyecto = in.readString();
        fecha = new Date(in.readLong());
        idProyecto = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(proyecto);
        dest.writeLong(fecha.getTime());
        dest.writeInt(idProyecto);
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
}
