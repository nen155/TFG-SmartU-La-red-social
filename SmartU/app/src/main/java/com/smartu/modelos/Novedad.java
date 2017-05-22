package com.smartu.modelos;

import android.os.Parcel;

import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 19/05/2017.
 */

public class Novedad extends Publicacion {
    private int id;
    private Date fecha;
    private String nombre;
    private String descripcion;
    private String detalle;
    private int idElemento;
    private String tipo;
    private String nombreElemento;

    public Novedad() {
    }

    public Novedad(int id, Date fecha, String nombre, String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }


    protected Novedad(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        detalle = in.readString();
        idElemento = in.readInt();
        tipo = in.readString();
        nombreElemento = in.readString();
    }

    public static final Creator<Novedad> CREATOR = new Creator<Novedad>() {
        @Override
        public Novedad createFromParcel(Parcel in) {
            return new Novedad(in);
        }

        @Override
        public Novedad[] newArray(int size) {
            return new Novedad[size];
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(int idElemento) {
        this.idElemento = idElemento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreElemento() {
        return nombreElemento;
    }

    public void setNombreElemento(String nombreElemento) {
        this.nombreElemento = nombreElemento;
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
        dest.writeString(detalle);
        dest.writeInt(idElemento);
        dest.writeString(tipo);
        dest.writeString(nombreElemento);
    }
}
