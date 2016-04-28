package com.training.startandroid.trapp.database.dao.interfaces;

import com.training.startandroid.trapp.model.Catalog;

import java.util.List;

/**
 * Created by Администратор on 15.04.2016.
 */
public interface CatalogsDAO {

    public void addCatalog(Catalog newCatalog);

    public boolean updateCatalog(Catalog catalog);

    public boolean removeCatalogById(int catalogId);

    public List<Catalog> getAllCatalogs();

}
