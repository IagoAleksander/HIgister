package com.iaz.higister.ui.createItem;

import android.Manifest;
import android.support.v4.app.FragmentTransaction;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.BaseItem;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.data.repository.ListRepository;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.viewList.ViewListActivity;
import com.iaz.higister.util.CustomPhotoPickerDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.PERMISSION_WRITE_EXTERNAL;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class CreateItemActivity extends BaseActivity {

    @Inject
    CreateItemPresenter mCreateItemPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.next_button)
    TextView nextButton;

    UserList list;
    int position;
    ListItem listItem;

    public String listName;
    public String listDescription;

    private CustomPhotoPickerDialog photoDialog;
    private static final int CONTENT_VIEW_ID = 10101010;

    ListRepository listRepository = new ListRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mCreateItemPresenter.setActivity(this);

        setContentView(R.layout.activity_create_item);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            position = getIntent().getExtras().getInt("position", 0);
            list = getIntent().getExtras().getParcelable("list");

            if (position == -1) {
                listItem = getIntent().getExtras().getParcelable("listItem");
            } else {
                listItem = list.listItems.get(position);
            }
        }

//        mCreateItemPresenter.attachView(this);
        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        frameLayout.setId(CONTENT_VIEW_ID);

        if (list != null) {

            if (position == -1) {
                getSupportActionBar().setTitle("Edit List Item");
            } else {
                getSupportActionBar().setTitle("Create List Item");
            }

            Fragment newFragment = CreateItemFragment.newInstance(listItem);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(CONTENT_VIEW_ID, newFragment).commit();
        }

        nextButton.setOnClickListener(v -> {

            if (position == -1) {
                list.listItems.add(listItem);

                position = list.listItems.size() - 1;

                list.listItems.get(position).name = listName;
                list.listItems.get(position).description = listDescription;

                if (list.listItems.size() == 1) {
                    list.creatorId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    listRepository.saveList(list);
                } else {
                    listRepository.saveItem(list, list.listItems.size() - 1, new ListRepository.OnItemSaved() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(CreateItemActivity.this, ViewListActivity.class);
                            intent.putExtra("list", list);
                            CreateItemActivity.this.startActivity(intent);
                        }

                        @Override
                        public void onFailed(Exception exception) {
                            Log.d("saveItem: ", exception.getMessage());
                        }
                    });
                }
            } else {

                list.listItems.get(position).name = listName;
                list.listItems.get(position).description = listDescription;

                listRepository.updateItem(list, position, new ListRepository.OnItemUpdated() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(CreateItemActivity.this, ViewListActivity.class);
                        intent.putExtra("list", list);
                        CreateItemActivity.this.startActivity(intent);
                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.d("updateItem: ", exception.getMessage());
                    }
                });
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

}
