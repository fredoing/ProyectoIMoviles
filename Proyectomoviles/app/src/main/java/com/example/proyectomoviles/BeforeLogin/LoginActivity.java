package com.example.proyectomoviles.BeforeLogin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.proyectomoviles.Menu.MenuActivity;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Utils.Connector;
import com.example.proyectomoviles.Utils.ObjectSerializer;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private EditText txtCorreo,txtContrasena;
    private Usuario usuario ;
    private CallbackManager callbackManager;
    SharedPreferences sharedPreferences ;
    public   static boolean resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.btn_LogInFacebook);
        txtCorreo = findViewById(R.id.editTxt_LogIn_Correo);
        txtContrasena = findViewById(R.id.editTxt_LogIn_Contrasena);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        sharedPreferences = this.getSharedPreferences("com.example.sharedpreferences", Context.MODE_PRIVATE);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Profile profile = Profile.getCurrentProfile();
                String id = profile.getId();
                String first_name = profile.getFirstName();
                String last_name = profile.getLastName();

                ArrayList<String> datos = new ArrayList<String>();
                try {
                    datos = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Datos",ObjectSerializer.serialize(new ArrayList<String>())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String email = datos.get(0);

                String[] comandos = {"Autentificar Usuario F", email,id};
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

                if(!resultado){
                  String[] newComandos = {"Registrar Usuario F",first_name+last_name, email,id};
                    connector = new Connector(newComandos);
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
                }

                usuario = new Usuario(first_name+" "+last_name,"",email,true);
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("Usuario",usuario);
                intent.putExtra("Id",id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.i("Resultados",error.toString());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken != null){
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void  loadUserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    String email = object.getString("email");



                    ArrayList<String> datos = new ArrayList<String>();
                    datos.add(email);


                    //Guardar
                    try {
                        sharedPreferences.edit().putString("Datos",ObjectSerializer.serialize(datos)).apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields","email");
        request.setParameters(bundle);
        request.executeAsync();
    }

    public void iniciarSesion(View view){
        String email = txtCorreo.getText().toString();
        String contrasena = txtContrasena.getText().toString();

        String[] comandos = {"Autentificar Usuario", email,contrasena};
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
        if(resultado) {
            usuario = new Usuario("", txtCorreo.getText().toString(), txtContrasena.getText().toString(), false);
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Usuario", usuario);

            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(),"Usuario no válido",Toast.LENGTH_LONG).show();
    }

    public void recuperarContrasena(View view){
        if(!txtCorreo.getText().toString().isEmpty()){
            String email = txtCorreo.getText().toString();
            String[] comandos = {"Recuperar Contraseña", email};
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

            if(resultado)
                Toast.makeText(getApplicationContext(),"Se ha enviado tu contraseña a tu correo",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Se ha detectado un error",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Debe introducir un correo",Toast.LENGTH_LONG).show();
        }


    }
}
