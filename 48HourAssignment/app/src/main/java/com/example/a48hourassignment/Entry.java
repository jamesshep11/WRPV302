package com.example.a48hourassignment;

public class Entry {
    int image;
    String date, type, text;

    public Entry(int image, String date, String type, String text) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
