package com.iaz.HIgister.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.HIgister.data.model.UserList;
import com.iaz.HIgister.data.repository.ListRepository;
import com.iaz.HIgister.ui.createList.CreateListActivity;
import com.iaz.HIgister.ui.viewList.ViewListActivity;
import com.iaz.HIgister.ui.viewUser.ViewUserActivity;
import com.iaz.HIgister.util.DialogFactory;
import com.iaz.Higister.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

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

public class MyListsAdapter extends RecyclerView.Adapter<MyListsAdapter.ListViewHolder> {

    private ArrayList<UserList> mLists;
    private MyListsFragment fragment;
    private String type;

    boolean canClick = true;

    ListRepository listRepository = new ListRepository();
    private Dialog mDialog;

    MyListsAdapter(MyListsFragment fragment, ArrayList<UserList> lists, String type) {
        mLists = lists;
        this.fragment = fragment;
        this.type = type;


    }

    public void setLists(ArrayList<UserList> lists) {
        mLists = lists;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_big, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {

        UserList currentList = mLists.get(position);

        if (currentList != null) {
            if (mLists.get(position).getName() != null)
                holder.listNameTextView.setText(mLists.get(position).getName());
            else
                holder.listNameTextView.setVisibility(View.GONE);

            if (mLists.get(position).getDescription() != null)
                holder.listDescriptionTextView.setText(mLists.get(position).getDescription());
            else
                holder.listDescriptionTextView.setVisibility(View.GONE);

            if (mLists.get(position).getListPictureUri() != null) {
                Glide.with(fragment)
                        .load(mLists.get(position).getListPictureUri())
                        .into(holder.image);
            } else {
                holder.image.setImageDrawable(ResourcesCompat.getDrawable(fragment.getResources(), R.drawable.large_movie_poster, null));
            }

            holder.listItem.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getActivity(), ViewListActivity.class);
                intent.putExtra("list", mLists.get(position));
                fragment.getActivity().startActivity(intent);
                fragment.getActivity().overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
            });

            if (mLists.get(position).getCreatorName() != null && !mLists.get(position).getCreatorName().isEmpty()) {

                if (mLists.get(position).getCreatorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    holder.creatorNameText.setText("By me");
                    holder.creatorsLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.creatorNameText.setText(String.format("By %s", mLists.get(position).getCreatorName()));
                    holder.creatorsLayout.setVisibility(View.VISIBLE);

                    holder.creatorsLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(fragment.getActivity(), ViewUserActivity.class);
                            intent.putExtra("userId", mLists.get(position).getCreatorId());
//                            intent.putStringArrayListExtra("myFavoritedListsId", fragment.activity.favoritedListsId);
                            fragment.getActivity().startActivity(intent);
                        }
                    });
                }
            } else
                holder.creatorsLayout.setVisibility(View.GONE);

            switch (type) {
                case "created":

                    holder.editButtonLayout.setVisibility(View.VISIBLE);
                    holder.removeButton.setVisibility(View.VISIBLE);
                    holder.favoriteButtonLayout.setVisibility(View.GONE);
                    holder.likeButtonLayout.setVisibility(View.GONE);

                    holder.editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(fragment.getActivity(), CreateListActivity.class);
                            intent.putExtra("list", mLists.get(position));
                            fragment.getActivity().startActivity(intent);
                            fragment.getActivity().overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
                        }
                    });

                    holder.removeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            MaterialDialog dialog = DialogFactory.newMaterialDialogConfirmation(fragment.getActivity(), "Do you really want to remove this list? (all the information will be deleted and will not be recoverable anymore)").show();
                            View positive = dialog.getActionButton(DialogAction.POSITIVE);
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();

                                    mDialog = DialogFactory.newDialog(fragment.activity, "Removing list...");
                                    mDialog.show();

                                    listRepository.removeList(mLists.get(position), new ListRepository.OnListRemoved() {
                                        @Override
                                        public void onSuccess(String listUid) {
                                            DialogFactory.finalizeDialogOnClick(mDialog, true, "List removed with success", () ->
                                                    notifyDataSetChanged()
                                            );
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

                        }
                    });

                    break;
                case "favorited":

                    holder.editButtonLayout.setVisibility(View.GONE);
                    holder.removeButton.setVisibility(View.GONE);
                    holder.favoriteButtonLayout.setVisibility(View.VISIBLE);
                    holder.likeButtonLayout.setVisibility(View.VISIBLE);

                    holder.favoriteButton.setLiked(true);

                    holder.favoriteButton.setOnLikeListener(new OnLikeListener() {
                        @Override
                        public void liked(LikeButton likeButton) {
                        }

                        @Override
                        public void unLiked(LikeButton likeButton) {
                            if (canClick) {
                                canClick = false;
//                                if (fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
//                                    fragment.activity.favoritedListsId.remove(mLists.get(position).uid);

                                listRepository.unfavoriteList(mLists.get(position), new ListRepository.OnListRemoved() {
                                            @Override
                                            public void onSuccess(String listUid) {
                                                canClick = true;
                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onFailed(String exception) {
                                                canClick = true;
                                                likeButton.setLiked(true);

//                                                if (!fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
//                                                    fragment.activity.favoritedListsId.add(mLists.get(position).uid);
                                            }
                                        }
                                );
                            }
                        }
                    });

                    holder.likeButton.setLiked(mLists.get(position).getLikedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                    holder.likeButton.setOnLikeListener(new OnLikeListener() {
                        @Override
                        public void liked(LikeButton likeButton) {
                            if (canClick) {
                                canClick = false;
                                listRepository.likeList(mLists.get(position), new ListRepository.OnListLiked() {
                                    @Override
                                    public void onSuccess(String listUid) {
                                        canClick = true;
//                                            notifyDataSetChanged();
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

                                listRepository.unlikeList(mLists.get(position), new ListRepository.OnListRemoved() {
                                            @Override
                                            public void onSuccess(String listUid) {
                                                canClick = true;
//                                                    notifyDataSetChanged();
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

                    break;
                default:

                    if (mLists.get(position).getCreatorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        holder.editButtonLayout.setVisibility(View.VISIBLE);
                        holder.removeButton.setVisibility(View.VISIBLE);
                        holder.favoriteButtonLayout.setVisibility(View.GONE);
                        holder.likeButtonLayout.setVisibility(View.GONE);

                        holder.editButton.setOnClickListener(v -> {
                            Intent intent = new Intent(fragment.getActivity(), CreateListActivity.class);
                            intent.putExtra("list", mLists.get(position));
                            fragment.getActivity().startActivity(intent);
                            fragment.getActivity().overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
                        });

                        holder.removeButton.setOnClickListener(v -> {

                            MaterialDialog dialog = DialogFactory.newMaterialDialogConfirmation(fragment.getActivity(), "Do you really want to remove this list? (all the information will be deleted and will not be recoverable anymore)").show();
                            View positive = dialog.getActionButton(DialogAction.POSITIVE);
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();

                                    mDialog = DialogFactory.newDialog(fragment.activity, "Removing list...");
                                    mDialog.show();

                                    listRepository.removeList(mLists.get(position), new ListRepository.OnListRemoved() {
                                        @Override
                                        public void onSuccess(String listUid) {
                                            DialogFactory.finalizeDialogOnClick(mDialog, true, "List removed with success", () -> {
                                                notifyDataSetChanged();
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
                    } else {
                        holder.editButtonLayout.setVisibility(View.GONE);
                        holder.removeButton.setVisibility(View.GONE);
                        holder.favoriteButtonLayout.setVisibility(View.VISIBLE);
                        holder.likeButtonLayout.setVisibility(View.VISIBLE);

                        holder.favoriteButton.setLiked(mLists.get(position).getFavoritedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()));

                        holder.favoriteButton.setOnLikeListener(new OnLikeListener() {
                            @Override
                            public void liked(LikeButton likeButton) {
                                if (canClick) {
                                    canClick = false;
                                    listRepository.favoriteList(mLists.get(position), new ListRepository.OnListFavorited() {
                                        @Override
                                        public void onSuccess(String listUid) {
                                            canClick = true;
//                                            if (!fragment.activity.favoritedListsId.contains(listUid))
//                                                fragment.activity.favoritedListsId.add(listUid);

                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailed(Exception e) {
                                            canClick = true;
                                            Log.d("onFavoriteList", e.getMessage());

                                            likeButton.setLiked(false);

//                                            if (fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
//                                                fragment.activity.favoritedListsId.remove(mLists.get(position).uid);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void unLiked(LikeButton likeButton) {
                                if (canClick) {
                                    canClick = false;
//                                    if (fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
//                                        fragment.activity.favoritedListsId.remove(mLists.get(position).uid);

                                    listRepository.unfavoriteList(mLists.get(position), new ListRepository.OnListRemoved() {
                                                @Override
                                                public void onSuccess(String listUid) {
                                                    canClick = true;
                                                    notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onFailed(String exception) {
                                                    canClick = true;
                                                    likeButton.setLiked(true);

//                                                    if (!fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
//                                                        fragment.activity.favoritedListsId.add(mLists.get(position).uid);
                                                }
                                            }
                                    );
                                }
                            }
                        });


                        holder.likeButton.setLiked(mLists.get(position).getLikedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                        holder.likeButton.setOnLikeListener(new OnLikeListener() {
                            @Override
                            public void liked(LikeButton likeButton) {
                                if (canClick) {
                                    canClick = false;
                                    listRepository.likeList(mLists.get(position), new ListRepository.OnListLiked() {
                                        @Override
                                        public void onSuccess(String listUid) {
                                            canClick = true;
//                                                notifyDataSetChanged();
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

                                    listRepository.unlikeList(mLists.get(position), new ListRepository.OnListRemoved() {
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

                    }

                    break;
            }

            populateLabel(holder, mLists.get(position).getType());

        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public void populateLabel(final ListViewHolder holder, int type) {
        switch (type) {
            case MOVIES:
                holder.labelLayout.setBackgroundResource(R.color.accent_dark);
                holder.labelText.setText("MOVIES");
                break;
            case TV_SERIES:
                holder.labelLayout.setBackgroundResource(R.color.pink);
                holder.labelText.setText("TV SERIES");
                break;
            case ANIMES:
                holder.labelLayout.setBackgroundResource(R.color.green);
                holder.labelText.setText("ANIMES");
                break;
            case MANGAS:
                holder.labelLayout.setBackgroundResource(R.color.sienna);
                holder.labelText.setText("MANGAS");
                break;
            case BOOKS:
                holder.labelLayout.setBackgroundResource(R.color.orange);
                holder.labelText.setText("BOOKS");
                break;
            case MUSICS:
                holder.labelLayout.setBackgroundResource(R.color.saffron);
                holder.labelText.setText("MUSICS");
                break;
            case COMICS:
                holder.labelLayout.setBackgroundResource(R.color.purple);
                holder.labelText.setText("COMICS");
                break;

            default:
                holder.labelLayout.setBackgroundResource(R.color.primary_light);
                holder.labelText.setText("MISC");

        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.label_layout)
        RelativeLayout labelLayout;

        @BindView(R.id.label_text)
        TextView labelText;

        @BindView(R.id.list_item)
        RelativeLayout listItem;
        //        @BindView(R.id.view_hex_color) View hexColorView;
        @BindView(R.id.list_name)
        TextView listNameTextView;

        @BindView(R.id.list_description)
        TextView listDescriptionTextView;

        @BindView(R.id.item_image)
        ImageView image;

        @BindView(R.id.edit_button_layout)
        LinearLayout editButtonLayout;

        @BindView(R.id.edit_button)
        Button editButton;

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

        @BindView(R.id.creator_name_text)
        TextView creatorNameText;

        @BindView(R.id.creators_layout)
        LinearLayout creatorsLayout;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
