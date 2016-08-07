package com.training.startandroid.trapp.ui.fragments.words;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.startandroid.trapp.model.Word;
import com.training.startandroid.trapp.ui.fragments.NestedFragmentIteraction;

public class EditWordFragment extends Fragment
        implements View.OnClickListener, NestedFragmentIteraction {

    private final String LOG_TAG = EditWordFragment.class.getSimpleName();

    private final String[] bundleArgs = {"editCatalog", "reqHeight", "reqWidth"};

    private Word mEditWord;

    private int mImageHeight;
    private int mImageWidth;

    private EditWordEventListener mEditListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        Bundle args = this.getArguments();
        if (args != null) {

            mEditWord = (Word) args.getSerializable(bundleArgs[0]);
            mImageHeight = args.getInt(bundleArgs[1]);
            mImageWidth = args.getInt(bundleArgs[2]);

        } else {
            Log.d(LOG_TAG, "Received bundle args = null");
            getActivity().onBackPressed();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mEditWord = (Word) savedInstanceState.getSerializable(bundleArgs[0]);
            mImageHeight = savedInstanceState.getInt(bundleArgs[1]);
            mImageWidth = savedInstanceState.getInt(bundleArgs[2]);

            mEditListener = (EditWordEventListener) getParentFragment();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(bundleArgs[0], mEditWord);
        outState.putInt(bundleArgs[1], mImageHeight);
        outState.putInt(bundleArgs[2], mImageWidth);

        mEditListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {

//        switch(){
//            case :
//
//            case :
//
//            case :
//        }
    }

    @Override
    public void notifyParent2ChildState(boolean isAlive) {
        ((WordsViewFragment) getParentFragment()).setVisible(!isAlive);
    }

    public interface EditWordEventListener {
        public void editWord(Word editWord);
    }
}
