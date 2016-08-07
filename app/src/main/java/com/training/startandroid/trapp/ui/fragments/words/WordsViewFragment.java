package com.training.startandroid.trapp.ui.fragments.words;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.database.dao.DAOFactory;
import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.model.Word;
import com.training.startandroid.trapp.ui.fragments.NestedFragmentIteraction;
import com.training.startandroid.trapp.ui.fragments.catalogs.CatalogsViewFragment;
import com.training.startandroid.trapp.ui.selection.HolderClickObserver;
import com.training.startandroid.trapp.util.Constants;
import com.training.startandroid.trapp.util.ImageFetcher;
import com.training.startandroid.trapp.util.RecyclerViewConfiguration;

import java.util.List;

public class WordsViewFragment extends Fragment
        implements AddWordFragment.AddWordEventListener, EditWordFragment.EditWordEventListener,
        NestedFragmentIteraction, HolderClickObserver {

    private final String LOG_TAG = WordsViewFragment.class.getSimpleName();

    private final String[] bundleArgsKey = {"openedCatalog"};
    private final String[] bundleAddWordArgsKey = {"reqHeight", "reqWidth"};

    private Catalog mCatalog;

    private ImageFetcher mImageFetcher;

    private int mImageHeight;
    private int mImageWidth;

    private ImageView mCollapsingCatalogImage;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private RecyclerView mRecyclerView;

    private SelectableRecyclerViewAdapterForWords mAdapter;
    private Context mContext;

    private CoordinatorLayout mCoordinatorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();

        if (args != null) {
            mCatalog = (Catalog) args.getSerializable(bundleArgsKey[0]);
        } else {
            getActivity().onBackPressed();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.words_view_container, container, false);

        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.wordsview_coordinator_layout);

        mCollapsingCatalogImage = (ImageView) view.findViewById(R.id.wordsview_collapsing_catalog_image);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.wordsview_collapsing_toolbar_layout);

        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.wordsview_collapsing_add_words_button);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                final String operationTag = AddWordFragment.class.getName();

                AddWordFragment addWordFragment = new AddWordFragment();

                Bundle args = new Bundle();
                args.putInt(bundleAddWordArgsKey[0], mImageHeight);
                args.putInt(bundleAddWordArgsKey[1], mImageWidth);
                addWordFragment.setArguments(args);

                fragmentTransaction.add(R.id.wordsview_container, addWordFragment, operationTag);
                fragmentTransaction.addToBackStack(operationTag);

                int result = fragmentTransaction.commit();

                if (result >= 0) {
                    setVisible(false);

                } else {
                    Toast.makeText(getContext(), Constants.MESSAGE_ERROR_RUN_FRAGMENT, Toast.LENGTH_SHORT).show();
                }

                addWordFragment.setAddWordEventListener(WordsViewFragment.this);

            }
        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        view.findViewById(R.id.fab).setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        DatabaseConnection.initializeConnection(mContext);
        DatabaseConnection.openConnection();

        if (savedInstanceState != null) {
            mCatalog = (Catalog) savedInstanceState.getSerializable(bundleArgsKey[0]);
        }

        if (mCatalog.getImagePath() != null) {

            mImageFetcher = new ImageFetcher(getParentFragment().getFragmentManager());

            Log.d(LOG_TAG, "collapsing image measure height: " + mCollapsingCatalogImage.getMeasuredHeight());
            Log.d(LOG_TAG, "collapsing image width: " + mCollapsingCatalogImage.getWidth());

            mImageFetcher.loadImage(mCollapsingCatalogImage, 400, 240, mCatalog.getImagePath());
        } else {
            Drawable removeIcon = null;
            /*
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                removeIcon = getResources().getDrawable(R.drawable.add_picture_style, null);
            } else {
                removeIcon = getResources().getDrawable(R.drawable.add_picture_style);
            }
            mCollapsingCatalogImage.setImageDrawable(removeIcon);
            */
        }

        mCollapsingToolbarLayout.setTitle(mCatalog.getName());

        List<Word> listWords = mCatalog.getWords();
        if (listWords.isEmpty()) {
            listWords = DAOFactory.getWordsDAO().getCatalogWordsByCatalogId(mCatalog.getId());
            mCatalog.setWords(listWords);
        }

        Log.d(LOG_TAG, "size of word list: " + listWords.size());

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(RecyclerViewConfiguration.getDisplayColumns(mContext), StaggeredGridLayoutManager.VERTICAL));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);

        mAdapter = new SelectableRecyclerViewAdapterForWords(this, listWords);
        mRecyclerView.setAdapter(mAdapter);

        int[] sizeImage = mAdapter.getImageSize();

        mImageHeight = sizeImage[0];
        mImageWidth = sizeImage[1];

        notifyParent2ChildState(true);
    }

    @Override
    public void onDestroy() {
        notifyParent2ChildState(false);
        super.onDestroy();

    }

    @Override
    public void onPause() {
        DatabaseConnection.closeConnection();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(bundleArgsKey[0], mCatalog);
    }

    public void setVisible(boolean visible) {

        if (visible) {
            mCoordinatorLayout.setVisibility(View.VISIBLE);
        } else {
            mCoordinatorLayout.setVisibility(View.GONE);
        }
    }

    public void addNewWord(Word newWord) {
        Log.d(LOG_TAG, "add New Word");
    }

    public void editWord(Word editWord) {
    }

    @Override
    public void onHolderClick(RecyclerView.ViewHolder viewHolder) {
        Log.d(LOG_TAG, "on Holder Click");
    }


    @Override
    public boolean onHolderLongClick(RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @Override
    public void notifyParent2ChildState(boolean isAlive) {
        ((CatalogsViewFragment) getParentFragment()).setVisible(!isAlive);
    }
}
