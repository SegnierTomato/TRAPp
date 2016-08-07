package com.training.startandroid.trapp.ui.fragments.translationofword;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.DAOFactory;
import com.training.startandroid.trapp.model.Word;
import com.training.startandroid.trapp.util.Constants;

import java.util.Arrays;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class TranslationOfWordFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private final String LOG_TAG = TranslationOfWordFragment.class.getSimpleName();

    private final String[] bundleArgs = {"typeTranslation", "word"};

    private Constants.SpecificDictionary TYPE_TRANSLATION;

    private Word mWord = null;
    private String wordName;

    private Context mContext;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SectionedRecyclerViewAdapter mSectionAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();

        if (args != null) {
            TYPE_TRANSLATION = (Constants.SpecificDictionary) args.getSerializable(bundleArgs[0]);
            mWord = (Word) args.getSerializable(bundleArgs[1]);

        } else {
//          think about closing this fragment
            Log.d(LOG_TAG, "Received bundle args = null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            TYPE_TRANSLATION = (Constants.SpecificDictionary) savedInstanceState.getSerializable(bundleArgs[0]);

        }

        View view = inflater.inflate(R.layout.translation_of_word_view, parent, false);

        mSectionAdapter = new SectionedRecyclerViewAdapter();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.translation_recycler_view);
        mRecyclerView.setAdapter(mSectionAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.translation_swipe_refresh_layout);

        if (TYPE_TRANSLATION == Constants.SpecificDictionary.CUSTOM_DICTIONARY) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
        } else {

            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        mContext = getActivity().getApplicationContext();
        DatabaseConnection.initializeConnection(mContext);
        DatabaseConnection.openConnection();

//        DAOFactory.getTranslationsWordDAO().assignedTranslatedWords2SpecificDictionary();
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseConnection.closeConnection();
    }

    @Override
    public void onRefresh() {

//        mSwipeRefreshLayout.setRefreshing(false);

        switch (TYPE_TRANSLATION) {
            case ALL:
                break;

            case GOOGLE_DICTIONARY:
                break;

            case YANDEX_DICTIONARY:
                break;

        }

    }

    private void initSectionAdapter(SectionedRecyclerViewAdapter adapter) {

        mSectionAdapter.addSection(new TranslationRVSection("Google", Arrays.asList("Тест 1", "Тест 2", "Тест 3")));
        mSectionAdapter.addSection(new TranslationRVSection("Yandex", Arrays.asList("Тест 4")));
        mSectionAdapter.addSection(new TranslationRVSection("Custom", Arrays.asList("Тест 5", "Тест 6", "Тест 7", "Тест 8")));
    }

}
