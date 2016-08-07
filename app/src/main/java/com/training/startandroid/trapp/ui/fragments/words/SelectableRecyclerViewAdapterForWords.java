package com.training.startandroid.trapp.ui.fragments.words;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.model.Word;
import com.training.startandroid.trapp.ui.selection.HolderClickObserver;
import com.training.startandroid.trapp.ui.selection.SelectionHelper;
import com.training.startandroid.trapp.ui.selection.SelectionObserver;
import com.training.startandroid.trapp.util.DateConverter;
import com.training.startandroid.trapp.util.FragmentHelper;
import com.training.startandroid.trapp.util.ImageFetcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectableRecyclerViewAdapterForWords extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements HolderClickObserver, SelectionObserver {

    private final String LOG_TAG = SelectableRecyclerViewAdapterForWords.class.getSimpleName();

    private WordsViewFragment mFragment;

    private SelectionHelper mSelectionHelper;
    private List<Word> mListWords;

    private int reqHeight = -1;
    private int reqWidth = -1;

    public SelectableRecyclerViewAdapterForWords(@NonNull WordsViewFragment fragment, List<Word> listWords) {

        mFragment = fragment;

        if (listWords != null) {
            mListWords = listWords;
        } else {
            mListWords = new ArrayList<>();
        }

        mSelectionHelper = new SelectionHelper();
        mSelectionHelper.registerSelectionObserver(this);
    }

    public SelectionHelper getSelectionHelper() {
        return mSelectionHelper;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_cardview, parent, false);
        WordHolder holder = new WordHolder(view);
        return mSelectionHelper.wrapSelectable(holder);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Word itemWord = mListWords.get(position);

        WordHolder wordHolder = (WordHolder) viewHolder;
        wordHolder.bindInfo(itemWord);

        Checkable view = (Checkable) viewHolder.itemView;
        view.setChecked(mSelectionHelper.isItemSelected(position));

        mSelectionHelper.bindHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return mListWords.size();
    }

    @Override
    public void onHolderClick(RecyclerView.ViewHolder viewHolder) {
        Log.d(LOG_TAG, "onClick CardView Event");
    }

    @Override
    public boolean onHolderLongClick(RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, boolean isSelected) {

        mSelectionHelper.setItemSelected(viewHolder, isSelected);
    }

    @Override
    public void onSelectableChanged(boolean isSelectable) {

    }

    public int[] getImageSize(){
        return new int[] {reqHeight, reqWidth};
    }

    private class WordHolder extends RecyclerView.ViewHolder {

        private TextView word;
        private TextView wordCreatedDate;

        private ImageView wordImage;

        private ImageFetcher imageFetcher;
        private Button showButton;
        private Button expandButton;


        public WordHolder(View view) {
            super(view);

            FragmentManager fragmentManager = FragmentHelper.getLowLevelFragmentManager(mFragment);

            imageFetcher = new ImageFetcher(fragmentManager);

            word = (TextView) view.findViewById(R.id.word);
            wordCreatedDate = (TextView) view.findViewById(R.id.word_created_date);

            wordImage = (ImageView) view.findViewById(R.id.word_image);

            showButton = (Button) view.findViewById(R.id.card_open_action_button);
            expandButton = (Button) view.findViewById(R.id.card_expandable_action_button);

        }

        public void bindInfo(Word itemWord) {


            String imagePath = itemWord.getImagePath();
            String soundPath = itemWord.getSoundPath();

            if (imagePath != null) {

                if (reqHeight == -1 || reqWidth == -1) {

                    ViewTreeObserver viewTreeObserver = wordImage.getViewTreeObserver();
                    viewTreeObserver.addOnPreDrawListener(new WordHolderOnPreDrawListener(imagePath));

                } else {
                    imageFetcher.loadImage(wordImage, reqHeight, reqWidth, imagePath);
                }

            } else {
                wordImage.setVisibility(View.GONE);
            }

            if (soundPath != null) {
                Log.d(LOG_TAG, "not null soundPath");
            } else {
                Log.d(LOG_TAG, "null soundPath");
            }

            word.setText(itemWord.getWord());

            Date date = itemWord.getDate();
            wordCreatedDate.setText(DateConverter.convertDate2String(date));
        }

        private class WordHolderOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {

            private final String imagePath;

            public WordHolderOnPreDrawListener(String imagePath) {
                this.imagePath = imagePath;
            }

            @Override
            public boolean onPreDraw() {

                wordImage.getViewTreeObserver().removeOnPreDrawListener(this);
                reqHeight = wordImage.getMeasuredHeight();
                reqWidth = wordImage.getMeasuredWidth();

                imageFetcher.loadImage(wordImage, reqHeight, reqWidth, imagePath);
                return false;
            }
        }

    }

}
