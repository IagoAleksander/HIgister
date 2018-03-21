package com.iaz.higister.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iaz.higister.R;
import com.iaz.higister.data.model.Ribot;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.viewList.ViewListActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListsAdapter extends RecyclerView.Adapter<UserListsAdapter.ListViewHolder> {

    private ArrayList<UserList> mLists;
    private Activity activity;

    public UserListsAdapter(Activity activity, ArrayList<UserList> lists) {
        mLists = lists;
        this.activity = activity;
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
//        Ribot ribot = mRibots.get(position);
//        holder.hexColorView.setBackgroundColor(Color.parseColor(ribot.profile().hexColor()));
//        holder.nameTextView.setText(String.format("%s %s",
//                ribot.profile().name().first(), ribot.profile().name().last()));
//        holder.emailTextView.setText(ribot.profile().email());
        holder.listNameTextView.setText(mLists.get(position).name);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewListActivity.class);
                intent.putExtra("ListID", mLists.get(position).uid);
                activity.startActivity(intent);
            }
        });
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

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
