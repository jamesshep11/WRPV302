package com.example.segrada;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.segrada.Die.Dice;
import com.example.segrada.Die.DiceView;
import com.example.segrada.Die.Die;
import com.example.segrada.Die.Timer;
import com.example.segrada.Grids.Grid;
import com.example.segrada.PubSubBroker.Broker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GamePlayActivity extends AppCompatActivity {

    private ClientController server = ClientController.getInstance(null);
    private Game game = Game.getInstance(null);
    private Broker broker = Broker.getInstance();

    static private ArrayList<GamePlayFragment> frags;
    private GamePlayFragment curFrag;
    private Map<String, Object> tempParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        subToBroker();
        initFrags();
        curFrag = frags.get(game.getThisPLayer());
        loadFrag(curFrag);

        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "GameStarted");
        server.sendObject(params);
    }

    private void subToBroker(){
        broker.subscribe("StartRound", (publisher, topic, params) -> {
            int roundNum = (int) params.get("round");
            Die draftPool = (Die)params.get("draftPool");
            game.setDraftPool(draftPool);

            showRollingAnimation(roundNum);
        });
        broker.subscribe("StartTurn", (publisher, topic, params) -> {
            int player = (int)params.get("player");
            GamePlayFragment thisPlayersFrag = frags.get(game.getThisPLayer());
            thisPlayersFrag.setActive(player == game.getThisPLayer());
            thisPlayersFrag.setSkippable(checkSkippable());
            refreshFrag(thisPlayersFrag);

            runOnUiThread(() -> showNotice(player));
        });
        broker.subscribe("diceSelected", (publisher, topic, params) -> onDiceSelected(params));
        broker.subscribe("ValidSlots", (publisher, topic, params) ->    {
            Grid validSlots = (Grid) params.get("validSlots");

            GamePlayFragment frag = frags.get(game.getThisPLayer());
            frag.setGrid(validSlots);
            refreshFrag(frag);
        });
        broker.subscribe("DicePlaced", (publisher, topic, params) -> dicePlaced(params));
        broker.subscribe("FinishGame", (publisher, topic, params) -> finishGame());
        broker.subscribe("ShowScores", (publisher, topic, params) -> {
            tempParams = params;
            Intent intent = new Intent(this, ResultsActivity.class);
            startActivity(intent);
        });
        broker.subscribe("getResults", (publisher, topic, params) -> {
            broker.publish(this, "loadResults", tempParams);
        });
    }

    private void initFrags(){
        // Initialize the fragments for each player
        frags = new ArrayList<>();
        frags.add(GamePlayFragment.newInstance( this, 0));
        frags.add(GamePlayFragment.newInstance( this, 1));
        frags.add(GamePlayFragment.newInstance( this, 2));
        frags.add(GamePlayFragment.newInstance( this, 3));
    }

    private void showRollingAnimation(int roundNum){
        Intent intent = new Intent(this, RollDiceActivity.class);
        intent.putExtra("roundNum", roundNum);
        startActivity(intent);
    }

    private void showNotice(int num){
        TextView txtNotice = findViewById(R.id.txtNotice);
        txtNotice.setText(getString(R.string.Turn, num+1));
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
                .commitAllowingStateLoss();
    }

    private void onDiceSelected(Map<String, Object> param){
        DiceView diceView = (DiceView)param.get("diceView");
        diceView.setBorder("blue");
        DiceView curDice = game.getCurDice();
        if (curDice != null)
            curDice.setBorder(null);
        game.setCurDice(diceView);

        new AlertDialog.Builder(this)
                .setMessage(diceView.getDice().getColor() + " " + diceView.getDice().getValue() + " selected.")
                .show();

        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "GetValidSlots");
        params.put("dice", diceView.getDice());
        params.put("grid", game.getGrids().get(game.getThisPLayer()));
        server.sendObject(params);
    }

    public void PlaceDice(View view){
        DiceView button = (DiceView) view;
        DiceView diceView = game.getCurDice();
        Dice dice = diceView.getDice();

        curFrag.getGridView().placeDiceView(button, dice);
        curFrag.getGridView().setClickable(false);
        curFrag.getGridView().getGrid().invalidateAll();

        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "DicePlaced");
        params.put("dice", game.getDraftPool().find(dice));
        params.put("grid", curFrag.getGridView().getGrid());
        server.sendObject(params);
    }

    private void dicePlaced(Map<String, Object> params){
        int player = (int) params.get("player");
        Grid grid = (Grid) params.get("grid");
        int dicePos = (int) params.get("dice");
        Dice dice = game.getDraftPool().get(dicePos);

        game.getDraftPool().remove(dice);
        game.setGrid(player, grid);

        frags.get(player).setGrid(grid);
        refreshFrag(curFrag);

        HashMap<String, Object> newParams = new HashMap<>();
        newParams.put("topic", "EndTurn");
        server.sendObject(newParams);
    }

    private void finishGame(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "CalcScores");
        params.put("grids", game.getGrids());
        server.sendObject(params);
    }

    // Calc and display next frag
    public void nextFrag(View view){
        frags.get(game.getThisPLayer()).getGridView().getGrid().invalidateAll();
        frags.get(game.getThisPLayer()).setSkippable(false);

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
        frags.get(game.getThisPLayer()).setSkippable(false);

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

    private boolean skippable;
    private boolean checkSkippable(){
        skippable = true;

        broker.subscribe("Skippable", (publisher, topic, params) -> {
            boolean canSkip = (boolean)params.get("skippable");
            if (!canSkip)
                skippable = false;
        });

        Die draftPool = game.getDraftPool();
        for (int i = 0; i < draftPool.count(); i++) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("topic", "CheckSkippable");
            params.put("dice", draftPool.get(i));
            params.put("grid", game.getGrids().get(game.getThisPLayer()));
            server.sendObject(params);
        }

        return skippable;
    }

    public void skipTurn(View view){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to skip your turn?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", (dialog, which) -> {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("topic", "SkipTurn");
                    server.sendObject(params);
                })
                .show();
    }
}
