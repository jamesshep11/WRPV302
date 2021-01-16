package com.example.segrada.Grids;

import com.example.segrada.Die.Dice;
import com.example.segrada.Score;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

    public Score calcScore(String color){
        Score score = new Score();
        int holes = 0;
        int playerColor = 0;
        int columns = 0;
        int count5 = 0;
        int count6 = 0;
        int[] colorCounts  = new int[] {0, 0, 0, 0, 0};

        // For every dice
        for (int x = 0; x < gridBlocks.length; x++)
            for (int y = 0; y < gridBlocks[x].length; y++) {
                GridBlock block = gridBlocks[x][y];
                if (!block.isSet())
                    holes--;    // -1 for every blank space
                else {
                    // Add the dice in the player's color
                    if (block.getColor().equals(color))
                        playerColor += block.getValue();

                    // Count the number of 5s and 6s on the board
                    if (block.getValue() == 5)
                        count5++;
                    else if (block.getValue() == 6)
                        count6++;

                    // Count each color on the board
                    switch (block.getColor()){
                            case "red": colorCounts[0]++;
                                break;
                            case "green": colorCounts[1]++;
                                break;
                            case "blue": colorCounts[2]++;
                                break;
                            case "yellow": colorCounts[3]++;
                                break;
                            case "purple": colorCounts[4]++;
                                break;
                        }
                }
            }

        // +5 for each col with no repeated colors
        ArrayList<String> colColors = new ArrayList<>();
        for (int y = 0; y < gridBlocks[0].length; y++) {
            for (int x = 0; x < gridBlocks.length; x++) {
                GridBlock block = gridBlocks[x][y];
                if (!colColors.contains(block.getColor())) {
                    colColors.add(block.getColor());
                }
                else break;
            }
            if (colColors.size() == gridBlocks.length) {
                columns += 5;
            }
            colColors.clear();
        }

        score.setHoles(holes);
        score.setPlayerColor(playerColor);
        score.setColumns(columns);

        // +2 for every unique pair of 5s or 6s
        // According to google it doesn't matter where they are on the board a 5 and a 6 can be considered a pair.
        // Each 5 and each 6 can only be paired once. e.g. You can't use the same 5 in two pairings.
        score.setPair56(Math.min(count5, count6) * 2);

        // +4 for every unique set of purple, red, yellow, green and blue
        // This follows the same logic as sets of 5s and 6s above
        score.setColorSets(Math.min(colorCounts[0], Math.min(colorCounts[1], Math.min(colorCounts[2], Math.min(colorCounts[3], colorCounts[4])))) * 4);

        score.calcScore();

        return score;
    }
}