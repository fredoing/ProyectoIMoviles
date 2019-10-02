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

    public static ArrayList<Restaurante> aplicarFiltros(ArrayList<Restaurante> restaurantes,ArrayList<Filtro> filtros){
        for(Filtro filtro: filtros){
            int i = 0;
            while (i< restaurantes.size()){
                switch (filtro.getTipoFiltro()){
                    case "Nombre":
                        if(!restaurantes.get(i).getNombre().equals(filtro.getFiltro()))
                            restaurantes.remove(i);
                        else
                            i++;
                        break;

                    case "Calificación":
                        double calificacion = Double.valueOf(restaurantes.get(i).getCalificacion());
                        if(calificacion < Integer.valueOf(filtro.getFiltro()))
                            restaurantes.remove(i);
                        else
                            i++;
                        break;
                    case "Precio":
                        if(restaurantes.get(i).getPrecio().equals(filtro.getFiltro()))
                            restaurantes.remove(i);
                        else
                            i++;
                        break;
                    case "Tipo de Comida":
                        if(restaurantes.get(i).getTipoDeComida().equals(filtro.getFiltro()))
                            restaurantes.remove(i);
                        else
                            i++;
                        break;

                }
            }
        }

        return restaurantes;
    }
}
