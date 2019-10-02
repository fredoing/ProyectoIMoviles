package com.example.proyectomoviles.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectomoviles.AgregarRestaurante.MapGetUbicacionRestaurante;
import com.example.proyectomoviles.Objetos.Filtro;
import com.example.proyectomoviles.Objetos.Restaurante;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Utils.AdaptadorFiltros;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FragmentFiltros extends Fragment implements AdapterView.OnItemSelectedListener {
    //Variables
    private View view;
    private ArrayList<Restaurante> restaurantes;
    private ArrayList<Filtro> filtros;
    private Usuario usuario;
    private boolean agregoUbicacion = false;
    private LatLng ubicacion;
    //Layouts
    private RelativeLayout relativeLayoutAgregarFiltro;
    //Views
    private EditText editTextFiltro;
    private EditText editTextDistancia;
    private TextView textViewCoordenadas;
    private TextView textFiltroSpinner;
    private ListView listViewFiltros;
    //Buttons
    private Button btnAgregarUbicacion;
    private Button btnAgregarFiltro;
    private Button btnBuscar;
    //Spinners
    private Spinner spinnerTipoFiltro;
    private Spinner spinnerFiltro;

    //Codigos
    private final int codigoUbicacion = 9000;
    public FragmentFiltros(Usuario usuario) {
        this.usuario = usuario;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filtros,container,false);

        relativeLayoutAgregarFiltro = view.findViewById(R.id.relativeLayout_AgregarFiltro_EditText);
        editTextFiltro = view.findViewById(R.id.editTxt_AgregarFiltro);
        editTextDistancia = view.findViewById(R.id.editTxt_AgregarDistancia);
        textViewCoordenadas = view.findViewById(R.id.text_coordenadas);
        textFiltroSpinner = view.findViewById(R.id.txt_relativeLayout_Spinner);
        listViewFiltros = view.findViewById(R.id.listView_Filtros);
        btnAgregarUbicacion = view.findViewById(R.id.btn_buscar_AgregarUbicacion);
        btnAgregarFiltro = view.findViewById(R.id.btn_buscar_AgregarFiltro);
        btnBuscar = view.findViewById(R.id.btn_buscar_BuscarRestaurante);
        spinnerTipoFiltro = view.findViewById(R.id.spinner_TipoFiltro);
        spinnerFiltro = view.findViewById(R.id.spinner_Filtro);


        spinnerTipoFiltro.setOnItemSelectedListener(this);

        btnAgregarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapGetUbicacionRestaurante.class );
                startActivityForResult(intent,codigoUbicacion);
            }
        });

        btnAgregarFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!existeFiltro(spinnerTipoFiltro.getSelectedItem().toString())){
                    switch (spinnerTipoFiltro.getSelectedItem().toString()){
                        case "Nombre":
                            if(!editTextFiltro.getText().toString().isEmpty()){
                                filtros.add(new Filtro("Nombre",editTextFiltro.getText().toString()));

                            }
                            else
                                Toast.makeText(getContext(),"Se debe introducir un nombre",Toast.LENGTH_LONG).show();
                            break;
                        case "Calificación":
                            filtros.add(new Filtro("Calificación",spinnerFiltro.getSelectedItem().toString()));
                            break;
                        case "Precio":
                            filtros.add(new Filtro("Precio",spinnerFiltro.getSelectedItem().toString()));
                            break;
                        case "Ubicación":
                            if(agregoUbicacion){
                                if(!editTextDistancia.getText().toString().isEmpty()){
                                    filtros.add(new Filtro("Distancia",editTextDistancia.getText().toString()));
                                }
                                else
                                    Toast.makeText(getContext(),"Se debe incluir una distancia",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getContext(),"Se debe introducir un nombre",Toast.LENGTH_LONG).show();
                        case "Tipo de Comida":
                            filtros.add(new Filtro("Tipo de Comida",spinnerFiltro.getSelectedItem().toString()));



                    }

                    actualizarListaFiltros();


                }else
                    Toast.makeText(getContext(),"Ya existe un filtro con ese tipo de filtro",Toast.LENGTH_LONG).show();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListaRestaurantes(usuario)).commit();
            }
        });


        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String seleccion = parent.getItemAtPosition(i).toString();
        cambiarTipoFiltro(seleccion);

    }

    private  void cambiarTipoFiltro(String seleccion){
        switch (seleccion){
            case "Nombre":
                relativeLayoutAgregarFiltro.setVisibility(View.INVISIBLE);
                relativeLayoutAgregarFiltro = view.findViewById(R.id.relativeLayout_AgregarFiltro_EditText);
                relativeLayoutAgregarFiltro.setVisibility(View.VISIBLE);
                break;
            case "Calificación":
                relativeLayoutAgregarFiltro.setVisibility(View.GONE);
                relativeLayoutAgregarFiltro = view.findViewById(R.id.relativeLayout_AgregarFiltro_Spinner);
                textFiltroSpinner.setText("Seleccione la calificación minima");
                llenarSpinnerCalificacion();
                relativeLayoutAgregarFiltro.setVisibility(View.VISIBLE);
                break;
            case "Precio":
                relativeLayoutAgregarFiltro.setVisibility(View.INVISIBLE);
                relativeLayoutAgregarFiltro = view.findViewById(R.id.relativeLayout_AgregarFiltro_Spinner);
                textFiltroSpinner.setText("Seleccione el precio");
                llenarSpinnerPrecio();
                relativeLayoutAgregarFiltro.setVisibility(View.VISIBLE);
                break;
            case "Ubicación":
                relativeLayoutAgregarFiltro.setVisibility(View.INVISIBLE);
                relativeLayoutAgregarFiltro = view.findViewById(R.id.relativeLayout_AgregarFiltro_Ubicacion);
                relativeLayoutAgregarFiltro.setVisibility(View.VISIBLE);
                break;
            case "Tipo de Comida":
                relativeLayoutAgregarFiltro.setVisibility(View.GONE);
                relativeLayoutAgregarFiltro = view.findViewById(R.id.relativeLayout_AgregarFiltro_Spinner);
                textFiltroSpinner.setText("Seleccione el precio");
                llenarSpinnerTipoComida();
                relativeLayoutAgregarFiltro.setVisibility(View.VISIBLE);
                break;



        }
    }

    private void llenarSpinnerCalificacion(){
        List<String> list = new ArrayList<String>();

        int i = 0;
        while(i<6){
            list.add(String.valueOf(i));
            i++;
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(dataAdapter);
    }

    private void llenarSpinnerPrecio(){
        List<String> list = new ArrayList<String>();
        list.add("Barato");
        list.add("Medio");
        list.add("Caro");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(dataAdapter);
;    }

    private void llenarSpinnerTipoComida(){
        List<String> list = new ArrayList<String>();
        list.add("Mexicana");
        list.add("Casera");
        list.add("Italiana");
        list.add("Italiana");
        list.add("Rapida");
        list.add("Gourmet");
        list.add("Caribeña");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(dataAdapter);
    }

    private  boolean existeFiltro(String tipoFiltro){
        int i = 0;
        while (i < filtros.size()){
            if(filtros.get(i).getTipoFiltro().equals(tipoFiltro))
                return true;
            i++;
        }

        return false;

    }

    private void actualizarListaFiltros(){
        AdaptadorFiltros adaptadorFiltros = new AdaptadorFiltros(getContext(),filtros);

        listViewFiltros.setAdapter(adaptadorFiltros);
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == codigoUbicacion ){
            ubicacion = data.getParcelableExtra("Ubicacion");
            DecimalFormat formato = new DecimalFormat("#.00");
            textViewCoordenadas.setText("Ubicación:\n" +
                    "Latitud: "+formato.format(ubicacion.latitude)+"\n" +
                    "Longitud: "+formato.format(ubicacion.longitude));
            agregoUbicacion = true;

        }
    }
}
