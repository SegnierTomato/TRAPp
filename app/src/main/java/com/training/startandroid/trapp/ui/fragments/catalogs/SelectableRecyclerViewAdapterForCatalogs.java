package com.training.startandroid.trapp.ui.fragments.catalogs;

import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.training.startandroid.trapp.util.ImageFetcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class SelectableRecyclerViewAdapterForCatalogs extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements HolderClickObserver, SelectionObserver {

    private final String LOG_TAG = SelectableRecyclerViewAdapterForCatalogs.class.getSimpleName();

    private List<Catalog> mListCatalogs;
    private SelectionHelper mSelectionHelper;
    private CatalogsViewFragment mFragment;

    private int mImageHeight = -1;
    private int mImageWidth = -1;

    public SelectableRecyclerViewAdapterForCatalogs(@NonNull CatalogsViewFragment fragment, List<Catalog> listCatalogs) {

        mFragment = fragment;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.catalog_cardview, viewGroup, false);
        CatalogHolder holder = new CatalogHolder(view);
        return mSelectionHelper.wrapSelectable(holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Log.d(LOG_TAG, "" + mListCatalogs.get(position).getName());
        Log.d(LOG_TAG, "" + mListCatalogs.get(position).getImagePath());

        CatalogHolder catalogHolder = (CatalogHolder) viewHolder;
        catalogHolder.bindInfo(mListCatalogs.get(position));

        Checkable view = (Checkable) viewHolder.itemView;
        view.setChecked(mSelectionHelper.isItemSelected(position));
        mSelectionHelper.bindHolder(catalogHolder, position);
    }

    public int[] getImageSize() {
        return new int[]{mImageHeight, mImageWidth};
    }

    public void addNewItem(Catalog newItem) {

        /* Add in this place sorting algorithm*/

        mListCatalogs.add(newItem);
        notifyItemInserted(mListCatalogs.size() - 1);
    }

    public void editItem(Catalog editItem) {
//        mListCatalogs.add();
        Iterator<Catalog> iterator = mListCatalogs.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            count++;
            Catalog temp = iterator.next();
            if (temp.getId() == editItem.getId()) {
                Log.d(LOG_TAG, "count" + count);
                break;
            }
//        notifyItemChanged(position);
        }
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
        mSelectionHelper.setItemsSelected(mListCatalogs.size());
        notifyDataSetChanged();

        changeActionModeMenu();
    }

    public Catalog getElement(int position) {

        if (position < mListCatalogs.size() && position > -1) {
            return mListCatalogs.get(position);
        } else {
            return null;
        }
    }

    /* Methods onHolderClick and LongClick now not using
    * */

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
        ActionMode actionMode = mFragment.getActionMode();
        int countSelectedItems = mSelectionHelper.getSelectedItemsCount();
        MenuItem item = actionMode.getMenu().getItem(0);

        if (countSelectedItems > 1) {
            item.setVisible(false);
        } else if (!item.isVisible()) {
            item.setVisible(true);
        }

        actionMode.setTitle(String.valueOf(countSelectedItems));
    }

    @Override
    public void onSelectableChanged(boolean isSelectable) {
        if (isSelectable) {
            mFragment.startActionMode();
        }
    }

    private class CatalogHolder extends RecyclerView.ViewHolder {

        public ImageView catalogImage;

        public TextView catalogName;
        public TextView catalogCountWords;
        public TextView catalogCreatedDate;

        public Button showButton;
        public Button expandButton;

        private ImageFetcher imageFetcher;

        public CatalogHolder(View itemView) {
            super(itemView);

            imageFetcher = new ImageFetcher(mFragment.getFragmentManager());

            catalogImage = (ImageView) itemView.findViewById(R.id.catalog_image);
            catalogName = (TextView) itemView.findViewById(R.id.catalog_name);
            catalogCountWords = (TextView) itemView.findViewById(R.id.catalog_count_words);
            catalogCreatedDate = (TextView) itemView.findViewById(R.id.catalog_created_date);

            showButton = (Button) itemView.findViewById(R.id.card_open_action_button);
            expandButton = (Button) itemView.findViewById(R.id.card_expandable_action_button);

//            itemView.setOnClickListener(this);
        }

        public void bindInfo(Catalog catalog) {

            String imagePath = catalog.getImagePath();
            Log.d(LOG_TAG, "catalog name: " + catalogName.getText());
            Log.d(LOG_TAG, "imagePath: " + imagePath);

            if (imagePath != null) {

                if (mImageHeight == -1 || mImageWidth == -1) {
                    ViewTreeObserver viewTreeObserver = catalogImage.getViewTreeObserver();
                    viewTreeObserver.addOnPreDrawListener(new CatalogHolderOnPreDrawListener(imagePath));
                } else {
                    imageFetcher.loadImage(catalogImage, mImageHeight, mImageWidth, imagePath);
                }

            } else {
                catalogImage.setVisibility(View.GONE);

            }

            catalogName.setText(catalog.getName());
            catalogCountWords.setText("Words count: " + "10");

            Date date = catalog.getDate();
            catalogCreatedDate.setText(DateConverter.convertDate2String(date));

        }

        private class CatalogHolderOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {

            private final String imagePath;

            public CatalogHolderOnPreDrawListener(String imagePath) {
                this.imagePath = imagePath;
            }

            @Override
            public boolean onPreDraw() {

                Log.d(LOG_TAG, " onPreDraw");

                catalogImage.getViewTreeObserver().removeOnPreDrawListener(this);

                mImageHeight = catalogImage.getMeasuredHeight();
                mImageWidth = catalogImage.getMeasuredWidth();
                imageFetcher.loadImage(catalogImage, mImageHeight, mImageWidth, imagePath);

                return true;
            }
        }
    }
}
