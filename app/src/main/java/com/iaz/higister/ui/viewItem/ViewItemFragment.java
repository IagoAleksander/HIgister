package com.iaz.higister.ui.viewItem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.iaz.higister.R;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.util.CustomPhotoPickerDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.PERMISSION_WRITE_EXTERNAL;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class ViewItemFragment extends Fragment implements ViewItemMvpView {

    @BindView(R.id.item_title)
    TextView itemTitle;
    @BindView(R.id.item_description)
    TextView itemDescription;
    @BindView(R.id.listLogoImageLayout)
    RelativeLayout listLogoImageLayout;
    @BindView(R.id.listLogoImageView)
    ImageView listLogoImage;
    @BindView(R.id.logo_placeholder)
    LinearLayout listLogoImagePlaceholder;
    @BindView(R.id.text_input_list_name)
    TextInputLayout listNameLayout;
    @BindView(R.id.text_input_list_desc)
    TextInputLayout listDescriptionLayout;


    private CustomPhotoPickerDialog photoDialog;

    ListItem listItem;
    ViewItemActivity activity;

    // newInstance constructor for creating fragment with arguments
    public static ViewItemFragment newInstance(ListItem listItem) {
        ViewItemFragment viewItemFragment = new ViewItemFragment();
        Bundle args = new Bundle();
        args.putParcelable("listItem", listItem);
        viewItemFragment.setArguments(args);
        return viewItemFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItem = getArguments().getParcelable("listItem");
        activity = (ViewItemActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.activity_create_item, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (listItem != null) {

            if (listItem.baseItem.imageUrl != null) {
                Glide.with(this)
                        .load(listItem.baseItem.imageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                listLogoImagePlaceholder.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(listLogoImage);
            }

            itemTitle.setText(listItem.name);
            itemDescription.setText(listItem.description);
        }

        listLogoImageLayout.setOnClickListener(v -> {
            photoDialog = new CustomPhotoPickerDialog(activity, new CustomPhotoPickerDialog
                    .OnOptionPhotoSelected() {
                @Override
                public void onGallery() {
                    activity.mViewItemPresenter.openDialogWindow();
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
                        activity.mViewItemPresenter.getPhoto();
                        photoDialog.dismiss();
                    }
                }
            });
            photoDialog.show();

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activity.mViewItemPresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        activity.mViewItemPresenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void callGlide(Uri uri) {
        if (uri != null) {
            listLogoImagePlaceholder.setVisibility(View.GONE);
        }
        try {
            Glide.with(ViewItemFragment.this)
                    .load(uri)
                    .into(listLogoImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showSnackBar(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissDialog() {
        if (photoDialog != null)
            photoDialog.cancel();
    }

    @Override
    public ViewItemActivity getActivityFromView() {
        return activity;
    }



}
