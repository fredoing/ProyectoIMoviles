package com.example.proyectomoviles.AgregarRestaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectomoviles.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapGetUbicacionRestaurante extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Button btnAtras;
    private Button btnVerificar;
    private GoogleMap mMap;
    private ImageView imgMarker;
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_get_ubicacion_restaurante);

        mMapView = (MapView) findViewById(R.id.map_get_ubicacion);
        btnAtras = findViewById(R.id.btn_volverGetUbicaion);
        btnVerificar = findViewById(R.id.btn_SeleccionarUbicacion);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        imgMarker = findViewById(R.id.img_marker);

        iniciarlizarMapa(savedInstanceState);
        inicializarBotonAtras();
        inicializarBotonSeleccionar();

    }

    private  void inicializarBotonAtras(){
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void inicializarBotonSeleccionar(){

        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CameraPosition cameraPosition = mMap.getCameraPosition();
                LatLng latLng = cameraPosition.target;

                Intent intent = new Intent();

                intent.putExtra("Ubicacion",(Parcelable) latLng);
                setResult(RESULT_OK,intent);
                finish();

            }
        });

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
        mMap = map;
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            LatLng posActual = new LatLng(location.getLatitude(),  location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posActual,17));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"GPS desactivado",Toast.LENGTH_LONG).show();
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
}
