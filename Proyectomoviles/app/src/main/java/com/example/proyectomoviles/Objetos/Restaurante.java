package com.example.proyectomoviles.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.proyectomoviles.Utils.Connector;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Restaurante implements Parcelable {
    private String nombre;
    private LatLng ubicacion;
    private String tipoDeComida;
    private String contacto;
    private String precio;
    private String horario;
    private Double calificacion;
    private ArrayList<Horario> horarios;
    private ArrayList<String> imagenesURL;
    private ArrayList<Comentario> comentarios;
    private ArrayList<Calificacion> calificaciones;
    private int id;
    public static boolean resultado;
    public static int userID;
    public static JSONArray jsonArray;
    public static Double nuevaCalificacion = -1.0;

    public Restaurante(String nombre, LatLng ubicacion, String tipoDeComida,String contacto, String precio, String horario, ArrayList<String> imagenesURL,Double calificacion,int id) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tipoDeComida = tipoDeComida;
        this.precio = precio;
        this.contacto = contacto;
        this.horario = horario;
        this.horarios = new ArrayList<Horario>();
        this.imagenesURL = imagenesURL;
        this.comentarios = new ArrayList<Comentario>();
        this.calificaciones = new ArrayList<Calificacion>();
        this.calificacion = calificacion;
        this.id = id;
    }


    protected Restaurante(Parcel in) {
        nombre = in.readString();
        ubicacion = in.readParcelable(LatLng.class.getClassLoader());
        tipoDeComida = in.readString();
        precio = in.readString();
        contacto = in.readString();
        horario = in.readString();
        horarios = in.createTypedArrayList(Horario.CREATOR);
        imagenesURL = in.createStringArrayList();
        comentarios = in.createTypedArrayList(Comentario.CREATOR);
        calificaciones = in.createTypedArrayList(Calificacion.CREATOR);
        calificacion = in.readDouble();
        id = in.readInt();
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
    
    public String agregarCalificacion(int nuevaCalificacion, String correoAutor){
        String[] comandosGetUserId = {"Obtener Id Usuario",correoAutor};
        Connector connectorGetUserId = new Connector(comandosGetUserId);
        connectorGetUserId.execute();
        try {
            connectorGetUserId.get(20, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        if(userID != -1) {
            String[] comandosCalficacion = {"Calificar Restaurnate", String.valueOf(this.id),String.valueOf(userID),String.valueOf(nuevaCalificacion)};
            Connector connector = new Connector(comandosCalficacion);
            connector.execute();
            try {
                connector.get(20, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

       /* String[] comandos= {"Obtener Calificación",String.valueOf(id)};
        Connector connector = new Connector(comandos);
        connector.execute();
        try {
            connector.get(20, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        if(nuevaCalificacion ==0){
            return "0.0";
        }
        DecimalFormat formato = new DecimalFormat("#.0");
        return formato.format(nuevaCalificacion);*/


        return "";
    }
    public void cargarComentarios(){
        String[] comandos= {"Obtener Comentarios Restaurante",String.valueOf(id)};
        Connector connectorGetUserId = new Connector(comandos);
        connectorGetUserId.execute();
        try {
            connectorGetUserId.get(20, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        int i = 0;
        Comentario comentario;
        while(i<jsonArray.length()){
            try {
                JSONObject object = (JSONObject) jsonArray.get(i);

                String autorComentario = object.getString("nombre");
                String textoComentario = object.getString("comentario");

                comentario = new Comentario(textoComentario,autorComentario);

                comentarios.add(comentario);
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
                i = jsonArray.length();
            }

        }

    }
    public boolean agregarComentario(String nuevoComentario,String correoAutor){
        String[] comandosGetUserId = {"Obtener Id Usuario",correoAutor};
        Connector connectorGetUserId = new Connector(comandosGetUserId);
        connectorGetUserId.execute();
        try {
            connectorGetUserId.get(20, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        if(userID != -1) {
            String[] comandosComentario = {"Comentar", String.valueOf(this.id),String.valueOf(userID),nuevoComentario};
            Connector connector = new Connector(comandosComentario);
            connector.execute();
            try {
                connector.get(20, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            comentarios.add(new Comentario(nuevoComentario,correoAutor));
        }
        else
            return false;

        return resultado;
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

    public String getContacto() {
        return contacto;
    }

    public String getHorario() {
        return horario;
    }

    public ArrayList<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public String getCalificacion(){
        if(calificacion ==0){
            return "0.0";
        }
        DecimalFormat formato = new DecimalFormat("#.0");
        return formato.format(calificacion);
    }

    public int getId() {
        return id;
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
        parcel.writeString(precio);
        parcel.writeString(contacto);
        parcel.writeString(horario);
        parcel.writeTypedList(horarios);
        parcel.writeStringList(imagenesURL);
        parcel.writeTypedList(comentarios);
        parcel.writeTypedList(calificaciones);
        parcel.writeDouble(calificacion);
        parcel.writeInt(id);
    }
}
