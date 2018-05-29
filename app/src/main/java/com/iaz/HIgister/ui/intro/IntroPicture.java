package com.iaz.HIgister.ui.intro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iaz.HIgister.util.CustomPhotoPickerDialog;
import com.iaz.Higister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 27/05/2018.
 */

public class IntroPicture extends Fragment implements IntroMvpView {

    private CustomPhotoPickerDialog photoDialog;

    @BindView(R.id.profile_image_layout)
    LinearLayout profileImageLayout;

    @BindView(R.id.profile_image)
    ImageView profileImage;

    IntroActivity activity;
    String uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (IntroActivity) getActivity();
        activity.fragmentPicture = this;
//        activity.addSlide(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (activity.user.getProfilePictureUri() != null && !activity.user.getProfilePictureUri().isEmpty())
            callGlide(Uri.parse(activity.user.getProfilePictureUri()));

        profileImageLayout.setOnClickListener(v -> {
            photoDialog = new CustomPhotoPickerDialog(activity, new CustomPhotoPickerDialog
                    .OnOptionPhotoSelected() {
                @Override
                public void onGallery() {
                    activity.mIntroPresenter.openDialogWindow();
                    photoDialog.dismiss();
                }

                @Override
                public void onCamera() {
                    // Here, thisActivity is the current activity
//                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                            || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                        photoDialog.dismiss();
//                        ActivityCompat.requestPermissions(activity,
//                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
//                    } else {
                    activity.mIntroPresenter.getPhoto();
                    photoDialog.dismiss();
//                    }
                }
            });
            photoDialog.show();


        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activity.mIntroPresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        activity.mIntroPresenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void showSnackBar(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void callGlide(Uri uri) {
        this.uri = uri.toString();
        try {
            Glide.with(IntroPicture.this)
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
