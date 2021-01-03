package com.example.segrada;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.segrada.PubSubBroker.Broker;

import java.util.ArrayList;
import java.util.Map;

public class GamePlay extends AppCompatActivity {

    private ArrayList<GamePlayFragment> frags;
    private GamePlayFragment curFrag;
    private Broker broker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        frags = new ArrayList<>();

        broker = Broker.getInstance();
        broker.subscribe("Init", (publisher, topic, params) -> init(params));

        broker.publish(this, "GameStarted", null);
    }

    private void init(Map<String, Object> params){
        // Initialize the fragments for each player
        int player = (int)params.get("player");
        frags.add(GamePlayFragment.newInstance(player == 1, (Grid)params.get("grid1"), (String)params.get("color1")));
        frags.add(GamePlayFragment.newInstance(player == 2, (Grid)params.get("grid2"), (String)params.get("color2")));
        frags.add(GamePlayFragment.newInstance(player == 3, (Grid)params.get("grid3"), (String)params.get("color3")));
        frags.add(GamePlayFragment.newInstance(player == 4, (Grid)params.get("grid4"), (String)params.get("color4")));

        // find this player's fragment and load it
        curFrag = frags.get(player-1);
        loadFrag(curFrag);
    }

    // Add fragment to container view
    private void loadFrag(GamePlayFragment frag){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragContainer, frag)
                .commit();
    }

    // Calc and display next frag
    private void nextFrag(){
        int pos = frags.indexOf(curFrag);   // pos of current frag in list
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
    private void prevFrag(){
        int pos = frags.indexOf(curFrag);   // pos of current frag in list
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
