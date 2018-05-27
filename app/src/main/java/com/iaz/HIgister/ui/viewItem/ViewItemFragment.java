package com.iaz.HIgister.ui.viewItem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import com.iaz.HIgister.data.model.ListItem;
import com.iaz.HIgister.ui.gallery.GalleryActivity;
import com.iaz.HIgister.util.CustomPhotoPickerDialog;
import com.iaz.HIgister.util.ViewUtil;
import com.iaz.Higister.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.HIgister.util.Constants.ANIMES;
import static com.iaz.HIgister.util.Constants.BOOKS;
import static com.iaz.HIgister.util.Constants.COMICS;
import static com.iaz.HIgister.util.Constants.MANGAS;
import static com.iaz.HIgister.util.Constants.MOVIES;
import static com.iaz.HIgister.util.Constants.MUSICS;
import static com.iaz.HIgister.util.Constants.TV_SERIES;

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
    @BindView(R.id.item_url_layout)
    LinearLayout itemUrlLayout;
    @BindView(R.id.item_url)
    TextView itemUrl;
    @BindView(R.id.item_list_name)
    TextView itemListTitle;
    @BindView(R.id.item_list_description)
    TextView itemListDescription;
    @BindView(R.id.listLogoImageLayout)
    LinearLayout listLogoImageLayout;
    @BindView(R.id.listLogoImageView)
    ImageView listLogoImage;
    @BindView(R.id.label_text)
    TextView listType;


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

                if (listItem.getBaseItem().description != null && !listItem.getBaseItem().description.isEmpty()) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        itemExtraInfo.setText((Html.fromHtml(listItem.getBaseItem().description, Html.FROM_HTML_MODE_LEGACY)).toString());
                    } else {
                        itemExtraInfo.setText((Html.fromHtml(listItem.getBaseItem().description)).toString());
                    }

                    itemExtraInfo.setVisibility(View.VISIBLE);
                }
                else {
                    itemExtraInfoHeader.setVisibility(View.GONE);
                    separator.setVisibility(View.GONE);
                }

                if (listItem.getBaseItem().detailsUrl != null && !listItem.getBaseItem().detailsUrl.isEmpty()) {
                    itemUrl.setText(listItem.getBaseItem().detailsUrl);
                    itemUrl.setVisibility(View.VISIBLE);

                    itemUrlLayout.setOnClickListener(view12 -> {

                        if (!listItem.getBaseItem().detailsUrl.startsWith("http://") && !listItem.getBaseItem().detailsUrl.startsWith("https://"))
                            listItem.getBaseItem().detailsUrl = "http://" + listItem.getBaseItem().detailsUrl;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listItem.getBaseItem().detailsUrl));
                        startActivity(browserIntent);
                    });
                }
                else {
                    itemUrlLayout.setVisibility(View.GONE);
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

            populateLabel(listItem.getType());
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

    public void populateLabel(int type) {
        switch (type) {
            case MOVIES:
                listType.setBackgroundResource(R.color.accent_dark);
                listType.setText("MOVIES");
                break;
            case TV_SERIES:
                listType.setBackgroundResource(R.color.pink);
                listType.setText("TV SERIES");
                break;
            case ANIMES:
                listType.setBackgroundResource(R.color.green);
                listType.setText("ANIMES");
                break;
            case MANGAS:
                listType.setBackgroundResource(R.color.sienna);
                listType.setText("MANGAS");
                break;
            case BOOKS:
                listType.setBackgroundResource(R.color.orange);
                listType.setText("BOOKS");
                break;
            case MUSICS:
                listType.setBackgroundResource(R.color.saffron);
                listType.setText("MUSICS");
                break;
            case COMICS:
                listType.setBackgroundResource(R.color.purple);
                listType.setText("COMICS");
                break;

            default:
                listType.setBackgroundResource(R.color.primary_light);
                listType.setText("MISC");

        }
        listType.setVisibility(View.VISIBLE);
    }


}
