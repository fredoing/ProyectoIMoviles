package com.example.proyectomoviles.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

public class Comentario implements Parcelable {
    private String comentario;
    private String correoAutor;

    public Comentario(String comentario, String correoAutor) {
        this.comentario = comentario;
        this.correoAutor = correoAutor;
    }

    protected Comentario(Parcel in) {
        comentario = in.readString();
        correoAutor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comentario);
        dest.writeString(correoAutor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comentario> CREATOR = new Creator<Comentario>() {
        @Override
        public Comentario createFromParcel(Parcel in) {
            return new Comentario(in);
        }

        @Override
        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };

    public String getComentario() {
        return comentario;
    }

    public String getAutor() {
        return correoAutor;
    }
}
