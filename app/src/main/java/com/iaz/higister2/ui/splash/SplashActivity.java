package com.iaz.higister2.ui.splash;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.iaz.higister2.R;
import com.iaz.higister2.ui.base.BaseActivity;
import com.iaz.higister2.ui.login.AuthActivity;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * Created by alks_ander on 19/05/2017.
 */

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashPresenter mSplashPresenter;

    @BindView(R.id.switcher_header)
    TextView rotatingTextHeader;

    @BindView(R.id.custom_switcher)
    RotatingTextWrapper rotatingTextWrapper;

    private FirebaseAuth.AuthStateListener mAuthListener;

    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mSplashPresenter.attachView(this);

        mSplashPresenter.setAutenticationListener();
        mSplashPresenter.addAuthStateListener();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSplashPresenter.removeAuthStateListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSplashPresenter.detachView();
    }

    @Override
    public SplashActivity getActivity() {
        return this;
    }

    public void setRotatingText() {

        //TODO not working on android 7.0+
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

        rotatingTextHeader.setTypeface(typeface);

        final Rotatable rotatable = new Rotatable(getResources().getColor(R.color.white), 2000, "","your movies", "your books", "your musics", "YOU...", "", "");
        rotatable.setSize(35);
        rotatable.setAnimationDuration(500);
        rotatable.setTypeface(typeface);
        rotatable.setCenter(true);

        rotatingTextWrapper.setSize(35);
        rotatingTextWrapper.setContent("?", rotatable);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                rotatingTextWrapper.pause(0);
//                rotatingTextLayout.setVisibility(View.GONE);

                Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        }, 9500);   //1.5 seconds
    }
}
