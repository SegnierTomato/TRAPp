package com.training.startandroid.trapp.model;

import android.media.Image;
import android.provider.MediaStore;

import java.util.Date;
import java.util.List;

/**
 * Created by Администратор on 15.04.2016.
 */
public class Word {

    private int id;
    private String word;
    private Image image;
    private MediaStore.Audio sound;
    private Date date;

    private List<TranslationOfWord> translation_of_words_google;

    private List<TranslationOfWord> translation_of_words_yandex;
    private List<TranslationOfWord> translation_of_words_custom;

    public Word(int id, String word) {
        this.id = id;
        this.word = word;
    }

    public Word(int id, String word, Image image) {
        this.id = id;
        this.word = word;
        this.image = image;
    }

    public Word(int id, String word, Image image, MediaStore.Audio sound) {
        this.id = id;
        this.word = word;
        this.image = image;
        this.sound = sound;
    }

    public Word(int id, String word, Image image, MediaStore.Audio sound, List<TranslationOfWord> translation_of_words_google,
                List<TranslationOfWord> translation_of_words_yandex, List<TranslationOfWord> translation_of_words_custom) {

        this.id = id;
        this.word = word;
        this.image = image;
        this.sound = sound;
        this.translation_of_words_google = translation_of_words_google;
        this.translation_of_words_yandex = translation_of_words_yandex;
        this.translation_of_words_custom = translation_of_words_custom;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public MediaStore.Audio getSound() {
        return sound;
    }

    public void setSound(MediaStore.Audio sound) {
        this.sound = sound;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
