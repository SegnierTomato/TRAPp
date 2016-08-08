package com.training.startandroid.trapp.ui.fragments.catalogs;


import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
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
import com.training.startandroid.trapp.ui.MainActivity;
import com.training.startandroid.trapp.ui.selection.HolderClickObserver;
import com.training.startandroid.trapp.ui.selection.SelectionHelper;
import com.training.startandroid.trapp.ui.selection.SelectionObserver;
import com.training.startandroid.trapp.ui.fragments.words.WordsViewFragment;
import com.training.startandroid.trapp.util.Constants;
import com.training.startandroid.trapp.util.RecyclerViewConfiguration;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CatalogsViewFragment extends Fragment
        implements AddCatalogFragment.AddCatalogEventListener, EditCatalogFragment.EditCatalogEventListener, HolderClickObserver {

    private final String LOG_TAG = CatalogsViewFragment.class.getSimpleName();

    private final String[] bundleArgsKeysImageSize = {"reqHeight", "reqWidth"};

    private final String bundleArgsKeyForEditCatalogObject = "editCatalog";
    private final String bundleArgsKeyForWordsView = "openedCatalog";

    private final ActionModeCallback mActionModeCallback = new ActionModeCallback();
    private SelectableRecyclerViewAdapterForCatalogs mAdapter;
    private ActionMode mActionMode;
    private Context mContext;

    private int mImageHeight;
    private int mImageWidth;

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.recycler_view_container, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);

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

        mAdapter = new SelectableRecyclerViewAdapterForCatalogs(this, listCatalogs);

        mAdapter.getSelectionHelper().registerHolderClickObserver(this);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(RecyclerViewConfiguration.getDisplayColumns(mContext), StaggeredGridLayoutManager.VERTICAL));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        mRecyclerView.setAdapter(mAdapter);

        int[] imageSize = mAdapter.getImageSize();
        mImageHeight = imageSize[0];
        mImageWidth = imageSize[1];

        mFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d(LOG_TAG, "FAB onClick");

                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                AddCatalogFragment addFragment = new AddCatalogFragment();
                Bundle args = new Bundle();
                args.putInt(bundleArgsKeysImageSize[0], mImageHeight);
                args.putInt(bundleArgsKeysImageSize[1], mImageWidth);
                addFragment.setArguments(args);

                final String currentOperationTag = AddCatalogFragment.class.getName();

                fragmentTransaction.add(R.id.fragment_child_layout, addFragment, currentOperationTag);

                fragmentTransaction.addToBackStack(currentOperationTag);
                int result = fragmentTransaction.commit();

                if (result >= 0) {
                    addFragment.setAddCatalogEventListener(CatalogsViewFragment.this);
                } else {
                    Toast.makeText(mContext, Constants.MESSAGE_ERROR_RUN_FRAGMENT, Toast.LENGTH_SHORT).show();
                }


            }

        });


    }

    @Override
    public void onPause() {
        mAdapter.getSelectionHelper().unregisterHolderClickObserver(this);
        DatabaseConnection.closeConnection();
        super.onPause();

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
            mRecyclerView.setVisibility(View.VISIBLE);
            mFab.setVisibility(View.VISIBLE);

            mRecyclerView.invalidate();
            mFab.invalidate();

        } else {
            mRecyclerView.setVisibility(View.GONE);
            mFab.setVisibility(View.GONE);
        }
    }


    public void onHolderClick(RecyclerView.ViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        Catalog itemCatalog = mAdapter.getElement(adapterPosition);

        openCatalog(itemCatalog);
    }

    public boolean onHolderLongClick(RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    private void openCatalog(Catalog openedCatalog) {

        FragmentManager fragmentManager = getChildFragmentManager();
        WordsViewFragment wordsFragment = new WordsViewFragment();

        Bundle args = new Bundle();
        args.putSerializable(bundleArgsKeyForWordsView, openedCatalog);
        wordsFragment.setArguments(args);

//        int result = FragmentHelper.addFragmentInStack(fragmentManager, wordsFragment, R.id.fragment_child_layout);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final String operationTag = WordsViewFragment.class.getName();

        fragmentTransaction.add(R.id.fragment_child_layout, wordsFragment, operationTag);
        fragmentTransaction.addToBackStack(operationTag);
        int result = fragmentTransaction.commit();

        if (result < 0) {
            Toast.makeText(getContext(), Constants.MESSAGE_ERROR_RUN_FRAGMENT, Toast.LENGTH_SHORT).show();
        }
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
                        args.putSerializable(bundleArgsKeyForEditCatalogObject, editElement);
                        args.putInt(bundleArgsKeysImageSize[0], mImageHeight);
                        args.putInt(bundleArgsKeysImageSize[1], mImageWidth);

                        editCatalogFragment.setArguments(args);

                        fragmentTransaction.add(R.id.fragment_child_layout, editCatalogFragment, currentOperationTag);

                        fragmentTransaction.addToBackStack(currentOperationTag);
                        int result = fragmentTransaction.commit();

                        if (result >= 0) {
                            editCatalogFragment.setEditCatalogEventListener(CatalogsViewFragment.this);
                        } else {
                            Toast.makeText(getContext(), Constants.MESSAGE_ERROR_RUN_FRAGMENT, Toast.LENGTH_SHORT).show();
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
