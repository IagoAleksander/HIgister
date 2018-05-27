package com.iaz.HIgister.ui.viewList;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.HIgister.data.model.ListItem;
import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.data.model.UserList;
import com.iaz.HIgister.data.repository.ListRepository;
import com.iaz.HIgister.data.repository.UserRepository;
import com.iaz.HIgister.ui.base.BaseActivity;
import com.iaz.HIgister.ui.createList.CreateListActivity;
import com.iaz.HIgister.ui.gallery.GalleryActivity;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.ui.search.SearchActivity;
import com.iaz.HIgister.ui.viewUser.ViewUserActivity;
import com.iaz.HIgister.util.CustomPhotoPickerDialog;
import com.iaz.HIgister.util.DialogFactory;
import com.iaz.HIgister.util.ViewUtil;
import com.iaz.Higister.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.list_name)
    TextView listName;
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
    @BindView(R.id.list_layout)
    LinearLayout listLayout;
    @BindView(R.id.add_item_text_layout)
    CardView addItemTextLayout;
    @BindView(R.id.bottom_bar)
    RelativeLayout bottomBar;
    @BindView(R.id.add_new_item_button)
    TextView addNewItemButton;
    @BindView(R.id.creators_layout)
    LinearLayout creatorsLayout;
    @BindView(R.id.creator_name_text)
    TextView creatorNameText;
    @BindView(R.id.description_layout)
    LinearLayout descriptionLayout;
    @BindView(R.id.description_button_text)
    TextView descriptionButtonText;
    @BindView(R.id.description_button_image)
    ImageView descriptionButtonImage;
    @BindView(R.id.expandable_layout)
    ExpandableLayout expandableLayout;

    @BindView(R.id.favorite_button_layout)
    LinearLayout favoriteButtonLayout;
    @BindView(R.id.favorite_button)
    LikeButton favoriteButton;
    @BindView(R.id.like_button_layout)
    LinearLayout likeButtonLayout;
    @BindView(R.id.like_button)
    LikeButton likeButton;
    @BindView(R.id.remove_button)
    Button removeButton;
    @BindView(R.id.edit_button_layout)
    LinearLayout editButtonLayout;
    @BindView(R.id.edit_button)
    Button editButton;
    @BindView(R.id.share_button)
    Button shareButton;
    @BindView(R.id.share_button_layout)
    LinearLayout shareButtonLayout;

    UserList list;
    User user;
    boolean canClick = true;

    Dialog mDialog;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() != null)
            list = getIntent().getExtras().getParcelable("list");

        if (list != null) {
            populateList();
        } else if (getIntent().getExtras().getString("listId") != null)
            listRepository.receiveListById(getIntent().getExtras().getString("listId"), new ListRepository.OnListFetched() {
                @Override
                public void onSuccess(UserList userList) {
                    list = userList;
                    populateList();
                }

                @Override
                public void onFailed(String exception) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                }
            });

        descriptionLayout.setOnClickListener(view ->
                showDescriptiion());
    }

    public void populateList() {
        listName.setText(list.getName());

        if (list.getDescription() != null && !list.getDescription().isEmpty()) {
            listDescription.setText(list.getDescription());
            descriptionLayout.setVisibility(View.VISIBLE);
        } else {
            descriptionLayout.setVisibility(View.GONE);
        }
        populateLabel(list.getType());

        listLogoImage.setVisibility(View.VISIBLE);
        if (list.getListPictureUri() != null) {

            Glide.with(this)
                    .asBitmap()
                    .load(list.getListPictureUri())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Bitmap resourceWithBorder = ViewUtil.addWhiteBorder(resource, 3);
                            listLogoImage.setImageBitmap(resourceWithBorder);
                        }
                    });

            listLogoImage.setOnClickListener(view -> {
                Intent goToCarousel = new Intent(getApplicationContext(), GalleryActivity.class);
                ArrayList<String> listOfPaths = new ArrayList<>();
                listOfPaths.add(list.getListPictureUri());

                goToCarousel.putStringArrayListExtra("photos", listOfPaths);
                goToCarousel.putExtra("startPosition", 0);
                getApplicationContext().startActivity(goToCarousel);

            });
        }

        if (list.uid != null) {
            mViewListPresenter.getListItems(list.uid);
        } else {
            updateData(new ArrayList<>());
        }

        if (list.getListItems() != null && !list.getListItems().isEmpty()) {

            shareButtonLayout.setVisibility(View.VISIBLE);
            shareButton.setOnClickListener(view -> mViewListPresenter.shareListToFacebook(list));

            if (list.getComments() != null && !list.getComments().isEmpty()) {
                populateCommentsLayout();

                if (!list.isCommentsEnabled()) {
                    addCommentButton.setVisibility(View.GONE);
                }
            } else if (!list.isCommentsEnabled()) {
                addCommentButton.setVisibility(View.GONE);
                commentsLayout.setVisibility(View.GONE);
                //TODO change button
            }

            MaterialDialog.Builder dialog = DialogFactory.newMaterialDialogWithInput(ViewListActivity.this, text -> {
                list.getComments().add(FirebaseAuth.getInstance().getCurrentUser().getUid() + ":" + text);
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
            });
            addCommentButton.setVisibility(View.VISIBLE);
            addCommentButton.setOnClickListener(view -> dialog.show());

        } else {
            addCommentButton.setVisibility(View.GONE);
            commentsLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.GONE);
            shareButtonLayout.setVisibility(View.GONE);

            if (list.getCreatorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                addItemTextLayout.setVisibility(View.VISIBLE);
        }

        if (list.getCreatorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            addNewItemButton.setVisibility(View.VISIBLE);
            addNewItemButton.setOnClickListener(v -> {
                if (list.getListItems().size() < 5) {
                    Intent intent = new Intent(ViewListActivity.this, SearchActivity.class);
                    intent.putExtra("list", list);
                    startActivity(intent);
                } else {
                    DialogFactory.newMaterialDialog(ViewListActivity.this, "It is not possible to add more than 5 items to a list. Please remove one of the others to add a new item").show();
                }
            });
            bottomBar.setVisibility(View.VISIBLE);
        } else {
            creatorsLayout.setVisibility(View.VISIBLE);
            creatorNameText.setText(list.getCreatorName());
            bottomBar.setOnClickListener(v -> {
                Intent intent = new Intent(ViewListActivity.this, ViewUserActivity.class);
                intent.putExtra("userId", list.getCreatorId());
                ViewListActivity.this.startActivity(intent);
            });
            bottomBar.setVisibility(View.VISIBLE);
        }

        if (!list.getCreatorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            favoriteButtonLayout.setVisibility(View.VISIBLE);
            likeButtonLayout.setVisibility(View.VISIBLE);

            favoriteButton.setLiked(list.getFavoritedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()));

            favoriteButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if (canClick) {
                        canClick = false;
                        listRepository.favoriteList(list, new ListRepository.OnListFavorited() {
                            @Override
                            public void onSuccess(String listUid) {
                                canClick = true;
                            }

                            @Override
                            public void onFailed(Exception e) {
                                canClick = true;
                                Log.d("onFavoriteList", e.getMessage());

                                likeButton.setLiked(false);
                            }
                        });
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (canClick) {
                        canClick = false;

                        listRepository.unfavoriteList(list, new ListRepository.OnListRemoved() {
                                    @Override
                                    public void onSuccess(String listUid) {
                                        canClick = true;
                                    }

                                    @Override
                                    public void onFailed(String exception) {
                                        canClick = true;
                                        likeButton.setLiked(true);
                                    }
                                }
                        );
                    }
                }
            });


            likeButton.setLiked(list.getLikedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()));
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if (canClick) {
                        canClick = false;
                        listRepository.likeList(list, new ListRepository.OnListLiked() {
                            @Override
                            public void onSuccess(String listUid) {
                                canClick = true;
                            }

                            @Override
                            public void onFailed(Exception e) {
                                canClick = true;
                                Log.d("onFavoriteList", e.getMessage());

                                likeButton.setLiked(false);
                            }
                        });
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (canClick) {
                        canClick = false;

                        listRepository.unlikeList(list, new ListRepository.OnListRemoved() {
                                    @Override
                                    public void onSuccess(String listUid) {
                                        canClick = true;
//                                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailed(String exception) {
                                        canClick = true;
                                        likeButton.setLiked(true);
                                    }
                                }
                        );
                    }
                }
            });
        } else if (list.uid != null) {
            removeButton.setVisibility(View.VISIBLE);
            removeButton.setOnClickListener(v -> {

                MaterialDialog dialog = DialogFactory.newMaterialDialogConfirmation(ViewListActivity.this, "Do you really want to remove this list? (all the information will be deleted and will not be recoverable anymore)").show();
                View positive = dialog.getActionButton(DialogAction.POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        mDialog = DialogFactory.newDialog(ViewListActivity.this, "Removing list...");
                        mDialog.show();

                        listRepository.removeList(list, new ListRepository.OnListRemoved() {
                            @Override
                            public void onSuccess(String listUid) {
                                DialogFactory.finalizeDialogOnClick(mDialog, true, "List removed with success", () -> {
                                    Intent intent = new Intent(ViewListActivity.this, MainActivity.class);
                                    ViewListActivity.this.startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                                });
                            }

                            @Override
                            public void onFailed(String exception) {
                                Log.d("onRemoveList", exception);
                                DialogFactory.finalizeDialogOnClick(mDialog, false, "Sorry, an error occurred on list removal", () -> {
                                });
                            }
                        });
                    }
                });

            });

            editButtonLayout.setVisibility(View.VISIBLE);
            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(ViewListActivity.this, CreateListActivity.class);
                intent.putExtra("list", list);
                ViewListActivity.this.startActivity(intent);
                ViewListActivity.this.overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
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
        } else {
            mListItemAdapter.setListItem(listItems);
            mListItemAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setAdapter(mListItemAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewListPresenter.attachView(this);
    }

    public void populateCommentsLayout() {
        commentsLayout.setVisibility(View.VISIBLE);
        for (int i = 0; i < list.getComments().size(); i++) {

            String entry = list.getComments().get(i);

            int index = entry.indexOf(":");
            String key = entry.substring(0, index);
            String value = entry.substring(index + 1, entry.length());

            int finalI = i;
            userRepository.receiveProfileInfo(key, new UserRepository.OnUpdateProfile() {
                @Override
                public void onSuccess(User user) {
                    ViewListActivity.this.user = user;

                    View item = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_comment, null);

                    RelativeLayout commentItem = item.findViewById(R.id.comment_item);
                    TextView userName = item.findViewById(R.id.text_name);
                    TextView userComment = item.findViewById(R.id.item_description);
                    CircleImageView userImage = item.findViewById(R.id.user_image);

                    commentsLayout.setVisibility(View.VISIBLE);

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
        listType.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
    }

    private void showDescriptiion() {
        expandableLayout.expand();
        descriptionButtonImage.setImageDrawable(ContextCompat.getDrawable(ViewListActivity.this, R.drawable.ic_keyboard_arrow_up));
        descriptionButtonText.setText(getResources().getString(R.string.hide_description));
        descriptionLayout.setOnClickListener(view -> hideDescriptiion());

    }

    private void hideDescriptiion() {
        expandableLayout.collapse();
        descriptionButtonImage.setImageDrawable(ContextCompat.getDrawable(ViewListActivity.this, R.drawable.ic_keyboard_arrow_down));
        descriptionButtonText.setText(getResources().getString(R.string.show_description));
        descriptionLayout.setOnClickListener(view -> showDescriptiion());
    }

}
