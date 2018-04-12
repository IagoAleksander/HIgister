package com.iaz.higister.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iaz.higister.ui.createItem.CreateItemActivity;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.List;

import com.iaz.higister.R;
import com.iaz.higister.data.model.BaseItem;

public class ResultsAdapter extends BaseFlipAdapter {

    private final int PAGES = 3;
    //    private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4, R.id.interest_5};
    SearchActivity activity;

    public ResultsAdapter(SearchActivity activity, List items, FlipSettings settings) {

        super(activity.getApplicationContext(), items, settings);
        this.activity = activity;
    }

    public void setRibots(List ribots) {
        super.setItems(ribots);
    }

    @Override
    public View getPage(int position, View convertView, ViewGroup parent, Object result1, Object result2, final CloseListener closeListener) {
        final MoviesViewHolder holder;

        if (convertView == null) {
            holder = new MoviesViewHolder();
            convertView = activity.getLayoutInflater().inflate(R.layout.friends_merge_page, parent, false);
            holder.leftAvatar = (ImageView) convertView.findViewById(R.id.first);
            holder.rightAvatar = (ImageView) convertView.findViewById(R.id.second);
            holder.titleLeft = (TextView) convertView.findViewById(R.id.titleLeft);
            holder.titleRight = (TextView) convertView.findViewById(R.id.titleRight);
            holder.infoPage = activity.getLayoutInflater().inflate(R.layout.friends_info, parent, false);
            holder.nickName = (TextView) holder.infoPage.findViewById(R.id.item_title);
            holder.emailTextView = (TextView) holder.infoPage.findViewById(R.id.item_description);
//            holder.image = (ImageView) holder.infoPage.findViewById(R.id.item_image);

//            for (int id : IDS_INTEREST)
//                holder.interests.add((TextView) holder.infoPage.findViewById(id));
            holder.choose = (Button) holder.infoPage.findViewById(R.id.item_buttom);

            convertView.setTag(holder);
        } else {
            holder = (MoviesViewHolder) convertView.getTag();
        }

        switch (position) {
            // Merged page with 2 friends
            case 1:
                holder.leftAvatar.setImageBitmap(((BaseItem) result1).getBit());
                setTitle(holder, (BaseItem) result1, 0);
                if (result2 != null) {
                    holder.rightAvatar.setImageBitmap(((BaseItem) result2).getBit());
                    setTitle(holder, (BaseItem) result2, 1);
                }
                break;
            default:
                fillHolder(holder, position == 0 ? (BaseItem) result1 : (BaseItem) result2);
                holder.infoPage.setTag(holder);
                return holder.infoPage;
        }

        convertView.setBackgroundColor(ContextCompat.getColor(activity.getApplicationContext(), android.R.color.transparent));
        return convertView;
    }

    @Override
    public int getPagesCount() {
        return PAGES;
    }

    private void setTitle(MoviesViewHolder holder, BaseItem item, int position) {
        if (item == null)
            return;

        if (position == 0)
            holder.titleLeft.setText(item.title);
        else
            holder.titleRight.setText(item.title);
    }

    private void fillHolder(MoviesViewHolder holder, BaseItem item) {
        if (item == null)
            return;

        setClick(holder, item);

        holder.nickName.setText(item.title);
        holder.emailTextView.setText(item.description);

    }

    class MoviesViewHolder {


        ImageView leftAvatar;
        ImageView rightAvatar;
        View infoPage;
        ImageView hexColorView;
        TextView titleLeft;
        TextView titleRight;

        TextView nameTextView;

        TextView emailTextView;
        List<TextView> interests = new ArrayList<>();
        TextView nickName;
        //        ImageView image;
        Button choose;
    }

    public void setClick(MoviesViewHolder holder, BaseItem item) {
        holder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CreateItemActivity.class);
                intent.putExtra("list", activity.list);
                intent.putExtra("item", item);
                activity.startActivity(intent);
            }
        });
    }
}
