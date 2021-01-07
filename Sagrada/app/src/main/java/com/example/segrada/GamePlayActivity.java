package com.example.segrada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.segrada.Die.Die;
import com.example.segrada.PubSubBroker.Broker;
import com.example.segrada.PubSubBroker.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        curFrag = frags.get(game.getPlayerNum()-1);
        loadFrag(curFrag);
        server.sendObject(new HashMap<String, Object>(){{put("topic", "GameStarted");}});
    }

    private void subToBroker(){
        broker.subscribe("StartRound", (publisher, topic, params) -> {
            Die draftPool = (Die)params.get("draftPool");
            draftPool.sort();
            game.setDraftPool(draftPool);

            rollDice();
        });
    }

    private void rollDice(){
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

    // Calc and display next frag
    public void nextFrag(View view){
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
}
