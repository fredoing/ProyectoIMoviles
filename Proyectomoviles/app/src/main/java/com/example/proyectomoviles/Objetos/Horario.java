package com.example.proyectomoviles.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Horario implements Parcelable {
    private String dia;
    private String horaInicio;
    private String horaFin;

    public Horario(String dia, String horaInicio, String horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public boolean isHorarioValido(){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date thisFechaInicio = sdf.parse(this.horaInicio);
            Date thisFechaFin = sdf.parse(this.horaFin);

            if(thisFechaInicio.equals(thisFechaFin))
                return false;
            else{
                if(thisFechaInicio.after(thisFechaFin)){
                    return false;
                }
            }
        } catch (ParseException e) {
            return false;
        }


        return true;
    }

    public boolean isHorarioAntes(Horario anotherHorario){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date thisFechaFin = sdf.parse(this.horaFin);
            Date anotherFechaInicio = sdf.parse(anotherHorario.horaInicio);

            if(thisFechaFin.before(anotherFechaInicio))
                return true;
            else
                return false;
        } catch (ParseException e) {
            return false;
        }

    }
    public boolean comprobarChoque(Horario anotherHorario){
        if(anotherHorario.getDia().equals(this.dia)){

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date thisFechaInicio = sdf.parse(this.horaInicio);
                Date thisFechaFin = sdf.parse(this.horaFin);
                Date anotherFechaInicio = sdf.parse(anotherHorario.horaInicio);
                Date anotherFechaFin = sdf.parse(anotherHorario.horaFin);

                if(thisFechaInicio.equals(anotherFechaInicio))
                    return true;
                else{
                    if(thisFechaInicio.before(anotherFechaInicio)){
                        if(thisFechaFin.equals(anotherFechaInicio) ||thisFechaFin.after(anotherFechaInicio))
                            return true;
                    }
                    else{
                        if(thisFechaInicio.equals(anotherFechaFin) ||thisFechaInicio.before(anotherFechaFin))
                            return true;
                    }
                }
            } catch (ParseException e) {
                return true;
            }

        }

        return false;

    }


    protected Horario(Parcel in) {
        dia = in.readString();
        horaInicio = in.readString();
        horaFin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dia);
        dest.writeString(horaInicio);
        dest.writeString(horaFin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Horario> CREATOR = new Creator<Horario>() {
        @Override
        public Horario createFromParcel(Parcel in) {
            return new Horario(in);
        }

        @Override
        public Horario[] newArray(int size) {
            return new Horario[size];
        }
    };

    public String getDia() {
        return dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String toString(){
        return dia+"  "+horaInicio+"  -  "+horaFin;
    }

}
