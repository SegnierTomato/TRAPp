package com.training.startandroid.trapp.model;

/**
 * Created by Администратор on 15.04.2016.
 */
public class TranslationOfWord {

    private int id;
    private String word;
    private String type_of_speech;

    public TranslationOfWord(int id, String word) {
        this.id = id;
        this.word = word;
    }

    public TranslationOfWord(int id, String word, String type_of_speech) {
        this.id = id;
        this.word = word;
        this.type_of_speech = type_of_speech;
    }

    public String getType_of_speech() {
        return type_of_speech;
    }

    public void setType_of_speech(String type_of_speech) {
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
}
