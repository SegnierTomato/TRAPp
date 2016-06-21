package com.training.startandroid.trapp.database.dao.interfaces;

import com.training.startandroid.trapp.model.Word;
import com.training.startandroid.trapp.util.Constants;

import java.util.List;

public interface WordsDAO {

    public Constants.ResultAddStatusDatabase addWord(Word newWord);

    public boolean updateWord(Word word);

    public boolean updateWordImage(Word word);

    public boolean updateWordSound(Word word);

    public boolean removeWordsById(List<Integer> wordsId);

    public List<Word> getCatalogWordsByCatalogId(final int catalogId);

    public List<Word> getAllWords();

    public int moveWords2Catalog(final int idNewCatalog, List<Word> listWords, final int idOldCatalog);

    public int copyWords2Catalog(final int idNewCatalog, List<Word> listWords);

}
