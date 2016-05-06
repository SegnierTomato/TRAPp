package com.training.startandroid.trapp.database.dao.interfaces;

import com.training.startandroid.trapp.model.TranslationOfWord;
import com.training.startandroid.trapp.util.Constants;

import java.util.List;

/**
 * Created by Администратор on 16.04.2016.
 */
public interface TranslationsWordDAO {

    public boolean add2SpecificDictionaryNewTranslationOfWord(Constants.SpecificDictionary specificDictionary, List<Integer> idWord, TranslationOfWord newTranslationOfWord);

    public boolean assignedTranslatedWords2SpecificDictionary(Constants.SpecificDictionary specificDictionary, List<Integer> idWord, List<Integer> listIdTranslatedWords);

    public boolean updateTranslatedOfWord(TranslationOfWord translationOfWord);

    public int removeTranslatedWordsById(List<Integer> listIdTranslatedWords);

    public int removeTranslatedWordsByIdFromSpecificDictionary(Constants.SpecificDictionary specificDictionary, List<Integer> listIdTranslatedWords);

    public List<TranslationOfWord> getTranslatedWordsByWordIdFromSpecificDictionary(Constants.SpecificDictionary specificDictionary, final int wordId);

    public List<TranslationOfWord> getTranslatedWordsFromSpecificDictionary(Constants.SpecificDictionary specificDictionary);

    public List<TranslationOfWord> getAllTranslatedWords();

}
