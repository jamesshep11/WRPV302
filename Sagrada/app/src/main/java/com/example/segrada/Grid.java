package com.example.segrada;

import android.graphics.Color;
import java.io.Serializable;

public class Grid implements Serializable {
    private GridBlock[][] grid;

    public GridBlock getAt(int x, int y){
        return grid[x][y];
    }

    /*private boolean isValid(GridBlock block){

    }*/

    public GridBlock[][] getGrid() {
        return grid;
    }
}

class GridBlock {
    private Color color;
    private int value;
    private boolean set;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }
}
