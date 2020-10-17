package com.example.androidsosv2;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import PubSubBroker.Broker;
import PubSubBroker.Subscriber;

public class Controller {
    private Context mainActivityContext;                //Context of main activity
    private Broker broker = Broker.getInstance();       //Broker instance

    private Button activeTile = null;                   // The tile being changed
    private String currentPlayer;
    private int player1Score, player2Score;             // Track players' scores
    private String[][] grid = new String[5][5];         // 2D array track the tile values. Easier to search for SOS sequences.
    private int turnCount;                              // Counts the number of turns taken. 25 turns => game over

    public Controller(Context context) {
        mainActivityContext = context;
        currentPlayer = "Player 1";
        player1Score = 0;
        player2Score = 0;
        turnCount = 0;

        // Initialize 2D array (we don't want null values)
        for (String[] x : grid)
            Arrays.fill(x, "");

        loadSubscribers();
    }

    private void loadSubscribers(){
        Subscriber tileClicked = (publisher, topic, params)
                -> tileClicked((View) publisher);
        Subscriber turnOver = (publisher, topic, params)
                -> onDoneClicked();

        broker.subscribe(tileClicked, "tileClicked");
        broker.subscribe(turnOver, "turnOver");
    }

    // Change value of tile when clicked
    private void tileClicked(View view){
            // Check if player has already changed another tile
            if (activeTile != null && activeTile != view){
                new AlertDialog.Builder(mainActivityContext)
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

    private void onDoneClicked(){
        // Ensure the player has made changes. Can't skip turn.
        if (activeTile == null){
            new AlertDialog.Builder(mainActivityContext)
                    .setTitle("What's wrong?")
                    .setMessage("You didn't make any changes. Don't give up now.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        // Get position of tile from Tag property and update 2D array
        String tile = activeTile.getTag().toString();
        grid[Integer.parseInt(String.valueOf(tile.charAt(0)))][Integer.parseInt(String.valueOf(tile.charAt(1)))] = activeTile.getText().toString();
        turnCount++;

        // Check for SOS sequences
        int pointsScored = findMatches(tile);

        // Did player score? Yes => Go again, No => Next player
        if (pointsScored > 0)
            if (currentPlayer.equals("Player 1")) {
                player1Score += pointsScored;
                Map<String, Object> params = new HashMap<>();
                params.put("score", Integer.toString(player1Score));
                broker.publish(this, "player1Scored", params);
            } else {
                player2Score += pointsScored;
                Map<String, Object> params = new HashMap<>();
                params.put("score", Integer.toString(player2Score));
                broker.publish(this, "player2Scored", params);
            }
        else
            nextPlayer();

        // Is the game over?
        if (isFinished())
            finishGame();

        // Deactivate tile so it can't be changed
        activeTile.setClickable(false);
        activeTile = null;
    }

    // Switch to the next player
    private void nextPlayer(){
        switch (currentPlayer){
            case "Player 1": currentPlayer = "Player 2";
                break;
            case "Player 2": currentPlayer = "Player 1";
                break;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("currentPlayer", currentPlayer);
        broker.publish(this, "nextPlayer", params);
    }

    // Check for SOS sequences and calculate points scored
    private int findMatches(String pos){
        int points = 0;
        String currentText = activeTile.getText().toString();   // Did user enter S or O

        if (currentText.equals("S"))
            points +=  checkS(pos); // Player entered S
        else
            points += checkO(pos);  // Player entered O

        return points;
    }

    // Check for SOS sequences if S was entered
    private int checkS(String pos){
        int points = 0;
        int x = Integer.parseInt(String.valueOf(pos.charAt(0)));
        int y = Integer.parseInt(String.valueOf(pos.charAt(1)));

        //Check left
        if (x-2 >= 0 && grid[x - 1][y].equals("O"))
            if (grid[x - 2][y].equals("S"))
                points++;

        //Check right
        if (x+2 <= 4 && grid[x + 1][y].equals("O"))
            if (grid[x + 2][y].equals("S"))
                points++;

        //Check Up
        if (y-2 >= 0 && grid[x][y - 1].equals("O"))
            if (grid[x][y - 2].equals("S"))
                points++;

        //Check Down
        if (y+2 <= 5 && grid[x][y + 1].equals("O"))
            if (grid[x][y + 2].equals("S"))
                points++;

        //Check UpLeft
        if (x-2 >= 0 && y-2 >= 0 && grid[x - 1][y - 1].equals("O"))
            if (grid[x - 2][y - 2].equals("S"))
                points++;

        //Check UpRight
        if (x+2 <= 4 && y-2 >= 0 && grid[x + 1][y - 1].equals("O"))
            if (grid[x + 2][y - 2].equals("S"))
                points++;

        //Check DownLeft
        if (x-2 >= 0 && y+2 <= 4 && grid[x - 1][y + 1].equals("O"))
            if (grid[x - 2][y + 2].equals("S"))
                points++;

        //Check DownLeft
        if (x+2 <= 4 && y+2 <= 4 && grid[x + 1][y + 1].equals("O"))
            if (grid[x + 2][y + 2].equals("S"))
                points++;


        return points;
    }

    // Check for SOS sequences if O was entered
    private int checkO(String pos){
        int points = 0;
        int x = Integer.parseInt(String.valueOf(pos.charAt(0)));
        int y = Integer.parseInt(String.valueOf(pos.charAt(1)));

        //Check horizontal
        if (x-1 >= 0 && grid[x - 1][y].equals("S"))
            if (x+1 <= 4 && grid[x + 1][y].equals("S"))
                points++;

        //Check vertical
        if (y-1 >= 0 && grid[x][y - 1].equals("S"))
            if (y+1 <= 4 && grid[x][y + 1].equals("S"))
                points++;

        //Check DiagLeft
        if (x-1 >= 0 && y-1 >= 0 && grid[x - 1][y - 1].equals("S"))
            if (x+1 <= 4 && y+1 <= 4 && grid[x + 1][y + 1].equals("S"))
                points++;

        //Check DiagRight
        if (x+1 <= 4 && y-1 >= 0 && grid[x + 1][y - 1].equals("S"))
            if (x-1 >= 0 && y+1 <= 4 && grid[x - 1][y + 1].equals("S"))
                points++;


        return points;
    }

    // Is the game over?
    private boolean isFinished(){
        return turnCount >= 25;
    }

    private void finishGame(){
        // Calculate the winner
        String message;
        if (player1Score > player2Score)
            message = "Player 1 is the WINNER!!!";
        else if (player1Score < player2Score)
            message = "Player 2 is the WINNER!!!";
        else
            message = "It's a DRAW!!!";

        // Display the winner
        new AlertDialog.Builder(mainActivityContext)
                .setTitle("Congratulations")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
