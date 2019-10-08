package com.example.proyectomoviles.Fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectomoviles.Objetos.Restaurante;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Utils.Connector;
import com.example.proyectomoviles.VerRestaurante.VistaRestaurante;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FragmentRestaurantesCercanos extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mMapView;
    private Usuario usuario;
    private GoogleMap myMap;
    public static JSONArray jsonArray;
    private ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
    private FusedLocationProviderClient fusedLocationClient;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());




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
    public void onMapReady(final GoogleMap map) {
        map.setOnMarkerClickListener(this);
        for(Restaurante restaurante: restaurantes){

            Marker marker = map.addMarker(new MarkerOptions().position(restaurante.getUbicacion()).title(restaurante.getNombre()));
            marker.setTag(restaurante);
        }
        map.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            LatLng posActual = new LatLng(location.getLatitude(),  location.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(posActual,17));
                        }
                        else{
                            Toast.makeText(getContext(),"GPS desactivado",Toast.LENGTH_LONG).show();
                        }
                    }
                });


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
                precio = precio.toUpperCase().charAt(0)+precio.substring(1);
                double califcacion = object.getDouble("calificacion");
                String tipoComida = object.getString("tipocom");
                tipoComida = tipoComida.toUpperCase().charAt(0)+tipoComida.substring(1);
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        Restaurante restaurante =(Restaurante) marker.getTag();
        mostrarRestaurante(restaurante);


        return false;
    }

    private void mostrarRestaurante(Restaurante restaurante){
        Intent intent = new Intent(getContext(), VistaRestaurante.class);
        intent.putExtra("Restaurante",restaurante);
        intent.putExtra("Usuario",usuario);
        startActivity(intent);
    }
}
