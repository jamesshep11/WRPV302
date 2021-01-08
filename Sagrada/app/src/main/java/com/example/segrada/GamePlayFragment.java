package com.example.segrada;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.segrada.Die.Dice;
import com.example.segrada.Die.DiceView;
import com.example.segrada.Die.Die;
import com.example.segrada.Grids.Grid;
import com.example.segrada.Grids.GridView;
import com.example.segrada.PubSubBroker.Broker;

import java.lang.reflect.Field;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamePlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamePlayFragment extends Fragment {
    private Broker broker;
    private Game game;

    private boolean active;
    private Grid grid;
    private GridView gridView;
    private String color;
    private int fragNum;


    public GamePlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fragNum the fragment number. Corresponds with the player number that this fragment represents.
     * @return A new instance of fragment GamePlayFragment.
     */
    public static GamePlayFragment newInstance(int fragNum) {
        GamePlayFragment fragment = new GamePlayFragment();
        fragment.game = Game.getInstance(null);
        fragment.broker = Broker.getInstance();

        fragment.fragNum = fragNum;
        fragment.grid = fragment.game.getGrids().get(fragNum);
        fragment.color = fragment.game.getColors().get(fragNum);
        fragment.active = false;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gridView = new GridView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subToBroker();

        renderDraftPool();

        //region Handle Player# header
        TextView txtPlayer = getView().findViewById(R.id.txtPlayer);
        txtPlayer.setText(getString(R.string.Player, fragNum+1));
        int colorResId;
        if (game.getPlayerNum() == fragNum)
            colorResId = getResId(color, R.color.class);
        else
            colorResId = getResId("white", R.color.class);
        txtPlayer.setBackgroundColor(getResources().getColor(colorResId));
        //endregion

        gridView.connectToUI();
        gridView.loadGrid(grid);
    }

    private void subToBroker(){

    }

    private void renderDraftPool(){
        Die draftPool = game.getDraftPool();
        for (int i = 0; i < draftPool.count(); i++){
            int diceId = getResId("diceView"+(i+1), R.id.class);
            DiceView dice = getView().findViewById(diceId);
            dice.setDice(draftPool.get(i));
        }
    }

    private int getResId(String rec, Class<?> aClass) {
        try {
            Field field = aClass.getDeclaredField(rec);
            return field.getInt(field);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getFragNum(){
        return fragNum;
    }
}
