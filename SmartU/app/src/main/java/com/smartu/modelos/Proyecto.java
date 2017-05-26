package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Proyecto implements Parcelable {
    private int id;
    private String nombre;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaFinalizacion;
    private String imagenDestacada;
    private String localizacion;
    private String coordenadas;
    private String web;
    private Usuario propietario =null;
    //Contenedores de los que es propietario
    private ArrayList<BuenaIdea> buenaIdea;
    private ArrayList<Comentario> misComentarios=null;
    private ArrayList<Area> misAreas=null;
    private ArrayList<Vacante> vacantesProyecto =null;
    private ArrayList<Multimedia> misArchivos=null;
    private ArrayList<RedSocial> misRedesSociales=null;
    private ArrayList<Hashtag> misHashtag=null;
    private ArrayList<Avance> misAvances=null;
    private ArrayList<Usuario> integrantes=null;

    public Proyecto(){
        misComentarios =new ArrayList<>();
        misAreas =new ArrayList<>();
        vacantesProyecto =new ArrayList<>();
        misRedesSociales = new ArrayList<>();
        misArchivos =new ArrayList<>();
        misHashtag =new ArrayList<>();
        misAvances =new ArrayList<>();
        buenaIdea = new ArrayList<>();
        integrantes = new ArrayList<>();
    }
    public Proyecto(int id, String nombre, String descripcion, Date fechaCreacion, String imagenDestacada, Date fechaFinalizacion, String localizacion, String web, Usuario propietario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenDestacada =imagenDestacada;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.localizacion = localizacion;
        this.web = web;
        this.propietario = propietario;
        misComentarios =new ArrayList<>();
        misAreas =new ArrayList<>();
        vacantesProyecto =new ArrayList<>();
        misArchivos =new ArrayList<>();
        misHashtag =new ArrayList<>();
        misAvances =new ArrayList<>();
        buenaIdea = new ArrayList<>();
        integrantes = new ArrayList<>();
    }


    protected Proyecto(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        imagenDestacada = in.readString();
        localizacion = in.readString();
        coordenadas = in.readString();
        web = in.readString();
        propietario = in.readParcelable(Usuario.class.getClassLoader());
        buenaIdea = in.createTypedArrayList(BuenaIdea.CREATOR);
        misComentarios = in.createTypedArrayList(Comentario.CREATOR);
        misAreas = in.createTypedArrayList(Area.CREATOR);
        misArchivos = in.createTypedArrayList(Multimedia.CREATOR);
        misRedesSociales = in.createTypedArrayList(RedSocial.CREATOR);
        misHashtag = in.createTypedArrayList(Hashtag.CREATOR);
        misAvances = in.createTypedArrayList(Avance.CREATOR);
        integrantes = in.createTypedArrayList(Usuario.CREATOR);
        vacantesProyecto = in.createTypedArrayList(Vacante.CREATOR);
        fechaCreacion = new Date(in.readLong());
        fechaFinalizacion = new Date(in.readLong());
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

    public ArrayList<BuenaIdea> getBuenaIdea() {
        return buenaIdea;
    }

    public void setBuenaIdea(ArrayList<BuenaIdea> buenaIdea) {
        this.buenaIdea = buenaIdea;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public String getImagenDestacada() {
        return imagenDestacada;
    }

    public void setImagenDestacada(String imagenDestacada) {
        this.imagenDestacada = imagenDestacada;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
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

    public ArrayList<Vacante> getVacantesProyecto() {
        return vacantesProyecto;
    }

    public void setVacantesProyecto(ArrayList<Vacante> vacantesProyecto) {
        this.vacantesProyecto = vacantesProyecto;
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

    public ArrayList<Usuario> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(ArrayList<Usuario> integrantes) {
        this.integrantes = integrantes;
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
        dest.writeString(imagenDestacada);
        dest.writeString(localizacion);
        dest.writeString(coordenadas);
        dest.writeString(web);
        dest.writeParcelable(propietario, flags);
        dest.writeTypedList(buenaIdea);
        dest.writeTypedList(misComentarios);
        dest.writeTypedList(misAreas);
        dest.writeTypedList(misArchivos);
        dest.writeTypedList(misRedesSociales);
        dest.writeTypedList(misHashtag);
        dest.writeTypedList(misAvances);
        dest.writeTypedList(integrantes);
        dest.writeTypedList(vacantesProyecto);
        dest.writeLong(fechaCreacion.getTime());
        dest.writeLong(fechaFinalizacion.getTime());
    }
}
