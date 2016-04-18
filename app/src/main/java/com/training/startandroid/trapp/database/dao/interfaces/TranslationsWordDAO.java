package com.training.startandroid.trapp.database.dao.interfaces;

import com.training.startandroid.trapp.model.TranslationOfWord;

import java.util.List;

/**
 * Created by Администратор on 16.04.2016.
 */
public interface TranslationsWordDAO {

    public void addGoogleTranslationOfWord(TranslationOfWord newTranslationOfWord);

    public void addYandexTranslationOfWord(TranslationOfWord newTranslationOfWord);

    public void addCustomTranslationOfWord(TranslationOfWord newTranslationOfWord);

    public void updateTranslationOfWord(TranslationOfWord translationOfWord);

    public void removeTranslationById(int translationId);

    public List<TranslationOfWord> getGoogleTranslationsByWordId(int wordId);

    public List<TranslationOfWord> getYandexTranslationsByWordId(int wordId);

    public List<TranslationOfWord> getCustomTranslationsByWordId(int wordId);

}
