package com.example.proyectomoviles.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyectomoviles.AgregarRestaurante.AgregarHorario;
import com.example.proyectomoviles.AgregarRestaurante.MapGetUbicacionRestaurante;
import com.example.proyectomoviles.Objetos.Horario;
import com.example.proyectomoviles.Objetos.Restaurante;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Utils.AdaptadorListaHorarios;
import com.example.proyectomoviles.Utils.GridImageAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentNuevoRestaurante extends Fragment {
    //Botones
    private Button btnUbicacion;
    private Button btnHorario;
    private Button btnImagen;
    private Button btnAgregar;
    //Variables
    private LatLng ubicacion;
    private ArrayList<Horario> horarios = new ArrayList<Horario>();
    private ArrayList<String> imagenesURL = new ArrayList<String>();
    private Boolean ubicacionIniciada = false;
    private Usuario usuario;
    //Views
    private TextView textUbicacion;
    private ListView listViewHorarios;
    private GridView gridViewImagenes;
    private Spinner spinnerPrecio;
    private View view;
    //EditText
    private EditText editTextNombre;
    private EditText editTextTipoComida;
    private EditText editTextTelefono;
    private EditText editTextCorreo;

    //Request Codes
    private final int codigoUbicacion = 9000;
    private final int codigoHorario = 9001;
    private final int codigoImagen = 9002;


    public FragmentNuevoRestaurante(Usuario usuario) {
        this.usuario = usuario;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agregar_restaurante,container,false);

        btnUbicacion = view.findViewById(R.id.btn_AgregarUbicacion);
        btnHorario = view.findViewById(R.id.btn_AgregarHorario);
        btnImagen = view.findViewById(R.id.btn_AgregarImagen);
        btnAgregar = view.findViewById(R.id.btn_AgregarRestaurante);
        textUbicacion = view.findViewById(R.id.txt_agregarUbicacion);
        listViewHorarios = view.findViewById(R.id.listView_AgregarHorarios);
        gridViewImagenes = view.findViewById(R.id.gridView_AgregarRestaurante);
        spinnerPrecio = view.findViewById(R.id.spinner_Precio);
        editTextNombre = view.findViewById(R.id.editTxt_NuevoRestaurante_Nombre);
        editTextTipoComida = view.findViewById(R.id.editTxt_NuevoRestaurante_TipoComida);
        editTextTelefono = view.findViewById(R.id.editTxt_NuevoRestaurante_Telefono);
        editTextCorreo = view.findViewById(R.id.editTxt_NuevoRestaurante_Correo);


        inicializarBtnUbicacion();
        inicializarBtnHorario();
        inicializarBtnImagen();
        inicializarBtnAgregar();
        configurarGridView();


        return view;
    }

    public void inicializarBtnUbicacion(){
        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapGetUbicacionRestaurante.class );
                startActivityForResult(intent,codigoUbicacion);
            }
        });

    }

    public void inicializarBtnHorario(){
        btnHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AgregarHorario.class );
                startActivityForResult(intent,codigoHorario);
            }
        });

    }

    public void inicializarBtnImagen(){
        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,codigoImagen);
            }
        });
    }

    public void inicializarBtnAgregar(){
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = editTextNombre.getText().toString();
                String tipoComida = editTextTipoComida.getText().toString();
                String telefono = editTextTelefono.getText().toString();
                String correo = editTextCorreo.getText().toString();
                if(!nombre.isEmpty() && !tipoComida.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() &&
                    ubicacionIniciada && !horarios.isEmpty()){
                    String precio = String.valueOf(spinnerPrecio.getSelectedItem());
                    Restaurante restaurante = new Restaurante(nombre,ubicacion,tipoComida,telefono,correo,precio,horarios,imagenesURL);
                    Log.i("Resultados",restaurante.toString());
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListaRestaurantes(usuario)).commit();

                }
                else
                    Toast.makeText(getContext(),"Faltan datos",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void actualizarListaHorarios(){
        listViewHorarios = view.findViewById(R.id.listView_AgregarHorarios);

        AdaptadorListaHorarios adaptadorListaHorarios = new AdaptadorListaHorarios(getContext(),horarios);
        ViewGroup.LayoutParams layoutParams = listViewHorarios.getLayoutParams();
        layoutParams.height = 100 * horarios.size();
        listViewHorarios.setLayoutParams(layoutParams);
        listViewHorarios.setAdapter(adaptadorListaHorarios);


    }
    
    public void configurarGridView(){
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/4;
        gridViewImagenes.setColumnWidth(imageWidth);



    }

    public void actualizarGridViewImagenes(){
        GridImageAdapter adapter = new GridImageAdapter(getContext(),R.layout.layout_grid_imageview,"",imagenesURL);
        ViewGroup.LayoutParams layoutParams = gridViewImagenes.getLayoutParams();


        gridViewImagenes.setAdapter(adapter);
        int layoutHeight = (((imagenesURL.size()-1)/3)+1) *300;
        layoutParams.height = layoutHeight;
        gridViewImagenes.setLayoutParams(layoutParams);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case codigoUbicacion:
                    ubicacion = data.getParcelableExtra("Ubicacion");
                    DecimalFormat formato = new DecimalFormat("#.00");
                    textUbicacion.setText("Ubicación:\n" +
                                          "Latitud: "+formato.format(ubicacion.latitude)+"\n" +
                                          "Longitud: "+formato.format(ubicacion.longitude));
                    ubicacionIniciada = true;
                    break;
                case  codigoHorario:
                    ArrayList<Horario> nuevosHorarios = data.getParcelableArrayListExtra("Horarios");
                    horarios = nuevosHorarios;
                    actualizarListaHorarios();
                    break;
                case codigoImagen:
                    Uri imageUri = data.getData();
                    imagenesURL.add(imageUri.toString());
                    actualizarGridViewImagenes();
            }
        }
    }
}
