package com.example.chat_2022_eleves;

import static android.os.SystemClock.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListMessageActivity extends AppCompatActivity implements View.OnClickListener{
    GlobalState gs;
    private RecyclerView mMessageRecycler;
    private ListMessageAdapter mMessageAdapter;
    private int lastMessageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_message);
        Button b = findViewById(R.id.button_gchat_send);
        b.setOnClickListener(this);
        gs = (GlobalState) getApplication();
        Bundle bdl = this.getIntent().getExtras();
        String hash = bdl.getString("hash");
        int idConv = bdl.getInt("idConv");
        boolean active = bdl.getBoolean("active");

        Button sendbutton = findViewById(R.id.button_gchat_send);
        sendbutton.setEnabled(active);

        if(active){
            sendbutton.setTextColor(0xFF6200EE);
        }else{
            sendbutton.setTextColor(Color.GRAY);
        }


        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        Context context = this;

        APIInterface apiService = APIClient.getClient().create(APIInterface.class);

        Call<ListMessages> callListMessages = apiService.doGetListMessage(hash, idConv);
        handleMessagesApi(callListMessages, context);
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            getLastItemsApi(callListMessages, context);
                        } catch (Exception e) {
                            System.out.println(e);
                            Log.d(GlobalState.CAT, "ERREUR");
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 1000, 1000); //execute in every 10 seconds

    }

    private void handleMessagesApi(Call<ListMessages> callListMessages, Context context)
    {
        callListMessages.enqueue(new Callback<ListMessages>() {
            @Override
            public void onResponse(Call<ListMessages> call, Response<ListMessages> response) {
                ListMessages messages = response.body();
                Log.i(gs.CAT,messages.toString());
                mMessageAdapter = new ListMessageAdapter(context, messages, gs);
                mMessageRecycler.setLayoutManager(new LinearLayoutManager(context));
                mMessageRecycler.setAdapter(mMessageAdapter);
                if(messages.getSize() > 0)
                {
                    mMessageRecycler.scrollToPosition(messages.getSize()-1);
                    int nbMessages = mMessageAdapter.getItemCount();
                    Message lastMessage = mMessageAdapter.getItem(nbMessages-1);
                    lastMessageId = lastMessage.getId();
                }
            }

            @Override
            public void onFailure(Call<ListMessages> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void getLastItemsApi(Call<ListMessages> callListMessages, Context context)
    {
        callListMessages.clone().enqueue(new Callback<ListMessages>() {
            @Override
            public void onResponse(Call<ListMessages> call, Response<ListMessages> response) {
                ListMessages messages = response.body();
                boolean changed = false;
                for(Message message : messages.getMessages())
                {
                    if (message.getId() > lastMessageId)
                    {
                        mMessageAdapter.addMessage(message);
                        changed = true;
                    }
                }
                if(changed)
                {
                    mMessageAdapter.notifyDataSetChanged();
                    mMessageRecycler.scrollToPosition(messages.getSize()-1);

                    int nbMessages = mMessageAdapter.getItemCount();
                    Message lastMessage = mMessageAdapter.getItem(nbMessages-1);
                    lastMessageId = lastMessage.getId();
                }
            }

            @Override
            public void onFailure(Call<ListMessages> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void fermerClavier(){
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void sendMessage(){


        gs = (GlobalState) getApplication();
        Bundle bdl = this.getIntent().getExtras();
        String hash = bdl.getString("hash");
        int idConv = bdl.getInt("idConv");
        boolean active = bdl.getBoolean("active");

        if(active) {

            EditText edit = findViewById(R.id.edit_gchat_message);
            APIInterface apiService = APIClient.getClient().create(APIInterface.class);

            Call<ResponseBody> callSendMessage = apiService.doSendMessage(hash, idConv, edit.getText().toString());
            callSendMessage.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ResponseBody messages = response.body();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                }
            });

            edit.setText(""); // vider le textview après l'envoi de message
            fermerClavier(); // ferme le clavier
        }

    }



    @Override
    public void onClick(View view) {


        if(view.getId() == R.id.button_gchat_send)// si c'est le bouton qui est cliqué
            sendMessage();
    }





}