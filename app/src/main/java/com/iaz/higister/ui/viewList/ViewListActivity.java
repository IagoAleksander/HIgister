package com.iaz.higister.ui.viewList;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iaz.higister.R;
import com.iaz.higister.data.model.BaseItem;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.search.SearchActivity;
import com.iaz.higister.util.CustomPhotoPickerDialog;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

/**
 * Created by Iago Aleksander on 06/03/18.
 */

public class ViewListActivity extends BaseActivity implements ViewListMvpView {

    @Inject
    public ViewListPresenter mViewListPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.listLogoImageView)
    ImageView listLogoImage;
    @BindView(R.id.list_item_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.list_description)
    TextView listDescription;
    @BindView(R.id.list_type)
    TextView listType;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    UserList list;

    private CustomPhotoPickerDialog photoDialog;

    ListItemAdapter mListItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mViewListPresenter.setActivity(this);

        setContentView(R.layout.activity_view_list);
        ButterKnife.bind(this);

        mViewListPresenter.attachView(this);
        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Test UserList");

        if (getIntent() != null)
            list = getIntent().getExtras().getParcelable("list");

        if (list != null) {
            listDescription.setText(list.description);
            listType.setText(Integer.toString(list.type));

            if (list.listPictureUri != null) {
                Glide.with(this)
                        .load(list.listPictureUri)
                        .into(listLogoImage);
            }

            if (list.uid != null) {
                mViewListPresenter.getListItems(list.uid);
            }
            else {
                updateData(new ArrayList<>());
            }
        }

        fab.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_add);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ViewListActivity.this, SearchActivity.class);
            intent.putExtra("list", list);
            startActivity(intent);
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
    public void updateData(ArrayList<ListItem> listItems) {

        list.listItems = listItems;

        if (mListItemAdapter == null) {
            mListItemAdapter = new ListItemAdapter(this, list);
        }
        else {
            mListItemAdapter.setListItem(listItems);
            mListItemAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setAdapter(mListItemAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewListPresenter.attachView(this);
    }

}
