package com.example.proyectomoviles.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectomoviles.Objetos.Horario;
import com.example.proyectomoviles.Objetos.Restaurante;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Utils.AdaptadorListaRestaurantes;
import com.example.proyectomoviles.Utils.Connector;
import com.example.proyectomoviles.VerRestaurante.VistaRestaurante;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FragmentListaRestaurantes extends Fragment {
    private View view;
    private ListView listViewRestaurantes;
    private ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
    private Usuario usuario;
    public static JSONArray jsonArray;

    public FragmentListaRestaurantes(ArrayList<Restaurante> restaurantes, Usuario usuario) {
        this.restaurantes = restaurantes;
        this.usuario = usuario;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista_restaurantes,container,false);

        listViewRestaurantes = view.findViewById(R.id.listView_listaRestaurantes);

        if(restaurantes.isEmpty()){
            String[] comandos = {"Obtener todos los restaurantes ListView"};
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
            cargarDatosJSON();

        }

        inicializarListViewRestaurantes();

        return view;
    }

    public void inicializarListViewRestaurantes(){
        AdaptadorListaRestaurantes adaptadorListaRestaurantes = new AdaptadorListaRestaurantes(getContext(),restaurantes);
        listViewRestaurantes.setAdapter(adaptadorListaRestaurantes);

        listViewRestaurantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarRestaurante(restaurantes.get(i));
            }
        });
    }

    public void cargarDatosJSON(){
        int i = 0;
        Restaurante restaurante;
       while(jsonArray.length() > i){
           try {
               JSONObject object = (JSONObject) jsonArray.get(i);
               String nombre = object.getString("nombre");
               Double latitud =object.getDouble("latitud");
               Double longitud =object.getDouble("longitud");
               String contacto = object.getString("contacto");
               String horario = object.getString("horario");
               String precio = object.getString("precio");
               precio = precio.toUpperCase().charAt(0)+precio.substring(1);
               double califcacion = object.getDouble("calificacion");
               String tipoComida = object.getString("tipocom");
               tipoComida = tipoComida.toUpperCase().charAt(0)+tipoComida.substring(1);
               int id = object.getInt("id");


               restaurante = new Restaurante(nombre,new LatLng(latitud,longitud),tipoComida,contacto,precio,horario,new ArrayList<String>(),califcacion,id);
               restaurantes.add(restaurante);
               i++;
           } catch (JSONException e) {
               Log.i("Resultados",e.toString());
               i = jsonArray.length();
           }

       }

    }

    private void mostrarRestaurante(Restaurante restaurante){
        Intent intent = new Intent(getContext(), VistaRestaurante.class);
        intent.putExtra("Restaurante",restaurante);
        intent.putExtra("Usuario",usuario);
        startActivity(intent);
    }

}
