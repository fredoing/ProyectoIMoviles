package com.example.proyectomoviles.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    private String nombre;
    private String contrasena;
    private String correo;
    private boolean isFromFacebook;

    public Usuario(String nombre, String contrasena, String correo,boolean isFromFacebook) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.correo = correo;
        this.isFromFacebook = isFromFacebook;
    }



    protected Usuario(Parcel in) {
        nombre = in.readString();
        contrasena = in.readString();
        correo = in.readString();
        isFromFacebook = in.readByte() != 0;
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

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isFromFacebook() {
        return isFromFacebook;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setFromFacebook(boolean fromFacebook) {
        isFromFacebook = fromFacebook;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(contrasena);
        parcel.writeString(correo);
        parcel.writeByte((byte) (isFromFacebook ? 1 : 0));
    }
}
