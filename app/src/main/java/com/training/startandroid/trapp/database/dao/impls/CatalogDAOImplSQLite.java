package com.training.startandroid.trapp.database.dao.impls;

import android.database.Cursor;
import android.util.Log;

import com.training.startandroid.trapp.database.DBHelper;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.interfaces.CatalogsDAO;
import com.training.startandroid.trapp.database.interfaces.CursorConverter;
import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.util.Constants;
import com.training.startandroid.trapp.util.DateConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CatalogDAOImplSQLite implements CatalogsDAO {

    @Override
    public Constants.ResultStatusDatabase addCatalog(Catalog newCatalog) {

        Object[][] parameters = new Object[1][];
        parameters[1] = new Object[]{newCatalog.getName()};

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        List<Integer> listIdAdd2Catalog = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_CATALOG, parameters);

        if (listIdAdd2Catalog==null) {
            return Constants.ResultStatusDatabase.CAN_NOT_ADD_RECORD;
        }else{
            newCatalog.setId(listIdAdd2Catalog.get(0));
        }

        if (newCatalog.getImagePath() != null) {

            parameters[0] = new Object[]{newCatalog.getId(), newCatalog.getImagePath()};
            List<Integer> listIdCatalogAdd2Image = dbHelper.executeInsertQuery(Constants.ActionStatement.INSERT_NEW_CATALOG_IMAGE, parameters);

            if (listIdCatalogAdd2Image==null) {
                return Constants.ResultStatusDatabase.CAN_NOT_ADD_IMAGE;
            }
        }

        return Constants.ResultStatusDatabase.ADD_SUCCESSFUL;
    }

    @Override
    public boolean updateCatalogName(Catalog catalog) {

        Object[][] parameters = new Object[1][];
        parameters[0] = new Object[]{catalog.getName(), catalog.getId()};
        return updateDeleteCatalog(Constants.ActionStatement.UPDATE_CATALOG_NAME, parameters);
    }

    @Override
    public boolean updateCatalogImage(Catalog catalog) {

        Object[][] parameters = new Object[1][];

        if (catalog.getImagePath() == null) {
            parameters[0] = new Object[]{catalog.getId()};
            return updateDeleteCatalog(Constants.ActionStatement.DELETE_CATALOG_IMAGE, parameters);
        }

        parameters[0] = new Object[]{catalog.getImagePath(), catalog.getId()};
        return updateDeleteCatalog(Constants.ActionStatement.UPDATE_CATALOG_IMAGE, parameters);
    }

    @Override
    public boolean removeCatalogById(final int catalogId) {
        Object [][]parameters = new Object[1][];
        parameters[0] = new Object[]{catalogId};
        return updateDeleteCatalog(Constants.ActionStatement.DELETE_CATALOG, parameters);
    }

    @Override
    public List<Catalog> getAllCatalogs() {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        CursorConverter converter = new CatalogConverter();

        List<Catalog> listCatalogs = (List<Catalog>) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_CATALOGS, null, converter);

        if (listCatalogs.isEmpty()) {
            return listCatalogs;
        }

        CursorConverter catalogImageConverter = new CatalogImageConverter();
        Map<Integer, String> imagesPath = (Map<Integer, String>) dbHelper.executeSelectQuery(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_CATALOG_IMAGE, null, catalogImageConverter);
        Set<Integer> idCatalogs = imagesPath.keySet();

        Iterator<Catalog> iterator = listCatalogs.iterator();
        Catalog catalog;

        for (Integer id : idCatalogs) {
            while (iterator.hasNext()) {
                catalog = iterator.next();
                if (catalog.getId() == id.intValue()) {
                    catalog.setImagePath(imagesPath.get(id));
                    break;
                }
            }
        }


        return listCatalogs;
    }

    private boolean updateDeleteCatalog(Constants.ActionStatement actionStatement, Object[][] parameters) {

        DBHelper dbHelper = DatabaseConnection.getInstanceDBHelper();
        final int result = dbHelper.executeUpdateDeleteQuery(actionStatement, parameters);

//        return result != -1?true:false;
        return result!=-1;
    }

}

class CatalogConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        List<Catalog> listCatalogs = new ArrayList<>();

        if(cursor==null){
            return listCatalogs;
        }

        try {

            while (cursor.moveToNext()) {
                listCatalogs.add(new Catalog(cursor.getInt(0), cursor.getString(1), DateConverter.convertString2Date(cursor.getString(2))));
            }

        } catch (Exception ex) {
            Log.e("CatalogConverter", ex.toString());
        } finally {
            cursor.close();
        }

        return listCatalogs;
    }
}

class CatalogImageConverter implements CursorConverter {

    @Override
    public Object convert(Cursor cursor) {

        Map<Integer, String> imagesPath = new HashMap<>();

        if(cursor==null){
            return imagesPath;
        }

        try {
            while (cursor.moveToNext()) {
                imagesPath.put(cursor.getInt(0), cursor.getString(1));
            }

        } catch (Exception ex) {
            Log.e("CatalogImageConverter", ex.toString());
        } finally {
            cursor.close();
        }

        return imagesPath;
    }
}
