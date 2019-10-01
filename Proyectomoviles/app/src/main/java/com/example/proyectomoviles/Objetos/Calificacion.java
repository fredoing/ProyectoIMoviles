package com.example.proyectomoviles.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

public class Calificacion implements Parcelable {
    private int nota;
    private String correoAutor;

    public Calificacion(int nota, String correoAutor) {
        this.nota = nota;
        this.correoAutor = correoAutor;
    }

    protected Calificacion(Parcel in) {
        nota = in.readInt();
        correoAutor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nota);
        dest.writeString(correoAutor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Calificacion> CREATOR = new Creator<Calificacion>() {
        @Override
        public Calificacion createFromParcel(Parcel in) {
            return new Calificacion(in);
        }

        @Override
        public Calificacion[] newArray(int size) {
            return new Calificacion[size];
        }
    };

    public int getNota() {
        return nota;
    }

    public String getCorreoAutor() {
        return correoAutor;
    }
}
