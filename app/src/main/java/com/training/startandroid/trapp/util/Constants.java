package com.training.startandroid.trapp.util;


public final class Constants {

    private Constants(){
    }

    public static final String CREATE_SQLITE_TABLE_CATALOGS = "CREATE TABLE catalogs (" +
            "id_catalog INTEGER PRIMARY KEY AUTOINCREMENT , " +
            "name TEXT NOT NULL COLLATE NOCASE, " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP )";

    public static final String CREATE_SQLITE_TABLE_WORDS = "CREATE TABLE words (" +
            "id_word INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "word TEXT NOT NULL COLLATE NOCASE, " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP )";

    public static final String CREATE_SQLITE_TABLE_CATALOGS_AND_WORDS_RELATIONS = "CREATE TABLE catalogs_and_words_relations (" +
            "id_catalog INTEGER NOT NULL, " +
            "id_word INTEGER NOT NULL, " +
            "PRIMARY KEY (id_catalog, id_word), " +
            "FOREIGN KEY (id_catalog) REFERENCES catalogs(id_catalog) " +
            "ON DELETE CASCADE ON UPDATE NO ACTION, " +
            "FOREIGN KEY (id_word) REFERENCES words(id_word) " +
            "ON DELETE CASCADE ON UPDATE NO ACTION )";


    public static final String CREATE_TABLE_CATALOGS_IMAGE = "CREATE TABLE catalogs_image ( " +
            "id_catalog INTEGER PRIMARY KEY NOT NULL, " +
            "image_path TEXT NOT NULL COLLATE NOCASE, " +
            "FOREIGN KEY (id_catalog) REFERENCES catalogs(id_catalog) " +
            "ON DELETE CASCADE ON UPDATE NO ACTION )";

    public static final String CREATE_TABLE_WORDS_IMAGE = "CREATE TABLE words_image ( " +
            "id_word INTEGER PRIMARY KEY NOT NULL, " +
            "image_path TEXT NOT NULL COLLATE NOCASE, " +
            "FOREIGN KEY (id_word) REFERENCES words(id_word) " +
            "ON DELETE CASCADE ON UPDATE NO ACTION )";

    public static final String CREATE_TABLE_WORDS_SOUND = "CREATE TABLE words_sound ( " +
            "id_word INTEGER PRIMARY KEY NOT NULL, " +
            "sound_path TEXT NOT NULL COLLATE NOCASE, " +
            "FOREIGN KEY (id_word) REFERENCES words(id_word) " +
            "ON DELETE CASCADE ON UPDATE NO ACTION )";


    public static final String CREATE_SQLITE_TABLE_TRANSLATE_WORDS = "CREATE TABLE translate_words ( " +
            "id_translate INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "translate_word TEXT NOT NULL COLLATE NOCASE, " +
            "part_of_speech TEXT COLLATE NOCASE, " +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP " +
            ")";

    public static final String CREATE_SQLITE_TABLE_GOOGLE_TRANSLATE = "CREATE TABLE google_translate ( " +
            "id_word INTEGER NOT NULL, " +
            "id_translate INTEGER NOT NULL, " +
            "PRIMARY KEY (id_word, id_translate), " +
            "FOREIGN KEY (id_word) REFERENCES words(id_word) " +
            "ON DELETE CASCADE " +
            "ON UPDATE CASCADE, " +
            "FOREIGN KEY (id_translate) REFERENCES translate_words(id_translate) " +
            "ON DELETE CASCADE " +
            "ON UPDATE NO ACTION " +
            ")";

    public static final String CREATE_SQLITE_TABLE_YANDEX_TRANSLATE = "CREATE TABLE yandex_translate ( " +
            "id_word INTEGER NOT NULL, " +
            "id_translate INTEGER NOT NULL, " +
            "PRIMARY KEY (id_word, id_translate), " +
            "FOREIGN KEY (id_word) REFERENCES words(id_word) " +
            "ON DELETE CASCADE " +
            "ON UPDATE CASCADE, " +
            "FOREIGN KEY (id_translate) REFERENCES translate_words(id_translate) " +
            "ON DELETE CASCADE " +
            "ON UPDATE NO ACTION " +
            ")";

    public static final String CREATE_SQLITE_TABLE_CUSTOM_TRANSLATE = "CREATE TABLE custom_translate (\n" +
            "id_word INTEGER NOT NULL, " +
            "id_translate INTEGER NOT NULL, " +
            "PRIMARY KEY (id_word, id_translate), " +
            "FOREIGN KEY (id_word) REFERENCES words(id_word) " +
            "ON DELETE CASCADE " +
            "ON UPDATE CASCADE, " +
            "FOREIGN KEY (id_translate) REFERENCES translate_words(id_translate) " +
            "ON DELETE CASCADE " +
            "ON UPDATE NO ACTION " +
            ")";


    public static final String CREATE_SQLITE_TRIGGER_DELETE_FREE_WORDS = "CREATE TRIGGER delete_free_word\n" +
            "  BEFORE DELETE\n" +
            "  ON catalogs_and_words_relations FOR EACH ROW\n" +
            "  WHEN (SELECT COUNT(id_word) FROM catalogs_and_words_relations WHERE OLD.id_word = catalogs_and_words_relations.id_word) = 1\n" +
            "BEGIN\n" +
            "\tDELETE FROM words WHERE words.id_word = OLD.id_word;\n" +
            "END;";

    public static final String CREATE_SQLITE_TRIGGER_DELETE_FREE_GOOGLE_TRANSLATION = "CREATE TRIGGER delete_free_translation_for_google_translate\n" +
            "\tBEFORE DELETE\n" +
            "\tON google_translate FOR EACH ROW\n" +
            "\tWHEN \n" +
            "\t\n" +
            "\t(SELECT COUNT (id_translate) FROM\n" +
            "       (\n" +
            "       SELECT id_word, id_translate FROM google_translate WHERE google_translate.id_translate = OLD.id_translate \n" +
            "       UNION ALL\n" +
            "            SELECT id_word, id_translate FROM yandex_translate WHERE yandex_translate.id_translate = OLD.id_translate \n" +
            "            UNION ALL\n" +
            "                 SELECT id_word, id_translate FROM custom_translate WHERE custom_translate.id_translate = OLD.id_translate \n" +
            "                                                                                                             )) = 1\n" +
            "\t\n" +
            "BEGIN\n" +
            "\tDELETE FROM translate_words WHERE translate_words.id_translate = OLD.id_translate;\n" +
            "END;";

    public static final String CREATE_SQLITE_TRIGGER_DELETE_FREE_YANDEX_TRANSLATION = "CREATE TRIGGER delete_free_translation_for_yandex_translate\n" +
            "\tBEFORE DELETE\n" +
            "\tON yandex_translate FOR EACH ROW\n" +
            "\tWHEN \n" +
            "\t\n" +
            "\t(SELECT COUNT (id_translate) FROM\n" +
            "       (\n" +
            "       SELECT id_word, id_translate FROM google_translate WHERE google_translate.id_translate = OLD.id_translate \n" +
            "       UNION ALL\n" +
            "            SELECT id_word, id_translate FROM yandex_translate WHERE yandex_translate.id_translate = OLD.id_translate \n" +
            "            UNION ALL \n" +
            "                 SELECT id_word, id_translate FROM custom_translate WHERE custom_translate.id_translate = OLD.id_translate \n" +
            "                                                                                                             )) = 1\n" +
            "BEGIN\n" +
            "\tDELETE FROM translate_words WHERE translate_words.id_translate = OLD.id_translate;\n" +
            "END;";

    public static final String CREATE_SQLITE_TRIGGER_DELETE_FREE_CUSTOM_TRANSLATION = "CREATE TRIGGER delete_free_translation_for_custom_translate\n" +
            "\tBEFORE DELETE\n" +
            "\tON custom_translate FOR EACH ROW\n" +
            "\tWHEN \n" +
            "\t\n" +
            "\t(SELECT COUNT (id_translate) FROM\n" +
            "       (\n" +
            "       SELECT id_word, id_translate FROM google_translate WHERE google_translate.id_translate = OLD.id_translate \n" +
            "       UNION ALL\n" +
            "            SELECT id_word, id_translate FROM yandex_translate WHERE yandex_translate.id_translate = OLD.id_translate \n" +
            "            UNION ALL\n" +
            "                 SELECT id_word, id_translate FROM custom_translate WHERE custom_translate.id_translate = OLD.id_translate \n" +
            "                                                                                                             )) = 1\n" +
            "\n" +
            "BEGIN\n" +
            "\tDELETE FROM translate_words WHERE translate_words.id_translate = OLD.id_translate;\n" +
            "END;";

    public static final String DB_SQLITE_NAME = "dictionary.db";
    public static final int DB_VERSION = 1;

    public static final String MESSAGE_ERROR_RUN_FRAGMENT= "Can't run selected action. Please try again.";

    public enum ActionStatement {
        INSERT_NEW_CATALOG,
        INSERT_NEW_WORD,
        INSERT_NEW_CATALOG_AND_WORD_RELATION,
        INSERT_NEW_CATALOG_IMAGE,
        INSERT_NEW_WORD_IMAGE,
        INSERT_NEW_WORD_SOUND,

        INSERT_NEW_TRANSLATE_WITH_PART_OF_SPEECH,
        INSERT_NEW_TRANSLATE_WITHOUT_PART_OF_SPEECH,
        INSERT_NEW_GOOGLE_TRANSLATE,
        INSERT_NEW_YANDEX_TRANSLATE,
        INSERT_NEW_CUSTOM_TRANSLATE,

        DELETE_CATALOG,
        DELETE_WORD,

        DELETE_TRANSLATED_WORD,
        DELETE_GOOGLE_TRANSLATE,
        DELETE_YANDEX_TRANSLATE,
        DELETE_CUSTOM_TRANSLATE,

        DELETE_WORD_IMAGE,
        DELETE_WORD_SOUND,

        DELETE_CATALOG_IMAGE,

        DELETE_RELATIONS_BETWEEN_CATALOG_AND_WORD,

        UPDATE_CATALOG_NAME,
        UPDATE_CATALOG_IMAGE,
        UPDATE_WORD,
        UPDATE_WORD_IMAGE,
        UPDATE_WORD_SOUND,

        UPDATE_TRANSLETED_WORD
    }

    public enum ResultAddStatusDatabase {
        ADD_SUCCESSFUL,
        CAN_NOT_ADD_RECORD,
        CAN_NOT_ADD_IMAGE,
        CAN_NOT_ADD_SOUND,
        CAN_NOT_ADD_IMAGE_AND_SOUND
    }

    public enum ResultUpdateStatusDatabase{
        UPDATE_SUCCESSFUL,
        CAN_NOT_UPDATE_RECORD,
        CAN_NOT_UPDATE_IMAGE,
        CAN_NOT_UPDATE_SOUND,
        CAN_NOT_UPDATE_NAME,
        CAN_NOT_UPDATE_IMAGE_AND_SOUND
    }

    public enum SpecificDictionary{
        GOOGLE_DICTIONARY,
        YANDEX_DICTIONARY,
        CUSTOM_DICTIONARY
    }

    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_CATALOGS_AND_WORDS_RELATIONS = "SELECT id_word FROM catalogs_and_words_relations WHERE id_catalog=?";

    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_CATALOGS = "SELECT * FROM catalogs";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_CATALOG_IMAGE = "SELECT * FROM catalogs_image";

    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_WORDS_BY_ID_WORD = "SELECT * FROM words WHERE id_word=?";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_WORD_IMAGE_BY_ID_WORD = "SELECT image_path FROM words_image WHERE id_word=?";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_WORD_SOUND_BY_ID_WORD = "SELECT sound_path FROM words_sound WHERE id_word=?";

    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_WORDS = "SELECT * FROM words";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_WORD_IMAGE = "SELECT * FROM words_image";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_WORD_SOUND = "SELECT * FROM words_sound";

    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_GOOGLE_TRANSLATE = "SELECT id_translate FROM google_translate";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_YANDEX_TRANSLATE = "SELECT id_translate FROM yandex_translate";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_CUSTOM_TRANSLATE = "SELECT id_translate FROM custom_translate";

    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_GOOGLE_TRANSLATE_BY_ID_WORD = "SELECT id_translate FROM google_translate WHERE id_word=?";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_YANDEX_TRANSLATE_BY_ID_WORD = "SELECT id_translate FROM yandex_translate WHERE id_word=?";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_CUSTOM_TRANSLATE_BY_ID_WORD = "SELECT id_translate FROM custom_translate WHERE id_word=?";

    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_TRANSLATED_WORDS = "SELECT * FROM translate_words";
    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_TRANSLATED_WORDS_BY_ID_WORD = "SELECT * FROM translate_words WHERE id_translate =?";
}
