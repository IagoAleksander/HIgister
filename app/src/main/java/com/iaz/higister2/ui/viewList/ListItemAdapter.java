package com.iaz.higister.ui.viewList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.createItem.CreateItemActivity;
import com.iaz.higister.ui.viewItem.ViewItemActivity;
import com.iaz.higister.util.DialogFactory;

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

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    private UserList list;
    private ViewListActivity activity;
    private Dialog mDialog;

    ListItemAdapter(Activity activity, UserList list) {
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
        ListItem listItem = list.getListItems().get(holder.getAdapterPosition());

        if (listItem.getName() != null)
            holder.itemNameTextView.setText(listItem.getName());
        else
            holder.itemNameTextView.setVisibility(View.GONE);

        if (listItem.getDescription() != null)
            holder.itemDescriptionTextView.setText(listItem.getDescription());
        else
            holder.itemDescriptionTextView.setVisibility(View.GONE);

        if (listItem.getBaseItem() != null && listItem.getBaseItem().imageUrl != null)
            Glide.with(activity)
                    .load(listItem.getBaseItem().imageUrl)
                    .into(holder.image);

        holder.creatorsLayout.setVisibility(View.GONE);
        populateLabel(holder, listItem.getType());

        holder.listItem.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ViewItemActivity.class);
            intent.putParcelableArrayListExtra("listItems", list.getListItems());
            intent.putExtra("position", holder.getAdapterPosition());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
        });

        if (list.getCreatorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            holder.editButtonLayout.setVisibility(View.VISIBLE);
            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(activity, CreateItemActivity.class);
                intent.putExtra("list", list);
                intent.putExtra("position", holder.getAdapterPosition());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_foward, R.anim.slide_out_forward);
            });

            holder.removeButton.setVisibility(View.VISIBLE);
            holder.removeButton.setOnClickListener(v -> {

                mDialog = DialogFactory.newDialog(activity, "Removing item...");
                mDialog.show();
                activity.mViewListPresenter.removeListItem(list, holder.getAdapterPosition(), new ViewListPresenter.OnListItemRemoved() {
                    @Override
                    public void onSuccess() {
                        Log.d("removeListItem: ", "success");
                        DialogFactory.finalizeDialog(mDialog, true, "Item removed with success", () -> {
                            list.getListItems().remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        });

                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("removeListItem: ", "failed", e);
                        DialogFactory.finalizeDialog(mDialog, false, "Sorry, an error occurred on item removal", () -> {
                        });
                    }
                });
            });
        } else {
            holder.editButtonLayout.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);
        }

    }

    public void populateLabel(final ListItemAdapter.ListItemViewHolder holder, int type) {
        switch (type) {
            case MOVIES:
                holder.labelLayout.setBackgroundResource(R.color.accent_dark);
                holder.labelText.setText("MOVIE");
                break;
            case TV_SERIES:
                holder.labelLayout.setBackgroundResource(R.color.pink);
                holder.labelText.setText("TV SERIE");
                break;
            case ANIMES:
                holder.labelLayout.setBackgroundResource(R.color.green);
                holder.labelText.setText("ANIME");
                break;
            case MANGAS:
                holder.labelLayout.setBackgroundResource(R.color.sienna);
                holder.labelText.setText("MANGA");
                break;
            case BOOKS:
                holder.labelLayout.setBackgroundResource(R.color.orange);
                holder.labelText.setText("BOOK");
                break;
            case MUSICS:
                holder.labelLayout.setBackgroundResource(R.color.saffron);
                holder.labelText.setText("MUSIC");
                break;
            case COMICS:
                holder.labelLayout.setBackgroundResource(R.color.purple);
                holder.labelText.setText("COMIC");
                break;

            default:
                holder.labelLayout.setBackgroundResource(R.color.primary_light);
                holder.labelText.setText("MISC");

        }
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
        TextView itemNameTextView;

        @BindView(R.id.list_description)
        TextView itemDescriptionTextView;

        @BindView(R.id.item_image)
        ImageView image;

        @BindView(R.id.edit_button_layout)
        LinearLayout editButtonLayout;

        @BindView(R.id.edit_button)
        Button editButton;

        @BindView(R.id.remove_button)
        Button removeButton;

        @BindView(R.id.creators_layout)
        LinearLayout creatorsLayout;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
