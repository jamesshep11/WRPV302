package com.example.segrada;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.segrada.Die.DiceView;
import com.example.segrada.Die.Die;
import com.example.segrada.PubSubBroker.Broker;

import java.util.Map;

public class RollDiceActivity extends AppCompatActivity {

    private Game game = Game.getInstance(null);
    private Broker broker = Broker.getInstance();
    private Die die;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_dice);

        die = game.getDraftPool();
        rollDice();
    }

    private void rollDice(){
        // Add code to roll dice

        new AlertDialog.Builder(this)
                .setMessage("Imagine the dice were rolling.")
                .setOnDismissListener((param)-> finish())
                .show();
    }
}
