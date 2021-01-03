package com.example.segrada;

import java.io.Serializable;

public class Grid implements Serializable {
    private static final long serialVersionUID = 999L;

    private GridBlock[][] grid;

    public Grid() {
        this.grid = new GridBlock[4][5];
    }

    public void addBlock(int x, int y, GridBlock block){
        grid[x][y] = block;
    }

    public GridBlock getAt(int x, int y){
        return grid[x][y];
    }

    /*private boolean isValid(com.example.segrada.GridBlock block){

    }*/

    public GridBlock[][] getGrid() {
        return grid;
    }

    public Grid clone(){
        Grid newGrid = new Grid();
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 5; y++)
                newGrid.addBlock(x, y, grid[x][y].clone());

        return newGrid;
    }
}