package com.training.startandroid.trapp.ui;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.DAOFactory;
import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.ui.selection.SelectionHelper;
import com.training.startandroid.trapp.ui.selection.SelectionObserver;
import com.training.startandroid.trapp.util.Constants;
import com.training.startandroid.trapp.util.FragmentHelper;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CatalogsViewFragment extends android.app.Fragment
        implements AddCatalogFragment.AddCatalogEventListener, EditCatalogFragment.EditCatalogEventListener {

    private final String LOG_TAG = CatalogsViewFragment.class.getSimpleName();

    private final ActionModeCallback mActionModeCallback = new ActionModeCallback();
    private SelectableRecyclerViewAdapter mAdapter;
    private ActionMode mActionMode;
    private Context mContext;

    private int mImageHeight;
    private int mImageWidth;

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
        List<Catalog> listCatalogs = DAOFactory.getCatalogsDAO().getAllCatalogs();

        mAdapter = new SelectableRecyclerViewAdapter(this, listCatalogs);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getDisplayColumns(mContext), StaggeredGridLayoutManager.VERTICAL));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(mAdapter);

        int[] imageSize = mAdapter.getImageSize();
        mImageHeight = imageSize[0];
        mImageWidth = imageSize[1];
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

    public int[] getImageSize() {
        return new int[]{mImageHeight, mImageWidth};
    }

    public void startActionMode() {
        MainActivity parentActivity = (MainActivity) getActivity();
        mActionMode = parentActivity.startSupportActionMode(mActionModeCallback);
    }

    @Override
    public void addNewCatalog(String catalogName, String imagePath) {

        Log.d(LOG_TAG, " addNewCatalog");
        Log.d(LOG_TAG, "" + imagePath);
        Catalog newCatalog = new Catalog(catalogName, imagePath);
        Constants.ResultAddStatusDatabase resultInserting = DAOFactory.getCatalogsDAO().addCatalog(newCatalog);
        Toast.makeText(mContext, resultInserting.toString(), Toast.LENGTH_SHORT).show();

        newCatalog.setDate(new Date());
        mAdapter.addNewItem(newCatalog);
    }

    public void editCatalog(Catalog editCatalog) {

        Constants.ResultUpdateStatusDatabase resultUpdating = DAOFactory.getCatalogsDAO().updateCatalog(editCatalog);
        Toast.makeText(mContext, resultUpdating.toString(), Toast.LENGTH_SHORT).show();

//        mAdapter.editItem();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                        Catalog selectedCatalog = mAdapter.getElement(position);
                        DAOFactory.getCatalogsDAO().removeCatalogById(selectedCatalog.getId());
                    }
                    mAdapter.removeSelectedItems();

                    Toast.makeText(mContext,
                            "Remove selected catalogs", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.action_edit:


                    HashSet<Integer> selectedItems = selectionHelper.getSelectedItemsPositions();
                    Iterator<Integer> iterator = selectedItems.iterator();

                    if (iterator.hasNext()) {
                        int positionEditItem = iterator.next();
                        Catalog editElement = mAdapter.getElement(positionEditItem);


                        FragmentManager fragmentManager = getFragmentManager();

//                        if () {
//                            = getFragmentManager();
//                        } else {
//                            getChildFragmentManager();
//                        }


                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        Fragment currentFragment = FragmentHelper.getHigherFragmentInStack(fragmentManager);

                        if (currentFragment != null) {

                            fragmentTransaction.hide(currentFragment);
                            final String currentOperationTag = EditCatalogFragment.class.getName();

//                            EditCatalogFragment editCatalogFragment = new EditCatalogFragment(editElement, mImageHeight, mImageWidth);
//                            fragmentTransaction.add(editCatalogFragment, currentOperationTag);

                            AddCatalogFragment addCatalogFragment = new AddCatalogFragment(mImageHeight, mImageWidth);
                            fragmentTransaction.add(addCatalogFragment, currentOperationTag);

                            FragmentTransaction tr = fragmentTransaction.addToBackStack(currentOperationTag);
                            if (tr == null) {
                                Log.d(LOG_TAG, "Fragment transaction = null");
                            }

                            fragmentTransaction.commit();

                        }
                    }

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
