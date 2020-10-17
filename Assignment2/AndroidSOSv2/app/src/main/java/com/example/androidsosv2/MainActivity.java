package com.example.androidsosv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import PubSubBroker.Broker;
import PubSubBroker.Subscriber;

public class MainActivity extends AppCompatActivity {

    private Broker broker = Broker.getInstance();
    private Controller gameController = new Controller(this);

    TextView txtCurrentPlayer;          //
    TextView txtPlayer1Score;           // Reference to the text views that will change
    TextView txtPlayer2Score;           //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get reference to TextView values
        txtCurrentPlayer = findViewById(R.id.txtCurrentPlayer);
        txtPlayer1Score = findViewById(R.id.txtPlayer1Score);
        txtPlayer2Score = findViewById(R.id.txtPlayer2Score);

        loadSubscribers();
    }

    private void loadSubscribers(){
        Subscriber currentPlayer = (publisher, topic, params)
                -> txtCurrentPlayer.setText((String) params.get("currentPlayer"));
        Subscriber player1Score = (publisher, topic, params)
                -> txtPlayer1Score.setText((String) params.get("score"));
        Subscriber player2Score = (publisher, topic, params)
                -> txtPlayer2Score.setText((String) params.get("score"));

        broker.subscribe(currentPlayer, "nextPlayer");
        broker.subscribe(player1Score, "player1Scored");
        broker.subscribe(player2Score, "player2Scored");
    }

    // Launch Instructions activity
    public void onInstructionsClicked(View view){
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }

    // Change value of tile when clicked
    public void onTileClicked(View view){
        broker.publish(view, "tileClicked", null);
    }

    // End turn
    public void onDoneClicked(View view){
        broker.publish(this, "turnOver", null);
    }
}
