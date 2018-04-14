package com.iaz.higister.ui.viewList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.iaz.higister.R;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.Ribot;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    private ArrayList<ListItem> listItems;
    private Activity activity;

    public ListItemAdapter(Activity activity, ArrayList<ListItem> listItems) {
        this.listItems = listItems;
        this.activity = activity;
    }

    public void setListItem(ArrayList<ListItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_big, parent, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.listNameTextView.setText(listItem.name);
        Glide.with(activity)
                .load(listItem.baseItem.imageUrl)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_name)
        TextView listNameTextView;

        @BindView(R.id.item_image)
        ImageView image;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
