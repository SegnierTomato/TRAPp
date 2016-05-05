package com.training.startandroid.trapp.database.dao.impls;

import android.database.Cursor;

import com.training.startandroid.trapp.database.DBHelper;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.interfaces.WordsDAO;
import com.training.startandroid.trapp.database.interfaces.CursorConverter;
import com.training.startandroid.trapp.model.Word;
import com.training.startandroid.trapp.util.Constants;
import com.training.startandroid.trapp.util.ConvertString2Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Администратор on 16.04.2016.
 */
public class WordsDAOImplSQLite implements WordsDAO {

    @Override
    public Constants.ResultStatusDatabase addWord(Word newWord) {

        Object[][] parameters = new Object[1][];
        parameters[0] = new Object[]{newWord.getWord()};

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        List<Integer> listIdAddWords = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_WORD, parameters);

        if (listIdAddWords != null) {
            newWord.setId(listIdAddWords.get(0).intValue());
        } else {
            return Constants.ResultStatusDatabase.CAN_NOT_ADD_RECORD;
        }

        boolean hasImage = false;
        boolean hasSound = false;

        if (newWord.getImagePath() != null) {

            parameters[0] = new Object[]{newWord.getId(), newWord.getImagePath()};
            List<Integer> listIdAddImage2Word = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_WORD_IMAGE, parameters);
            hasImage = true;
        }

        if (newWord.getSoundPath() != null) {

            parameters[0] = new Object[]{newWord.getId(), newWord.getSoundPath()};
            List<Integer> listIdAddSound2Word = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_WORD_SOUND, parameters);
            hasSound = true;
        }


        if (hasImage && hasSound) {

        }

        if (listIdAddImage2Word == null && listIdAddSound2Word == null) {
            return Constants.ResultStatusDatabase.CAN_NOT_ADD_IMAGE_AND_SOUND;
        } else if (listIdAddImage2Word == null) {
            return Constants.ResultStatusDatabase.CAN_NOT_ADD_IMAGE;
        } else if (listIdAddSound2Word == null) {
            return Constants.ResultStatusDatabase.CAN_NOT_ADD_SOUND;
        }

        return Constants.ResultStatusDatabase.ADD_SUCCESSFUL;
    }

    @Override
    public boolean updateWord(Word word) {

        Object[][] parameters = new Object[1][];
        parameters[0] = new Object[]{word.getWord(), word.getId()};
        return updateDeleteWord(Constants.ActionStatement.UPDATE_WORD, parameters);
    }

    @Override
    public boolean updateWordImage(Word word) {

        Object[][] parameters = new Object[1][];

        if (word.getImagePath() == null) {
            parameters[0] = new Object[]{word.getId()};
            return updateDeleteWord(Constants.ActionStatement.DELETE_WORD_IMAGE, parameters)
        }

        parameters[0] = new Object[]{word.getImagePath(), word.getId()};
        return updateDeleteWord(Constants.ActionStatement.UPDATE_WORD_IMAGE, parameters);
    }

    @Override
    public boolean updateWordSound(Word word) {

        Object[][] parameters = new Object[1][];

        if (word.getSoundPath() == null) {
            parameters[0] = new Object[]{word.getId()};
            return updateDeleteWord(Constants.ActionStatement.DELETE_WORD_SOUND, parameters);
        }

        parameters[0] = new Object[]{word.getSoundPath(), word.getId()};
        return updateDeleteWord(Constants.ActionStatement.UPDATE_WORD_SOUND, parameters);
    }

    @Override
    public boolean removeWordsById(List<Integer> wordsId) {

        Object[][] parameters = new Object[wordsId.size()][];

        for(int i=0;i<wordsId.size();i++) {
            parameters[i] = new Object[]{wordsId.get(i)};
        }
        return updateDeleteWord(Constants.ActionStatement.DELETE_WORD, parameters);
    }

    @Override
    public List<Word> getCatalogWordsByCatalogId(final int catalogId) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        CursorConverter converterWordsId = new WordsIdConverter();
        List<Integer> listWordId = (List<Integer>) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_CATALOGS_AND_WORDS_RELATIONS,
                new String[]{String.valueOf(catalogId)}, converterWordsId);

        List<Word> listWords = new ArrayList<>();

        if (listWordId.isEmpty()) {
            return listWords;
        }

        CursorConverter wordConverter = new WordConverter();
        CursorConverter wordImageSoundConverter = new ImageSoundConverterByWordId();

        for (Integer wordId : listWordId) {
            String[] parameters = new String[]{wordId.toString()};

            String imagePath = (String) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_WORD_IMAGE_BY_ID_WORD, parameters, wordImageSoundConverter);
            String soundPath = (String) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_WORD_SOUND_BY_ID_WORD, parameters, wordImageSoundConverter);
            Word word = (Word) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_WORDS_BY_ID_WORD, parameters, wordConverter);

            if (!imagePath.isEmpty()) {
                word.setImagePath(imagePath);
            }

            if (!soundPath.isEmpty()) {
                word.setSoundPath(soundPath);
            }
            listWords.add(word);
        }

        return listWords;
    }

    @Override
    public List<Word> getAllWords() {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();

        CursorConverter converterAllWords = new AllWordsConverter();
        List<Word> listAllWords = (List<Word>) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_WORDS, null, converterAllWords);

        CursorConverter imageSoundConverter = new ImageSoundConverter();

        Map<Integer, String> listWordsPath = (Map<Integer, String>) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_WORD_IMAGE, null, imageSoundConverter);
        assignedWordImagesOrSoundsPath(listAllWords, listWordsPath);

        listWordsPath = (Map<Integer, String>) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_WORD_SOUND, null, imageSoundConverter);
        assignedWordImagesOrSoundsPath(listAllWords, listWordsPath);

        return listAllWords;
    }

    @Override
    public int moveWords2Catalog(int idNewCatalog, List<Word> listWords, int idOldCatalog) {

        /**
         * This note actual for methods moveWords2Catalog and copyWords2Catalog
         *
         *  Think about using transaction
         *  Re-write DBHelper.executeUpdateDelete method: add List<Word> for inserting
         *  and deleting data in Database.
         *
         */

        int result = copyWords2Catalog(idNewCatalog, listWords);

        if (result == -1) {
            return -1;
        }

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        Object[][] parameters = new Object[listWords.size()][];
        for (int i = 0; i < listWords.size(); i++) {
            parameters[i] = new Object[]{idOldCatalog, listWords.get(i).getId()};
        }

        result = dbHelper.executeUpdateDeleteQuery(Constants.ActionStatement.DELETE_RELATIONS_BETWEEN_CATALOG_AND_WORD, parameters);

        return result;
    }

    @Override
    public int copyWords2Catalog(int idNewCatalog, List<Word> listWords) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        Object[][] parameters = new Object[listWords.size()][];

        for (int i = 0; i < listWords.size(); i++) {
            parameters[i] = new Object[]{idNewCatalog, listWords.get(i).getId()};
        }
        List<Integer> listIdAddWords2Catalog = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_CATALOG_AND_WORD_RELATION, parameters);

        if (listIdAddWords2Catalog == null) {
            return -1;
        }
        return listIdAddWords2Catalog.size();

    }

    private boolean assignedWordImagesOrSoundsPath(List<Word> listWords, Map<Integer, String> paths) {

        try {
            Iterator<Word> iterator = listWords.iterator();
            Set<Integer> wordsIdHavePaths = paths.keySet();
            Word word;

            for (Integer wordId : wordsIdHavePaths) {

                while (iterator.hasNext()) {
                    word = iterator.next();
                    if (word.getId() == wordId.intValue()) {
                        word.setImagePath(paths.get(wordId));
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    private boolean updateDeleteWord(Constants.ActionStatement actionStatement, Object[][] parameters) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        final int count = dbHelper.executeUpdateDeleteQuery(actionStatement, parameters);

        if (count == -1) {
            return false;
        }

        return true;
    }
}

class WordsIdConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        List<Integer> wordsId = new ArrayList<>();

        try {

            while (cursor.moveToNext()) {
                wordsId.add(new Integer(cursor.getInt(0)));
            }

        } catch (Exception ex) {

        } finally {
            cursor.close();
        }
        return wordsId;
    }
}

class WordConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        Word word;

        try {
            word = new Word(cursor.getInt(0), cursor.getString(1), ConvertString2Date.convert(cursor.getString(2)));

        } catch (Exception ex) {
            word = new Word(-1, "", null);
        } finally {
            cursor.close();
        }
        return word;
    }
}

class ImageSoundConverterByWordId implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        String path;

        try {
            path = cursor.getString(0);

        } catch (Exception ex) {
            path = new String();
        } finally {
            cursor.close();
        }

        return path;
    }
}

class AllWordsConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        List<Word> listAllWords = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                Word word = new Word(cursor.getInt(0), cursor.getString(1), ConvertString2Date.convert(cursor.getString(2)));
                listAllWords.add(word);
            }
        } catch (Exception ex) {

        } finally {
            cursor.close();
        }
        return listAllWords;
    }
}

class ImageSoundConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        Map<Integer, String> listPaths = new HashMap<>();

        try {
            while (cursor.moveToNext()) {
                listPaths.put(cursor.getInt(0), cursor.getString(1));
            }
        } catch (Exception ex) {

        } finally {
            cursor.close();
        }

        return listPaths;
    }
}
