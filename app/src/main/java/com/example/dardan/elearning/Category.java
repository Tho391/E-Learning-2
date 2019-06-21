package com.example.dardan.elearning;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.dardan.elearning.Ultis.COLORS;
import static com.example.dardan.elearning.Ultis.THEMES;
import static com.example.dardan.elearning.Ultis.getRandom;

public class Category implements Serializable {
    private int id;
    private String title;
    private String imagePath;
    private int highScore = 0;
    private int color;
    private int theme;
    private ArrayList<Thing> things = new ArrayList<>();

    private Bitmap tempImage;

    public Bitmap getTempImage() {
        return tempImage;
    }

    public void setTempImage(Bitmap tempImage) {
        this.tempImage = tempImage;
    }

    public Category(int id, String title, String imagePath, int highScore, int color, int theme, ArrayList<Thing> things) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.highScore = highScore;
        this.color = color;
        this.theme = theme;
        this.things = things;
    }

    public Category() {
        highScore = 0;
        color = getRandom(COLORS);
        theme = getRandom(THEMES);
    }

    public Category(String title, String imagePath, ArrayList<Thing> things) {
        this.title = title;
        this.imagePath = imagePath;
        highScore = 0;
        color = getRandom(COLORS);
        theme = getRandom(THEMES);
        this.things = things;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public ArrayList<Thing> getThings() {
        return things;
    }

    public void setThings(ArrayList<Thing> things) {
        this.things = things;
    }
}
