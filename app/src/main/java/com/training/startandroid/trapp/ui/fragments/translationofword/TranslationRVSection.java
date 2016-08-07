package com.training.startandroid.trapp.ui.fragments.translationofword;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.training.startandroid.trapp.R;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class TranslationRVSection extends StatelessSection {

    private String mTitle = null;
    private List<String> mListTranslations;

    public TranslationRVSection(@NonNull List<String> translations){
        super(R.layout.translation_of_word_view_item_component);

        this.mListTranslations = translations;
    }

    public TranslationRVSection(final String title, @NonNull List<String> translations) {
        super(R.layout.translation_of_word_view_item_component, R.layout.translation_of_word_view_item_component);

        this.mTitle = title;
        this.mListTranslations = translations;
    }

    @Override
    public int getContentItemsTotal() {
        return mListTranslations.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new TranslationsItemHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new TranslationsHeaderHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        TranslationsItemHolder itemHolder = (TranslationsItemHolder) holder;
        itemHolder.bindInfo(mListTranslations.get(position));
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {

        TranslationsHeaderHolder itemHolder = (TranslationsHeaderHolder) holder;
        itemHolder.bindInfo(mTitle);
    }

    private class TranslationsItemHolder extends RecyclerView.ViewHolder {

        private TextView mTranslationView;

        public TranslationsItemHolder(View itemView) {
            super(itemView);

            mTranslationView = (TextView) itemView.findViewById(R.id.translation_translation_header_and_item);
        }

        public void bindInfo(String translation) {
            mTranslationView.setText(translation);
        }
    }

    private class TranslationsHeaderHolder extends RecyclerView.ViewHolder {

        private TextView mHeaderView;

        public TranslationsHeaderHolder(View itemView) {
            super(itemView);

            mHeaderView = (TextView) itemView.findViewById(R.id.translation_translation_header_and_item);
        }

        public void bindInfo(String headerName) {
            mHeaderView.setText(headerName);
        }
    }
}
