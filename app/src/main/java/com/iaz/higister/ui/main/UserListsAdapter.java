package com.iaz.higister.ui.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iaz.higister.R;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.createList.CreateListActivity;
import com.iaz.higister.ui.viewList.ViewListActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListsAdapter extends RecyclerView.Adapter<UserListsAdapter.ListViewHolder> {

    private ArrayList<UserList> mLists;
    private MyListsFragment fragment;
    private String type;

    public UserListsAdapter(MyListsFragment fragment, ArrayList<UserList> lists, String type) {
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
        holder.listNameTextView.setText(mLists.get(position).name);
        if (mLists.get(position).listPictureUri != null) {
            Glide.with(fragment)
                    .load(mLists.get(position).listPictureUri)
                    .into(holder.image);
        }

        holder.image.setOnClickListener(v -> {
            Intent intent = new Intent(fragment.getActivity(), ViewListActivity.class);
            intent.putExtra("list", mLists.get(position));
            fragment.getActivity().startActivity(intent);
        });

        if (type.equals("created")) {

            holder.favoriteButton.setVisibility(View.GONE);

            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getActivity(), CreateListActivity.class);
                intent.putExtra("list", mLists.get(position));
                fragment.getActivity().startActivity(intent);
            });

            holder.removeButton.setOnClickListener(v ->
                    fragment.mListsPresenter.removeList(mLists.get(position), new MyListsPresenter.OnListRemoved() {
                        @Override
                        public void onSuccess() {
                            mLists.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailed(String exception) {
                            Log.d("onRemoveList", exception);
                        }
                    }));
        } else if (type.equals("favorited")) {
            holder.editButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);

            holder.favoriteButton.setOnClickListener(v ->
//                    fragment.mListsPresenter.favoriteList(mLists.get(position), new MyListsPresenter.OnListFavorited() {
//                        @Override
//                        public void onSuccess() {
////                mLists.remove(position);
////                notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onFailed(Exception e) {
//                            Log.d("onFavoriteList", e.getMessage());
//                        }
//                    })

                    fragment.mListsPresenter.unfavoriteList(mLists.get(position), new MyListsPresenter.OnListRemoved() {
                                @Override
                                public void onSuccess() {
                                    mLists.remove(position);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailed(String exception) {

                                }
                            }
                    ));
        } else {

            holder.editButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);

            if (fragment.mListsPresenter.favoritedListsId.contains(mLists.get(position).uid)) {
                holder.favoriteButton.setText("unfavorite");

                holder.favoriteButton.setOnClickListener(v ->
                        fragment.mListsPresenter.unfavoriteList(mLists.get(position), new MyListsPresenter.OnListRemoved() {
                            @Override
                            public void onSuccess() {
                                mLists.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailed(String exception) {

                            }
                        }
                ));

            }
            else {
                holder.favoriteButton.setText("favorite");
                holder.favoriteButton.setOnClickListener(v ->
                        fragment.mListsPresenter.favoriteList(mLists.get(position), new MyListsPresenter.OnListFavorited() {
                            @Override
                            public void onSuccess() {
//                mLists.remove(position);
//                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailed(Exception e) {
                                Log.d("onFavoriteList", e.getMessage());
                            }
                        }));
            }

        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.view_hex_color) View hexColorView;
        @BindView(R.id.list_name)
        TextView listNameTextView;
//        @BindView(R.id.item_description) TextView emailTextView;

        @BindView(R.id.item_image)
        ImageView image;

        @BindView(R.id.edit_button)
        Button editButton;

        @BindView(R.id.favorite_button)
        Button favoriteButton;

        @BindView(R.id.remove_button)
        Button removeButton;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
