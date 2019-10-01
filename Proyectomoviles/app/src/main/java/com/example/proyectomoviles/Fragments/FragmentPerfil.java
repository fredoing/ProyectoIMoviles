package com.example.proyectomoviles.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class FragmentPerfil extends Fragment {
    private Usuario usuario;
    private String id;
    private TextView correo;
    private TextView nombre;
    private CircleImageView circleImageView;
    private Button button;

    public FragmentPerfil(Usuario usuario,String id) {
        this.usuario = usuario;
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil,container,false);

        correo =  view.findViewById(R.id.txt_profile_nombre);
        nombre =  view.findViewById(R.id.txt_profile_correo);
        circleImageView =  view.findViewById(R.id.img_profile);

        correo.setText(usuario.getCorreo());
        nombre.setText(usuario.getNombre());

        if(usuario.isFromFacebook()){
            String urlImagen ="https://graph.facebook.com/"+id+ "/picture?type-normal";

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();

            Glide.with(FragmentPerfil.this).load(urlImagen).into(circleImageView);
        }


       button = view.findViewById(R.id.btn_perfil_cambiarImagen);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalley();
            }
        });
        return view;
    }

    private void openGalley(){
        Intent intent  = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 100){
            Uri imageUri = data.getData();
            circleImageView.setImageURI(imageUri);
        }
    }
}


