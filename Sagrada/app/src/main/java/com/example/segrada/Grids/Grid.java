package com.example.segrada.Grids;

import com.example.segrada.Die.Dice;

import java.io.Serializable;

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

    public void invalidateAll(){
        for (int x = 0; x < gridBlocks.length; x++)
            for (int y = 0; y < gridBlocks[x].length; y++)
                gridBlocks[x][y].setValid(false);
    }

    public boolean hasValid(){
        for (int x = 0; x < gridBlocks.length; x++)
            for (int y = 0; y < gridBlocks[x].length; y++)
                if (gridBlocks[x][y].isValid())
                    return true;

        return false;
    }

    public GridBlock getAt(int x, int y){
        return gridBlocks[x][y];
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

    public void setBlock(GridBlock block, Dice dice){
        block.setColor(dice.getColor());
        block.setValue(dice.getValue());
        block.setSet(true);
    }
}