package com.smartu.modelos;

import java.util.ArrayList;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class Usuario {
    private int id;
    private String nombre;
    private String apellidos;
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

    public Usuario(int id, String nombre, String apellidos, String email, String password, int nPuntos, String CIF, String localizacion, String biografia, String web, boolean verificado, boolean admin, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
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
}
