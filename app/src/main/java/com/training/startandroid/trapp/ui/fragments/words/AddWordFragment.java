package com.training.startandroid.trapp.ui.fragments.words;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.model.Word;
import com.training.startandroid.trapp.ui.fragments.NestedFragmentIteraction;
import com.training.startandroid.trapp.util.Constants;
import com.training.startandroid.trapp.util.ImageFetcher;

import java.util.ArrayList;
import java.util.List;


public class AddWordFragment extends Fragment
        implements View.OnClickListener, NestedFragmentIteraction {

    private final String LOG_TAG = AddWordFragment.class.getSimpleName();

    private final String[] bundleArgsKey = {"reqHeight", "reqWidth"};
    private final String[] bundleForTabAdapterArgsKey = {"typeTranslation", "word"};

    private final String[] mTitleFragmentTabs = {"All", "Google", "Yandex", "Custom"};

    private int mImageHeight;
    private int mImageWidth;

    private ImageView mWordImage;
    private TextInputLayout mInputLayout;
    private EditText mEditText;

    private Button mAddButton;
    private FloatingActionButton mRemoveFab;

    private int SELECT_PHOTO = 1;

    private ImageFetcher mImageFetcher;

    private AddWordEventListener mAddEventListener;

    private String mImagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();

        if (args != null) {
            mImageHeight = args.getInt(bundleArgsKey[0]);
            mImageWidth = args.getInt(bundleArgsKey[1]);

        } else {
            Log.d(LOG_TAG, "Received bundle args = null");
            getActivity().onBackPressed();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_and_edit_word, container, false);

        mWordImage = (ImageView) view.findViewById(R.id.add_edit_word_image);

        mWordImage.setOnClickListener(this);

        mInputLayout = (TextInputLayout) view.findViewById(R.id.add_edit_word_input_layout);
        mEditText = (EditText) view.findViewById(R.id.add_edit_word_input);

        mEditText.addTextChangedListener(new WordTextWatcher());

        mAddButton = (Button) view.findViewById(R.id.add_edit_word_add_button);
        mAddButton.setOnClickListener(this);

        mRemoveFab = (FloatingActionButton) view.findViewById(R.id.add_edit_word_remove_fab);
        mRemoveFab.setOnClickListener(this);

        FragmentPagerAdapter adapter = initViewPageAdapter(getFragmentManager());

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.add_edit_word_viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.add_edit_word_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setEnabled(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mImageFetcher = new ImageFetcher(this);

        if (savedInstanceState != null) {
            mImageHeight = savedInstanceState.getInt(bundleArgsKey[0]);
            mImageWidth = savedInstanceState.getInt(bundleArgsKey[1]);

            mAddEventListener = (WordsViewFragment) getParentFragment();
        }

        notifyParent2ChildState(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(bundleArgsKey[0], mImageHeight);
        outState.putInt(bundleArgsKey[1], mImageWidth);

        mAddEventListener = null;

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        notifyParent2ChildState(false);
        super.onDestroy();
    }

    private FragmentPagerAdapter initViewPageAdapter(FragmentManager fragmentManager) {

        FragmentTabAdapter adapter = new FragmentTabAdapter(getFragmentManager());

        for (int i = 0; i < mTitleFragmentTabs.length; i++) {

            Fragment fragment = FragmentTranslationTabsCreator.getTranslationFragment(Constants.SpecificDictionary.values()[i]);
            adapter.addNewPage(fragment, mTitleFragmentTabs[i]);
        }

        return adapter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_edit_word_image:
                view.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
                break;

            case R.id.add_edit_word_remove_fab:

                Drawable removeDrawableIcon;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    removeDrawableIcon = getResources().getDrawable(R.drawable.material_flat, null);
                } else {
                    removeDrawableIcon = getResources().getDrawable(R.drawable.material_flat);
                }

                mWordImage.setImageDrawable(removeDrawableIcon);
                mImagePath = null;
                break;

//            case :
//            break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("Result Activity", "On");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                Uri selectedImageUri = data.getData();
                String imagePath = getPathFromUri(selectedImageUri);

                if (imagePath != null) {

                    if (mImageHeight < 0 || mImageWidth < 0) {
                        mImageHeight = mWordImage.getHeight();
                        mImageWidth = mWordImage.getWidth();
                    }

                    mImageFetcher.loadImage(mWordImage, mImageHeight, mImageWidth, imagePath);
                    mImagePath = imagePath;
                }

            }
        }
    }

    private String getPathFromUri(Uri uri) {

        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } else {
            return uri.getPath();
        }

    }

    private void requestFocus(final EditText primaryTextField) {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 100);
    }

    public void setAddWordEventListener(AddWordEventListener listener) {
        mAddEventListener = listener;
    }

    @Override
    public void notifyParent2ChildState(boolean isAlive) {
        ((WordsViewFragment) getParentFragment()).setVisible(!isAlive);
    }

    interface AddWordEventListener {
        public void addNewWord(Word newWord);
    }

    private class FragmentTabAdapter extends FragmentPagerAdapter {

        private List<Fragment> listFragments = new ArrayList<>();
        private List<String> listPageTitle = new ArrayList<>();

        public FragmentTabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addNewPage(Fragment fragment, String pageTitle) {
            listFragments.add(fragment);
            listPageTitle.add(pageTitle);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listPageTitle.get(position);
        }

    }

    private class WordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEditText.getText().toString().trim().isEmpty()) {
                mInputLayout.setError("Enter valid word");
                requestFocus(mEditText);
            } else {
                mInputLayout.setErrorEnabled(false);
            }
        }
    }

}
