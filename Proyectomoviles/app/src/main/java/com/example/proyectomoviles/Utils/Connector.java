package com.example.proyectomoviles.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.proyectomoviles.BeforeLogin.LoginActivity;
import com.example.proyectomoviles.BeforeLogin.RegisterActivity;
import com.example.proyectomoviles.Fragments.FragmentListaRestaurantes;
import com.example.proyectomoviles.Objetos.Restaurante;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class Connector extends AsyncTask<Void,Void,Void> {

    private static String serverUrl = "https://moviles1-db.herokuapp.com/";
    public static int BARATO = 1;
    public static int MEDIO = 2;
    public static int CARO = 3;
    private String[] comandos;
    private Boolean resultado = false;
    JSONArray json;

    public Connector(String[] comandos) {
        this.comandos = comandos;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private void readJsonFromUrl(String url) {
        InputStream is = null;
        try {

            URL tempurl = new URL(url);
            URLConnection conn = tempurl.openConnection();
            is = conn.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            json = new JSONArray(jsonText);
            is.close();
        } catch (Exception e) {
            Log.i("Resultados",e.toString());
            resultado = false;
        }
    }


    @Override
    protected Void doInBackground(Void... voids) {
        String url ;
        switch (comandos[0]){
            case "Registrar Usuario":
                url = serverUrl+"newUser/"+comandos[1]+"/"+comandos[2]+"/"+comandos[3];
                readJsonFromUrl(url);
                if (json != null) {
                    if(json.length() == 0){
                        resultado = true;
                    }
                }
                RegisterActivity.resultado = resultado;
                break;
            case "Registrar Usuario F":
                url = serverUrl+"newFaceUser/"+comandos[1]+"/"+comandos[2]+"/"+comandos[3];
                readJsonFromUrl(url);
                if (json != null) {
                    if(json.length() == 0){
                        resultado = true;
                    }
                }
                RegisterActivity.resultado = resultado;
                break;
            case "Autentificar Usuario" :
                url = serverUrl+"user/"+comandos[1]+"/"+comandos[2];
                Log.i("Resultados",comandos[1]+comandos[2]);
                readJsonFromUrl(url);
                if(json != null){
                    if(json.length() > 0){
                        try {
                            JSONObject jobject = json.getJSONObject(0);
                            Log.i("Resultados",String.valueOf(jobject.getBoolean("passed")));
                            if (jobject.getBoolean("passed")) {
                                resultado = true;
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                LoginActivity.resultado = resultado;
                break;
            case "Autentificar Usuario F" :
                url = serverUrl+"facebookuser/"+comandos[1]+"/"+comandos[2];
                readJsonFromUrl(url);
                if(json != null){
                    if(json.length() > 0){
                        try {
                            JSONObject jobject = json.getJSONObject(0);
                            if (jobject.getBoolean("passed")) {
                                resultado = true;
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                    LoginActivity.resultado = resultado;
                    break;
            case "Agregar Restaurante":
                url =  serverUrl+"newrest/"+comandos[1]+"/"+comandos[2]+"/"+comandos[3]+"/"+comandos[4]
                        +"/"+comandos[5]+"/"+comandos[6]+"/"+comandos[7];
                readJsonFromUrl(url);
                if (json == null) {
                    resultado = false;
                } else if (json.length() == 0) {
                    resultado = true;
                }
            case "Obtener todos los restaurantes ListView":
                url = serverUrl+"rests/"+1000000+"/"+0+"/"+0;
                readJsonFromUrl(url);
                FragmentListaRestaurantes.jsonArray = json;


                break;
            case "Comentar":
                url = serverUrl+"comment/"+comandos[1]+"/"+comandos[2]+"/"+comandos[3];
                readJsonFromUrl(url);
                if (json == null) {
                    Restaurante.resultado = false;
                } else if (json.length() == 0) {
                    Restaurante.resultado = true;
                }
                break;
            case "Obtener Id Usuario":
                url = serverUrl+"userid/"+comandos[1];
                int id = -1;
                readJsonFromUrl(url);
                JSONObject jobject = null;
                try {
                    jobject = json.getJSONObject(0);
                    id = jobject.getInt("id");
                } catch (JSONException e) {
                    Log.i("Resultados",e.toString());
                }
                Restaurante.userID = id;
                break;
            case "Obtener Comentarios Restaurante":
                url = serverUrl+"comments/"+comandos[1];
                readJsonFromUrl(url);
                Restaurante.jsonArray = json;
                break;
            case "Calificar Restaurnate":
                url = serverUrl+"califica/"+comandos[1]+"/"+comandos[2]+"/"+comandos[3];
                readJsonFromUrl(url);
                if (json == null) {
                    resultado = false;
                } else if (json.length() == 0) {
                    resultado = true;
                }



        }

        return null;
    }




}