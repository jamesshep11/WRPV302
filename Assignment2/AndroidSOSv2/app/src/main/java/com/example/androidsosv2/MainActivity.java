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

public class MainActivity extends AppCompatActivity {

    Button activeTile = null;           // The tile being changed
    TextView txtCurrentPlayer;          //
    TextView txtPlayer1Score;           // Reference to the text views that will change
    TextView txtPlayer2Score;           //
    String[][] grid = new String[5][5]; // 2D array track the tile values. Easier to search for SOS sequences.

    int turnCount = 0;                  // Counts the number of turns taken. 25 turns => game over

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set initial TextView values
        txtCurrentPlayer = findViewById(R.id.txtCurrentPlayer);
        txtCurrentPlayer.setText(R.string.Player1);
        txtPlayer1Score = findViewById(R.id.txtPlayer1Score);
        txtPlayer1Score.setText("0");
        txtPlayer2Score = findViewById(R.id.txtPlayer2Score);
        txtPlayer2Score.setText("0");

        // Initialize 2D array (we don't want null values)
        for (String[] x : grid)
            Arrays.fill(x, "");
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

        // Get position of tile from Tag property and update 2D array
        String tile = activeTile.getTag().toString();
        grid[Integer.parseInt(String.valueOf(tile.charAt(0)))][Integer.parseInt(String.valueOf(tile.charAt(1)))] = activeTile.getText().toString();
        turnCount++;

        // Check for SOS sequences
        int points = findMatches(tile);

        // Did player score? Yes => Go again, No => Next player
        if (points > 0)
            if (txtCurrentPlayer.getText().toString().equals("Player 1")) {
                points += Integer.parseInt(txtPlayer1Score.getText().toString());
                txtPlayer1Score.setText(String.valueOf(points));
            } else {
                points += Integer.parseInt(txtPlayer2Score.getText().toString());
                txtPlayer2Score.setText(String.valueOf(points));
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

    // Calculate and display the winner
    private void finishGame(){
        // Get each player's score
        int score1 = Integer.parseInt(txtPlayer1Score.getText().toString());
        int score2 = Integer.parseInt(txtPlayer2Score.getText().toString());

        // Calculate the winner
        String message;
        if (score1 > score2)
            message = "Player 1 is the WINNER!!!";
        else if (score1 < score2)
            message = "Player 2 is the WINNER!!!";
        else
            message = "It's a DRAW!!!";

        // Display the winner
        new AlertDialog.Builder(this)
                .setTitle("Congratulations")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    // Is the game over?
    private boolean isFinished(){
        if (turnCount >= 25)
            return true;

        return false;
    }

    // Switch to the next player
    private void nextPlayer(){
        switch (txtCurrentPlayer.getText().toString()){
            case "Player 1": txtCurrentPlayer.setText(R.string.Player2);
                break;
            case "Player 2": txtCurrentPlayer.setText(R.string.Player1);
                break;
        }
    }

    // Check for SOS sequences and calculate points scored
    private int findMatches(String pos){
        int points = 0;
        String currentText = activeTile.getText().toString();   // Did user enter S or O

        if (currentText == "S")
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
}
