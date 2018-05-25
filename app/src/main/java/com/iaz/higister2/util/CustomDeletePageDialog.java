package com.iaz.higister.util;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.iaz.higister.R;
import com.iaz.higister.ui.createItem.CreateItemActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Iago Aleksander on 30/01/18.
 */

public class CustomDeletePageDialog extends Dialog {

    private CustomDeletePageDialog.CustomDeletePageHolder holder;

    public CustomDeletePageDialog(@NonNull CreateItemActivity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_remove_page, null);
        setContentView(view);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        holder = new CustomDeletePageDialog.CustomDeletePageHolder(view);

        holder.mCancelBtn.setOnClickListener(v -> cancel());
        holder.acceptTerms.setOnCheckedChangeListener((compoundButton, b) -> holder.setmDeletePageBtnClick(b));

        holder.mDeletePageBtn.setEnabled(false);
        holder.mDeletePageBtn.setOnClickListener(view1 -> {

        });
    }

    public class CustomDeletePageHolder {
        @BindView(R.id.delete_page_button)
        TextView mDeletePageBtn;
        @BindView(R.id.cancel_button)
        TextView mCancelBtn;
        @BindView(R.id.confirmation_input)
        CheckBox acceptTerms;

        CustomDeletePageHolder(View view) {
            ButterKnife.bind(this, view);

        }

        public void setmDeletePageBtnClick(boolean clickable) {
            if (clickable) {
                holder.mDeletePageBtn.setEnabled(true);
                mDeletePageBtn.setBackgroundColor(getContext().getResources().getColor(R.color.red));
            } else {
                holder.mDeletePageBtn.setEnabled(false);
                mDeletePageBtn.setBackgroundColor(getContext().getResources().getColor(R.color.primary_light));
            }
        }
    }
}
