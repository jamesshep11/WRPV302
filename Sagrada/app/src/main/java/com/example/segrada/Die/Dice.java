package com.example.segrada.Die;

import com.example.segrada.MainActivity;

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

    public void move() {
        // Bounce off walls
        if ((x < width) || (x >= (MainActivity.SCREEN_WIDTH - width))) {
            dx = dx * -1;
        }
        if ((y < height) || (y >= (MainActivity.SCREEN_HEIGHT - height))) {
            dy = dy * -1;
        }

        // Move one step
        x = x + dx;
        y = y + dy;

        // Slow down
        if (dx < 0) dx += 0.5;
        else if (dx > 0) dx -= 0.5;
        if (dy < 0) dy += 0.5;
        else if (dy > 0) dy -= 0.5;

        // Check if stopped
        if (dx == 0 && dy == 0)
            rolling = false;
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

    public void setRandomViewVariable(){
        Random random = new Random();

        x = random.nextInt(MainActivity.SCREEN_WIDTH - width);
        y = random.nextInt(MainActivity.SCREEN_HEIGHT - height);
        dx = random.nextInt(11) - 5;
        dy = random.nextInt(11) - 5;
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
