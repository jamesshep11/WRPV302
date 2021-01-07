package com.example.segrada.Die;

import java.io.Serializable;
import java.util.Random;

public class Dice implements Serializable {
    private static final long serialVersionUID = 996L;

    String color;
    int value;

    public Dice(String color) {
        this.color = color;
    }

    public void roll(){
        value = new Random().nextInt(6) + 1;
    }

    public String getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }
}
