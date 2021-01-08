package com.example.segrada.Die;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable {
    private static final long serialVersionUID = 996L;

    private float x;
    private float y;
    private float dx;
    private float dy;
    private int width = 100;
    private int height = 100;
    private String color;
    private int value;

    private boolean rolling;

    public Dice(String color) {
        this.color = color;
    }

    public void roll(){
        value = new Random().nextInt(6) + 1;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setViewVariables(float x, float y, float dx, float dy){
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    //region Getter & Setters
    public boolean isRolling(){
        return rolling;
    }

    public void setRolling(boolean rolling){
        this.rolling = rolling;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }
    //endregion
}
