package com.example.a48hourassignment;

public class Entry {
    private int type;
    private String date, text, image;
    private Boolean hasPhoto;

    public Entry(String image, String date, int type, String text) {
        this.image = image;
        this.date = date;
        this.type = type;
        this.text = text;
        this.hasPhoto = false;
    }

    //region Getters & Setters
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public Boolean hasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(Boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

    //endregion
}
