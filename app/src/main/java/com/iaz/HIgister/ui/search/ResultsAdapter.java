package com.iaz.HIgister.ui.search;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iaz.HIgister.data.BackendManager;
import com.iaz.HIgister.data.model.BaseItem;
import com.iaz.HIgister.data.model.ListItem;
import com.iaz.HIgister.ui.createItem.CreateItemActivity;
import com.iaz.HIgister.util.Constants;
import com.iaz.HIgister.util.ViewUtil;
import com.iaz.Higister.R;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.List;

public class ResultsAdapter extends BaseFlipAdapter {

    private final int PAGES = 3;
    //    private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4, R.id.interest_5};
    SearchActivity activity;

    public ResultsAdapter(SearchActivity activity, List items, FlipSettings settings) {

        super(activity.getApplicationContext(), items, settings);
        this.activity = activity;
    }

    @Override
    public View getPage(int position, View convertView, ViewGroup parent, Object result1, Object result2, final CloseListener closeListener) {
        final MoviesViewHolder holder;

        if (convertView == null) {
            holder = new MoviesViewHolder();
            convertView = activity.getLayoutInflater().inflate(R.layout.item_result_merge_page, parent, false);
            holder.leftAvatar = convertView.findViewById(R.id.first);
            holder.rightAvatar = convertView.findViewById(R.id.second);
            holder.rightItem = convertView.findViewById(R.id.second_item);
            holder.titleLeft = convertView.findViewById(R.id.titleLeft);
            holder.titleRight = convertView.findViewById(R.id.titleRight);
            holder.infoPage = activity.getLayoutInflater().inflate(R.layout.item_result_info, parent, false);
            holder.nickName = holder.infoPage.findViewById(R.id.item_title);
            holder.itemDescription = holder.infoPage.findViewById(R.id.item_description);
//            holder.image = (ImageView) holder.infoPage.findViewById(R.id.item_image);

//            for (int id : IDS_INTEREST)
//                holder.interests.add((TextView) holder.infoPage.findViewById(id));
            holder.choose = holder.infoPage.findViewById(R.id.item_select);
            holder.close = holder.infoPage.findViewById(R.id.item_back);

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
                else {
                    holder.rightItem.setVisibility(View.GONE);
                }
                break;
            default:
                fillHolder(holder, position == 0 ? (BaseItem) result1 : (BaseItem) result2);
                holder.infoPage.setTag(holder);
                holder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeListener.onClickClose();
                    }
                });
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
        holder.itemDescription.setText(ViewUtil.stripHtml(item.description));

    }

    class MoviesViewHolder {


        ImageView leftAvatar;
        ImageView rightAvatar;
        RelativeLayout rightItem;
        View infoPage;
        TextView titleLeft;
        TextView titleRight;

        TextView itemDescription;
        TextView nickName;
        Button choose;
        Button close;
    }

    public void setClick(MoviesViewHolder holder, BaseItem item) {
        holder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListItem listItem = new ListItem();


                BackendManager backendManager = new BackendManager();

                if (item.getMyType() == Constants.BOOKS) {
                    backendManager.fetchBookDetails(item.id, new BackendManager.OnUpdateResult() {
                        @Override
                        public void onSuccess(BaseItem baseItem) {
                            setItemAndProceed(listItem, baseItem);
                        }

                        @Override
                        public void onFailed(String e) {
                            setItemAndProceed(listItem, item);
                        }
                    });
                }
                else if (item.getMyType() == Constants.MOVIES || item.getMyType() == Constants.TV_SERIES) {
                    backendManager.fetchMovieDetails(item.id, item.getMyType(), new BackendManager.OnUpdateResult() {
                        @Override
                        public void onSuccess(BaseItem baseItem) {
                            setItemAndProceed(listItem, baseItem);
                        }

                        @Override
                        public void onFailed(String e) {
                            setItemAndProceed(listItem, item);
                        }
                    });
                }
                else {
                    setItemAndProceed(listItem, item);
                }
            }
        });
    }

    public void setItemAndProceed(ListItem listItem, BaseItem item) {
        listItem.setBaseItem(item);
        Intent intent = new Intent(activity, CreateItemActivity.class);
        intent.putExtra("list", activity.list);
        intent.putExtra("position", -1);
        intent.putExtra("listItem", listItem);
        activity.startActivity(intent);
    }
}
