package com.training.startandroid.trapp.database.dao.interfaces;

import com.training.startandroid.trapp.model.Word;

import java.util.List;

/**
 * Created by Администратор on 15.04.2016.
 */
public interface WordsDAO {

    public void addWord(Word newWord);

    public void updateWord(Word word);

    public void removeWordById(int wordId);

    public Word getWordById(int wordsId);

    public List<Word> getCatalogWordsByCatalogId(int catalogId);
}
