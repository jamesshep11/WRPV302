package com.example.segrada;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.segrada.Die.DiceView;
import com.example.segrada.Die.Die;
import com.example.segrada.Die.Timer;
import com.example.segrada.PubSubBroker.Broker;

import java.util.HashMap;
import java.util.Map;

public class RollDiceActivity extends AppCompatActivity {

    private ClientController server = ClientController.getInstance(null);
    private Game game = Game.getInstance(null);
    private Broker broker = Broker.getInstance();
    private Die die;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_dice);

        die = game.getDraftPool();
        int roundNum = getIntent().getIntExtra("roundNum", 0);
        rollDice(roundNum);
    }

    boolean timeRunning;
    private void rollDice(int roundNum){
        // Add code to roll dice

        //region Display Round Number
        TextView txtNotice = findViewById(R.id.txtNotice2);
        txtNotice.setText(getString(R.string.Round, roundNum));
        txtNotice.setVisibility(View.VISIBLE);

        timeRunning = true;
        Runnable tick = () -> {
            runOnUiThread(() -> txtNotice.setVisibility(View.INVISIBLE));
            timeRunning = false;
        };
        Timer timer = new Timer( 4000, tick, timeRunning);
        timer.start();
        //endregion

        new AlertDialog.Builder(this)
                .setMessage("Imagine the dice were rolling.")
                .setOnDismissListener((param)-> {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("topic", "RoundStarted");
                    server.sendObject(params);
                    finish();
                })
                .show();
    }
}
