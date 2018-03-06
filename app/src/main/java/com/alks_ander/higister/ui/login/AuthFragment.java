package com.alks_ander.higister.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alks_ander.higister.R;
import com.alks_ander.higister.ui.profile.ProfileActivity;
import com.alks_ander.higister.ui.search.SearchActivity;
import com.alks_ander.higister.util.Rotate;
import com.alks_ander.higister.util.TextSizeTransition;
import com.alks_ander.higister.util.VerticalTextView;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class AuthFragment extends Fragment {

    protected Callback callback;

    @BindView(R.id.caption)
    protected VerticalTextView caption;

    @BindView(R.id.root)
    protected ViewGroup parent;

    protected boolean lock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(authLayout(),container,false);
        ButterKnife.bind(this,root);

        KeyboardVisibilityEvent.setEventListener(getActivity(), isOpen -> {
            callback.scale(isOpen);
            if(!isOpen){
                clearFocus();
            }
        });

        caption.setOnClickListener(v -> {

            AuthActivity activity = (AuthActivity)getActivity();
            if (activity.pager.getCurrentItem() == 0 && caption.getText().equals(activity.getString(R.string.log_in_label))) {
                //TODO login
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                getActivity().startActivity(intent);
            }
            else if (activity.pager.getCurrentItem() == 1 && caption.getText().equals(activity.getString(R.string.sign_up_label))) {
                //TODO register
            }
            else
                unfold();
        });

        return root;

    }

    public void setCallback(@NonNull Callback callback) {
        this.callback = callback;
    }

    public void unfold(){
            if(!lock) {
                caption.setVerticalText(false);
                caption.requestLayout();
                Rotate transition = new Rotate();
                transition.setStartAngle(-90f);
                transition.setEndAngle(0f);
                transition.addTarget(caption);
                TransitionSet set=new TransitionSet();
                set.setDuration(getResources().getInteger(R.integer.duration));
                ChangeBounds changeBounds=new ChangeBounds();
                set.addTransition(changeBounds);
                set.addTransition(transition);
                TextSizeTransition sizeTransition=new TextSizeTransition();
                sizeTransition.addTarget(caption);
                set.addTransition(sizeTransition);
                set.setOrdering(TransitionSet.ORDERING_TOGETHER);
                caption.post(()->{
                    TransitionManager.beginDelayedTransition(parent, set);
                    caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.unfolded_size));
                    caption.setTextColor(ContextCompat.getColor(getContext(),R.color.color_label));
                    caption.setTranslationX(0);
                    ConstraintLayout.LayoutParams params = getParams();
                    params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                    params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                    params.verticalBias = 0.78f;
                    caption.setLayoutParams(params);
                });

                lock=true;

                //after everything's been set up, tell the ViewPager to flip the page
                callback.show(this);
            }
    }

    @LayoutRes
    public abstract int authLayout();
    public abstract void fold();
    public abstract void clearFocus();

    interface Callback {
        void show(AuthFragment fragment);
        void scale(boolean hasFocus);
    }

    protected ConstraintLayout.LayoutParams getParams(){
        return ConstraintLayout.LayoutParams.class.cast(caption.getLayoutParams());
    }
}