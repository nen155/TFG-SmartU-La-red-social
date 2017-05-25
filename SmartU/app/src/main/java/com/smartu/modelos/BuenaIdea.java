package com.smartu.modelos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Emilio Chica Jiménez on 25/05/2017.
 */

public class BuenaIdea implements Parcelable{
    private int id;
    private int idUsuario;
    private int idProyecto;

    public BuenaIdea(int id, int idUsuario, int idProyecto) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idProyecto = idProyecto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    protected BuenaIdea(Parcel in) {
        id = in.readInt();
        idUsuario = in.readInt();
        idProyecto = in.readInt();
    }

    public static final Creator<BuenaIdea> CREATOR = new Creator<BuenaIdea>() {
        @Override
        public BuenaIdea createFromParcel(Parcel in) {
            return new BuenaIdea(in);
        }

        @Override
        public BuenaIdea[] newArray(int size) {
            return new BuenaIdea[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idUsuario);
        dest.writeInt(idProyecto);
    }
}
