package com.training.startandroid.trapp.model;

import android.media.Image;

import java.util.List;

/**
 * Created by Администратор on 15.04.2016.
 */
public class Catalog {

    private int id;
    private String name;
    private Image image;
    private List<Word> listWords;

    public Catalog(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Catalog(int id, String name, List<Word> listWords) {
        this.id = id;
        this.name = name;
        this.listWords = listWords;
    }

    public Catalog(int id, String name, List<Word> listWords, Image image) {
        this.id = id;

        this.name = name;
        this.listWords = listWords;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Word> getWords() {
        return listWords;
    }

    public void setWords(List<Word> words) {
        this.listWords = words;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
