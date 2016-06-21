package com.training.startandroid.trapp.database.dao.interfaces;

import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.util.Constants;

import java.util.List;


public interface CatalogsDAO {

    public Constants.ResultAddStatusDatabase addCatalog(Catalog newCatalog);

    public Constants.ResultUpdateStatusDatabase updateCatalog(Catalog updateCatalog);

    public boolean removeCatalogById(final int catalogId);

    public List<Catalog> getAllCatalogs();

}
