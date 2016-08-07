package com.training.startandroid.trapp.ui.fragments.catalogs;


import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
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
import com.training.startandroid.trapp.ui.fragments.NestedFragmentIteraction;
import com.training.startandroid.trapp.util.FragmentHelper;
import com.training.startandroid.trapp.util.ImageFetcher;

public class AddCatalogFragment extends Fragment
        implements View.OnClickListener, View.OnFocusChangeListener, NestedFragmentIteraction {

    private final String LOG_TAG = AddCatalogFragment.class.getSimpleName();

    private final String[] bundleArgsKey = {"reqHeight", "reqWidth"};

    private EditText mCatalogName;
    private ImageView mCatalogImage;
    private Button mAddButton;
    private TextInputLayout mLayoutCatalogName;

    private int SELECT_PHOTO = 1;

    private ImageFetcher mImageFetcher;

    private AddCatalogEventListener mAddCatalogEventListener;

    private String mImagePath = null;
    private int mImageHeight;
    private int mImageWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();

        if (args != null) {
            mImageHeight = args.getInt(bundleArgsKey[0]);
            mImageWidth = args.getInt(bundleArgsKey[1]);

        } else {
            getActivity().onBackPressed();
            Log.d(LOG_TAG, "Received bundle args = null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_and_edit_catalog, viewGroup, false);

        mCatalogImage = (ImageView) view.findViewById(R.id.add_catalog_image);
        mCatalogName = (EditText) view.findViewById(R.id.input_catalog_name);
        mAddButton = (Button) view.findViewById(R.id.add_edit_catalog_button);
        mAddButton.setText(R.string.button_add_text);
        mLayoutCatalogName = (TextInputLayout) view.findViewById(R.id.input_layout_catalog_name);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Activity activity = getActivity();

        mAddButton.setOnClickListener(this);
        mCatalogImage.setOnClickListener(this);
        mCatalogName.setOnFocusChangeListener(this);

        mCatalogName.addTextChangedListener(new CatalogTextWatcher(null));

        mImageFetcher = new ImageFetcher(getParentFragment().getFragmentManager());

        if (savedInstanceState != null) {
            mImageWidth = savedInstanceState.getInt(bundleArgsKey[0]);
            mImageWidth = savedInstanceState.getInt(bundleArgsKey[1]);

            mAddCatalogEventListener = (CatalogsViewFragment) getParentFragment();
        }

        notifyParent2ChildState(true);

        Log.d(LOG_TAG, "hash Code this fragments: " + this.hashCode());
        Log.d(LOG_TAG, "hash Code Listener Fragment: " + mAddCatalogEventListener.hashCode());
    }

    @Override
    public void notifyParent2ChildState(boolean isAlive){
        ((CatalogsViewFragment) getParentFragment()).setVisible(!isAlive);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(bundleArgsKey[0], mImageHeight);
        outState.putInt(bundleArgsKey[1], mImageWidth);

        mAddCatalogEventListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        mImageFetcher.setPauseBackgroundLoadingTask(true);
        notifyParent2ChildState(false);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "onDestroy");
        mImageFetcher.setExitTasksEarly(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_edit_catalog_button:

                if (validateName()) {
                    if (mAddCatalogEventListener != null) {
                        mAddCatalogEventListener.addNewCatalog(mCatalogName.getText().toString(), mImagePath);
                    }

                    FragmentHelper.closeFragment(getFragmentManager());
                }
                break;

            case R.id.add_catalog_image:
                v.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.image_click));

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("Result Activity", "On");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                Uri selectedImageUri = data.getData();
                String imagePath = getPathFromUri(selectedImageUri);

                if (imagePath != null) {

                    if (mImageHeight < 0 || mImageWidth < 0) {
                        mImageHeight = mCatalogImage.getHeight();
                        mImageWidth = mCatalogImage.getWidth();
                    }

                    mImageFetcher.loadImage(mCatalogImage, mImageHeight, mImageWidth, imagePath);
                    mImagePath = imagePath;
                }

            }
        }
    }

    private String getPathFromUri(Uri uri) {

        if (uri == null) {
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } else {
            return uri.getPath();
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v.getId() == mCatalogName.getId()) {
            validateName();
        }
    }

    public boolean validateName() {
        if (mCatalogName.getText().toString().trim().isEmpty()) {
            mLayoutCatalogName.setError("Enter valid name");
            requestFocus(mCatalogName);
            return false;
        } else {
            mLayoutCatalogName.setErrorEnabled(false);
        }
        return true;
    }


    private void requestFocus(final EditText primaryTextField){
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        }, 100);
    }

    private class CatalogTextWatcher implements TextWatcher {

        private View view;

        public CatalogTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable s) {

            if(s.toString().trim().isEmpty()){
                mLayoutCatalogName.setError("Enter valid name");
                requestFocus(mCatalogName);
            }else{
                mLayoutCatalogName.setErrorEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    public void setAddCatalogEventListener(AddCatalogEventListener listener) {
        mAddCatalogEventListener = listener;
    }

    public interface AddCatalogEventListener {
        public void addNewCatalog(String catalogName, String imagePath);
    }
}
