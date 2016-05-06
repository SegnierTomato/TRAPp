package com.training.startandroid.trapp.model;

import com.training.startandroid.trapp.database.dao.interfaces.TranslationsWordDAO;

import java.util.Date;

/**
 * Created by Администратор on 15.04.2016.
 */
public class TranslationOfWord {


    private int id;
    private String word;
    private String type_of_speech;
    private Date date;

    public TranslationOfWord(final String word) {
        this.word = word;
    }

    public TranslationOfWord(final int id, String word) {
        this.id = id;
        this.word = word;
    }

    public TranslationOfWord(final int id, String word, String type_of_speech) {
        this.id = id;
        this.word = word;
        this.type_of_speech = type_of_speech;
    }

    public TranslationOfWord(final int id, String word, String type_of_speech, Date date) {
        this.id = id;
        this.word = word;
        this.type_of_speech = type_of_speech;
        this.date = date;
    }

    public String getTypeOfSpeech() {
        return type_of_speech;
    }

    public void setTypeOfSpeech(String type_of_speech) {
        this.type_of_speech = type_of_speech;
    }

    public String getWord() {

        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }
}
