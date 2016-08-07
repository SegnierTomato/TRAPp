package com.training.startandroid.trapp.ui.fragments.words;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.training.startandroid.trapp.model.Word;
import com.training.startandroid.trapp.ui.fragments.translationofword.TranslationOfWordFragment;
import com.training.startandroid.trapp.util.Constants;

class FragmentTranslationTabsCreator {

    private static final String bundleArg = "typeTranslation";

    public static Fragment getTranslationFragment(Constants.SpecificDictionary type) {

        Bundle args = new Bundle();
        args.putSerializable(bundleArg, type);

        Fragment fragment = new TranslationOfWordFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
