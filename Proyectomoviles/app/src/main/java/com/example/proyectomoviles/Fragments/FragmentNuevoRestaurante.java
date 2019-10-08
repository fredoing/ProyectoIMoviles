package com.example.proyectomoviles.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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
import com.example.proyectomoviles.Utils.Connector;
import com.example.proyectomoviles.Utils.GridImageAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private Spinner spinnerPrecio;
    private Spinner spinnerTipoDeComida;
    private View view;
    GridView gridViewImagenes;
    //EditText
    private EditText editTextNombre;
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
        spinnerPrecio = view.findViewById(R.id.spinner_Precio);
        editTextNombre = view.findViewById(R.id.editTxt_NuevoRestaurante_Nombre);
        spinnerTipoDeComida = view.findViewById(R.id.spinner_NuevoRestaurante_TipoComida);
        editTextTelefono = view.findViewById(R.id.editTxt_NuevoRestaurante_Telefono);
        editTextCorreo = view.findViewById(R.id.editTxt_NuevoRestaurante_Correo);
        gridViewImagenes = view.findViewById(R.id.gridView_AgregarRestaurante);


        inicializarBtnUbicacion();
        inicializarBtnHorario();
        inicializarBtnImagen();
        inicializarBtnAgregar();

        registerForContextMenu(gridViewImagenes);

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
                intent.putParcelableArrayListExtra("Horarios",horarios);
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
                String telefono = editTextTelefono.getText().toString();
                String correo = editTextCorreo.getText().toString();
                if(!nombre.isEmpty() && !telefono.isEmpty() && !correo.isEmpty() &&
                    ubicacionIniciada && !horarios.isEmpty()){
                    String precio = String.valueOf(spinnerPrecio.getSelectedItem());
                    int numeroPrecio = 0;
                    int numeroTipoComida =0;
                    switch (spinnerTipoDeComida.getSelectedItem().toString()){
                        case "Mexicana":
                            numeroTipoComida = 1;
                            break;
                        case "Casera":
                            numeroTipoComida = 2;
                            break;
                        case "Italiana":
                            numeroTipoComida = 3;
                            break;
                        case "Rapida":
                            numeroTipoComida = 4;
                            break;
                        case "Gourmet":
                            numeroTipoComida = 5;
                            break;
                        case "Caribeña":
                            numeroTipoComida = 6;
                            break;



                    }
                    switch (precio){
                        case "Caro":
                            numeroPrecio = 3;
                            break;
                        case "Medio":
                            numeroPrecio = 2;
                            break;
                        case "Barato":
                            numeroPrecio = 1;
                            break;
                    }
                    int i = 0;
                    String textoHorarios = "";
                    while(i<horarios.size()){
                        textoHorarios += horarios.get(i).toString()+"\n";
                        i++;
                    }
                    String contacto = "Correo: "+correo+"\n"+"Teléfono: "+telefono;
                    String[] comandos = {"Agregar Restaurante",nombre,Double.toString(ubicacion.latitude),Double.toString(ubicacion.longitude),contacto,textoHorarios,String.valueOf(numeroPrecio),String.valueOf(numeroTipoComida)};
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
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListaRestaurantes(new ArrayList<Restaurante>(),usuario)).commit();

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
    


    private void actualizarListaImagenes(){
        GridImageAdapter adapter = new GridImageAdapter(getContext(),R.layout.layout_grid_imageview,"",imagenesURL);
        gridViewImagenes.setAdapter(adapter);

        if(!imagenesURL.isEmpty()){


            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int imageWidth = 200;
            int gridViewImagenesWidth = (int) (imageWidth * imagenesURL.size() * density);
            int itemWidth = (int) (imageWidth * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridViewImagenesWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            gridViewImagenes.setLayoutParams(params);
            gridViewImagenes.setNumColumns(imagenesURL.size());
            gridViewImagenes.setHorizontalSpacing(2);
            gridViewImagenes.setStretchMode(GridView.STRETCH_SPACING);
            gridViewImagenes.setColumnWidth(itemWidth);
        }
        else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            gridViewImagenes.setLayoutParams(params);
            gridViewImagenes.setNumColumns(0);
            gridViewImagenes.setHorizontalSpacing(0);
            gridViewImagenes.setColumnWidth(0);
        }

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
                    actualizarListaImagenes();
                    break;
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.eliminar_menu,menu);

    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.opcionEliminar:
                imagenesURL.remove(info.position);
                actualizarListaImagenes();
                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }
}
