package com.example.proyectomoviles.Objetos;

import java.util.ArrayList;

public class Filtro {
    private String tipoFiltro;
    private String filtro;


    public Filtro(String tipoFiltro, String filtro) {
        this.tipoFiltro = tipoFiltro;
        this.filtro = filtro;
    }

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public String getFiltro() {
        return filtro;
    }


}
