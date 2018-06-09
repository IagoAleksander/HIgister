package com.iaz.HIgister.ui.listsTutorial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iaz.HIgister.util.CustomPhotoPickerDialog;
import com.iaz.Higister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.HIgister.util.Constants.PERMISSION_WRITE_EXTERNAL;

/**
 * Created by alksander on 27/05/2018.
 */

public class ListsTutorialPicture extends Fragment implements ListsTutorialMvpView {

    private CustomPhotoPickerDialog photoDialog;

    @BindView(R.id.list_picture_header)
    TextView textHeader;

    @BindView(R.id.profile_image_layout)
    LinearLayout profileImageLayout;

    @BindView(R.id.profile_image)
    ImageView profileImage;

    @BindView(R.id.text_input_user_name)
    TextInputLayout mNameTextInput;

    ListsTutorialActivity activity;
    String uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (ListsTutorialActivity) getActivity();
//        activity.fragmentPicture = this;
//        activity.addSlide(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lists_tutorial_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        textHeader.setText("Now that you completed the registration process, we can create your first list. Let's start by choosing a photo for your list and its name");

        mNameTextInput.setError(null);

        profileImageLayout.setOnClickListener(v -> {
            photoDialog = new CustomPhotoPickerDialog(activity, new CustomPhotoPickerDialog
                    .OnOptionPhotoSelected() {
                @Override
                public void onGallery() {
                    activity.mListsTutorialPresenter.openDialogWindow();
                    photoDialog.dismiss();
                }

                @Override
                public void onCamera() {
                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        photoDialog.dismiss();
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                    } else {
                    activity.mListsTutorialPresenter.getPhoto();
                    photoDialog.dismiss();
                    }
                }
            });
            photoDialog.show();


        });

        mNameTextInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    mNameTextInput.setError("This field is required");
                }
                else {
                    mNameTextInput.setError(null);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activity.mListsTutorialPresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        activity.mListsTutorialPresenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void showSnackBar(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void callGlide(Uri uri) {
        this.uri = uri.toString();
        try {
            Glide.with(ListsTutorialPicture.this)
                    .load(uri)
                    .into(profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismissDialog() {
        if (photoDialog != null)
            photoDialog.cancel();
    }
}
