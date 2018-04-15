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
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.iaz.higister.R;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.profile.MyListsPresenter;
import com.iaz.higister.ui.viewItem.ViewItemActivity;
import com.iaz.higister.ui.viewItem.ViewItemFragment;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    private UserList list;
    private ViewListActivity activity;

    public ListItemAdapter(Activity activity, UserList list) {
        this.list = list;

        if (activity instanceof ViewListActivity)
            this.activity = (ViewListActivity) activity;
    }

    public void setListItem(ArrayList<ListItem> listItems) {
        list.listItems = listItems;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_big, parent, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, int position) {
        ListItem listItem = list.listItems.get(position);

        holder.listNameTextView.setText(listItem.name);
        Glide.with(activity)
                .load(listItem.baseItem.imageUrl)
                .into(holder.image);

        holder.image.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ViewItemActivity.class);
            intent.putParcelableArrayListExtra("listItems", list.listItems);
            intent.putExtra("position", position);
            activity.startActivity(intent);
        });

        holder.removeButton.setOnClickListener(v ->
                activity.mViewListPresenter.removeListItem(list, position, new ViewListPresenter.OnListItemRemoved() {
                    @Override
                    public void onSuccess() {
                        Log.d("removeListItem: ", "success");
                        list.listItems.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("removeListItem: ", "failed", e);
                    }
                }));

    }

    @Override
    public int getItemCount() {
        return list.listItems.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_name)
        TextView listNameTextView;

        @BindView(R.id.item_image)
        ImageView image;

        @BindView(R.id.remove_button)
        Button removeButton;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
