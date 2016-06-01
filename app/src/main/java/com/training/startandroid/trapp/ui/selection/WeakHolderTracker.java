package com.training.startandroid.trapp.ui.selection;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class WeakHolderTracker {

    private final SparseArray<WeakReference<RecyclerView.ViewHolder>> mHolderByPosition =
            new SparseArray<>();

    public void bindHolder(RecyclerView.ViewHolder holder, int position) {
        mHolderByPosition.put(position, new WeakReference<>(holder));
    }

    @Nullable
    private RecyclerView.ViewHolder getHolder(int position) {

        WeakReference<RecyclerView.ViewHolder> holderWeakReference = mHolderByPosition.get(position);

        if (holderWeakReference == null) {
            mHolderByPosition.remove(position);
            return null;
        }

        RecyclerView.ViewHolder holder = holderWeakReference.get();
        if (holder == null || (holder.getAdapterPosition() != position && holder.getAdapterPosition() != RecyclerView.NO_POSITION)) {
            mHolderByPosition.remove(position);
            return null;
        }

        return holder;
    }

    public List<RecyclerView.ViewHolder> getTrackedHolders() {

        List<RecyclerView.ViewHolder> listTrackedHolders = new ArrayList<>();

        for (int i = 0; i < mHolderByPosition.size(); i++) {
            int key = mHolderByPosition.keyAt(i);
            RecyclerView.ViewHolder holder = getHolder(key);

            if (holder != null) {
                listTrackedHolders.add(holder);
            }
        }

        return listTrackedHolders;
    }
}
