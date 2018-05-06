package com.iaz.higister.ui.viewList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.data.repository.ListRepository;
import com.iaz.higister.data.repository.UserRepository;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.main.MainActivity;
import com.iaz.higister.ui.search.SearchActivity;
import com.iaz.higister.util.CustomPhotoPickerDialog;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.iaz.higister.util.Constants.*;

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
    @BindView(R.id.label_text)
    TextView listType;
    @BindView(R.id.comments_layout)
    CardView commentsLayout;
    @BindView(R.id.comments_container)
    LinearLayout commentsContainer;
    @BindView(R.id.add_comment_button)
    Button addCommentButton;

    @BindView(R.id.add_item_text_layout)
    CardView addItemTextLayout;
    @BindView(R.id.bottom_bar)
    RelativeLayout bottomBar;
    @BindView(R.id.add_new_item_button)
    TextView addNewItemButton;

    UserList list;
    User user;

    private CustomPhotoPickerDialog photoDialog;

    ListItemAdapter mListItemAdapter;
    ListRepository listRepository = new ListRepository();
    UserRepository userRepository = new UserRepository();


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
            listDescription.setText(list.getDescription());
            populateLabel(list.getType());

            if (list.getListPictureUri() != null) {
                Glide.with(this)
                        .load(list.getListPictureUri())
                        .into(listLogoImage);
            }

            if (list.uid != null) {
                mViewListPresenter.getListItems(list.uid);
            }
            else {
                updateData(new ArrayList<>());
            }

            if (list.getListItems() != null && !list.getListItems().isEmpty()) {

                if (list.getComments() != null && !list.getComments().isEmpty()) {
                    populateCommentsLayout();
                }
                addCommentButton.setOnClickListener(view -> new MaterialDialog.Builder(ViewListActivity.this)
                        .title("Add new comment")
                        .cancelable(true)
                        .negativeText("cancel")
                        .input("Insert a new comment", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                list.getComments().add(FirebaseAuth.getInstance().getCurrentUser().getUid() + ":" + input.toString());
                                listRepository.updateListInfo(list, new ListRepository.OnListUpdated() {
                                    @Override
                                    public void onSuccess() {

                                        commentsContainer.removeAllViews();
                                        populateCommentsLayout();
                                    }

                                    @Override
                                    public void onFailed(Exception exception) {

                                    }
                                });
                            }
                        }).show());

            }
            else {
                addCommentButton.setVisibility(View.GONE);
                commentsLayout.setVisibility(View.GONE);
                addItemTextLayout.setVisibility(View.VISIBLE);
            }

            if (list.getCreatorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                addNewItemButton.setOnClickListener(v -> {
                    Intent intent = new Intent(ViewListActivity.this, SearchActivity.class);
                    intent.putExtra("list", list);
                    startActivity(intent);
                });
            }
            else {
                bottomBar.setVisibility(View.GONE);
            }
        }
        else {
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
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

        list.setListItems(listItems);

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

    public void populateCommentsLayout() {
        for(int i = 0; i < list.getComments().size(); i++) {

            String entry = list.getComments().get(i);

            int index = entry.indexOf(":");
            String key = entry.substring(0, index);
            String value = entry.substring(index+1, entry.length());

            int finalI = i;
            userRepository.receiveProfileInfo(key, new UserRepository.OnUpdateProfile() {
                @Override
                public void onSuccess(User user) {
                    ViewListActivity.this.user = user;

                    android.view.View item = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_comment, null);

                    RelativeLayout commentItem = item.findViewById(R.id.comment_item);
                    TextView userName = item.findViewById(R.id.text_name);
                    TextView userComment = item.findViewById(R.id.item_description);
                    CircleImageView userImage = item.findViewById(R.id.user_image);

                    commentsLayout.setVisibility(android.view.View.VISIBLE);

                    userName.setText(user.getName());
                    userComment.setText(value);
                    if (key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        commentItem.setOnLongClickListener(view -> {
                            new MaterialDialog.Builder(ViewListActivity.this)
                                    .title("Edit comment")
                                    .cancelable(true)
                                    .negativeText("cancel")
                                    .input("", value, (dialog, input) -> {
                                        list.getComments().set(finalI, FirebaseAuth.getInstance().getCurrentUser().getUid() + ":" + input.toString());
                                        listRepository.updateListInfo(list, new ListRepository.OnListUpdated() {
                                            @Override
                                            public void onSuccess() {

                                                commentsContainer.removeAllViews();
                                                populateCommentsLayout();
                                            }

                                            @Override
                                            public void onFailed(Exception exception) {

                                            }
                                        });
                                    }).show();
                            return true;
                        });
                    }

                    Glide.with(ViewListActivity.this)
                            .load(user.getProfilePictureUri())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    commentsContainer.addView(item);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    commentsContainer.addView(item);
                                    return false;
                                }
                            })
                            .into(userImage);



                }

                @Override
                public void onFailure(String exception) {

                }
            });
        }
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
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

}
