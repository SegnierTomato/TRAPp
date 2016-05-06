package com.training.startandroid.trapp.database.dao.impls;

import android.database.Cursor;

import com.training.startandroid.trapp.database.DBHelper;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.interfaces.TranslationsWordDAO;
import com.training.startandroid.trapp.database.interfaces.CursorConverter;
import com.training.startandroid.trapp.model.TranslationOfWord;
import com.training.startandroid.trapp.util.Constants;
import com.training.startandroid.trapp.util.ConvertString2Date;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Администратор on 16.04.2016.
 */
public class TranslationsWordDAOImplSQLite implements TranslationsWordDAO {

    @Override
    public boolean add2SpecificDictionaryNewTranslationOfWord(Constants.SpecificDictionary specificDictionary, List<Integer> idWords, TranslationOfWord newTranslationOfWord) {

        newTranslationOfWord.setId(addNewTranslatedWord(newTranslationOfWord));

        List<Integer> idTranslatedWords = new ArrayList<>();

        for (Integer idWord : idWords) {
            idTranslatedWords.add(newTranslationOfWord.getId());
        }
        return assignedTranslatedWords2SpecificDictionary(specificDictionary, idWords, idTranslatedWords);
    }


    public boolean assignedTranslatedWords2SpecificDictionary(Constants.SpecificDictionary specificDictionary, List<Integer> idWord, List<Integer> listIdTranslatedWords) {

        if (idWord.size() != listIdTranslatedWords.size()) {
            return false;
        }

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        Object[][] parameters = new Object[listIdTranslatedWords.size()][];

        for (int i = 0; i < listIdTranslatedWords.size(); i++) {
            parameters[0] = new Object[]{listIdTranslatedWords.get(i), idWord.get(i)};
        }

        Constants.ActionStatement actionStatement;

        switch (specificDictionary) {
            case GOOGLE_DICTIONARY:
                actionStatement = Constants.ActionStatement.INSERT_NEW_GOOGLE_TRANSLATE;
                break;

            case YANDEX_DICTIONARY:
                actionStatement = Constants.ActionStatement.INSERT_NEW_YANDEX_TRANSLATE;
                break;

            case CUSTOM_DICTIONARY:
                actionStatement = Constants.ActionStatement.INSERT_NEW_CUSTOM_TRANSLATE;
                break;

            default:
                return false;
        }
        List<Integer> idResult = dbHelper.executeInsertQuery(actionStatement, parameters);

        if (idResult == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean updateTranslatedOfWord(TranslationOfWord translationOfWord) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        Object[][] paramaters = new Object[1][];
        paramaters[0] = new Object[]{translationOfWord.getWord(), translationOfWord.getTypeOfSpeech(), translationOfWord.getId()};

        final int result = dbHelper.executeUpdateDeleteQuery(Constants.ActionStatement.UPDATE_TRANSLETED_WORD, paramaters);

        if (result == -1) {
            return false;
        }

        return true;
    }

    @Override
    public int removeTranslatedWordsById(List<Integer> listIdTranslatedWords) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        Object[][] paramaters = new Object[listIdTranslatedWords.size()][];

        for (int i = 0; i < listIdTranslatedWords.size(); i++) {
            paramaters[i] = new Object[]{listIdTranslatedWords.get(i)};
        }

        return dbHelper.executeUpdateDeleteQuery(Constants.ActionStatement.DELETE_TRANSLATED_WORD, paramaters);
    }

    @Override
    public int removeTranslatedWordsByIdFromSpecificDictionary(Constants.SpecificDictionary specificDictionary, List<Integer> listIdTranslatedWords) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        Object[][] paramaters = new Object[listIdTranslatedWords.size()][];

        for (int i = 0; i < listIdTranslatedWords.size(); i++) {
            paramaters[i] = new Object[]{listIdTranslatedWords.get(i)};
        }

        Constants.ActionStatement actionStatement;
        switch (specificDictionary) {
            case GOOGLE_DICTIONARY:
                actionStatement = Constants.ActionStatement.DELETE_GOOGLE_TRANSLATE;
                break;
            case YANDEX_DICTIONARY:
                actionStatement = Constants.ActionStatement.DELETE_GOOGLE_TRANSLATE;
                break;
            case CUSTOM_DICTIONARY:
                actionStatement = Constants.ActionStatement.DELETE_GOOGLE_TRANSLATE;

            default:
                return -1;
        }

        return dbHelper.executeUpdateDeleteQuery(actionStatement, paramaters);
    }

    @Override
    public List<TranslationOfWord> getTranslatedWordsByWordIdFromSpecificDictionary(Constants.SpecificDictionary specificDictionary, final int wordId) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        final String selectQuery;
        switch (specificDictionary) {
            case GOOGLE_DICTIONARY:
                selectQuery = Constants.SQLITE_SELECT_QUERY_FROM_TABLE_GOOGLE_TRANSLATE_BY_ID_WORD;
                break;
            case YANDEX_DICTIONARY:
                selectQuery = Constants.SQLITE_SELECT_QUERY_FROM_TABLE_YANDEX_TRANSLATE_BY_ID_WORD;
                break;
            case CUSTOM_DICTIONARY:
                selectQuery = Constants.SQLITE_SELECT_QUERY_FROM_TABLE_CUSTOM_TRANSLATE_BY_ID_WORD;

            default:
                return null;
        }

        CursorConverter specificDictionaryConverter = new SpecificDictionaryConverter();
        List<Integer> listIdSpecDictWords = (List<Integer>) dbHelper.executeSelectQuery(selectQuery,
                new String[]{String.valueOf(wordId)}, specificDictionaryConverter);

        List<TranslationOfWord> listTranslatedWords = new ArrayList<>();

        CursorConverter translatedWordConverter = new TranslatedWordConverter();
        TranslationOfWord translatedWord;

        for (Integer idTranslatedWord : listIdSpecDictWords) {
            translatedWord = getTranslatedWordById(dbHelper, idTranslatedWord, translatedWordConverter);
            if (translatedWord != null) {
                listTranslatedWords.add(translatedWord);
            }
        }

        return listTranslatedWords;
    }

    private TranslationOfWord getTranslatedWordById(DBHelper dbHelper, final int idTranslatedWord, CursorConverter converter) {

//        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
//        CursorConverter converter = new TranslatedWordConverter();

        return (TranslationOfWord) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_TRANSLATED_WORDS_BY_ID_WORD,
                new String[]{String.valueOf(idTranslatedWord)}, converter);
    }

    @Override
    public List<TranslationOfWord> getTranslatedWordsFromSpecificDictionary(Constants.SpecificDictionary specificDictionary) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        final String selectQuery;
        switch (specificDictionary) {
            case GOOGLE_DICTIONARY:
                selectQuery = Constants.SQLITE_SELECT_QUERY_FROM_TABLE_GOOGLE_TRANSLATE;
                break;
            case YANDEX_DICTIONARY:
                selectQuery = Constants.SQLITE_SELECT_QUERY_FROM_TABLE_YANDEX_TRANSLATE;
                break;
            case CUSTOM_DICTIONARY:
                selectQuery = Constants.SQLITE_SELECT_QUERY_FROM_TABLE_CUSTOM_TRANSLATE;

            default:
                return null;
        }

        CursorConverter specificDictionaryConverter = new SpecificDictionaryConverter();
        List<Integer> listIdSpecDictWords = (List<Integer>) dbHelper.executeSelectQuery(selectQuery, null, specificDictionaryConverter);

        List<TranslationOfWord> listTranslatedWords = new ArrayList<>();
        CursorConverter translatedWordConverter = new TranslatedWordConverter();

        Set<Integer> listIdTranslatedWords = new HashSet<>(listIdSpecDictWords);
        Iterator<Integer> iterator = listIdTranslatedWords.iterator();

        while (iterator.hasNext()) {
            listTranslatedWords.add(getTranslatedWordById(dbHelper, iterator.next(), translatedWordConverter));
        }

        return listTranslatedWords;

    }

    @Override
    public List<TranslationOfWord> getAllTranslatedWords() {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        CursorConverter converter = new AllTranslatedWordsConverter();
        return (List<TranslationOfWord>) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_TRANSLATED_WORDS, null, converter);
    }

    private int addNewTranslatedWord(TranslationOfWord newTranslationOfWord) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        Object[][] parameters = new Object[1][];
        List<Integer> listTranslatedWordsId;

        if (newTranslationOfWord.getTypeOfSpeech() == null) {

            parameters[0] = new Object[]{newTranslationOfWord.getWord()};
            listTranslatedWordsId = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_TRANSLATE_WITHOUT_PART_OF_SPEECH, parameters);
        } else {
            parameters[0] = new Object[]{newTranslationOfWord.getWord(), newTranslationOfWord.getTypeOfSpeech()};
            listTranslatedWordsId = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_TRANSLATE_WITH_PART_OF_SPEECH, parameters);
        }

        if (listTranslatedWordsId == null) {
            return -1;
        }

        return listTranslatedWordsId.get(0);
    }
}

class SpecificDictionaryConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        List<Integer> idWords = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                idWords.add(cursor.getInt(0));
            }

        } catch (Exception ex) {

        } finally {
            cursor.close();
        }

        return idWords;
    }
}

class TranslatedWordConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        TranslationOfWord translatedWord = null;
        try {

            while (cursor.moveToNext()) {
                translatedWord = new TranslationOfWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), ConvertString2Date.convert(cursor.getString(3)));
            }


        } catch (Exception ex) {
            translatedWord = new TranslationOfWord(-1, null, null);
        } finally {
            cursor.close();
        }
        return translatedWord;
    }
}

class AllTranslatedWordsConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        List<TranslationOfWord> listWords = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                listWords.add(new TranslationOfWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), ConvertString2Date.convert(cursor.getString(3))));
            }

        } catch (Exception ex) {

        } finally {
            cursor.close();
        }

        return listWords;
    }
}
