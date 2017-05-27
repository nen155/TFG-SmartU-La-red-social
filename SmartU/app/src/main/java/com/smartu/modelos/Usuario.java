package com.smartu.modelos;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Usuario extends Publicacion{
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
    private String imagenPerfil;
    private boolean verificado;
    private boolean admin;
    private boolean activo;
    //Contenedores para los elementos de los que es propietario el usuario
    private ArrayList<Proyecto> misProyectos;
    private ArrayList<Area> misAreasInteres;
    private ArrayList<Especialidad> misEspecialidades;
    private ArrayList<Usuario> misSeguidos;
    private ArrayList<RedSocial> misRedesSociales;
    private ArrayList<SolicitudUnion> misSolicitudes;
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

    /**
     * Mantengo sólo 6 parámetros, los más usados para mantener la eficiencia y que a la hora de construir el objeto
     * se haga de una manera eficiente
     * @param id
     * @param nombre
     * @param user
     * @param apellidos
     * @param email
     * @param imagenPerfil
     */
    public Usuario(int id, String nombre,String user, String apellidos, String email, String imagenPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.user=user;
        this.email = email;
        this.imagenPerfil = imagenPerfil;
        misAreasInteres = new ArrayList<>();
        misEspecialidades = new ArrayList<>();
        misProyectos = new ArrayList<>();
        misRedesSociales =new ArrayList<>();
        misSeguidos = new ArrayList<>();
        misSolicitudes = new ArrayList<>();
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
        imagenPerfil = in.readString();
        verificado = in.readByte() != 0;
        admin = in.readByte() != 0;
        activo = in.readByte() != 0;
        misProyectos = in.createTypedArrayList(Proyecto.CREATOR);
        misAreasInteres = in.createTypedArrayList(Area.CREATOR);
        misEspecialidades = in.createTypedArrayList(Especialidad.CREATOR);
        misSeguidos = in.createTypedArrayList(Usuario.CREATOR);
        misRedesSociales = in.createTypedArrayList(RedSocial.CREATOR);
        misSolicitudes = in.createTypedArrayList(SolicitudUnion.CREATOR);
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

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public ArrayList<Proyecto> getMisProyectos() {
        return misProyectos;
    }

    public void setMisProyectos(ArrayList<Proyecto> misProyectos) {
        this.misProyectos = misProyectos;
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

    public ArrayList<Usuario> getMisSeguidos() {
        return misSeguidos;
    }

    public void setMisSeguidos(ArrayList<Usuario> misSeguidos) {
        this.misSeguidos = misSeguidos;
    }

    public ArrayList<RedSocial> getMisRedesSociales() {
        return misRedesSociales;
    }

    public void setMisRedesSociales(ArrayList<RedSocial> misRedesSociales) {
        this.misRedesSociales = misRedesSociales;
    }

    public ArrayList<SolicitudUnion> getMisSolicitudes() {
        return misSolicitudes;
    }

    public void setMisSolicitudes(ArrayList<SolicitudUnion> misSolicitudes) {
        this.misSolicitudes = misSolicitudes;
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
        dest.writeString(user);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(nPuntos);
        dest.writeString(CIF);
        dest.writeString(localizacion);
        dest.writeString(biografia);
        dest.writeString(web);
        dest.writeString(imagenPerfil);
        dest.writeByte((byte) (verificado ? 1 : 0));
        dest.writeByte((byte) (admin ? 1 : 0));
        dest.writeByte((byte) (activo ? 1 : 0));
        dest.writeTypedList(misProyectos);
        dest.writeTypedList(misAreasInteres);
        dest.writeTypedList(misEspecialidades);
        dest.writeTypedList(misSeguidos);
        dest.writeTypedList(misRedesSociales);
        dest.writeTypedList(misSolicitudes);
        dest.writeParcelable(miStatus, flags);
    }
}
