package com.iaz.higister.ui.main;

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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.data.repository.ListRepository;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.ui.viewList.ViewListActivity;
import com.iaz.higister.util.DialogFactory;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iaz.higister.util.Constants.ANIMES;
import static com.iaz.higister.util.Constants.BOOKS;
import static com.iaz.higister.util.Constants.COMICS;
import static com.iaz.higister.util.Constants.MANGAS;
import static com.iaz.higister.util.Constants.MOVIES;
import static com.iaz.higister.util.Constants.MUSICS;
import static com.iaz.higister.util.Constants.TV_SERIES;

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

        listRepository.addListener(fragment.activity, type, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                new ListRepository.OnUpdateLists() {
                    @Override
                    public void onSuccess(ArrayList<UserList> userLists) {

                        mLists = userLists;


                        if (type.equals("created") && fragment.activity != null && fragment.activity.user != null) {
                            fragment.activity.user.setListsCreatedNumber(userLists.size());
                            fragment.activity.updateUserInfo();
                        } else if (type.equals("favorited") && fragment.activity != null) {
                            fragment.activity.favoritedListsId.clear();

                            for (UserList list : userLists) {
                                fragment.activity.favoritedListsId.add(list.uid);
                            }

                            if (fragment.activity.user == null)
                                fragment.activity.user = new User();

                            fragment.activity.user.setListsFavouritedNumber(userLists.size());
                            fragment.activity.updateUserInfo();
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });
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
                holder.creatorNameText.setText(String.format("By %s", mLists.get(position).getCreatorName()));
                holder.creatorNameText.setVisibility(View.VISIBLE);
            } else
                holder.creatorNameText.setVisibility(View.GONE);

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
                                if (fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
                                    fragment.activity.favoritedListsId.remove(mLists.get(position).uid);

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

                                                if (!fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
                                                    fragment.activity.favoritedListsId.add(mLists.get(position).uid);
                                            }
                                        }
                                );
                            }
                        }
                    });
                    if (mLists.get(position).getLikedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        holder.likeButton.setLiked(true);
                        holder.likeButton.setOnLikeListener(new OnLikeListener() {
                            @Override
                            public void liked(LikeButton likeButton) {

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
                    }
                    else {
                        holder.likeButton.setLiked(false);
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

                            }
                        });
                    }
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
                        });
                    } else {
                        holder.editButtonLayout.setVisibility(View.GONE);
                        holder.removeButton.setVisibility(View.GONE);
                        holder.favoriteButtonLayout.setVisibility(View.VISIBLE);
                        holder.likeButtonLayout.setVisibility(View.VISIBLE);

                        if (fragment.activity.favoritedListsId.contains(mLists.get(position).uid)) {
                            holder.favoriteButton.setLiked(true);

                            holder.favoriteButton.setOnLikeListener(new OnLikeListener() {
                                @Override
                                public void liked(LikeButton likeButton) {

                                }

                                @Override
                                public void unLiked(LikeButton likeButton) {
                                    if (canClick) {
                                        canClick = false;
                                        if (fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
                                            fragment.activity.favoritedListsId.remove(mLists.get(position).uid);

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

                                                        if (!fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
                                                            fragment.activity.favoritedListsId.add(mLists.get(position).uid);
                                                    }
                                                }
                                        );
                                    }
                                }
                            });

                        } else {
                            holder.favoriteButton.setLiked(false);
                            holder.favoriteButton.setOnLikeListener(new OnLikeListener() {
                                @Override
                                public void liked(LikeButton likeButton) {
                                    if (canClick) {
                                        canClick = false;
                                        listRepository.favoriteList(mLists.get(position), new ListRepository.OnListFavorited() {
                                            @Override
                                            public void onSuccess(String listUid) {
                                                canClick = true;
                                                if (!fragment.activity.favoritedListsId.contains(listUid))
                                                    fragment.activity.favoritedListsId.add(listUid);

                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onFailed(Exception e) {
                                                canClick = true;
                                                Log.d("onFavoriteList", e.getMessage());

                                                likeButton.setLiked(false);

                                                if (fragment.activity.favoritedListsId.contains(mLists.get(position).uid))
                                                    fragment.activity.favoritedListsId.remove(mLists.get(position).uid);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void unLiked(LikeButton likeButton) {

                                }
                            });

                        }
                        if (mLists.get(position).getLikedBy().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            holder.likeButton.setLiked(true);
                            holder.likeButton.setOnLikeListener(new OnLikeListener() {
                                @Override
                                public void liked(LikeButton likeButton) {

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
                        else {
                            holder.likeButton.setLiked(false);
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

                                }
                            });
                        }
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

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
