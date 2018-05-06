package com.iaz.higister.ui.viewList;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.createItem.CreateItemActivity;
import com.iaz.higister.ui.viewItem.ViewItemActivity;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    private UserList list;
    private ViewListActivity activity;

    public ListItemAdapter(Activity activity, UserList list) {
        this.list = list;

        if (activity instanceof ViewListActivity)
            this.activity = (ViewListActivity) activity;
    }

    public void setListItem(ArrayList<ListItem> listItems) {

        list.setListItems(listItems);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_big, parent, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, int position) {
        ListItem listItem = list.getListItems().get(position);

        holder.listNameTextView.setText(listItem.getName());

        if (listItem.getBaseItem() != null && listItem.getBaseItem().imageUrl != null)
            Glide.with(activity)
                    .load(listItem.getBaseItem().imageUrl)
                    .into(holder.image);

        holder.listItem.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ViewItemActivity.class);
            intent.putParcelableArrayListExtra("listItems", list.getListItems());
            intent.putExtra("position", position);
            activity.startActivity(intent);
        });

        if (list.getCreatorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            holder.editButton.setVisibility(View.VISIBLE);
            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(activity, CreateItemActivity.class);
                intent.putExtra("list", list);
                intent.putExtra("position", position);
                activity.startActivity(intent);
            });

            holder.removeButton.setVisibility(View.VISIBLE);
            holder.removeButton.setOnClickListener(v ->
                    activity.mViewListPresenter.removeListItem(list, position, new ViewListPresenter.OnListItemRemoved() {
                        @Override
                        public void onSuccess() {
                            Log.d("removeListItem: ", "success");
                            list.getListItems().remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.e("removeListItem: ", "failed", e);
                        }
                    }));
        }
        else {
            holder.editButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);
        }

        holder.favoriteButton.setVisibility(View.GONE);

    }



    @Override
    public int getItemCount() {
        return list.getListItems().size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.label_layout)
        RelativeLayout labelLayout;

        @BindView(R.id.label_text)
        TextView labelText;

        @BindView(R.id.list_item)
        RelativeLayout listItem;

        @BindView(R.id.list_name)
        TextView listNameTextView;

        @BindView(R.id.item_image)
        ImageView image;

        @BindView(R.id.edit_button)
        Button editButton;

        @BindView(R.id.favorite_button)
        Button favoriteButton;

        @BindView(R.id.remove_button)
        Button removeButton;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
