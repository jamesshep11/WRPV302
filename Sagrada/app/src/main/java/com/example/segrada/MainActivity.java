package com.example.segrada;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.segrada.PubSubBroker.Broker;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ClientController server;
    private Broker broker;
    private String serverAddress = "";

    public static String PACKAGE_NAME;
    private Map<String, Object> initParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        //region Connect to Server
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Network")
                .setMessage("Please enter the server address you would like to connect to:")
                .setView(input)
                .setPositiveButton("OK", ((dialog, which) -> {
                    serverAddress = input.getText().toString();
                    server = new ClientController(serverAddress);
                }))
                .show();
        //endregion

        // Set up PubSubBroker
        broker = Broker.getInstance();
        broker.subscribe("StartGame", (publisher, topic, params) -> {
            initParams = params;
            initParams.put("server", server);
            startGame();
        });
        broker.subscribe("GameStarted", ((publisher, topic, params) -> init(initParams)));
    }

    private void startGame(){
        Intent intent = new Intent(this, GamePlayActivity.class);
        startActivity(intent);
    }

    private void init(Map<String, Object> params){
        broker.publish(this, "Init", params);
        finish();
    }
}
