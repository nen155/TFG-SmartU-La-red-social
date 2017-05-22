package com.smartu.modelos;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Proyecto extends Publicacion {
    private int id;
    private String nombre;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaFinalizacion;
    private String localizacion;
    private int buenaIdea;
    private String web;
    private Usuario usuario;
    //Contenedores de los que es propietario
    private ArrayList<Comentario> misComentarios;
    private ArrayList<Area> misAreas;
    private ArrayList<Especialidad> especialidadesNecesarias;
    private ArrayList<Multimedia> misArchivos;
    private ArrayList<RedSocial> misRedesSociales;
    private ArrayList<Hashtag> misHashtag;
    private ArrayList<Avance> misAvances;

    public Proyecto(){

    }
    public Proyecto(int id, String nombre, String descripcion, Date fechaCreacion, Date fechaFinalizacion, String localizacion, int buenaIdea, String web,Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.localizacion = localizacion;
        this.buenaIdea = buenaIdea;
        this.web = web;
        this.usuario = usuario;
    }

    protected Proyecto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        localizacion = in.readString();
        buenaIdea = in.readInt();
        web = in.readString();
        fechaCreacion = new Date(in.readLong());
        fechaFinalizacion = new Date(in.readLong());
        usuario = in.readParcelable(Usuario.class.getClassLoader());
        misComentarios = in.createTypedArrayList(Comentario.CREATOR);
        misAreas = in.createTypedArrayList(Area.CREATOR);
        especialidadesNecesarias = in.createTypedArrayList(Especialidad.CREATOR);
        misArchivos = in.createTypedArrayList(Multimedia.CREATOR);
        misRedesSociales = in.createTypedArrayList(RedSocial.CREATOR);
        misHashtag = in.createTypedArrayList(Hashtag.CREATOR);
        misAvances = in.createTypedArrayList(Avance.CREATOR);
    }

    public static final Creator<Proyecto> CREATOR = new Creator<Proyecto>() {
        @Override
        public Proyecto createFromParcel(Parcel in) {
            return new Proyecto(in);
        }

        @Override
        public Proyecto[] newArray(int size) {
            return new Proyecto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public int getBuenaIdea() {
        return buenaIdea;
    }

    public void setBuenaIdea(int buenaIdea) {
        this.buenaIdea = buenaIdea;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Comentario> getMisComentarios() {
        return misComentarios;
    }

    public void setMisComentarios(ArrayList<Comentario> misComentarios) {
        this.misComentarios = misComentarios;
    }

    public ArrayList<Area> getMisAreas() {
        return misAreas;
    }

    public void setMisAreas(ArrayList<Area> misAreas) {
        this.misAreas = misAreas;
    }

    public ArrayList<Especialidad> getEspecialidadesNecesarias() {
        return especialidadesNecesarias;
    }

    public void setEspecialidadesNecesarias(ArrayList<Especialidad> especialidadesNecesarias) {
        this.especialidadesNecesarias = especialidadesNecesarias;
    }

    public ArrayList<Multimedia> getMisArchivos() {
        return misArchivos;
    }

    public void setMisArchivos(ArrayList<Multimedia> misArchivos) {
        this.misArchivos = misArchivos;
    }

    public ArrayList<RedSocial> getMisRedesSociales() {
        return misRedesSociales;
    }

    public void setMisRedesSociales(ArrayList<RedSocial> misRedesSociales) {
        this.misRedesSociales = misRedesSociales;
    }

    public ArrayList<Hashtag> getMisHashtag() {
        return misHashtag;
    }

    public void setMisHashtag(ArrayList<Hashtag> misHashtag) {
        this.misHashtag = misHashtag;
    }

    public ArrayList<Avance> getMisAvances() {
        return misAvances;
    }

    public void setMisAvances(ArrayList<Avance> misAvances) {
        this.misAvances = misAvances;
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
        dest.writeString(localizacion);
        dest.writeInt(buenaIdea);
        dest.writeString(web);
        dest.writeLong(fechaCreacion.getTime());
        dest.writeLong(fechaFinalizacion.getTime());
        dest.writeParcelable(usuario, flags);
        dest.writeTypedList(misComentarios);
        dest.writeTypedList(misAreas);
        dest.writeTypedList(especialidadesNecesarias);
        dest.writeTypedList(misArchivos);
        dest.writeTypedList(misRedesSociales);
        dest.writeTypedList(misHashtag);
        dest.writeTypedList(misAvances);
    }
}
