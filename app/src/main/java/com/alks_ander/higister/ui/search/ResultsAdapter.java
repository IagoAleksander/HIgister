package com.alks_ander.higister.ui.search;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.util.ArrayList;
import java.util.List;
import com.alks_ander.higister.R;
import com.alks_ander.higister.data.model.BaseItem;
import com.alks_ander.higister.data.model.ComicVine.Results;
import com.alks_ander.higister.data.model.GoodReads.BestBook;
import com.alks_ander.higister.data.model.LastFM.Track;
import com.alks_ander.higister.data.model.MyAnimeList.Result;
import com.alks_ander.higister.data.model.Omdb.Search;

public class ResultsAdapter extends BaseFlipAdapter {

    private final int PAGES = 3;
//    private int[] IDS_INTEREST = {R.id.interest_1, R.id.interest_2, R.id.interest_3, R.id.interest_4, R.id.interest_5};
    Activity activity;

    public ResultsAdapter(Activity activity, List items, FlipSettings settings) {

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
            holder.infoPage = activity.getLayoutInflater().inflate(R.layout.friends_info, parent, false);
            holder.nickName = (TextView) holder.infoPage.findViewById(R.id.nickname);
            holder.emailTextView = (TextView) holder.infoPage.findViewById(R.id.text_email);

//            for (int id : IDS_INTEREST)
//                holder.interests.add((TextView) holder.infoPage.findViewById(id));

            convertView.setTag(holder);
        } else {
            holder = (MoviesViewHolder) convertView.getTag();
        }

        switch (position) {
            // Merged page with 2 friends
            case 1:
                holder.leftAvatar.setImageBitmap(((BaseItem) result1).getBit());
                if (result2 != null)
                    holder.rightAvatar.setImageBitmap(((BaseItem) result2).getBit());
                break;
            default:
                fillHolder(holder, position == 0 ? (BaseItem) result1 : (BaseItem) result2);
                holder.infoPage.setTag(holder);
                return holder.infoPage;
        }

        convertView.setBackgroundColor(((BaseItem) result1).getBackgroundColor());
        return convertView;
    }

    @Override
    public int getPagesCount() {
        return PAGES;
    }

    private void fillHolder(MoviesViewHolder holder, BaseItem item) {
        if (item == null)
            return;
//        Iterator<TextView> iViews = holder.interests.iterator();
//        Iterator<String> iInterests = friend.getInterests().iterator();
//        while (iViews.hasNext() && iInterests.hasNext())
//            iViews.next().setText(iInterests.next());

        holder.infoPage.setBackgroundColor(item.getBackgroundColor());

        if (item instanceof Result) {
            Result result = (Result) item;

            holder.nickName.setText(result.getTitle());
            holder.emailTextView.setText(result.getDescription());
        }
        else if (item instanceof Results) {
            Results result = (Results) item;

            holder.nickName.setText(result.getName());
            holder.emailTextView.setText(result.getDescription());
        }
        else if (item instanceof BestBook) {
            BestBook result = (BestBook) item;

            holder.nickName.setText(result.getTitle());
            holder.emailTextView.setText(result.getType());
        }
        else if (item instanceof Search) {
            Search result = (Search) item;

            holder.nickName.setText(result.getTitle());
            holder.emailTextView.setText(result.getType());
        }
        else if (item instanceof Track) {
            Track result = (Track) item;

            holder.nickName.setText(result.getName());
            holder.emailTextView.setText(result.getArtist());
        }
    }

    class MoviesViewHolder  {


        ImageView leftAvatar;
        ImageView rightAvatar;
        View infoPage;
        ImageView hexColorView;

        TextView nameTextView;

        TextView emailTextView;
        List<TextView> interests = new ArrayList<>();
        TextView nickName;
    }
}
