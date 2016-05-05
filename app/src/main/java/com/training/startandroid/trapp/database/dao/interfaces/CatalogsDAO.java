package com.training.startandroid.trapp.database.dao.interfaces;

import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.util.Constants;

import java.util.List;

/**
 * Created by Администратор on 15.04.2016.
 */
public interface CatalogsDAO {

    public Constants.ResultStatusDatabase addCatalog(Catalog newCatalog);

    public boolean updateCatalogName(Catalog catalog);

    public boolean updateCatalogImage(Catalog catalog);

    public boolean removeCatalogById(final int catalogId);

    public List<Catalog> getAllCatalogs();

}
