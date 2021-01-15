package com.example.segrada.Grids;

import android.util.Log;
import android.widget.Button;

import com.example.segrada.Die.Dice;
import com.example.segrada.Die.DiceView;
import com.example.segrada.GamePlayFragment;
import com.example.segrada.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GridView {

    private GamePlayFragment context;
    private Grid grid;
    private DiceView[][] buttons;

    private final int[] dieVals = {R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six};

    public GridView(GamePlayFragment context) {
        this.context = context;
        grid = new Grid();
        buttons = new DiceView[4][5];
    }

    public void connectToUI(){
        // Row 1
        buttons[0][0] = context.getView().findViewById(R.id.button1);
        buttons[0][1] = context.getView().findViewById(R.id.button2);
        buttons[0][2] = context.getView().findViewById(R.id.button3);
        buttons[0][3] = context.getView().findViewById(R.id.button4);
        buttons[0][4] = context.getView().findViewById(R.id.button5);
        // Row 2
        buttons[1][0] = context.getView().findViewById(R.id.button6);
        buttons[1][1] = context.getView().findViewById(R.id.button7);
        buttons[1][2] = context.getView().findViewById(R.id.button8);
        buttons[1][3] = context.getView().findViewById(R.id.button9);
        buttons[1][4] = context.getView().findViewById(R.id.button10);
        // Row 3
        buttons[2][0] = context.getView().findViewById(R.id.button11);
        buttons[2][1] = context.getView().findViewById(R.id.button12);
        buttons[2][2] = context.getView().findViewById(R.id.button13);
        buttons[2][3] = context.getView().findViewById(R.id.button14);
        buttons[2][4] = context.getView().findViewById(R.id.button15);
        // Row 4
        buttons[3][0] = context.getView().findViewById(R.id.button16);
        buttons[3][1] = context.getView().findViewById(R.id.button17);
        buttons[3][2] = context.getView().findViewById(R.id.button18);
        buttons[3][3] = context.getView().findViewById(R.id.button19);
        buttons[3][4] = context.getView().findViewById(R.id.button20);
    }

    public void loadGrid(Grid grid){
        GridBlock[][] gridBlocks = grid.getGridBlocks();
        for (int x = 0; x <= 3; x++)
            for (int y = 0; y <= 4; y++){
                GridBlock block = gridBlocks[x][y];
                DiceView diceView = buttons[x][y];

                diceView.setBackground(context.getResources().getDrawable(dieVals[block.getValue()]));
                int colorResId = getResId(block.getColor(), R.color.class);
                diceView.setBackgroundTintList(context.getResources().getColorStateList(colorResId));

               diceView.setEnabled(block.isValid());
            }
        this.grid = grid;
    }

    public void placeDiceView(DiceView button, Dice dice){

        button.setBackground(context.getResources().getDrawable(dieVals[dice.getValue()]));
        int colorResId = getResId(dice.getColor(), R.color.class);
        button.setBackgroundTintList(context.getResources().getColorStateList(colorResId));

        for (int x = 0; x < buttons.length; x++)
            for (int y = 0; y < buttons[x].length; y++)
                if (buttons[x][y].getId() == button.getId()) {
                    grid.setBlock(grid.getAt(x, y), dice);
                    break;
                }
    }

    public void setClickable(boolean clickable){
        for (DiceView[] buttonss : buttons)
            for (DiceView button : buttonss)
                button.setEnabled(clickable);
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

    public Grid getGrid(){
        return grid;
    }

}
