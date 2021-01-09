package com.example.segrada;

import com.example.segrada.Die.Dice;
import com.example.segrada.Die.Die;
import com.example.segrada.Grids.Grid;

import java.util.ArrayList;
import java.util.Map;

public class Game {

    static private Game instance;

    private ClientController server = ClientController.getInstance(null);

    static private ArrayList<GamePlayFragment> frags = new ArrayList<>();
    private String color;
    private ArrayList<Grid> grids = new ArrayList<>();
    private Die draftPool;
    private int thisPlayer;

    private Game(Map<String, Object> params) {
        // Extract the Grids from params
        grids.add((Grid)params.get("grid1"));
        grids.add((Grid)params.get("grid2"));
        grids.add((Grid)params.get("grid3"));
        grids.add((Grid)params.get("grid4"));

        // Extract this player's playerNum from params
        thisPlayer = (int)params.get("player");

        // Extract the colors from params
        color = (String)params.get("color"+(thisPlayer+1));
    }

    static public Game getInstance(Map<String, Object> params) {
        if (instance == null) {
            instance = new Game(params);
            initFrags();
        }

        return instance;
    }

    static private void initFrags(){
        // Initialize the fragments for each player
        frags.add(GamePlayFragment.newInstance(0));
        frags.add(GamePlayFragment.newInstance(1));
        frags.add(GamePlayFragment.newInstance(2));
        frags.add(GamePlayFragment.newInstance(3));
    }

    //region Getters & Setters
    public ArrayList<GamePlayFragment> getFrags(){
        return frags;
    }

    public ArrayList<Grid> getGrids() {
        return grids;
    }

    public String getColor() {
        return color;
    }

    public int getThisPLayer() {
        return thisPlayer;
    }

    public Die getDraftPool(){
        return draftPool;
    }

    public void setDraftPool(Die draftPool){
        this.draftPool = draftPool;
    }
    //endregion
}
