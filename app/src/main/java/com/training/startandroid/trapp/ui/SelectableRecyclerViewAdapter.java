package com.training.startandroid.trapp.ui;


import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.ui.selection.HolderClickObserver;
import com.training.startandroid.trapp.ui.selection.SelectionHelper;
import com.training.startandroid.trapp.ui.selection.SelectionObserver;
import com.training.startandroid.trapp.util.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class SelectableRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements HolderClickObserver, SelectionObserver {

    private List<Catalog> mListCatalogs;
    private SelectionHelper mSelectionHelper;
    private MainActivity mActivity;

    public SelectableRecyclerViewAdapter(MainActivity activity, List<Catalog> listCatalogs) {

        mActivity = activity;

        if (listCatalogs != null) {
            this.mListCatalogs = listCatalogs;
        } else {
            this.mListCatalogs = new ArrayList<>();
        }

        mSelectionHelper = new SelectionHelper();
        mSelectionHelper.registerSelectionObserver(this);
    }

    public SelectionHelper getSelectionHelper() {
        return mSelectionHelper;
    }

    @Override
    public int getItemCount() {
        return mListCatalogs.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CatalogHolder holder = new CatalogHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.catalog_cardview, viewGroup, false));
        return mSelectionHelper.wrapSelectable(holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        CatalogHolder catalogHolder = (CatalogHolder) viewHolder;
        catalogHolder.bindInfo(mListCatalogs.get(position));

        Checkable view = (Checkable) viewHolder.itemView;
        view.setChecked(mSelectionHelper.isItemSelected(position));
        mSelectionHelper.bindHolder(catalogHolder, position);
    }

    public void removeSelectedItems() {

        HashSet<Integer> selectedPositions = mSelectionHelper.getSelectedItemsPositions();

        TreeSet<Integer> sortedPositions = new TreeSet<>(selectedPositions);
        Iterator iterator = sortedPositions.descendingIterator();

        while (iterator.hasNext()) {
            int removedPosition = (Integer) iterator.next();
            mListCatalogs.remove(removedPosition);
            selectedPositions.remove(removedPosition);
            notifyItemRemoved(removedPosition);
        }

        changeActionModeMenu();
    }

    public void clearSelectionsItems() {
        mSelectionHelper.clearSelection();
        notifyDataSetChanged();
    }


    public void selectAllItems() {
        mSelectionHelper.setAllItemsSelected(mListCatalogs.size());
        notifyDataSetChanged();

        changeActionModeMenu();
    }

    @Override
    public void onHolderClick(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public boolean onHolderLongClick(RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, boolean isSelected) {
        ((Checkable) viewHolder.itemView).setChecked(isSelected);
        changeActionModeMenu();
    }

    private void changeActionModeMenu() {
        ActionMode actionMode = mActivity.getActionMode();
        int countSelectedItems = mSelectionHelper.getSelectedItemsCount();
        MenuItem item = actionMode.getMenu().getItem(0);

        if (countSelectedItems > 1) {
            item.setVisible(false);
        } else if (!actionMode.getMenu().getItem(0).isVisible()) {
            item.setVisible(true);
        }

        actionMode.setTitle(String.valueOf(countSelectedItems));
    }

    @Override
    public void onSelectableChanged(boolean isSelectable) {
        if (isSelectable) {
            mActivity.startActionMode();
        }
    }

    private class CatalogHolder extends RecyclerView.ViewHolder {

        public ImageView catalogImage;

        public TextView catalogName;
        public TextView catalogCountWords;
        public TextView catalogCreatedDate;

        public Button showButton;
        public Button expandButton;

        public CatalogHolder(View itemView) {
            super(itemView);
//            catalogImage = (ImageView) itemView.findViewById(R.id.catalog_image);
            catalogName = (TextView) itemView.findViewById(R.id.catalog_name);
            catalogCountWords = (TextView) itemView.findViewById(R.id.catalog_count_words);
            catalogCreatedDate = (TextView) itemView.findViewById(R.id.catalog_created_date);

            showButton = (Button) itemView.findViewById(R.id.catalog_open_button);
            expandButton = (Button) itemView.findViewById(R.id.catalog_expandable_button);
        }

        public void bindInfo(Catalog catalog) {
//            catalogImage.setImageBitmap(BitmapFactory.decodeResource(catalogName.getContext().getResources(), R.drawable.empty_photo));
            catalogName.setText(catalog.getName());
            catalogCountWords.setText("Words count: " + "10");

            Date date = catalog.getDate();
            catalogCreatedDate.setText(DateConverter.convertDate2String(date));

        }
    }
}
