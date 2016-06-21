package com.training.startandroid.trapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Catalog {

    private int id = -1;

    private String mName;
    private String mImagePath = null;
    private Date mDate = null;
    private List<Word> mListWords = new ArrayList<>();

    public Catalog(String name, String imagePath) {
        this.mName = name;
        this.mImagePath = imagePath;
    }

    public Catalog(int id, String name, Date date) {
        this.id = id;
        this.mName = name;
        this.mDate = date;
    }

    public Catalog(int id, String name, List<Word> listWords, Date date) {
        this.id = id;
        this.mName = name;
        this.mListWords = listWords;
        this.mDate = date;
    }

    public Catalog(int id, String name, List<Word> listWords, Date date, String imagePath) {

        this.id = id;
        this.mName = name;
        this.mListWords = listWords;
        this.mDate = date;
        this.mImagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public List<Word> getWords() {
        return mListWords;
    }

    public void setWords(List<Word> words) {
        this.mListWords = words;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }
}
