package com.iaz.higister.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister.R;
import com.iaz.higister.data.repository.UserRepository;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.main.MainActivity;
import com.iaz.higister.util.AnimatedViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class AuthActivity extends BaseActivity implements AuthMvpView {

    @Inject
    AuthPresenter mAuthPresenter;

    @BindViews(value = {R.id.logo, R.id.first, R.id.second, R.id.last})
    protected List<ImageView> sharedElements;

    @BindView(R.id.pager)
    public AnimatedViewPager pager;

    @BindView(R.id.scrolling_background)
    public ImageView background;

    @BindView(R.id.login_button)
    LoginButton loginButton;

    private FirebaseAuth mAuth;
    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuthPresenter.attachView(this);
        mAuthPresenter.setAutenticationListener();

        setBackgroundImageAnimation();

        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("", "facebook:onSuccess:" + loginResult);
                mAuthPresenter.handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d("", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("", "facebook:onError", exception);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAuthPresenter.addAuthStateListener();

    }

    @Override
    public void onStop() {
        super.onStop();
        mAuthPresenter.removeAuthStateListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAuthPresenter.detachView();
    }

    @Override
    public AuthActivity getActivity() {
        return this;
    }

    private int[] screenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new int[]{size.x, size.y};
    }

    public void setBackgroundImageAnimation() {

//        pager = ButterKnife.findById(this, R.id.pager);
//        int[] screenSize = screenSize();

//        for (ImageView element : sharedElements) {
////            @ColorRes int color = element.getId() != R.id.logo ? R.color.white_transparent : R.color.color_logo_log_in;
//            if (element.getDrawable() != null)
//            DrawableCompat.setTint(element.getDrawable(), ContextCompat.getColor(this, R.color.white_transparent));
//        }

//        final ImageView background=ButterKnife.findById(this,R.id.scrolling_background);

        RequestOptions myOptions = new RequestOptions()
//                .override(screenSize[0]*2,screenSize[1])
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .dontTransform();

        Glide.with(this)
                .asBitmap()
                .load(R.drawable.wall)
                .apply(myOptions)
                .into(new ImageViewTarget<Bitmap>(background) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        background.setImageBitmap(resource);
                        background.scrollTo(-pager.getWidth()/2,0);
//                        background.post(()->{
//                            //we need to scroll to the very left edge of the image
//                            //fire the scale animation
//                            ObjectAnimator xAnimator=ObjectAnimator.ofFloat(background, View.SCALE_X,4f,background.getScaleX());
//                            ObjectAnimator yAnimator=ObjectAnimator.ofFloat(background,View.SCALE_Y,4f,background.getScaleY());
//                            AnimatorSet set=new AnimatorSet();
//                            set.playTogether(xAnimator,yAnimator);
//                            set.setDuration(getResources().getInteger(R.integer.duration));
//                            set.start();
//                        });
                        AuthAdapter adapter = new AuthAdapter(getSupportFragmentManager(), pager, background, sharedElements);
                        pager.setAdapter(adapter);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
