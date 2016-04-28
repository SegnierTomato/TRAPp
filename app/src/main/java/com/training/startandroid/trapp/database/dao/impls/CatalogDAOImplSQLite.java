package com.training.startandroid.trapp.database.dao.impls;

import com.training.startandroid.trapp.database.DBHelper;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.interfaces.CatalogsDAO;
import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.util.Constants;

import java.util.List;

/**
 * Created by Администратор on 16.04.2016.
 */
public class CatalogDAOImplSQLite implements CatalogsDAO {

    @Override
    public void addCatalog(Catalog newCatalog) {

        Object[] parameters = {newCatalog.getName()};

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        int id = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_CATALOG, parameters);
        newCatalog.setId(id);

    }

    @Override
    public boolean updateCatalog(Catalog catalog) {

        Object[] parameters = {catalog.getId(), catalog.getName()};
        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        int result = dbHelper.executeUpdateDeleteQuery(Constants.ActionStatement.UPDATE_CATALOGS, parameters);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean removeCatalogById(int catalogId) {

        Object[] parameters = {catalogId};
        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        int result = dbHelper.executeUpdateDeleteQuery(Constants.ActionStatement.DELETE_CATALOG, parameters);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Catalog> getAllCatalogs() {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        return null;
    }
}
