package com.training.startandroid.trapp.ui;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.training.startandroid.trapp.R;
import com.training.startandroid.trapp.util.BitmapLoader;
import com.training.startandroid.trapp.util.BitmapLoadingTask;

public class AddCatalogFragment extends Fragment
        implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText mInputCatalogName;
    private ImageView mCatalogImage;
    private Button mAddButton;
    private TextInputLayout mInputLayoutCatalogName;

    private int SELECT_PHOTO = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_and_edit_catalog, viewGroup, false);

        mCatalogImage = (ImageView) view.findViewById(R.id.add_catalog_image);
        mInputCatalogName = (EditText) view.findViewById(R.id.input_catalog_name);
        mAddButton = (Button) view.findViewById(R.id.add_edit_catalog_button);
        mAddButton.setText("Add");
        mInputLayoutCatalogName = (TextInputLayout) view.findViewById(R.id.input_layout_catalog_name);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();

        mAddButton.setOnClickListener(this);
        mCatalogImage.setOnClickListener(this);
        mInputCatalogName.setOnFocusChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_edit_catalog_button:

                if (validateName()) {

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

//                BitmapLoader.getImage(imagePath, mCatalogImage.getHeight(), mCatalogImage.getWidth());

                BitmapLoadingTask bitmapTask = new BitmapLoadingTask(mCatalogImage);
                bitmapTask.execute(imagePath);

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

        if (v.getId() == mInputCatalogName.getId()) {
            validateName();
        }
    }

    public boolean validateName() {
        if (mInputCatalogName.getText().toString().trim().isEmpty()) {
            mInputLayoutCatalogName.setError("Enter catalog name");
//            requestFocus(mInputCatalogName);
            return false;
        } else {
            mInputLayoutCatalogName.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {

        view.setFocusable(true);
    }

//    private class NewCatalogTextWatcher implements TextWatcher {
//
//        private View view;
//
//        public NewCatalogTextWatcher(View view) {
//            this.view = view;
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//            switch (view.getId()) {
//                case R.id.input_catalog_name:
//                    validateName();
//                    break;
//            }
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//        }
//    }

}
