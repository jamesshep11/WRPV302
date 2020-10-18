package com.example.androidsosv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import PubSubBroker.Broker;
import PubSubBroker.Subscriber;

public class MainActivity extends AppCompatActivity {

    private Broker broker = Broker.getInstance();
    private Controller gameController = new Controller(this);

    private Button activeTile = null;                   // The tile being changed
    //region Reference to the text views that will change
    TextView txtCurrentPlayer;
    TextView txtPlayer1TurnCount;
    TextView txtPlayer1SCount;
    TextView txtPlayer1OCount;
    TextView txtPlayer1Max;
    TextView txtPlayer1Score;
    TextView txtPlayer2TurnCount;
    TextView txtPlayer2SCount;
    TextView txtPlayer2OCount;
    TextView txtPlayer2Max;
    TextView txtPlayer2Score;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Get reference to TextView values
        txtCurrentPlayer = findViewById(R.id.txtCurrentPlayer);

        txtPlayer1TurnCount = findViewById(R.id.player1TurnCount);
        txtPlayer1SCount = findViewById(R.id.player1SCount);
        txtPlayer1OCount = findViewById(R.id.player1OCount);
        txtPlayer1Max = findViewById(R.id.player1Max);
        txtPlayer1Score = findViewById(R.id.txtPlayer1Score);

        txtPlayer2TurnCount = findViewById(R.id.player2TurnCount);
        txtPlayer2SCount = findViewById(R.id.player2SCount);
        txtPlayer2OCount = findViewById(R.id.player2OCount);
        txtPlayer2Max = findViewById(R.id.player2Max);
        txtPlayer2Score = findViewById(R.id.txtPlayer2Score);
        //endregion

        loadSubscribers();

        broker.publish(this, "newGame", null);
    }

    private void onStartGame(){
        activeTile = null;

        //region Add all button views to a list
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(findViewById(R.id.button1));
        buttons.add(findViewById(R.id.button2));
        buttons.add(findViewById(R.id.button3));
        buttons.add(findViewById(R.id.button4));
        buttons.add(findViewById(R.id.button5));
        buttons.add(findViewById(R.id.button6));
        buttons.add(findViewById(R.id.button7));
        buttons.add(findViewById(R.id.button8));
        buttons.add(findViewById(R.id.button9));
        buttons.add(findViewById(R.id.button10));
        buttons.add(findViewById(R.id.button11));
        buttons.add(findViewById(R.id.button12));
        buttons.add(findViewById(R.id.button13));
        buttons.add(findViewById(R.id.button14));
        buttons.add(findViewById(R.id.button15));
        buttons.add(findViewById(R.id.button16));
        buttons.add(findViewById(R.id.button17));
        buttons.add(findViewById(R.id.button18));
        buttons.add(findViewById(R.id.button19));
        buttons.add(findViewById(R.id.button20));
        buttons.add(findViewById(R.id.button21));
        buttons.add(findViewById(R.id.button22));
        buttons.add(findViewById(R.id.button23));
        buttons.add(findViewById(R.id.button24));
        buttons.add(findViewById(R.id.button25));
        //endregion

        for (Button x : buttons) {
            x.setText("");
            x.setClickable(true);
        }
    }

    private void loadSubscribers(){
        Subscriber currentPlayer = (publisher, topic, params)
                -> txtCurrentPlayer.setText((String) params.get("currentPlayer"));
        Subscriber updateOCount = (publisher, topic, params) -> {
            if (publisher.toString().equals("Player 1")) {
                txtPlayer1OCount.setText("O's placed: " + params.get("oCount"));
                txtPlayer1TurnCount.setText("Turns taken: " + params.get("turnCount"));
            }
            else {
                txtPlayer2OCount.setText(params.get("oCount") + " :O's placed");
                txtPlayer2TurnCount.setText(params.get("turnCount") + " :Turns taken");
            }
        };
        Subscriber updateSCount = (publisher, topic, params) -> {
            if (publisher.toString().equals("Player 1")){
                txtPlayer1SCount.setText("S's placed: " + params.get("sCount"));
                txtPlayer1TurnCount.setText("Turns taken: " + params.get("turnCount"));
            }
            else{
                txtPlayer2SCount.setText(params.get("sCount") + " :S's placed");
                txtPlayer2TurnCount.setText(params.get("turnCount") + " :Turns taken");
            }
        };
        Subscriber updateMax = (publisher, topic, params) -> {
            if (publisher.toString().equals("Player 1"))
                txtPlayer1Max.setText("Highest Score in 1 turn: " + params.get("max"));
            else
                txtPlayer2Max.setText(params.get("max") + " :Highest Score in 1 turn");
        };
        Subscriber updateScore = (publisher, topic, params) -> {
            if (publisher.toString().equals("Player 1"))
                txtPlayer1Score.setText("Score: " + params.get("score"));
            else
                txtPlayer2Score.setText(params.get("score") + " :Score");
        };
        Subscriber startGame = (publisher, topic, params)
                -> onStartGame();

        broker.subscribe(currentPlayer, "nextPlayer");
        broker.subscribe(updateScore, "updateScore");
        broker.subscribe(updateOCount, "updateOCount");
        broker.subscribe(updateSCount, "updateSCount");
        broker.subscribe(updateMax, "updateMax");
        broker.subscribe(startGame, "newGame");
    }

    // Launch Instructions activity
    public void onInstructionsClicked(View view){
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }

    // Change value of tile when clicked
    public void onTileClicked(View view){
        // Check if player has already changed another tile
        if (activeTile != null && activeTile != view){
            new AlertDialog.Builder(this)
                    .setTitle("Hold Up")
                    .setMessage("You can only change one tile per turn")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        // Player hasn't changed any other tiles yet. This change is legal.
        activeTile = (Button) view;
        switch(activeTile.getText().toString()){
            case "": activeTile.setText("S");
                break;
            case "S": activeTile.setText("O");
                break;
            case "O": activeTile.setText("");
                activeTile = null;
                break;
        }
    }

    // End turn
    public void onDoneClicked(View view){
        // Ensure the player has made changes. Can't skip turn.
        if (activeTile == null){
            new AlertDialog.Builder(this)
                    .setTitle("What's wrong?")
                    .setMessage("You didn't make any changes. Don't give up now.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        // Get letter entered
        String letter = activeTile.getText().toString();
        // Get position of tile from Tag property
        String tile = activeTile.getTag().toString();

        // Broadcast message 'TurnOver'
        Map<String, Object> params = new HashMap<>();
        params.put("letter", letter);
        params.put("tilePos", tile);
        params.put("player", txtCurrentPlayer.getText().toString());
        broker.publish(this, "turnOver", params);

        // Deactivate tile so it can't be changed
        activeTile.setClickable(false);
        activeTile = null;
    }
}
