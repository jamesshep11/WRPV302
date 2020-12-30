package com.example.segrada;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.segrada.PubSubBroker.Broker;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static androidx.core.os.LocaleListCompat.create;

public class MainActivity extends AppCompatActivity {

    private ClientController client;
    private Broker broker;
    private String serverAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Connect to Server
        EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Network")
                .setMessage("Please enter the server address you would like to connect to:")
                .setView(input)
                .setPositiveButton("OK", ((dialog, which) -> {
                    serverAddress = input.getText().toString();
                    client = new ClientController(serverAddress);
                    client.start();
                }))
                .show();
        //endregion

        // Set up PubSubBroker
        broker = Broker.getInstance();
        broker.subscribe("Testing", ((publisher, topic, params) -> {
            new AlertDialog.Builder(this)
                    .setMessage("Testing Successful")
                    .show();
        }));
    }

    public void startGame(View view){
        Intent intent = new Intent(this, this.getClass());
        startActivity(intent);
    }
}
