package com.example.segrada.Grids;

import com.example.segrada.Die.Dice;

import java.io.Serializable;
import java.util.HashMap;

public class Grid implements Serializable {
    private static final long serialVersionUID = 999L;

    private GridBlock[][] gridBlocks;
    private boolean firstDice;

    public Grid() {
        gridBlocks = new GridBlock[4][5];
        firstDice = true;
    }

    public void addBlock(int x, int y, GridBlock block){
        gridBlocks[x][y] = block;
    }

    public GridBlock getAt(int x, int y){
        return gridBlocks[x][y];
    }

    public Grid findValid(Dice dice){
        if (firstDice) {
            for (int x = 0; x < gridBlocks.length; x += gridBlocks.length-1)
                for (int y = 0; y < gridBlocks[x].length; y++)
                    gridBlocks[x][y].validate(dice);
            for (int x = 1; x < gridBlocks.length-1; x++)
                for (int y = 0; y < gridBlocks[x].length; y += gridBlocks[x].length-1)
                    gridBlocks[x][y].validate(dice);

            firstDice = false;
        } else {
            for (int x = 0; x < gridBlocks.length; x++)
                for (int y = 0; y < gridBlocks[x].length; y++)
                    gridBlocks[x][y].validate(dice);
        }

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