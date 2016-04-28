package com.training.startandroid.trapp.util;

/**
 * Created by Администратор on 20.04.2016.
 */
public final class Constants {

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


    public static final String CREATE_TABLE_CATALOGS_IMAGE = "CREATE TABLE catalogs_image( " +
            "id_catalog INTEGER PRIMARY KEY NOT NULL, " +
            "image_path TEXT NOT NULL COLLATE NOCASE, " +
            "FOREIGN KEY (id_catalog) REFERENCES catalogs(id_catalog) " +
            "ON DELETE CASCADE ON UPDATE NO ACTION )";

    public static final String CREATE_TABLE_WORDS_IMAGE = "CREATE TABLE words_image( " +
            "id_word INTEGER PRIMARY KEY NOT NULL, " +
            "image_path TEXT NOT NULL COLLATE NOCASE, " +
            "FOREIGN KEY (id_word) REFERENCES words(id_word) " +
            "ON DELETE CASCADE ON UPDATE NO ACTION )";

    public static final String CREATE_TABLE_WORDS_SOUND = "CREATE TABLE words_sound( " +
            "id_word INTEGER PRIMARY KEY NOT NULL, " +
            "sound_path TEXT NOT NULL COLLATE NOCASE, " +
            "FOREIGN KEY (id_word) REFERENCES words(id_word) " +
            "ON DELETE CASCADE ON UPDATE NO ACTION )";


    public static final String CREATE_SQLITE_TABLE_TRANSLATE_WORDS = "CREATE TABLE translate_words (\n" +
            "id_translate INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "translate_word TEXT NOT NULL COLLATE NOCASE,\n" +
            "part_of_speech TEXT COLLATE NOCASE,\n" +
            "created_at DATETIME DEFAULT CURRENT_TIMESTAMP\n" +
            ")";

    public static final String CREATE_SQLITE_TABLE_GOOGLE_TRANSLATE = "CREATE TABLE google_translate (\n" +
            "id_word INTEGER NOT NULL,\n" +
            "id_translate INTEGER NOT NULL,\n" +
            "PRIMARY KEY (id_word, id_translate),\n" +
            "FOREIGN KEY (id_word) REFERENCES words(id_word)\n" +
            "ON DELETE CASCADE\n" +
            "ON UPDATE CASCADE,\n" +
            "FOREIGN KEY (id_translate) REFERENCES translate_words(id_translate)\n" +
            "ON DELETE CASCADE\n" +
            "ON UPDATE NO ACTION\n" +
            ")";

    public static final String CREATE_SQLITE_TABLE_YANDEX_TRANSLATE = "CREATE TABLE yandex_translate (\n" +
            "id_word INTEGER NOT NULL,\n" +
            "id_translate INTEGER NOT NULL,\n" +
            "PRIMARY KEY (id_word, id_translate),\n" +
            "FOREIGN KEY (id_word) REFERENCES words(id_word)\n" +
            "ON DELETE CASCADE\n" +
            "ON UPDATE CASCADE,\n" +
            "FOREIGN KEY (id_translate) REFERENCES translate_words(id_translate)\n" +
            "ON DELETE CASCADE\n" +
            "ON UPDATE NO ACTION\n" +
            ")";

    public static final String CREATE_SQLITE_TABLE_CUSTOM_TRANSLATE = "CREATE TABLE custom_translate (\n" +
            "id_word INTEGER NOT NULL,\n" +
            "id_translate INTEGER NOT NULL,\n" +
            "PRIMARY KEY (id_word, id_translate),\n" +
            "FOREIGN KEY (id_word) REFERENCES words(id_word)\n" +
            "ON DELETE CASCADE\n" +
            "ON UPDATE CASCADE,\n" +
            "FOREIGN KEY (id_translate) REFERENCES translate_words(id_translate)\n" +
            "ON DELETE CASCADE\n" +
            "ON UPDATE NO ACTION\n" +
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
    public static int DB_VERSION = 1;


    public enum ActionStatement {
        INSERT_NEW_CATALOG,
        INSERT_NEW_WORD,
        INSERT_NEW_TRANSLATE,
        INSERT_NEW_CATALOG_AND_WORD_RELATION,
        DELETE_CATALOG,
        DELETE_WORD,
        DELETE_TRANSLATED_WORD,
        DELETE_GOOGLE_TRANSLATE,
        DELETE_YANDEX_TRANSLATE,
        DELETE_CUSTOM_TRANSLATE,
        UPDATE_CATALOGS
    }

    public static final String SQLITE_SELECT_QUERY_FROM_TABLE_CATALOGS = "SELECT * FROAM catalogs";

}