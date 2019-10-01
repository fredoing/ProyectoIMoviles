package com.example.proyectomoviles.Fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.proyectomoviles.VerRestaurante.VistaRestaurante;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class FragmentListaRestaurantes extends Fragment {
    private View view;
    private ListView listViewRestaurantes;
    private ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
    private Usuario usuario;

    public FragmentListaRestaurantes(Usuario usuario) {
        this.usuario = usuario;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista_restaurantes,container,false);

        listViewRestaurantes = view.findViewById(R.id.listView_listaRestaurantes);


        cargarDatos();
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

    public void cargarDatos(){
        ArrayList<String> imagenes = new ArrayList<String>();
        ArrayList<Horario> horarios = new ArrayList<Horario>();
        horarios.add(new Horario("Lunes","00:00","07:00"));
        horarios.add(new Horario("Viernes","00:00","07:00"));

        Restaurante restaurante = new Restaurante("Frijolitos",new LatLng(9.905744225339994,-84.01266675442456),"Frijoles","7070707","andylfoster@outlook.es","Bajo",horarios,imagenes);

        restaurantes.add(restaurante);
        restaurantes.add(restaurante);
        restaurantes.add(restaurante);
        restaurantes.add(restaurante);
        restaurantes.add(restaurante);
        restaurantes.add(restaurante);
        restaurantes.add(restaurante);
        restaurantes.add(restaurante);

    }

    private void mostrarRestaurante(Restaurante restaurante){
        Intent intent = new Intent(getContext(), VistaRestaurante.class);
        intent.putExtra("Restaurante",restaurante);
        intent.putExtra("Usuario",usuario);
        startActivity(intent);
    }

}
