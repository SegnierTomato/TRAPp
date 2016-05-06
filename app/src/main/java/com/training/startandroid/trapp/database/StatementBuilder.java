package com.training.startandroid.trapp.database;

import com.training.startandroid.trapp.util.Constants;

/**
 * Created by Администратор on 28.04.2016.
 * <p/>
 * This class contain SQL PreparedStatement for inserting, updating and deleting information from all tables
 */

public class StatementBuilder {

    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_CATALOGS = "INSERT INTO catalogs (name) VALUES (?)";
    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_CATALOG_IMAGE = "INSERT INTO catalogs (id_catalog, image_path) VALUES (?,?)";

    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_WORDS = "INSERT INTO words (word) VALUES (?)";
    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_WORD_IMAGE = "INSERT INTO word_image (id_word, image_path) VALUES (?,?)";
    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_WORD_SOUND = "INSERT INTO word_sound (id_word, sound_path) VALUES (?,?)";

    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_CATALOGS_AND_WORDS_RELATIONS = "INSERT INTO catalogs_and_words_relations (id_catalog, id_word) VALUES (?,?)";


    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_TRANSLATE_WORDS_WITHOUT_PART_OF_SPEECH = "INSERT INTO translate_words (translate_word) VALUES (?)";
    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_TRANSLATE_WORDS_WITH_PART_OF_SPEECH = "INSERT INTO translate_words (translate_word, part_of_speech) VALUES (?,?)";

    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_GOOGLE_TRANSLATE = "INSERT INTO google_translate (id_word, id_translate) VALUES (?,?)";
    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_YANDEX_TRANSLATE = "INSERT INTO yandex_translate (id_word, id_translate) VALUES (?,?)";
    private static final String SQLITE_STATEMENT_INSERT_IN_TABLE_CUSTOM_TRANSLATE = "INSERT INTO custom_translate (id_word, id_translate) VALUES (?,?)";


    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CATALOGS = "DELETE FROM catalogs WHERE id_catalog = ?";
    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORDS = "DELETE FROM words WHERE id_word = ?";

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CATALOGS_AND_WORDS_RELATIONS = "DELETE FROM catalogs_and_words_relations WHERE id_catalog = ? AND id_word = ?";

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_TRANSLATE_WORDS = "DELETE FROM translate_words WHERE id_translate = ?";
    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_GOOGLE_TRANSLATE = "DELETE FROM google_translate WHERE id_translate = ?";
    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_YANDEX_TRANSLATE = "DELETE FROM yandex_translate WHERE id_translate = ?";
    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CUSTOM_TRANSLATE = "DELETE FROM custom_translate WHERE id_translate = ?";

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CATALOG_IMAGE = "DELETE FROM catalog_image WHERE id_catalog = ?";

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORD_IMAGE = "DELETE FROM word_image WHERE id_word = ?";
    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORD_SOUND = "DELETE FROM word_sound WHERE id_word = ?";


    private static final String SQLITE_STATEMENT_UPDATE_TABLE_CATALOGS = "UPDATE catalogs SET name = ? where id_catalog = ?";
    private static final String SQLITE_STATEMENT_UPDATE_TABLE_CATALOG_IMAGE = "UPDATE catalog_image SET image_path = ? where id_catalog = ?";

    private static final String SQLITE_STATEMENT_UPDATE_TABLE_WORDS = "UPDATE words SET word = ? where id_word = ?";
    private static final String SQLITE_STATEMENT_UPDATE_TABLE_WORD_IMAGE = "UPDATE word_image SET image_path = ? where id_word = ?";
    private static final String SQLITE_STATEMENT_UPDATE_TABLE_WORD_SOUND = "UPDATE word_sound SET sound_path = ? where id_word = ?";

    private static final String SQLITE_STATEMENT_UPDATE_TABLE_TRANSLATED_WORDS = "UPDATE translate_words SET translate_word = ?, part_of_speech=? where id_translate = ?";


    public static String getStatement(Constants.ActionStatement actionStatement) {

        switch (actionStatement) {

            case INSERT_NEW_CATALOG:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_CATALOGS;

            case INSERT_NEW_WORD:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_WORDS;

            case INSERT_NEW_CATALOG_AND_WORD_RELATION:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_CATALOGS_AND_WORDS_RELATIONS;

            case INSERT_NEW_CATALOG_IMAGE:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_CATALOG_IMAGE;

            case INSERT_NEW_WORD_IMAGE:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_WORD_IMAGE;

            case INSERT_NEW_WORD_SOUND:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_WORD_SOUND;


            case INSERT_NEW_TRANSLATE_WITHOUT_PART_OF_SPEECH:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_TRANSLATE_WORDS_WITHOUT_PART_OF_SPEECH;

            case INSERT_NEW_TRANSLATE_WITH_PART_OF_SPEECH:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_TRANSLATE_WORDS_WITH_PART_OF_SPEECH;

            case INSERT_NEW_GOOGLE_TRANSLATE:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_GOOGLE_TRANSLATE;

            case INSERT_NEW_YANDEX_TRANSLATE:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_YANDEX_TRANSLATE;

            case INSERT_NEW_CUSTOM_TRANSLATE:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_CUSTOM_TRANSLATE;


            case DELETE_CATALOG:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CATALOGS;

            case DELETE_WORD:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORDS;

            case DELETE_TRANSLATED_WORD:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_TRANSLATE_WORDS;

            case DELETE_GOOGLE_TRANSLATE:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_GOOGLE_TRANSLATE;

            case DELETE_YANDEX_TRANSLATE:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_YANDEX_TRANSLATE;

            case DELETE_CUSTOM_TRANSLATE:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CUSTOM_TRANSLATE;


            case DELETE_WORD_IMAGE:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORD_IMAGE;

            case DELETE_WORD_SOUND:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORD_SOUND;

            case DELETE_CATALOG_IMAGE:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CATALOG_IMAGE;

            case DELETE_RELATIONS_BETWEEN_CATALOG_AND_WORD:
                return SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CATALOGS_AND_WORDS_RELATIONS;

            case UPDATE_CATALOG_NAME:
                return SQLITE_STATEMENT_UPDATE_TABLE_CATALOGS;

            case UPDATE_CATALOG_IMAGE:
                return SQLITE_STATEMENT_UPDATE_TABLE_CATALOG_IMAGE;

            case UPDATE_WORD:
                return SQLITE_STATEMENT_UPDATE_TABLE_WORDS;

            case UPDATE_WORD_IMAGE:
                return SQLITE_STATEMENT_UPDATE_TABLE_WORD_IMAGE;

            case UPDATE_WORD_SOUND:
                return SQLITE_STATEMENT_UPDATE_TABLE_WORD_SOUND;

            case UPDATE_TRANSLETED_WORD:
                return SQLITE_STATEMENT_UPDATE_TABLE_TRANSLATED_WORDS;

        }
        return null;
    }
}
