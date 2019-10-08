package com.example.proyectomoviles.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.proyectomoviles.Utils.Connector;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.app.Activity.RESULT_OK;

public class FragmentFiltros extends Fragment implements AdapterView.OnItemSelectedListener {
    //Variables
    private View view;
    private ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
    private ArrayList<Filtro> filtros = new ArrayList<Filtro>();
    private Usuario usuario;
    private boolean agregoUbicacion = false;
    private LatLng ubicacion;
    public static JSONArray jsonArray;
    public int distancia;
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


        inicializarBtnAgregarFiltro();
        inicializarBtnAgregarUbicacion();
        inicializarBtnBuscar();




        registerForContextMenu(listViewFiltros);
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
                relativeLayoutAgregarFiltro.setVisibility(View.INVISIBLE);
                relativeLayoutAgregarFiltro = view.findViewById(R.id.relativeLayout_AgregarFiltro_Spinner);
                textFiltroSpinner.setText("Seleccione el tipo de comida");
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

        }
    }


    private void inicializarBtnAgregarUbicacion(){
        btnAgregarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapGetUbicacionRestaurante.class );
                startActivityForResult(intent,codigoUbicacion);
            }
        });
    }

    private void inicializarBtnAgregarFiltro(){
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
                            if(!textViewCoordenadas.getText().toString().isEmpty()){
                                if(!editTextDistancia.getText().toString().isEmpty()){
                                    filtros.add(new Filtro("Distancia",editTextDistancia.getText().toString()));
                                    distancia = Integer.valueOf(editTextDistancia.getText().toString());
                                    agregoUbicacion = true;
                                }
                                else
                                    Toast.makeText(getContext(),"Se debe incluir una distancia",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getContext(),"Se debe introducir una ubicación",Toast.LENGTH_LONG).show();
                            break;
                        case "Tipo de Comida":
                            filtros.add(new Filtro("Tipo de Comida",spinnerFiltro.getSelectedItem().toString()));
                            break;


                    }

                    actualizarListaFiltros();


                }else
                    Toast.makeText(getContext(),"Ya existe un filtro con ese tipo de filtro",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void inicializarBtnBuscar(){
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obtenerRestautantesBase();
                aplicarFiltros();
                if(!restaurantes.isEmpty())
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListaRestaurantes(restaurantes,usuario)).commit();
                else
                    Toast.makeText(getContext(),"No se encontro ningun restaurante",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void obtenerRestautantesBase(){
        String[] comandos = new String[4];
        comandos[0] = "Obtener todos los restaurantes Filtro";
        if(agregoUbicacion) {
            comandos[1] = String.valueOf(distancia);
            comandos[2] = String.valueOf(ubicacion.latitude);
            comandos[3] = String.valueOf(ubicacion.longitude);
       }
       else {
            comandos[1] = "1000000";
            comandos[2] = "0";
            comandos[3] = "0";
       }

       Log.i("Resultados",comandos[1]+"\n"+comandos[2]+"\n"+comandos[3]);
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


    private void aplicarFiltros(){
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
                        if(!restaurantes.get(i).getPrecio().equals(filtro.getFiltro()))
                            restaurantes.remove(i);
                        else
                            i++;
                        break;
                    case "Tipo de Comida":
                        if(!restaurantes.get(i).getTipoDeComida().equals(filtro.getFiltro()))
                            restaurantes.remove(i);
                        else
                            i++;
                        break;
                    default:
                        i++;

                }
            }
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.listView_Filtros){
            getActivity().getMenuInflater().inflate(R.menu.eliminar_menu,menu);
        }

    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.opcionEliminar:
                filtros.remove(info.position);
                actualizarListaFiltros();
                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }


}
