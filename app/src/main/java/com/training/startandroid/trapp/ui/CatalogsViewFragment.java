package com.training.startandroid.trapp.ui;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.support.v7.view.ActionMode;
//import android.view.ActionMode;

import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.DAOFactory;
import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.ui.selection.SelectionHelper;
import com.training.startandroid.trapp.ui.selection.SelectionObserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class CatalogsViewFragment extends android.app.Fragment {

    private final String LOG_TAG = "CatalogsVFr";

    private final ActionModeCallback mActionModeCallback = new ActionModeCallback();
    private SelectableRecyclerViewAdapter mAdapter;
    private ActionMode mActionMode;
    private Context mContext;

    private static boolean isTablet(Context context) {

        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        return (xlarge || large);
    }

    private static int getDisplayColumns(Context context) {

        int columnCount = 1;
        if (isTablet(context)) {
            columnCount = 2;
        }

        return columnCount;
    }

    private List<Catalog> initializeListCatalogs(int count) {
        List<Catalog> listCatalogs = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Catalog newCatalog = new Catalog(i + 1, "Test Catalog " + (i + 1), new Date());
            listCatalogs.add(newCatalog);
        }
        return listCatalogs;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();

        mContext = activity.getApplicationContext();

        DatabaseConnection.initializeConnection(mContext);
        DatabaseConnection.openConnection();
//        List<Catalog> listCatalogs = DAOFactory.getCatalogsDAO().getAllCatalogs();

        List<Catalog> listCatalogs = initializeListCatalogs(50);
        mAdapter = new SelectableRecyclerViewAdapter(this, listCatalogs);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getDisplayColumns(mContext), StaggeredGridLayoutManager.VERTICAL));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        DatabaseConnection.closeConnection();
        super.onPause();
    }

    @Override
    public void onResume() {
        DatabaseConnection.openConnection();
        super.onResume();
        Log.d("Fragment", "onResume");
    }

    public void startActionMode() {
        MainActivity parentActivity = (MainActivity) getActivity();
        mActionMode = parentActivity.startSupportActionMode(mActionModeCallback);
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    private class ActionModeCallback implements ActionMode.Callback, SelectionObserver {

        private ActionMode mActionMode;

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            SelectionHelper selectionHelper = mAdapter.getSelectionHelper();
            selectionHelper.unregisterSelectionObserver(this);
            mActionMode = null;
            selectionHelper.setSelectable(false);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            mActionMode = actionMode;

            mActionMode.getMenuInflater().inflate(R.menu.catalog_selection, menu);
            mAdapter.getSelectionHelper().registerSelectionObserver(this);
            return true;

        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

            SelectionHelper selectionHelper = mAdapter.getSelectionHelper();

            switch (menuItem.getItemId()) {
                case R.id.action_remove:

                    HashSet<Integer> selectedPositions = selectionHelper.getSelectedItemsPositions();
                    for (Integer position : selectedPositions) {
//                        DAOFactory.getCatalogsDAO().removeCatalogById(position);
                    }
                    mAdapter.removeSelectedItems();

                    Toast.makeText(mContext,
                            "Remove selected catalogs", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.action_edit:

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    EditCatalogFragment editCatalogFragment = new EditCatalogFragment();
                    fragmentTransaction.add(editCatalogFragment, EditCatalogFragment.class.getName());
                    fragmentTransaction.commit();

                    break;

                case R.id.action_select_all:

                    mAdapter.selectAllItems();
                    break;

                case R.id.action_unselected_all:
                    mAdapter.clearSelectionsItems();
                    break;
            }

            return true;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder holder, boolean isSelected) {
            if (mActionMode != null) {
                int checkedCatalogsCount = mAdapter.getSelectionHelper().getSelectedItemsCount();
                mActionMode.setTitle(String.valueOf(checkedCatalogsCount));
            }
        }

        @Override
        public void onSelectableChanged(boolean isSelectable) {

            if (!isSelectable) {
                mActionMode.finish();
            }
        }
    }
}
