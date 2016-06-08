package com.training.startandroid.trapp.ui;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.training.startandroid.trapp.database.DatabaseConnection;
import com.training.startandroid.trapp.model.Catalog;

public class EditCatalogFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private Button mButton;
    private ImageView mCatalogImage;
    private EditText mCatalogName;
    private TextInputLayout mInputLayoutCatalogName;

    private int SELECT_PHOTO = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        Log.d("Fragment", "onCreate view");

        View view = inflater.inflate(R.layout.add_and_edit_catalog, viewGroup, false);

        mCatalogImage = (ImageView) view.findViewById(R.id.add_catalog_image);
        mCatalogName = (EditText) view.findViewById(R.id.input_catalog_name);

        mButton = (Button) view.findViewById(R.id.add_edit_catalog_button);
        mButton.setText("Edit");

        mInputLayoutCatalogName = (TextInputLayout) view.findViewById(R.id.input_layout_catalog_name);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();

        mButton.setOnClickListener(this);
        mCatalogImage.setOnClickListener(this);
        mCatalogName.setOnFocusChangeListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("Result Activity", "On");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                Log.d("Result", data.getData().toString());
                Log.d("Result", data.getData().getPath());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_edit_catalog_button:

                if(validateName()){

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
    public void onPause() {
        DatabaseConnection.closeConnection();
        super.onPause();
    }

    @Override
    public void onResume() {
        DatabaseConnection.openConnection();
        super.onResume();
        Log.d("Fragment", "onResume");
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
}
