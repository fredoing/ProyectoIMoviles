package com.example.proyectomoviles.BeforeLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.example.proyectomoviles.Menu.MenuActivity;
import com.example.proyectomoviles.Objetos.Usuario;
import com.example.proyectomoviles.R;
import com.example.proyectomoviles.Utils.Connector;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtNombre,txtCorreo,txtContrasena;
    public static boolean resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtNombre = findViewById(R.id.editTxt_Register_Nombre);
        txtCorreo = findViewById(R.id.editTxt_Register_Correo);
        txtContrasena = findViewById(R.id.editTxt_Register_Contrasena);

    }

    public void registrarse(View view){
       if(!txtNombre.getText().toString().isEmpty() && !txtContrasena.getText().toString().isEmpty() && !txtCorreo.getText().toString().isEmpty()){
           Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
           Matcher mather = pattern.matcher(txtCorreo.getText());

           if(mather.find()) {
               String[] comandos = {"Registrar Usuario", txtNombre.getText().toString(), txtCorreo.getText().toString(), txtContrasena.getText().toString()};
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

               if (resultado) {
                   Usuario nuevoUsuario = new Usuario(txtNombre.getText().toString(), txtContrasena.getText().toString(), txtCorreo.getText().toString(), false);

                   Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("Usuario", nuevoUsuario);
                   startActivity(intent);

               } else
                   Toast.makeText(getApplicationContext(), "Correo ya registrado", Toast.LENGTH_LONG).show();


           }
           else
               Toast.makeText(getApplicationContext(),"Correo no válido",Toast.LENGTH_LONG).show();

       }
       else
           Toast.makeText(getApplicationContext(),"Datos incompletos",Toast.LENGTH_LONG).show();




    }

}
