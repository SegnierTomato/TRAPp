package com.training.startandroid.trapp.ui.selection;

import android.support.v7.widget.RecyclerView;

public interface SelectionObserver {

    void onSelectedChanged(RecyclerView.ViewHolder viewHolder, boolean isSelected);

    void onSelectableChanged(boolean isSelectable);
}
