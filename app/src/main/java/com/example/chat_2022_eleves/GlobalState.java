package com.example.chat_2022_eleves;

import android.app.Application;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GlobalState extends Application {
    public static final String CAT = "IG2I";
    private String username;
    private String password;


    public void alerter(String s) {
        Log.i(CAT,s);
        Toast t = Toast.makeText(this,s,Toast.LENGTH_SHORT);
        t.show();
    }

    private String convertStreamToString(InputStream in) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean verifReseau()
    {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        Boolean bStatut = false;
        if (netInfo != null)
        {

            NetworkInfo.State netState = netInfo.getState();

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                bStatut = true;
                int netType= netInfo.getType();
                switch (netType)
                {
                    case ConnectivityManager.TYPE_MOBILE :
                        sType = "Réseau mobile détecté"; break;
                    case ConnectivityManager.TYPE_WIFI :
                        sType = "Réseau wifi détecté"; break;
                }

            }
        }

        return bStatut;
    }

    public String requeteGET(String urlData, String qs) {
        if (qs != null)
        {
            try {
                URL url = new URL(urlData + "?" + qs);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = null;
                in = new BufferedInputStream(urlConnection.getInputStream());
                String txtReponse = convertStreamToString(in);
                urlConnection.disconnect();
                return txtReponse;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String requetePOST(String urlData, String qs) {
        DataOutputStream dataout = null; // new:POST
        if (qs != null)
        {
            try {
                URL url = new URL(urlData); // new:POST
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();

                // new:POST
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                dataout = new DataOutputStream(urlConnection.getOutputStream());
                dataout.writeBytes(qs);
                // new:POST

                InputStream in = null;
                in = new BufferedInputStream(urlConnection.getInputStream());
                String txtReponse = convertStreamToString(in);
                urlConnection.disconnect();
                return txtReponse;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }



    public void enregistrerUtilisateur(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public int getCouleur(String couleur){
        switch(couleur) {
            case "blue":
                return Color.BLUE;
            case "grey":
                return Color.GRAY;
            case "dark":
                return Color.DKGRAY;
            case "red":
                return Color.RED;
            case "green":
                return Color.GREEN;
            case "white":
                return Color.WHITE;
            default:
                return Color.DKGRAY;
        }
    }
}
