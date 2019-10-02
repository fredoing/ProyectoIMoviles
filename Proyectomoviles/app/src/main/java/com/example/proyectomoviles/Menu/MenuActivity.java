package com.example.proyectomoviles.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyectomoviles.Fragments.FragmentFiltros;
import com.example.proyectomoviles.Fragments.FragmentListaRestaurantes;
import com.example.proyectomoviles.Fragments.FragmentNuevoRestaurante;
import com.example.proyectomoviles.Fragments.FragmentPerfil;
import com.example.proyectomoviles.Fragments.FragmentRestaurantesCercanos;
import com.example.proyectomoviles.Objetos.Restaurante;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Utils.Connector;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Usuario usuario;
    private DrawerLayout drawerLayout;
    private String id ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle   toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        Intent intent = getIntent();
        usuario = intent.getParcelableExtra("Usuario");

        NavigationView navigationView = findViewById(R.id.nav_view);

        LinearLayout linearLayout = (LinearLayout) navigationView.getHeaderView(0);


        TextView correo = (TextView) linearLayout.getChildAt(1);
        correo.setText(usuario.getCorreo());

        if(usuario.isFromFacebook()){
            CircleImageView circleImageView = (CircleImageView) linearLayout.getChildAt(0);
            id = intent.getStringExtra("Id");
            String urlImagen ="https://graph.facebook.com/"+id+ "/picture?type-normal";

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();

            Glide.with(MenuActivity.this).load(urlImagen).into(circleImageView);
        }

        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListaRestaurantes(new ArrayList<Restaurante>(),usuario)).commit();





    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_nuevo_restaurante:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentNuevoRestaurante(usuario)).commit();

                break;
            case R.id.nav_vista_mapa:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentRestaurantesCercanos()).commit();
                break;
            case R.id.nav_lista_restaurantes:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentListaRestaurantes(new ArrayList<Restaurante>(),usuario)).commit();
                break;

            case R.id.nav_buscar_restaurante:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentFiltros(usuario)).commit();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
        super.onBackPressed();
    }
}
