package com.smartu.modelos;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Usuario extends Publicacion {
    private int id;
    private String nombre;
    private String apellidos;
    private String user;
    private String email;
    private String password;
    private int nPuntos;
    private String CIF;
    private String localizacion;
    private String biografia;
    private String web;
    private boolean verificado;
    private boolean admin;
    private boolean activo;
    //Contenedores para los elementos de los que es propietario el usuario
    private ArrayList<Proyecto> misProyectos;
    private ArrayList<Proyecto> misColaboraciones;
    private ArrayList<Area> misAreasInteres;
    private ArrayList<Especialidad> misEspecialidades;
    private ArrayList<Usuario> misSeguidores;
    private ArrayList<RedSocial> misRedesSociales;
    private ArrayList<Multimedia> misArchivos;
    private Status miStatus;



    public Usuario(){

    }

    /**
     * Util para el inicio de sesión
     * @param email
     * @param password
     */
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Usuario(int id, String nombre,String user, String apellidos, String email, String password, int nPuntos, String CIF, String localizacion, String biografia, String web, boolean verificado, boolean admin, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.user=user;
        this.email = email;
        this.password = password;
        this.nPuntos = nPuntos;
        this.CIF = CIF;
        this.localizacion = localizacion;
        this.biografia = biografia;
        this.web = web;
        this.verificado = verificado;
        this.admin = admin;
        this.activo = activo;
    }

    protected Usuario(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        apellidos = in.readString();
        user = in.readString();
        email = in.readString();
        password = in.readString();
        nPuntos = in.readInt();
        CIF = in.readString();
        localizacion = in.readString();
        biografia = in.readString();
        web = in.readString();
        verificado = in.readByte() != 0;
        admin = in.readByte() != 0;
        activo = in.readByte() != 0;
        misProyectos = in.createTypedArrayList(Proyecto.CREATOR);
        misColaboraciones = in.createTypedArrayList(Proyecto.CREATOR);
        misAreasInteres = in.createTypedArrayList(Area.CREATOR);
        misEspecialidades = in.createTypedArrayList(Especialidad.CREATOR);
        misSeguidores = in.createTypedArrayList(Usuario.CREATOR);
        misRedesSociales = in.createTypedArrayList(RedSocial.CREATOR);
        misArchivos = in.createTypedArrayList(Multimedia.CREATOR);
        miStatus = in.readParcelable(Status.class.getClassLoader());
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getnPuntos() {
        return nPuntos;
    }

    public void setnPuntos(int nPuntos) {
        this.nPuntos = nPuntos;
    }

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public ArrayList<Proyecto> getMisProyectos() {
        return misProyectos;
    }

    public void setMisProyectos(ArrayList<Proyecto> misProyectos) {
        this.misProyectos = misProyectos;
    }

    public ArrayList<Proyecto> getMisColaboraciones() {
        return misColaboraciones;
    }

    public void setMisColaboraciones(ArrayList<Proyecto> misColaboraciones) {
        this.misColaboraciones = misColaboraciones;
    }

    public ArrayList<Area> getMisAreasInteres() {
        return misAreasInteres;
    }

    public void setMisAreasInteres(ArrayList<Area> misAreasInteres) {
        this.misAreasInteres = misAreasInteres;
    }

    public ArrayList<Especialidad> getMisEspecialidades() {
        return misEspecialidades;
    }

    public void setMisEspecialidades(ArrayList<Especialidad> misEspecialidades) {
        this.misEspecialidades = misEspecialidades;
    }

    public ArrayList<Usuario> getMisSeguidores() {
        return misSeguidores;
    }

    public void setMisSeguidores(ArrayList<Usuario> misSeguidores) {
        this.misSeguidores = misSeguidores;
    }

    public ArrayList<RedSocial> getMisRedesSociales() {
        return misRedesSociales;
    }

    public void setMisRedesSociales(ArrayList<RedSocial> misRedesSociales) {
        this.misRedesSociales = misRedesSociales;
    }

    public ArrayList<Multimedia> getMisArchivos() {
        return misArchivos;
    }

    public void setMisArchivos(ArrayList<Multimedia> misArchivos) {
        this.misArchivos = misArchivos;
    }

    public Status getMiStatus() {
        return miStatus;
    }

    public void setMiStatus(Status miStatus) {
        this.miStatus = miStatus;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(nPuntos);
        dest.writeString(CIF);
        dest.writeString(localizacion);
        dest.writeString(biografia);
        dest.writeString(web);
        dest.writeByte((byte) (verificado ? 1 : 0));
        dest.writeByte((byte) (admin ? 1 : 0));
        dest.writeByte((byte) (activo ? 1 : 0));
        dest.writeTypedList(misProyectos);
        dest.writeTypedList(misColaboraciones);
        dest.writeTypedList(misAreasInteres);
        dest.writeTypedList(misEspecialidades);
        dest.writeTypedList(misSeguidores);
        dest.writeTypedList(misRedesSociales);
        dest.writeTypedList(misArchivos);
        dest.writeParcelable(miStatus, flags);
    }
}
