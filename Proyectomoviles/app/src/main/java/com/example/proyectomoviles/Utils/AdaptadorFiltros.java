package com.example.proyectomoviles.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectomoviles.Objetos.Filtro;
import com.example.proyectomoviles.Objetos.Horario;
import com.example.proyectomoviles.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorFiltros extends BaseAdapter {
    Context context;
    ArrayList<Filtro> filtros = new ArrayList<Filtro>();

    public AdaptadorFiltros(Context context, ArrayList<Filtro> filtros) {
        this.context = context;
        this.filtros = filtros;
    }

    @Override
    public int getCount() {
        return filtros.size();
    }

    @Override
    public Object getItem(int i) {
        return filtros.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Filtro filtro = (Filtro) getItem(i);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_lista_filtros,null);

        TextView textView = convertView.findViewById(R.id.text_item_filtro);

        textView.setText(filtro.getTipoFiltro()+" = "+filtro.getFiltro());


        return convertView;

    }


}
