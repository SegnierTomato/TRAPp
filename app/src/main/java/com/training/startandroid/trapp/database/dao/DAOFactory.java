package com.training.startandroid.trapp.database.dao;

import com.training.startandroid.trapp.database.DBHelper;
import com.training.startandroid.trapp.database.dao.impls.CatalogDAOImplSQLite;
import com.training.startandroid.trapp.database.dao.impls.TranslationsWordDAOImplSQLite;
import com.training.startandroid.trapp.database.dao.impls.WordsDAOImplSQLite;
import com.training.startandroid.trapp.database.dao.interfaces.CatalogsDAO;
import com.training.startandroid.trapp.database.dao.interfaces.TranslationsWordDAO;
import com.training.startandroid.trapp.database.dao.interfaces.WordsDAO;

/**
 * Created by Администратор on 15.04.2016.
 */
public class DAOFactory {

    private static CatalogsDAO catalogsDAO;
    private static TranslationsWordDAO translationsWordDAO;
    private static WordsDAO wordsDAO;

    private DAOFactory() {
    }

    /*
        Think about using Singleton in Factory pattern
        Now it is using
    */


    public synchronized static WordsDAO getWordsDAOInstance() {

        if (wordsDAO == null) {
            wordsDAO = new WordsDAOImplSQLite();
        }

        return wordsDAO;
    }

    public synchronized static CatalogsDAO getCatalogsDAO() {

        if (catalogsDAO == null) {
            catalogsDAO = new CatalogDAOImplSQLite();
        }

        return catalogsDAO;
    }

    public synchronized static TranslationsWordDAO getTranslationsWordDAO() {

        if (translationsWordDAO == null) {
            translationsWordDAO = new TranslationsWordDAOImplSQLite();
        }

        return translationsWordDAO;
    }


}
