package com.example.dardan.elearning;

import java.io.Serializable;

/**
 * Created by Dardan on 4/5/2016.
 */
public class Thing implements Serializable {
    private int id;
    private String text;
    private String imagePath;
    private int categoryId;

    public Thing(int id, String text, String imagePath, int categoryId) {
        this.id = id;
        this.text = text;
        this.imagePath = imagePath;
        this.categoryId = categoryId;
    }

    public Thing() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}