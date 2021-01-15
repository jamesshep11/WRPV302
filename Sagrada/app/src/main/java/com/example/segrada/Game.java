package com.example.segrada;

import com.example.segrada.Die.DiceView;
import com.example.segrada.Die.Die;
import com.example.segrada.Grids.Grid;

import java.util.ArrayList;
import java.util.Map;

public class Game {

    static private Game instance;

    private ClientController server = ClientController.getInstance(null);

    private String color;
    private ArrayList<Grid> grids = new ArrayList<>();
    private Die draftPool;
    private int thisPlayer;
    private DiceView curDice = null;

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
        if (instance == null)
            instance = new Game(params);

        return instance;
    }

    //region Getters & Setters
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

    public DiceView getCurDice() {
        return curDice;
    }

    public void setCurDice(DiceView curDice) {
        this.curDice = curDice;
    }

    //endregion
}
