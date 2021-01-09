package com.example.segrada;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.segrada.Die.DiceView;
import com.example.segrada.Die.Die;
import com.example.segrada.Grids.Grid;
import com.example.segrada.PubSubBroker.Broker;

import java.util.ArrayList;
import java.util.HashMap;

public class GamePlayActivity extends AppCompatActivity {

    private ClientController server = ClientController.getInstance(null);
    private Game game = Game.getInstance(null);
    private Broker broker = Broker.getInstance();

    private ArrayList<GamePlayFragment> frags = game.getFrags();
    private GamePlayFragment curFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        subToBroker();
        curFrag = frags.get(game.getThisPLayer());
        loadFrag(curFrag);

        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "GameStarted");
        server.sendObject(params);
    }

    private void subToBroker(){
        broker.subscribe("StartRound", (publisher, topic, params) -> {
            Die draftPool = (Die)params.get("draftPool");
            game.setDraftPool(draftPool);

            showRollingAnimation();
        });
        broker.subscribe("StartTurn", (publisher, topic, params) -> {
            int player = (int)params.get("player");
            GamePlayFragment thisPlayersFrag = frags.get(game.getThisPLayer());
            thisPlayersFrag.setActive(player == game.getThisPLayer());

            //loadFrag(curFrag);
            Log.i("Player Turn: ", Integer.toString(player));
        });
        broker.subscribe("ValidSlots", (publisher, topic, params) -> {
            Grid validSlots = (Grid) params.get("validSlots");

            GamePlayFragment frag = frags.get(game.getThisPLayer());
            frag.setGrid(validSlots);
            refreshFrag(frag);
        });
    }

    private void showRollingAnimation(){
        Intent intent = new Intent(this, RollDiceActivity.class);
        startActivity(intent);
        loadDieToView();
    }

    private void loadDieToView(){

    }

    // Add fragment to container view
    private void loadFrag(GamePlayFragment frag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragContainer, frag)
                .commit();
    }

    private void refreshFrag(GamePlayFragment frag){
        getSupportFragmentManager()
                .beginTransaction()
                .detach(frag)
                .attach(frag)
                .commit();
    }

    // Calc and display next frag
    public void nextFrag(View view){
        frags.get(game.getThisPLayer()).getGridView().getGrid().invalidateAll();
        int pos = curFrag.getFragNum();   // pos of current frag in list
        // find pos of next frag
        switch (pos){
            case 3: pos = 0;    // end of list -> loop around
                break;
            default: pos++;
        }

        curFrag = frags.get(pos);
        loadFrag(curFrag);
    }

    // Calc and display prev frag
    public void prevFrag(View view){
        frags.get(game.getThisPLayer()).getGridView().getGrid().invalidateAll();
        int pos = curFrag.getFragNum();   // pos of current frag in list
        // find pos of prev frag
        switch (pos){
            case 0: pos = 3;    // beginning of list -> loop around
                break;
            default: pos--;
        }

        curFrag = frags.get(pos);
        loadFrag(curFrag);
    }

    public void onDiceSelected(View view){
        DiceView diceView = (DiceView)view;
        diceView.setBorder("blue");

        new AlertDialog.Builder(this)
                .setMessage(diceView.getDice().getColor() + " " + diceView.getDice().getValue() + " selected.")
                .show();

        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "GetValidSlots");
        params.put("dice", diceView.getDice());
        params.put("grid", curFrag.getGridView().getGrid());
        server.sendObject(params);
    }
}
