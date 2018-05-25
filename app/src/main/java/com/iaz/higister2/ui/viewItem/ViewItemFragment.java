package com.iaz.higister.ui.viewItem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iaz.higister.R;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.ui.gallery.GalleryActivity;
import com.iaz.higister.util.CustomPhotoPickerDialog;
import com.iaz.higister.util.ViewUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class ViewItemFragment extends Fragment implements ViewItemMvpView {

    @BindView(R.id.item_title)
    TextView itemTitle;
    @BindView(R.id.separator)
    View separator;
    @BindView(R.id.item_extra_info_header)
    TextView itemExtraInfoHeader;
    @BindView(R.id.item_extra_info)
    TextView itemExtraInfo;
    @BindView(R.id.item_list_name)
    TextView itemListTitle;
    @BindView(R.id.item_list_description)
    TextView itemListDescription;
    @BindView(R.id.listLogoImageLayout)
    LinearLayout listLogoImageLayout;
    @BindView(R.id.listLogoImageView)
    ImageView listLogoImage;


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
        View mView = inflater.inflate(R.layout.fragment_view_item, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (listItem != null) {

            if (listItem.getBaseItem() != null) {
                if (listItem.getBaseItem().imageUrl != null) {
                    Glide.with(this)
                            .asBitmap()
                            .load(listItem.getBaseItem().imageUrl)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                                    Bitmap resourceWithBorder = ViewUtil.addWhiteBorder(resource, 3);
                                    listLogoImage.setImageBitmap(resourceWithBorder);
                                }
                            });

                    listLogoImage.setOnClickListener(view1 -> {
                        Intent goToCarousel = new Intent(activity.getApplicationContext(), GalleryActivity.class);
                        ArrayList<String> listOfPaths = new ArrayList<>();
                        listOfPaths.add(listItem.getBaseItem().imageUrl);

                        goToCarousel.putStringArrayListExtra("photos", listOfPaths);
                        goToCarousel.putExtra("startPosition", 0);
                        activity.getApplicationContext().startActivity(goToCarousel);

                    });
                }

                if (listItem.getBaseItem().title != null && !listItem.getBaseItem().title.isEmpty())
                    itemTitle.setText(listItem.getBaseItem().title);

                if (listItem.getBaseItem().description != null && !listItem.getBaseItem().description.isEmpty())
                    itemExtraInfo.setText(listItem.getBaseItem().description);
                else {
                    itemExtraInfoHeader.setVisibility(View.GONE);
                    separator.setVisibility(View.GONE);
                }
            }

            if (listItem.getName() != null && !listItem.getName().isEmpty())
                itemListTitle.setText(listItem.getName());
            else
                itemListTitle.setVisibility(View.GONE);

            if (listItem.getDescription() != null && !listItem.getDescription().isEmpty())
                itemListDescription.setText(listItem.getDescription());
            else
                itemListDescription.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSnackBar(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public ViewItemActivity getActivityFromView() {
        return activity;
    }



}
