package com.iaz.higister.ui.viewList;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.iaz.higister.R;
import com.iaz.higister.data.model.Ribot;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.RibotViewHolder> {

    private List<Ribot> mRibots;
    private Activity activity;

    @Inject
    public ListItemAdapter(Activity activity) {
        mRibots = new ArrayList<>();
        this.activity = activity;
    }

    public void setListItem(List<Ribot> ribots) {
        mRibots = ribots;
    }

    @Override
    public RibotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card_big, parent, false);
        return new RibotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RibotViewHolder holder, int position) {
//        Ribot ribot = mRibots.get(position);
//        holder.hexColorView.setBackgroundColor(Color.parseColor(ribot.profile().hexColor()));
//        holder.nameTextView.setText(String.format("%s %s",
//                ribot.profile().name().first(), ribot.profile().name().last()));
//        holder.emailTextView.setText(ribot.profile().email());

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class RibotViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.view_hex_color) View hexColorView;
//        @BindView(R.id.text_name) TextView nameTextView;
//        @BindView(R.id.item_description) TextView emailTextView;

        @BindView(R.id.item_image)
        ImageView image;

        public RibotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
