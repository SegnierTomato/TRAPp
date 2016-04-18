package com.training.startandroid.trapp.database.dao.impls;

import com.training.startandroid.trapp.database.dao.interfaces.WordsDAO;
import com.training.startandroid.trapp.model.Word;

import java.util.List;

/**
 * Created by Администратор on 16.04.2016.
 */
public class WordsDAOImplSQLite implements WordsDAO {

    @Override
    public void addWord(Word newWord) {

    }

    @Override
    public void updateWord(Word word) {

    }

    @Override
    public void removeWordById(int wordId) {

    }

    @Override
    public Word getWordById(int wordsId) {
        return null;
    }

    @Override
    public List<Word> getCatalogWordsByCatalogId(int catalogId) {
        return null;
    }
}
