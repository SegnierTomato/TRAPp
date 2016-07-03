package com.training.startandroid.trapp.ui;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
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

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CatalogsViewFragment extends Fragment
        implements AddCatalogFragment.AddCatalogEventListener, EditCatalogFragment.EditCatalogEventListener, FragmentManager.OnBackStackChangedListener {

    private final String LOG_TAG = CatalogsViewFragment.class.getSimpleName();

    private final String[] bundleArgsKeysForEditCatalogs = {"editCatalog", "reqHeight", "reqWidth"};
    private final String bundleArgKey = "isHasChildrenFragments";

    private final ActionModeCallback mActionModeCallback = new ActionModeCallback();
    private SelectableRecyclerViewAdapter mAdapter;
    private ActionMode mActionMode;
    private Context mContext;

    private int mImageHeight;
    private int mImageWidth;

    private RecyclerView recyclerView;

    private boolean isHasChildrenFragments = false;

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

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "onActivityCreated");

        Activity activity = getActivity();
        mContext = activity.getApplicationContext();

        DatabaseConnection.initializeConnection(mContext);
        DatabaseConnection.openConnection();
        List<Catalog> listCatalogs = DAOFactory.getCatalogsDAO().getAllCatalogs();

        mAdapter = new SelectableRecyclerViewAdapter(this, listCatalogs);

        final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getDisplayColumns(mContext), StaggeredGridLayoutManager.VERTICAL));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(mAdapter);

        int[] imageSize = mAdapter.getImageSize();
        mImageHeight = imageSize[0];
        mImageWidth = imageSize[1];

        Log.d("AddCatalogFragment", "hash Code CatalogsViewFragment fragments: " + this.hashCode());

        if (savedInstanceState != null) {
            isHasChildrenFragments = savedInstanceState.getBoolean(bundleArgKey);

            if (isHasChildrenFragments) {
                setVisible(false);
            } else {
                setVisible(true);
            }
        }

        final FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

    }

    @Override
    public void onBackStackChanged() {

        Log.d(LOG_TAG, "onBackStackChanged");

        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            setVisible(false);
            isHasChildrenFragments = true;
        } else {
            setVisible(true);
            isHasChildrenFragments = false;
        }

    }

//        OrientationEventListener orientationEventListener = new OrientationEventListener(getContext(), SensorManager.SENSOR_DELAY_NORMAL) {
//            @Override
//            public void onOrientationChanged(int orientation) {
////                recyclerView.VISIBLE(View.GONE);
//                Log.d(LOG_TAG, "orientation changed");
//            }
//        };
//
//        orientationEventListener.enable();


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        Log.d(LOG_TAG, "onConfigurationChanged");
//
//        switch(newConfig.orientation){
//            case Configuration.ORIENTATION_PORTRAIT:
//                break;
//
//            case Configuration.ORIENTATION_LANDSCAPE:
//                break;
//        }
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(bundleArgKey, isHasChildrenFragments);
    }

    @Override
    public void onPause() {
        DatabaseConnection.closeConnection();
        getChildFragmentManager().removeOnBackStackChangedListener(this);
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

    public void setVisible(boolean visible) {

        Log.d(LOG_TAG, "setVisible");

        if (visible) {
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
        }


    }

    public void setFlagHasChildrenFragments(boolean flag) {
        isHasChildrenFragments = flag;
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

                        final String currentOperationTag = EditCatalogFragment.class.getName();

                        mActionMode.finish();

                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        EditCatalogFragment editCatalogFragment = new EditCatalogFragment();

                        Bundle args = new Bundle();
                        args.putSerializable(bundleArgsKeysForEditCatalogs[0], editElement);
                        args.putInt(bundleArgsKeysForEditCatalogs[1], mImageHeight);
                        args.putInt(bundleArgsKeysForEditCatalogs[2], mImageWidth);

                        editCatalogFragment.setArguments(args);

                        fragmentTransaction.add(R.id.parent_for_children_fragments, editCatalogFragment, currentOperationTag);

                        fragmentTransaction.addToBackStack(currentOperationTag);
                        int result = fragmentTransaction.commit();

                        if (result >= 0) {
                            CatalogsViewFragment.this.setVisible(false);
                            isHasChildrenFragments = true;
                        } else {
                            Toast.makeText(getContext(), Constants.MESSAGE_ERROR_RUN_FRAGMENT, Toast.LENGTH_SHORT).show();
                        }

                        editCatalogFragment.setEditCatalogEventListener(CatalogsViewFragment.this);


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
