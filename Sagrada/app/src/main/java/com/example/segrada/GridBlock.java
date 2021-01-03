package com.example.segrada;

import java.io.Serializable;

public class GridBlock implements Serializable {
    private static final long serialVersionUID = 998L;

    private String color;
    private int value;
    private boolean set;

    public GridBlock (){
        this.color = "white";
        this.value = 0;
        this.set = false;
    }

    public GridBlock(String color) {
        this.color = color;
        this.value = 0;
        this.set = false;
    }

    public GridBlock(int value) {
        this.color = "grey";
        this.value = value;
        this.set = false;
    }

    private GridBlock(String color, int value, boolean set){
        this.color = color;
        this.value = value;
        this.set = set;
    }

    public com.example.segrada.GridBlock clone(){
        return new com.example.segrada.GridBlock(this.color, this.value, this.set);
    }

    //region Getters & Setters
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }
    //endregion
}
