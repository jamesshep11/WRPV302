package com.example.androidsos;

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

    Button activeTile = null;
    TextView txtCurrentPlayer;
    TextView txtPlayer1Score;
    TextView txtPlayer2Score;
    String[][] grid = new String[5][5];

    int turnCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCurrentPlayer = findViewById(R.id.txtCurrentPlayer);
        txtCurrentPlayer.setText(R.string.Player1);
        txtPlayer1Score = findViewById(R.id.txtPlayer1Score);
        txtPlayer1Score.setText("0");
        txtPlayer2Score = findViewById(R.id.txtPlayer2Score);
        txtPlayer2Score.setText("0");

        for (String[] x : grid)
            Arrays.fill(x, "");
    }

    public void onInstructionsClicked(View view){
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }

    public void onTileClicked(View view){
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

    public void onDoneClicked(View view){
        if (activeTile == null){
            new AlertDialog.Builder(this)
                    .setTitle("What's worng?")
                    .setMessage("You didn't make any changes. Don't give up now.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        String tile = activeTile.getTag().toString();
        grid[Integer.parseInt(String.valueOf(tile.charAt(0)))][Integer.parseInt(String.valueOf(tile.charAt(1)))] = activeTile.getText().toString();
        turnCount++;

        int points = findMatches(tile);

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

        if (isFinished())
            finishGame();

        activeTile.setClickable(false);
        activeTile = null;
    }

    private void finishGame(){
        int score1 = Integer.parseInt(txtPlayer1Score.getText().toString());
        int score2 = Integer.parseInt(txtPlayer2Score.getText().toString());

        String message;
        if (score1 > score2)
            message = "Player 1 is the WINNER!!!";
        else if (score1 < score2)
            message = "Player 2 is the WINNER!!!";
        else
            message = "It's a DRAW!!!";

        new AlertDialog.Builder(this)
                .setTitle("Congratulations")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private boolean isFinished(){
        if (turnCount >= 25)
            return true;

        return false;
    }

    private void nextPlayer(){
        switch (txtCurrentPlayer.getText().toString()){
            case "Player 1": txtCurrentPlayer.setText(R.string.Player2);
                break;
            case "Player 2": txtCurrentPlayer.setText(R.string.Player1);
                break;
        }
    }

    private int findMatches(String pos){
        int points = 0;
        String currentText = activeTile.getText().toString();

        if (currentText == "S")
            points +=  checkS(pos);
        else
            points += checkO(pos);

        return points;
    }

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

    private int checkO(String pos){
        int points = 0;
        int x = Integer.parseInt(String.valueOf(pos.charAt(0)));
        int y = Integer.parseInt(String.valueOf(pos.charAt(1)));

        //Check X
        if (x-1 >= 0 && grid[x - 1][y].equals("S"))
            if (x+1 <= 4 && grid[x + 1][y].equals("S"))
                points++;

        //Check Y
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
