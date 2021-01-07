package com.example.segrada;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.EditText;

import com.example.segrada.PubSubBroker.Broker;

public class MainActivity extends AppCompatActivity {

    private ClientController server;
    private Broker broker;
    private String serverAddress = "";

    public static String PACKAGE_NAME;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;

        //region Connect to Server
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Network")
                .setMessage("Please enter the server address you would like to connect to:")
                .setView(input)
                .setPositiveButton("OK", ((dialog, which) -> {
                    serverAddress = input.getText().toString();
                    server = ClientController.getInstance(serverAddress);
                }))
                .show();
        //endregion

        // Set up PubSubBroker
        broker = Broker.getInstance();
        subToBroker();
    }

    private void subToBroker(){
        broker.subscribe("StartGame", (publisher, topic, params) -> {
            openGamePlayActivity();
            finish();
        });
    }

    private void openGamePlayActivity(){
        Intent intent = new Intent(this, GamePlayActivity.class);
        startActivity(intent);
    }
}
