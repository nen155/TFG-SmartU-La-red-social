package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Emilio Chica Jiménez on 18/05/2017.
 */

public class RedSocial implements Parcelable,Serializable {
    private int id;
    private String nombre;
    private String url;

    public RedSocial(){

    }
    public RedSocial(int id, String nombre, String url) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected RedSocial(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        url = in.readString();
    }

    public static final Creator<RedSocial> CREATOR = new Creator<RedSocial>() {
        @Override
        public RedSocial createFromParcel(Parcel in) {
            return new RedSocial(in);
        }

        @Override
        public RedSocial[] newArray(int size) {
            return new RedSocial[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(url);
    }
}
