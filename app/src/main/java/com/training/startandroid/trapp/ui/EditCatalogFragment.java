package com.training.startandroid.trapp.ui;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.model.Catalog;
import com.training.startandroid.trapp.util.FragmentHelper;
import com.training.startandroid.trapp.util.ImageFetcher;

public class EditCatalogFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private final String LOG_TAG = EditCatalogFragment.class.getSimpleName();

    private final String[] bundleArgsKey = {"editCatalog", "reqHeight", "reqWidth"};

    private Button mButton;
    private ImageView mCatalogImage;
    private EditText mCatalogName;
    private TextInputLayout mInputLayoutCatalogName;

    private EditCatalogEventListener mEditCatalogEventListener;

    private int SELECT_PHOTO = 1;

    private Catalog mEditCatalog;
    private int mImageHeight;
    private int mImageWidth;

    private ImageFetcher mImageFetcher;

    private String mImagePath = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();

        if (args != null) {
            mEditCatalog = (Catalog) args.getSerializable(bundleArgsKey[0]);
            mImageHeight = args.getInt(bundleArgsKey[1]);
            mImageWidth = args.getInt(bundleArgsKey[2]);

            Log.d(LOG_TAG, "Received image height: " + mImageHeight);
            Log.d(LOG_TAG, "Received image width: " + mImageWidth);

        } else {
            getActivity().onBackPressed();
            Log.d(LOG_TAG, "Received bundle args = null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");

        View view = inflater.inflate(R.layout.add_and_edit_catalog, viewGroup, false);

        mCatalogImage = (ImageView) view.findViewById(R.id.add_catalog_image);
        mCatalogName = (EditText) view.findViewById(R.id.input_catalog_name);

        mButton = (Button) view.findViewById(R.id.add_edit_catalog_button);
        mButton.setText("Edit");

        mInputLayoutCatalogName = (TextInputLayout) view.findViewById(R.id.input_layout_catalog_name);

//        Add button for removed existed added image

        PercentRelativeLayout percentRelativeLayout = (PercentRelativeLayout) view.findViewById(R.id.add_catalog_image_parent_layout);

        PercentRelativeLayout.LayoutParams lp = new PercentRelativeLayout.LayoutParams(PercentRelativeLayout.LayoutParams.WRAP_CONTENT,
                PercentRelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(PercentRelativeLayout.ALIGN_RIGHT, mCatalogImage.getId());
        lp.addRule(PercentRelativeLayout.ALIGN_TOP, mCatalogImage.getId());

        Context context = getActivity().getApplicationContext();
        Button removeImageButton = new Button(context);
        removeImageButton.setHint("Remove");

        Drawable removeIcon = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            removeIcon = getResources().getDrawable(R.drawable.ic_action_remove, null);
        } else {
            removeIcon = getResources().getDrawable(R.drawable.ic_action_remove);
        }
        removeImageButton.setBackground(removeIcon);


        removeImageButton.setId(1);
        removeImageButton.setTag("mRemoveImageButton");

        percentRelativeLayout.addView(removeImageButton, lp);

        removeImageButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mEditCatalog = (Catalog) savedInstanceState.getSerializable(bundleArgsKey[0]);
            mImageHeight = savedInstanceState.getInt(bundleArgsKey[1]);
            mImageWidth = savedInstanceState.getInt(bundleArgsKey[2]);

            Log.d(LOG_TAG, "restored image height = " + mImageHeight);
            Log.d(LOG_TAG, "restored image width = " + mImageWidth);

            mEditCatalogEventListener = (CatalogsViewFragment) getParentFragment();
        }

        Log.d(LOG_TAG, "onActivityCreated");
//        Activity activity = getActivity();

        mButton.setOnClickListener(this);
        mCatalogImage.setOnClickListener(this);
        mCatalogName.setOnFocusChangeListener(this);

        mCatalogName.setText(mEditCatalog.getName());

        mImagePath = mEditCatalog.getImagePath();

        FragmentManager activityFrManager = ((MainActivity) getActivity()).getSupportFragmentManager();

        mImageFetcher = new ImageFetcher(activityFrManager);

        if (mImagePath != null) {
            mImageFetcher.loadImage(mCatalogImage, mImageHeight, mImageWidth, mImagePath);
        }

        Log.d(LOG_TAG, "hash Code this fragments: " + this.hashCode());
        Log.d(LOG_TAG, "hash Code EditListener: " + mEditCatalogEventListener.hashCode());

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putSerializable(bundleArgsKey[0], mEditCatalog);
        outState.putInt(bundleArgsKey[1], mImageHeight);
        outState.putInt(bundleArgsKey[2], mImageWidth);

        mEditCatalogEventListener = null;
    }

    @Override
    public void onPause() {
        DatabaseConnection.closeConnection();
        super.onPause();
    }

    @Override
    public void onResume() {
        DatabaseConnection.openConnection();
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onClick(View v) {

        if (v.getTag() instanceof String) {
            String viewTag = (String) v.getTag();
            if (viewTag.equals("mRemoveImageButton")) {
                Log.d(LOG_TAG, " Tag working");
            }
        }

        switch (v.getId()) {
            case R.id.add_edit_catalog_button:

                if (validateName()) {
                    mEditCatalog.setName(mCatalogName.getText().toString());

                    if (mImagePath != null) {
                        mEditCatalog.setImagePath(mImagePath);
                    } else {
                        mEditCatalog.setImagePath(null);
                    }

                    if (mEditCatalogEventListener != null) {
                        mEditCatalogEventListener.editCatalog(mEditCatalog);
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

            case 1:

                Log.d(LOG_TAG, " case 1");
                mImagePath = null;
                Drawable defaultImage;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    defaultImage = getResources().getDrawable(R.drawable.ic_action_new_picture, null);
                } else {
                    defaultImage = getResources().getDrawable(R.drawable.ic_action_new_picture);
                }

                mCatalogImage.setImageDrawable(defaultImage);
                mCatalogImage.invalidate();
                break;
        }

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(LOG_TAG, "onActivityResult");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                Uri selectedImageUri = data.getData();
                String imagePath = getPathFromUri(selectedImageUri);

                if (imagePath != null) {
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
            if (!hasFocus) {
                validateName();
            }
        }
    }

    public boolean validateName() {
        if (mCatalogName.getText().toString().trim().isEmpty()) {
            mInputLayoutCatalogName.setError("Enter catalog name");
//            requestFocus(mInputCatalogName);
            return false;
        } else {
            mInputLayoutCatalogName.setErrorEnabled(false);
        }
        return true;
    }


    public void setEditCatalogEventListener(EditCatalogEventListener listener) {
        mEditCatalogEventListener = listener;
    }

    public interface EditCatalogEventListener {
        public void editCatalog(Catalog editCatalog);
    }


}
