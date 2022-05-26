package com.example.chat_2022_eleves;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixConvActivity extends AppCompatActivity {
    private String TAG = "ChoixConvActivity";
    GlobalState gs;
    private String hash;
    public Spinner spinConversations;
    public AlertDialog.Builder dialogBuilder; // popup creation conv
    public View creeConvPopup; // View popup creation cov
    public AlertDialog convdialog; // Creation conv popup


    public void goToConversation(View view) {
        if (hash == "") return;
        Intent versMessages = new Intent(ChoixConvActivity.this, ListMessageActivity.class);
        Bundle bdl = new Bundle();
        Conversation convSelected = (Conversation) spinConversations.getSelectedItem();
        bdl.putInt("idConv", convSelected.getId());
        bdl.putString("hash",hash);
        bdl.putBoolean("active", convSelected.getActive());
        versMessages.putExtras(bdl);
        startActivity(versMessages);

    }




    public void goToCreeConversation(View view){ // ouvre le menu popup créer conv
        dialogBuilder = new AlertDialog.Builder(this);
        creeConvPopup = getLayoutInflater().inflate(R.layout.cree_conv_menu, null);
        dialogBuilder.setView(creeConvPopup);
        convdialog = dialogBuilder.create();
        convdialog.show();


    }

    public class MyCustomAdapter extends ArrayAdapter<Conversation> {

        private int layoutId;
        private ArrayList<Conversation> dataConvs;

        public MyCustomAdapter(Context context,
                               int itemLayoutId,
                               ArrayList<Conversation> objects) {
            super(context, itemLayoutId, objects);
            layoutId = itemLayoutId;
            dataConvs = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            //return getCustomView(position, convertView, parent);
            // getLayoutInflater() vient de Android.Activity => il faut utiliser une classe interne
            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(layoutId, parent, false);
            Conversation nextC = dataConvs.get(position);

            TextView label = (TextView) item.findViewById(R.id.spinner_theme);
            label.setText(nextC.getTheme());

            ImageView icon = (ImageView) item.findViewById(R.id.spinner_icon);
            if (nextC.getActive()){
                icon.setImageResource(R.drawable.icon36);
            }
            else{
                icon.setImageResource(R.drawable.icongray36);
            }

            return item;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return getCustomView(position, convertView, parent);
            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(layoutId, parent, false);
            Conversation nextC = dataConvs.get(position);

            TextView label = (TextView) item.findViewById(R.id.spinner_theme);
            label.setText(nextC.getTheme());

            ImageView icon = (ImageView) item.findViewById(R.id.spinner_icon);
            if (nextC.getActive()) icon.setImageResource(R.drawable.icon);
            else icon.setImageResource(R.drawable.icongray);
            return item;
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_conversation);
        gs = (GlobalState) getApplication();
        Bundle bdl = this.getIntent().getExtras();
        hash = bdl.getString("hash");

        APIInterface apiService = APIClient.getClient().create(APIInterface.class);

        Call<ListConversations> call1 = apiService.doGetListConversation(hash);
        call1.enqueue(new Callback<ListConversations>() {
            @Override
            public void onResponse(Call<ListConversations> call, Response<ListConversations> response) {
                ListConversations listeConvs = response.body();
                Log.i(gs.CAT,listeConvs.toString());
                spinConversations = (Spinner) findViewById(R.id.spinConversations);
                spinConversations.setAdapter(new MyCustomAdapter(		ChoixConvActivity.this,
                        R.layout.spinner_item,
                        listeConvs.getConversations()));


                // gestion texte bouton active conversation pas obligatoire mais conseillé
                Conversation convSelected = (Conversation) spinConversations.getSelectedItem();
                Button boutonConv = findViewById(R.id.activeConv);

                if(convSelected.getActive()){
                    boutonConv.setText("Desactiver Conversation");
                }else{
                    boutonConv.setText("Activer Conversation");
                }
                //////////////////////////

                Spinner spinConve = findViewById(R.id.spinConversations);
                spinConve.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                        // TODO Auto-generated method stub
                        Conversation convSelected = (Conversation) spinConversations.getSelectedItem();
                        Button boutonConv = findViewById(R.id.activeConv);

                        if(convSelected.getActive()){
                            boutonConv.setText("Desactiver Conversation");
                        }else{
                            boutonConv.setText("Activer Conversation");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

            }

            @Override
            public void onFailure(Call<ListConversations> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void refreshConversationList(){
        gs = (GlobalState) getApplication();
        Bundle bdl = this.getIntent().getExtras();
        hash = bdl.getString("hash");

        APIInterface apiService = APIClient.getClient().create(APIInterface.class);

        Call<ListConversations> call1 = apiService.doGetListConversation(hash);
        call1.enqueue(new Callback<ListConversations>() {
            @Override
            public void onResponse(Call<ListConversations> call, Response<ListConversations> response) {
                ListConversations listeConvs = response.body();
                Log.i(gs.CAT,listeConvs.toString());
                spinConversations = (Spinner) findViewById(R.id.spinConversations);
                //ArrayAdapter<Conversation> dataAdapter = new ArrayAdapter<Conversation>(
                //        ChoixConversationActivity.this,
                //        android.R.layout.simple_spinner_item,
                //        listeConvs.getConversations());
                //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //spinConversations.setAdapter(dataAdapter);
                spinConversations.setAdapter(new MyCustomAdapter(		ChoixConvActivity.this,
                        R.layout.spinner_item,
                        listeConvs.getConversations()));

                if(convdialog != null) {
                    convdialog.dismiss(); // fermer popup
                }
            }

            @Override
            public void onFailure(Call<ListConversations> call, Throwable t) {
                if(convdialog != null) {
                    convdialog.dismiss(); // fermer popup
                }
                call.cancel();

            }
        });
    }

    public void createConversation(View view){

        gs = (GlobalState) getApplication();
        Bundle bdl = this.getIntent().getExtras();
        String hash = bdl.getString("hash");


        EditText edit = creeConvPopup.findViewById(R.id.edtCreateConv);

        if(edit == null){
            return;
        }

        APIInterface apiService = APIClient.getClient().create(APIInterface.class);

        Call<ResponseBody> callSendMessage = apiService.doCreateConversation(hash, edit.getText().toString());
        callSendMessage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody messages = response.body();
                refreshConversationList();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                creeConvPopup.findViewById(R.id.btnCreeConv).setEnabled(true);
                call.cancel();

            }
        });

        creeConvPopup.findViewById(R.id.btnCreeConv).setEnabled(false);


    }

    public void doactiverdesactiverConvesation(View view){

        Button active = findViewById(R.id.activeConv);
        String buttontext = active.getText().toString();
        gs = (GlobalState) getApplication();

        Conversation convSelected = (Conversation) spinConversations.getSelectedItem();
        int idConv = convSelected.getId();



        String activeString = "";
        String boutonString = "";

        if(buttontext.equals("Activer Conversation")){

            activeString = "active";
        }else{
            activeString = "inactive";

        }


        APIInterface apiService = APIClient.getClient().create(APIInterface.class);

        Call<ResponseBody> callSendMessage = apiService.doActiveDesactiveConversation(hash, idConv, activeString.toString());
        callSendMessage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody messages = response.body();
                refreshConversationList();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });


    }

    public void supprimerConversation(View view){
        gs = (GlobalState) getApplication();
        Bundle bdl = this.getIntent().getExtras();
        String hash = bdl.getString("hash");
        Conversation convSelected = (Conversation) spinConversations.getSelectedItem();
        int idConv = convSelected.getId();


        APIInterface apiService = APIClient.getClient().create(APIInterface.class);

        Call<ResponseBody> callSendMessage = apiService.doSupprimerConversation(hash, idConv);
        callSendMessage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody messages = response.body();
                refreshConversationList();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                gs.alerter("Erreur pendant la supression de la conversation");
                call.cancel();

            }
        });

    }




}