package com.example.androidsosv2;

import android.app.Application;
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
    private Context mainActivityContext;                // Context of main activity
    private Broker broker = Broker.getInstance();       // Broker instance
    private Player player1;
    private Player player2;

    private Player currentPlayer;
    private String[][] grid = new String[5][5];         // 2D array track the tile values. Easier to search for SOS sequences.
    private int turnCount;                              // Counts the number of turns taken. 25 turns => game over

    Controller(Context context) {
        mainActivityContext = context;

        loadSubscribers();
    }

    private void onStartGame(){
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        if (currentPlayer != player1)
            nextPlayer();
        turnCount = 0;

        // Initialize 2D array (we don't want null values)
        for (String[] x : grid)
            Arrays.fill(x, "");
    }

    private void loadSubscribers(){
        Subscriber turnOver = (publisher, topic, params)
                -> onTurnOver((String)params.get("tilePos"), (String)params.get("letter"));
        Subscriber newGame = (publisher, topic, params)
                -> onStartGame();

        broker.subscribe(turnOver, "turnOver");
        broker.subscribe(newGame, "newGame");
    }

    private void onTurnOver(String tilePos, String letter){
        // Update 2D array
        grid[Integer.parseInt(String.valueOf(tilePos.charAt(0)))][Integer.parseInt(String.valueOf(tilePos.charAt(1)))] = letter;
        turnCount++;

        // Check for SOS sequences
        int pointsScored = findMatches(tilePos, letter);

        // Did player score? Yes => Go again, No => Next player
        if (pointsScored > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("score", pointsScored);
            broker.publish(currentPlayer, "playerScored", params);
        }
        else
            nextPlayer();

        // Is the game over?
        if (isFinished())
            finishGame();
    }

    // Switch to the next player
    private void nextPlayer(){
        if (currentPlayer == player1)
            currentPlayer = player2;
        else
            currentPlayer = player1;

        Map<String, Object> params = new HashMap<>();
        params.put("currentPlayer", currentPlayer.toString());
        broker.publish(this, "nextPlayer", params);
    }

    // Check for SOS sequences and calculate points scored
    private int findMatches(String pos, String letter){
        int points = 0;

        // Did user enter S or O
        if (letter.equals("S"))
            points += checkS(pos); // Player entered S
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
        if (player1.getScore() > player2.getScore())
            message = "Player 1 is the WINNER!!!";
        else if (player1.getScore() < player2.getScore())
            message = "Player 2 is the WINNER!!!";
        else
            message = "It's a DRAW!!!";

        // Display the winner
        new AlertDialog.Builder(mainActivityContext)
                .setTitle("Congratulations")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        broker.publish(this, "newGame", null);
                    }
                })
                .show();
    }
}
