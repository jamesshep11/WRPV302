package com.example.segrada.Grids;

import com.example.segrada.Die.Dice;

import java.io.Serializable;
import java.util.HashMap;

public class Grid implements Serializable {
    private static final long serialVersionUID = 999L;

    private GridBlock[][] gridBlocks;

    public Grid() {
        this.gridBlocks = new GridBlock[4][5];
    }

    public void addBlock(int x, int y, GridBlock block){
        gridBlocks[x][y] = block;
    }

    public GridBlock getAt(int x, int y){
        return gridBlocks[x][y];
    }

    public Grid findValid(Dice dice){
        for (int x = 0; x < gridBlocks.length; x++)
            for (int y = 0; y < gridBlocks[x].length; y++)
                gridBlocks[x][y].validate(dice);

        return this;
    }

    public GridBlock[][] getGridBlocks() {
        return gridBlocks;
    }

    public Grid clone(){
        Grid newGrid = new Grid();
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 5; y++)
                newGrid.addBlock(x, y, gridBlocks[x][y].clone());

        return newGrid;
    }
}