package com.example.proyectomoviles.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectomoviles.Objetos.Horario;
import com.example.proyectomoviles.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdaptadorListaHorarios extends BaseAdapter {
    Context context;
    ArrayList<Horario> horarios = new ArrayList<Horario>();

    public AdaptadorListaHorarios(Context context, ArrayList<Horario> horarios) {
        this.context = context;
        this.horarios = horarios;
    }

    @Override
    public int getCount() {
        return horarios.size();
    }

    @Override
    public Object getItem(int i) {
        return horarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Horario horario = (Horario) getItem(i);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_lista_horario,null);

        TextView textHorario = convertView.findViewById(R.id.txt_listaHorarios);


        textHorario.setText(horario.toString());

        return convertView;
    }
}
