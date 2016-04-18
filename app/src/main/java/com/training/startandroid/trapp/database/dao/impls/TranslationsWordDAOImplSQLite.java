package com.training.startandroid.trapp.database.dao.impls;

import com.training.startandroid.trapp.database.dao.interfaces.TranslationsWordDAO;
import com.training.startandroid.trapp.model.TranslationOfWord;

import java.util.List;

/**
 * Created by Администратор on 16.04.2016.
 */
public class TranslationsWordDAOImplSQLite implements TranslationsWordDAO {
    
    @Override
    public void addGoogleTranslationOfWord(TranslationOfWord newTranslationOfWord) {

    }

    @Override
    public void addYandexTranslationOfWord(TranslationOfWord newTranslationOfWord) {

    }

    @Override
    public void addCustomTranslationOfWord(TranslationOfWord newTranslationOfWord) {

    }

    @Override
    public void updateTranslationOfWord(TranslationOfWord translationOfWord) {

    }

    @Override
    public void removeTranslationById(int translationId) {

    }

    @Override
    public List<TranslationOfWord> getGoogleTranslationsByWordId(int wordId) {
        return null;
    }

    @Override
    public List<TranslationOfWord> getYandexTranslationsByWordId(int wordId) {
        return null;
    }

    @Override
    public List<TranslationOfWord> getCustomTranslationsByWordId(int wordId) {
        return null;
    }
}
