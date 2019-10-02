package com.example.proyectomoviles.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectomoviles.Objetos.Comentario;
import com.example.proyectomoviles.R;

import java.util.ArrayList;

public class AdaptadorComentarios extends BaseAdapter {
    Context context;
    ArrayList<Comentario> comentarios = new ArrayList<Comentario>();

    public AdaptadorComentarios(Context context, ArrayList<Comentario> comentarios) {
        this.context = context;
        this.comentarios = comentarios;
    }

    @Override
    public int getCount() {
        return comentarios.size();
    }

    @Override
    public Object getItem(int i) {
        return comentarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Comentario comentario = (Comentario) getItem(i);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_lista_comentarios,null);

        TextView textUsuario = convertView.findViewById(R.id.text_listaComentarios_NombreUsuario);
        TextView textComentario = convertView.findViewById(R.id.text_listaComentarios_Comentario);

        textUsuario.setText(comentario.getAutor());
        textComentario.setText(comentario.getComentario());

        return convertView;
    }
}
