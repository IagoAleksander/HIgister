package com.iaz.higister.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.iaz.higister.R;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.repository.ListRepository;
import com.iaz.higister.ui.viewList.ViewListActivity;
import com.iaz.higister.ui.viewUser.ViewUserActivity;
import com.iaz.higister.util.ViewUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 12/05/2018.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private ArrayList<User> mPeopleList;
    private MyListsFragment fragment;

    public PeopleAdapter(MyListsFragment fragment, ArrayList<User> peopleList) {
        mPeopleList = peopleList;
        this.fragment = fragment;
    }

    public void setPeople(ArrayList<User> peopleList) {
        mPeopleList = peopleList;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_small, parent, false);
        return new PeopleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PeopleViewHolder holder, int position) {

        User currentPerson = mPeopleList.get(position);

        if (currentPerson != null) {

            if (currentPerson.getProfilePictureUri() != null && !currentPerson.getProfilePictureUri().isEmpty()) {
                Glide.with(fragment)
                        .asBitmap()
                        .load(currentPerson.getProfilePictureUri())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Bitmap resourceWithBorder = ViewUtil.addWhiteBorder(resource, 3);
                                holder.personProfileImage.setImageBitmap(resourceWithBorder);
                            }
                        });
            } else {
                holder.personProfileImage.setImageDrawable(ResourcesCompat.getDrawable(fragment.getResources(), R.drawable.com_facebook_profile_picture_blank_portrait, null));
            }
            if (currentPerson.getName() != null)
                holder.personNameText.setText(currentPerson.getName());
            else
                holder.personNameText.setVisibility(View.GONE);

            holder.listItem.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getActivity(), ViewUserActivity.class);
                intent.putExtra("user", currentPerson);
                intent.putStringArrayListExtra("myFavoritedListsId", fragment.activity.favoritedListsId);
                fragment.getActivity().startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        return mPeopleList.size();
    }

    class PeopleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item)
        CardView listItem;

        @BindView(R.id.person_profile_image)
        ImageView personProfileImage;

        @BindView(R.id.person_name_text)
        TextView personNameText;

        public PeopleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}