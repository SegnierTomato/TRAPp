package com.training.startandroid.trapp.model;

import java.util.Date;
import java.util.List;


public class Word {

    private int id;
    private String word;
    private String imagePath;
    private String soundPath;
    private Date date;

    private List<TranslationOfWord> translation_of_words_google;
    private List<TranslationOfWord> translation_of_words_yandex;
    private List<TranslationOfWord> translation_of_words_custom;

    public Word(String word) {
        this.word = word;
    }

    public Word(final String word, final String imagePath) {
        this.word = word;
        this.imagePath = imagePath;
    }

    public Word(String word, final String imagePath, final String soundPath) {
        this.word = word;
        this.imagePath = imagePath;
        this.soundPath = soundPath;
    }

    public Word(int id, String word, Date date) {
        this.id = id;
        this.word = word;
        this.date = date;
    }

    public Word(int id, String word, String imagePath, Date date) {
        this.id = id;
        this.word = word;
        this.imagePath = imagePath;
        this.date = date;
    }

    public Word(int id, String word, String imagePath, String soundPath, Date date) {
        this.id = id;
        this.word = word;
        this.imagePath = imagePath;
        this.soundPath = soundPath;
        this.date = date;
    }

    public Word(int id, String word, String imagePath, String soundPath, Date date, List<TranslationOfWord> translation_of_words_google,
                List<TranslationOfWord> translation_of_words_yandex, List<TranslationOfWord> translation_of_words_custom) {

        this.id = id;
        this.word = word;
        this.imagePath = imagePath;
        this.soundPath = soundPath;
        this.date = date;
        this.translation_of_words_google = translation_of_words_google;
        this.translation_of_words_yandex = translation_of_words_yandex;
        this.translation_of_words_custom = translation_of_words_custom;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
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

    public List<TranslationOfWord> getTranslationOfWordsGoogle() {
        return translation_of_words_google;
    }

    public void setListTraslatedWords(List<TranslationOfWord> translation_of_words_google) {
        this.translation_of_words_google = translation_of_words_google;
    }

    public List<TranslationOfWord> getTranslationOfWordsCustom() {
        return translation_of_words_custom;
    }

    public void setTranslationOfWordsCustom(List<TranslationOfWord> translation_of_words_custom) {
        this.translation_of_words_custom = translation_of_words_custom;
    }

    public List<TranslationOfWord> getTranslationOfWordsYandex() {
        return translation_of_words_yandex;
    }

    public void setTranslationOfWordsYandex(List<TranslationOfWord> translation_of_words_yandex) {
        this.translation_of_words_yandex = translation_of_words_yandex;
    }
}
