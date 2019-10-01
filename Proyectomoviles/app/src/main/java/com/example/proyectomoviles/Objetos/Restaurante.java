package com.example.proyectomoviles.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Restaurante implements Parcelable {
    private String nombre;
    private LatLng ubicacion;
    private String tipoDeComida;
    private String telefono;
    private String correo;
    private String precio;
    private ArrayList<Horario> horarios;
    private ArrayList<String> imagenesURL;
    private ArrayList<Comentario> comentarios;
    private ArrayList<Calificacion> calificaciones;


    public Restaurante(String nombre, LatLng ubicacion, String tipoDeComida,String telefono,String correo, String precio, ArrayList<Horario> horarios, ArrayList<String> imagenesURL) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tipoDeComida = tipoDeComida;
        this.telefono = telefono;
        this.correo = correo;
        this.precio = precio;
        this.horarios = horarios;
        this.imagenesURL = imagenesURL;
        this.comentarios = new ArrayList<Comentario>();
        this.calificaciones = new ArrayList<Calificacion>();
    }


    protected Restaurante(Parcel in) {
        nombre = in.readString();
        ubicacion = in.readParcelable(LatLng.class.getClassLoader());
        tipoDeComida = in.readString();
        telefono = in.readString();
        correo = in.readString();
        precio = in.readString();
        horarios = in.createTypedArrayList(Horario.CREATOR);
        imagenesURL = in.createStringArrayList();
        comentarios = in.createTypedArrayList(Comentario.CREATOR);
        calificaciones = in.createTypedArrayList(Calificacion.CREATOR);
    }

    public static final Creator<Restaurante> CREATOR = new Creator<Restaurante>() {
        @Override
        public Restaurante createFromParcel(Parcel in) {
            return new Restaurante(in);
        }

        @Override
        public Restaurante[] newArray(int size) {
            return new Restaurante[size];
        }
    };

    public void agregarImagen(String imagenUrl){
        imagenesURL.add(imagenUrl);
    }
    
    public void agregarCalificacion(int nuevaCalificacion, String correoAutor){
        int i = 0;
        while(i < calificaciones.size()){
            Calificacion calificacion = calificaciones.get(i);
            
            if(calificacion.getCorreoAutor().equals(correoAutor)){
                calificaciones.remove(i);
                i = calificaciones.size();
            }
            
            i++;
        }

        calificaciones.add(new Calificacion(nuevaCalificacion,correoAutor));
    }

    public void agregarComentario(String nuevoComentario,String correoAutor){
        comentarios.add(new Comentario(nuevoComentario,correoAutor));
    }



    public String getNombre() {
        return nombre;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public String getTipoDeComida() {
        return tipoDeComida;
    }

    public String getPrecio() {
        return precio;
    }

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public ArrayList<String> getImagenesURL() {
        return imagenesURL;
    }

    public ArrayList<Comentario> getComentarios() {
        return comentarios;
    }

    public ArrayList<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public String getCalificacion(){
        if(!this.calificaciones.isEmpty()){
            Double calificacionActual = 0.0;
            int i = 0;
            while(i< calificaciones.size()){
                calificacionActual += calificaciones.get(i).getNota();
                i++;
            }
            if(calificacionActual == 0)
                return "0.0";
            else {
                DecimalFormat formato = new DecimalFormat("#.0");

                calificacionActual = calificacionActual / calificaciones.size();

                return formato.format(calificacionActual);
            }


        }else
            return "0.0";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeParcelable(ubicacion, i);
        parcel.writeString(tipoDeComida);
        parcel.writeString(telefono);
        parcel.writeString(correo);
        parcel.writeString(precio);
        parcel.writeTypedList(horarios);
        parcel.writeStringList(imagenesURL);
        parcel.writeTypedList(comentarios);
        parcel.writeTypedList(calificaciones);
    }
}
