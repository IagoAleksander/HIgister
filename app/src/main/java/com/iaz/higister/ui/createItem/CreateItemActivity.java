package com.iaz.higister.ui.createItem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.iaz.higister.R;
import com.iaz.higister.data.model.BaseItem;
import com.iaz.higister.data.model.ComicVine.Results;
import com.iaz.higister.data.model.GoodReads.BestBook;
import com.iaz.higister.data.model.LastFM.Track;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.MyAnimeList.Result;
import com.iaz.higister.data.model.Omdb.Search;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.util.AppBarStateChangeListener;
import com.iaz.higister.util.CustomPhotoPickerDialog;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.iaz.higister.util.Constants.PERMISSION_WRITE_EXTERNAL;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class CreateItemActivity extends BaseActivity implements CreateItemMvpView {

    @Inject
    CreateItemPresenter mCreateItemPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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
    @BindView(R.id.activity_create_list_next_button)
    TextView nextButton;

    BaseItem item;
    UserList list;

    private CustomPhotoPickerDialog photoDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mCreateItemPresenter.setActivity(this);

        setContentView(R.layout.activity_create_item);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            item = getIntent().getExtras().getParcelable("item");
            list = getIntent().getExtras().getParcelable("list");
        }

        mCreateItemPresenter.attachView(this);
        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Create List Item");

        if (item != null) {
            Glide.with(this)
                    .load(item.imageUrl)
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

            itemTitle.setText(item.title);
            itemDescription.setText(item.description);
        }

        listLogoImageLayout.setOnClickListener(v -> {
            photoDialog = new CustomPhotoPickerDialog(CreateItemActivity.this, new CustomPhotoPickerDialog
                    .OnOptionPhotoSelected() {
                @Override
                public void onGallery() {
                    mCreateItemPresenter.openDialogWindow();
                    photoDialog.dismiss();
                }

                @Override
                public void onCamera() {
                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(CreateItemActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(CreateItemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        photoDialog.dismiss();
                        ActivityCompat.requestPermissions(CreateItemActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                    } else {
                        mCreateItemPresenter.getPhoto();
                        photoDialog.dismiss();
                    }
                }
            });
            photoDialog.show();

        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ViewItemFragment.this, ViewListActivity.class);
//                ViewItemFragment.this.startActivity(intent);

                ListItem listItem = new ListItem();
                listItem.name = listNameLayout.getEditText().getText().toString();
                listItem.description = listDescriptionLayout.getEditText().getText().toString();
                listItem.baseItem = item;

                list.listItems.add(listItem);
                mCreateItemPresenter.checkIfExists(list);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
//                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                break;
//            case R.id.action_next:
//                goToNextSection();
//                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mCreateItemPresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCreateItemPresenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void callGlide(Uri uri) {
        if (uri != null) {
            listLogoImagePlaceholder.setVisibility(View.GONE);
        }
        try {
            Glide.with(CreateItemActivity.this)
                    .load(uri)
                    .into(listLogoImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showSnackBar(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissDialog() {
        if (photoDialog != null)
            photoDialog.cancel();
    }

    @Override
    public CreateItemActivity getActivity() {
        return this;
    }

}
