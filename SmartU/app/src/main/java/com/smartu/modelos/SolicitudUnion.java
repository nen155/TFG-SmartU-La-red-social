package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 26/05/2017.
 */

public class SolicitudUnion implements Parcelable {
    private int id;
    private Date fecha;
    private Proyecto proyecto;

    public SolicitudUnion(int id, Date fecha, Proyecto proyecto) {
        this.id = id;
        this.fecha = fecha;
        this.proyecto = proyecto;
    }

    protected SolicitudUnion(Parcel in) {
        id = in.readInt();
        proyecto = in.readParcelable(Proyecto.class.getClassLoader());
        fecha = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(proyecto, flags);
        dest.writeLong(fecha.getTime());
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

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
