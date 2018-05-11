package com.iaz.higister.ui.createList;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.data.repository.ListRepository;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.viewList.ViewListActivity;
import com.iaz.higister.util.AppBarStateChangeListener;
import com.iaz.higister.util.CustomPhotoPickerDialog;
import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.PERMISSION_WRITE_EXTERNAL;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class CreateListActivity extends BaseActivity implements CreateListMvpView {

    @Inject
    CreateListPresenter mCreateListPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.listLogoImageLayout)
    RelativeLayout listLogoImageLayout;
    @BindView(R.id.listLogoImageView)
    ImageView listLogoImage;
    @BindView(R.id.logo_placeholder)
    LinearLayout listLogoImagePlaceholder;
    @BindView(R.id.activity_create_page_add_banner)
    LinearLayout addBannerLayout;
    @BindView(R.id.list_banner)
    ImageView listBannerImage;
    @BindView(R.id.text_input_list_name)
    TextInputLayout listNameLayout;
    @BindView(R.id.text_input_list_desc)
    TextInputLayout listDescriptionLayout;
    @BindView(R.id.list_type_spinner)
    Spinner listTypeSpinner;
    @BindView(R.id.checkbox_list_isVisible)
    CheckBox isListVisible;
    @BindView(R.id.checkbox_list_commentsEnabled)
    CheckBox areCommentsEnabled;
    @BindView(R.id.activity_create_list_next_button)
    TextView nextButton;


    private CustomPhotoPickerDialog photoDialog;
    UserList list;

    String uri;
    ListRepository listRepository = new ListRepository();

    int typeSelected = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mCreateListPresenter.setActivity(this);

        setContentView(R.layout.activity_create_list);
        ButterKnife.bind(this);

        mCreateListPresenter.attachView(this);
        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() != null && getIntent().getExtras() != null)
            list = getIntent().getExtras().getParcelable("list");

        if (list != null) {

            getSupportActionBar().setTitle("Edit UserList");

            listNameLayout.getEditText().setText(list.getName());
            listDescriptionLayout.getEditText().setText(list.getDescription());

            if (list.getListPictureUri() != null) {
                Glide.with(this)
                        .load(list.getListPictureUri())
                        .into(listLogoImage);
            }

            isListVisible.setChecked(list.isVisibleForEveryone());
            areCommentsEnabled.setChecked(list.isCommentsEnabled());

            nextButton.setText("UPDATE LIST INFO");
        } else {
            getSupportActionBar().setTitle("Create UserList");
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int i) {

                float fraction = i / 400.0f;
                listLogoImageLayout.setAlpha(1.0f + fraction);
                addBannerLayout.setAlpha(1.0f + fraction);

                if (state == State.COLLAPSED) {
                    listLogoImageLayout.setVisibility(View.GONE);
                    addBannerLayout.setVisibility(View.GONE);
                } else {
                    listLogoImageLayout.setVisibility(View.VISIBLE);
                    addBannerLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        listLogoImageLayout.setOnClickListener(v -> {
            photoDialog = new CustomPhotoPickerDialog(CreateListActivity.this, new CustomPhotoPickerDialog
                    .OnOptionPhotoSelected() {
                @Override
                public void onGallery() {
                    mCreateListPresenter.openDialogWindow();
                    photoDialog.dismiss();
                }

                @Override
                public void onCamera() {
                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(CreateListActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(CreateListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        photoDialog.dismiss();
                        ActivityCompat.requestPermissions(CreateListActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                    } else {
                        mCreateListPresenter.getPhoto();
                        photoDialog.dismiss();
                    }
                }
            });
            photoDialog.show();

        });

        addBannerLayout.setOnClickListener(view -> {

            photoDialog = new CustomPhotoPickerDialog(CreateListActivity.this, new CustomPhotoPickerDialog
                    .OnOptionPhotoSelected() {
                @Override
                public void onGallery() {
                    mCreateListPresenter.openDialogWindowBanner();
                    photoDialog.dismiss();
                }

                @Override
                public void onCamera() {
                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(CreateListActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(CreateListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        photoDialog.dismiss();
                        ActivityCompat.requestPermissions(CreateListActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL);
                    } else {
                        mCreateListPresenter.getPhotoBanner();
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

                if (list == null)
                    list = new UserList();

                list.setName(listNameLayout.getEditText().getText().toString());
                list.setDescription(listDescriptionLayout.getEditText().getText().toString());
//                mCreateListPresenter.saveList(list);

                if (uri != null)
                    list.setListPictureUri(uri);

                list.setCreatorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                list.setCreatorName("Tester");
                //TODO

                list.setType(typeSelected);
                list.setVisibleForEveryone(isListVisible.isChecked());
                list.setCommentsEnabled(areCommentsEnabled.isChecked());

                if (list.uid == null) {
                    Intent intent = new Intent(CreateListActivity.this, ViewListActivity.class);
                    intent.putExtra("list", list);
                    startActivity(intent);
                } else {
                    listRepository.updateList(list, new ListRepository.OnListUpdated() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(CreateListActivity.this, ViewListActivity.class);
                            intent.putExtra("list", list);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.w("updateList: ", "failed", e);
                        }
                    });

                }
            }
        });

        bindSpinnerLabel();

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
        mCreateListPresenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCreateListPresenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void callGlide(Uri uri) {
        if (uri == null) {
            listLogoImagePlaceholder.setVisibility(View.GONE);
        }

        this.uri = uri.toString();
        try {
            Glide.with(CreateListActivity.this)
                    .load(uri)
                    .into(listLogoImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callGlideBanner(Uri uri) {
        try {
            Glide.with(CreateListActivity.this)
                    .load(uri)
                    .into(listBannerImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindSpinnerLabel() {

        String[] stringArray = getResources().getStringArray(R.array.list_type);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.item_list_type, stringArray) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                return view;
            }
        };

        listTypeSpinner.setAdapter(arrayAdapter);
        listTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    typeSelected = position;
                else
                    typeSelected = position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (list != null) {
            listTypeSpinner.setSelection(list.getType()+1);
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
    public CreateListActivity getActivity() {
        return this;
    }

}
