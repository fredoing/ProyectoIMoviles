package com.example.proyectomoviles.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.model.ResourceLoader;
import com.example.proyectomoviles.Objetos.Horario;
import com.example.proyectomoviles.Objetos.Restaurante;
import com.example.proyectomoviles.R;

import java.util.ArrayList;

public class AdaptadorListaRestaurantes extends BaseAdapter {
    Context context;
    ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();

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

        if(!restaurante.getImagenesURL().isEmpty()){
            ImageView imagenRestaurante = convertView.findViewById(R.id.img_listView_restaurante);
            imagenRestaurante.setImageURI(Uri.parse(restaurante.getImagenesURL().get(0)));
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
