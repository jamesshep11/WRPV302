package com.example.segrada;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.segrada.PubSubBroker.Broker;

import java.util.Map;

public class GamePlay extends AppCompatActivity {

    private GamePlayFragment frag1, frag2, frag3, frag4, curFrag;
    private Broker broker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        broker = Broker.getInstance();
        broker.subscribe("init", (publisher, topic, params) -> init(params));
    }

    private void init(Map<String, Object> params){
        // Initialize the fragments for each player
        frag1 = GamePlayFragment.newInstance((int)params.get("player") == 1, (Grid)params.get("grid1"));
        frag2 = GamePlayFragment.newInstance((int)params.get("player") == 2, (Grid)params.get("grid2"));
        frag3 = GamePlayFragment.newInstance((int)params.get("player") == 3, (Grid)params.get("grid3"));
        frag4 = GamePlayFragment.newInstance((int)params.get("player") == 4, (Grid)params.get("grid4"));

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
        if (curFrag == frag1)
            curFrag = frag2;
        else if (curFrag == frag2)
            curFrag = frag3;
        else if (curFrag == frag3)
            curFrag = frag4;
        else if (curFrag == frag4)
            curFrag = frag1;

        loadFrag(curFrag);
    }
}
