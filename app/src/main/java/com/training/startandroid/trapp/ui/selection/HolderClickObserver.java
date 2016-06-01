package com.training.startandroid.trapp.ui.selection;

import android.support.v7.widget.RecyclerView;

public interface HolderClickObserver {

    void onHolderClick(RecyclerView.ViewHolder viewHolder);

    boolean onHolderLongClick(RecyclerView.ViewHolder viewHolder);
}
