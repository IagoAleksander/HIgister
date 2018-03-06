package com.alks_ander.higister.ui.login;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.alks_ander.higister.R;
import com.alks_ander.higister.util.AnimatedViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {



    @BindViews(value = {R.id.logo, R.id.first, R.id.second, R.id.last})
    protected List<ImageView> sharedElements;

    public AnimatedViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pager = ButterKnife.findById(this, R.id.pager);
        int[] screenSize = screenSize();

//        for (ImageView element : sharedElements) {
////            @ColorRes int color = element.getId() != R.id.logo ? R.color.white_transparent : R.color.color_logo_log_in;
//            if (element.getDrawable() != null)
//            DrawableCompat.setTint(element.getDrawable(), ContextCompat.getColor(this, R.color.white_transparent));
//        }

        final ImageView background=ButterKnife.findById(this,R.id.scrolling_background);

        Glide.with(this)
                .load(R.drawable.wall)
                .asBitmap()
                .override(screenSize[0]*2,screenSize[1])
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new ImageViewTarget<Bitmap>(background) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        background.setImageBitmap(resource);
                        int res = pager.getWidth();
                        background.scrollTo(-pager.getWidth()/2,0);
                        background.post(()->{
                            //we need to scroll to the very left edge of the image
                            //fire the scale animation
                            ObjectAnimator xAnimator=ObjectAnimator.ofFloat(background, View.SCALE_X,4f,background.getScaleX());
                            ObjectAnimator yAnimator=ObjectAnimator.ofFloat(background,View.SCALE_Y,4f,background.getScaleY());
                            AnimatorSet set=new AnimatorSet();
                            set.playTogether(xAnimator,yAnimator);
                            set.setDuration(getResources().getInteger(R.integer.duration));
                            set.start();
                        });
                        AuthAdapter adapter = new AuthAdapter(getSupportFragmentManager(), pager, background, sharedElements);
                        pager.setAdapter(adapter);
                    }
                });
    }


    private int[] screenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new int[]{size.x, size.y};
    }
}