package com.example.segrada;

import android.widget.Button;

import java.lang.reflect.Field;

public class GridView {

    private GamePlayFragment context;
    private Grid grid;
    private Button[][] buttons;

    private final int[] dieVals = {R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six};

    public GridView(GamePlayFragment context) {
        this.context = context;
        grid = new Grid();
        buttons = new Button[4][5];
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
                Button button = buttons[x][y];

                button.setBackground(context.getResources().getDrawable(dieVals[block.getValue()]));
                int colorResId = getResId(block.getColor(), R.color.class);
                button.setBackgroundTintList(context.getResources().getColorStateList(colorResId));
            }
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
