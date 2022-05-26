package com.example.chat_2022_eleves;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText edtLogin;
    public EditText edtPasse;
    public Button btnLogin;
    public CheckBox cbRemember;
    public SharedPreferences sp;
    public GlobalState gs;


    class JSONAsyncTask extends AsyncTask<String, Void, JSONObject> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(gs.CAT,"onPreExecute");
        }

        @Override
        protected JSONObject doInBackground(String... data) {
            // pas d'interaction avec l'UI Thread ici
            // String... data : ellipse
            // data[0] contient le premier argument passé à .execute(...)
            // data[1] contient le second argument passé à .execute(...)

            // {"promo":"2020-2021",
            // "enseignants":[
            // {"prenom":"Mohamed","nom":"Boukadir"},
            // {"prenom":"Thomas","nom":"Bourdeaud'huy"},
            // {"prenom":"Mathieu","nom":"Haussher"},
            // {"prenom":"Slim","nom":"Hammadi"}]}

            Log.i(gs.CAT,"doInBackground");
            String res = gs.requeteGET(data[0] ,data[1]);
            try {
                JSONObject ob = new JSONObject(res);
                return ob;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(JSONObject result) {
            Log.i(gs.CAT,"onPostExecute");
            // parcourir le json reçu et afficher les enseignants
            gs.alerter(result.toString());
            // Utiliser la librairie gson pour l'afficher

            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create();

            gs.alerter(gson.toJson(result));
            Promo p = gson.fromJson(result.toString(),Promo.class);
            gs.alerter(p.toString());

            try {
                String s = "";
                JSONArray tabProfs = result.getJSONArray("enseignants");
                for(int i=0;i<tabProfs.length();i++) {
                    JSONObject nextProf = tabProfs.getJSONObject(i);
                    s += nextProf.getString("prenom") + " "
                        + nextProf.getString("nom") + " ";
                }
                gs.alerter(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtLogin = findViewById(R.id.edtLogin);
        edtPasse = findViewById(R.id.edtPasse);
        btnLogin = findViewById(R.id.btnLogin);
        cbRemember = findViewById(R.id.cbRemember);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        btnLogin.setOnClickListener(this);
        cbRemember.setOnClickListener(this);
        gs = (GlobalState) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Si le réseau est disponible, alors on réactive le bouton OK
        btnLogin.setEnabled(gs.verifReseau());

        // relire les préférences de l'application
        // mettre à jour le formulaire
        if (sp.getBoolean("remember",false)) {
            // on charge automatiquement les champs login/passe
            // on coche la case
            edtLogin.setText(sp.getString("login",""));
            edtPasse.setText(sp.getString("passe",""));
            cbRemember.setChecked(true);
        } else {
            // on vide
            edtLogin.setText("");
            edtPasse.setText("");
            cbRemember.setChecked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:

            // Changer d'activité pour afficher SettingsActivity
                Intent toSettings = new Intent(this,SettingsActivity.class);
                startActivity(toSettings);


            break;
            case R.id.action_account:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    class PostAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... data) {
            String res = gs.requetePOST(data[0] ,data[1]);
            // {"version":1.3,"success":true,
            // "status":202,"hash":"efd18c70f94a580d9dc85533ddcd9823"}
            try {
                JSONObject ob = new JSONObject(res);
                return ob.getString("hash");
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }

        protected void onPostExecute(String hash) {
            // changer d'activité => ChoixConversations
            if (hash == "") return;
            Intent versChoixConv = new Intent(LoginActivity.this,ChoixConvActivity.class);
            Bundle bdl = new Bundle();
            bdl.putString("hash",hash);

            versChoixConv.putExtras(bdl);
            startActivity(versChoixConv);
        }
    }


    @Override
    public void onClick(View view) {

        SharedPreferences.Editor editor = sp.edit();
        switch (view.getId()) {
            case R.id.btnLogin:
                PostAsyncTask reqPOST= new PostAsyncTask();
                String username = edtLogin.getText().toString();
                String password = edtPasse.getText().toString();
                gs.enregistrerUtilisateur(username, password);
                reqPOST.execute("http://tomnab.fr/chat-api/authenticate",
                        "user=" + username
                        + "&password=" + password);


                break;
            case R.id.cbRemember:
                if (cbRemember.isChecked()) {
                    // on sauvegarde tout
                    editor.putBoolean("remember", true);
                    editor.putString("login",edtLogin.getText().toString());
                    editor.putString("passe",edtPasse.getText().toString());
                } else {
                    // on oublie tout
                    editor.putBoolean("remember", false);
                    editor.putString("login","");
                    editor.putString("passe","");
                }

            break;
        }
        editor.commit();
    }
}