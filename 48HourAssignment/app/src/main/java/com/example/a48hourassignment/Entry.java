package com.example.a48hourassignment;

public class Entry {
    private int image, type;
    private String date, text;

    public Entry(int image, String date, int type, String text) {
        this.image = image;
        this.date = date;
        this.type = type;
        this.text = text;
    }

    //region Getters & Setters
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    //endregion
}
