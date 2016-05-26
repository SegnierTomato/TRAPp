package com.training.startandroid.trapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Catalog {

    private int id;

    private String name;
    private String imagePath;
    private Date date;
    private List<Word> listWords = new ArrayList<>();

    public Catalog(int id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Catalog(int id, String name, List<Word> listWords, Date date) {
        this.id = id;
        this.name = name;
        this.listWords = listWords;
        this.date = date;
    }

    public Catalog(int id, String name, List<Word> listWords, Date date, String imagePath) {

        this.id = id;
        this.name = name;
        this.listWords = listWords;
        this.date = date;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getDate() {
        return date;
    }
}
