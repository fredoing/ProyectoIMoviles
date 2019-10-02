package com.example.proyectomoviles.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectomoviles.Objetos.Restaurante;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Utils.Connector;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FragmentRestaurantesCercanos extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private Usuario usuario;
    private GoogleMap myMap;
    public static JSONArray jsonArray;
    private ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();

    public FragmentRestaurantesCercanos(Usuario usuario) {
        this.usuario = usuario;
    }

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurantes_cercanos,container,false);
        mMapView = (MapView) view.findViewById(R.id.map_restaurantes_cercanos);

        String[] comandos = {"Obtener todos los restaurantes MapView"};
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

        iniciarlizarMapa(savedInstanceState);




        return view;
    }

    private  void iniciarlizarMapa(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        for(Restaurante restaurante: restaurantes){
            map.addMarker(new MarkerOptions().position(restaurante.getUbicacion()).title(restaurante.getNombre()));
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void cargarDatosJSON() {
        int i = 0;
        Restaurante restaurante;
        while (jsonArray.length() > i) {
            try {
                JSONObject object = (JSONObject) jsonArray.get(i);
                String nombre = object.getString("nombre");
                Double latitud = object.getDouble("latitud");
                Double longitud = object.getDouble("longitud");
                String contacto = object.getString("contacto");
                String horario = object.getString("horario");
                String precio = object.getString("precio");
                double califcacion = object.getDouble("calificacion");
                String tipoComida = object.getString("tipocom");
                int id = object.getInt("id");


                restaurante = new Restaurante(nombre, new LatLng(latitud, longitud), tipoComida, contacto, precio, horario, new ArrayList<String>(), califcacion, id);
                restaurantes.add(restaurante);
                i++;
            } catch (JSONException e) {
                Log.i("Resultados", e.toString());
                i = jsonArray.length();
            }

        }
    }

}
