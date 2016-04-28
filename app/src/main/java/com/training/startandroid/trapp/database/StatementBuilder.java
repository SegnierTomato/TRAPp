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

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CATALOG_IMAGE = "DELETE FROM catalog_image WHERE id_catalog = ?";


    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORDS = "DELETE FROM words WHERE id_word = ?";

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORD_IMAGE = "DELETE FROM word_image WHERE id_word = ?";

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_WORD_SOUND = "DELETE FROM word_sound WHERE id_word = ?";

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_CATALOGS_AND_WORDS_RELATIONS = "DELETE FROM catalogs_and_words_relations WHERE id_catalog = ? AND id_word = ?";

    private static final String SQLITE_STATEMENT_DELETE_ROW_IN_TABLE_TRANSLATE_WORDS = "DELETE FROM translate_words WHERE id_translate = ?";


    private static final String SQLITE_STATEMENT_UPDATE_TABLE_CATALOGS = "UPDATE catalogs SET name = ? where id_catalog = ?";
    private static final String SQLITE_STATEMENT_UPDATE_TABLE_WORDS = "UPDATE words SET word = ? where id_word = ?";


    public static String getStatement(Constants.ActionStatement actionStatement) {

        switch (actionStatement) {
            case INSERT_NEW_CATALOG:
                return SQLITE_STATEMENT_INSERT_IN_TABLE_CATALOGS;

            case INSERT_NEW_WORD:
                break;

            case INSERT_NEW_TRANSLATE:
                break;

            case INSERT_NEW_CATALOG_AND_WORD_RELATION:
                break;

            case DELETE_CATALOG:
                break;

            case DELETE_WORD:
                break;

            case DELETE_TRANSLATED_WORD:
                break;

            case DELETE_GOOGLE_TRANSLATE:
                break;

            case DELETE_YANDEX_TRANSLATE:
                break;
            case DELETE_CUSTOM_TRANSLATE:
                break;

            case UPDATE_CATALOGS:
                return SQLITE_STATEMENT_UPDATE_TABLE_CATALOGS;
        }
        return null;
    }
}