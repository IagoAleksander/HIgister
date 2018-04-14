package com.iaz.higister.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iaz.higister.R;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.ui.login.AuthActivity;
import com.iaz.higister.ui.login.AuthMvpView;
import com.iaz.higister.ui.login.AuthPresenter;
import com.iaz.higister.ui.main.MainActivity;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import java.security.acl.Group;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.id;

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