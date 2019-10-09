package com.example.proyectomoviles.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.ResourceLoader;
import com.example.proyectomoviles.Fragments.FragmentListaRestaurantes;
import com.example.proyectomoviles.Fragments.FragmentPerfil;
import com.example.proyectomoviles.Objetos.Horario;
import com.example.proyectomoviles.Objetos.Restaurante;
import com.example.proyectomoviles.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AdaptadorListaRestaurantes extends BaseAdapter {
    Context context;
    ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();

    public static Drawable drawable;

    public AdaptadorListaRestaurantes(Context context, ArrayList<Restaurante> restaurantes) {
        this.context = context;
        this.restaurantes = restaurantes;
    }

    @Override
    public int getCount() {
        return restaurantes.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurantes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Restaurante restaurante = (Restaurante)getItem(i);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_lista_restaurtantes,null);
        ImageView imagenRestaurante = convertView.findViewById(R.id.img_listView_restaurante);

        String[] comandos = new String[2];
        comandos[0] = "Obtener Imagen URL";
        if(!restaurante.getImagenesURL().isEmpty()){

            comandos[1] = restaurante.getImagenesURL().get(0);
        }
        else {

            comandos[1] = "https://image.freepik.com/vector-gratis/fachada-restaurante-estilo-plano_23-2147537370.jpg";
        }


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
        TextView nombreRestaurante = convertView.findViewById(R.id.txt_listView_NombreRestaurante);
        TextView tipoComidaRestaurante = convertView.findViewById(R.id.txt_listView_TipoComida);
        TextView precioRestaurante = convertView.findViewById(R.id.txt_listView_Precio);
        TextView calificacionRestaurante = convertView.findViewById(R.id.txt_listView_Calificacion);

        nombreRestaurante.setText(restaurante.getNombre());
        tipoComidaRestaurante.setText(restaurante.getTipoDeComida());
        precioRestaurante.setText(restaurante.getPrecio());
        calificacionRestaurante.setText(restaurante.getCalificacion());




        return convertView;
    }



}
