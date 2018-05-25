package com.iaz.higister.ui.splash;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;
import com.iaz.higister.ui.main.MainActivity;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class SplashPresenter extends BasePresenter<SplashMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Inject
    public SplashPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SplashMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void setAutenticationListener() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@android.support.annotation.NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("auth", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                    getMvpView().getActivity().startActivity(intent);
                } else {
                    // User is signed out
                    Log.d("auth", "onAuthStateChanged:signed_out");
                    getMvpView().getActivity().setRotatingText();
                }
            }
        };
    }

    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void removeAuthStateListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
