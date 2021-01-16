package com.example.segrada;

public class Score {
    private static final long serialVersionUID = 900L;

    private int playerNum;
    private int total;
    private int holes;
    private int playerColor;
    private int columns;
    private int pair56;
    private int colorSets;

    public Score() {
        playerNum = 0;
        total = 0;
        holes = 0;
        playerColor = 0;
        columns = 0;
        pair56 = 0;
        colorSets = 0;
    }

    public void calcScore(){
        total += holes + playerColor + columns + pair56 + colorSets;
    }

    //region Getters and Setters


    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getHoles() {
        return holes;
    }

    public void setHoles(int holes) {
        this.holes = holes;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getPair56() {
        return pair56;
    }

    public void setPair56(int pair56) {
        this.pair56 = pair56;
    }

    public int getColorSets() {
        return colorSets;
    }

    public void setColorSets(int colorSets) {
        this.colorSets = colorSets;
    }

    //endregion
}
