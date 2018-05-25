package com.iaz.HIgister.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.iaz.HIgister.Application;
import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.data.repository.UserRepository;
import com.iaz.HIgister.injection.component.ActivityComponent;
import com.iaz.HIgister.injection.component.ConfigPersistentComponent;
import com.iaz.HIgister.injection.component.DaggerConfigPersistentComponent;
import com.iaz.HIgister.injection.module.ActivityModule;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.ui.viewList.ViewListActivity;
import com.iaz.HIgister.util.DialogFactory;

import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ActivityInfo ai = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(mActivityId, null);

        if (configPersistentComponent == null) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(Application.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();


//        try {
//            ai = getPackageManager().getActivityInfo(this.getComponentName(), PackageManager.GET_META_DATA);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(
                        new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData data) {
                                if (data == null || data.getLink() == null) {
                                    // No FDL pending for this app, don't do anything.
                                    Log.d("onGetDynamicLinkFail.b:", "empty");
                                    return;
                                }

                                String listId = null;

                                Uri deepLink = data.getLink();
                                if (deepLink.getQueryParameter("listId") != null) {
                                    listId = deepLink.getQueryParameter("listId");
                                    Log.d("onSuccess1.b:", listId);
                                } else
                                    Log.d("onSuccess3.b:", deepLink.toString());

                                setAutenticationListener(listId);
                                addAuthStateListener();
                            }
                        })
                .addOnFailureListener(e -> {
                    Log.d("onGetDynamicLinkFail.b:", e.getMessage());
                });
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    public void setAutenticationListener(String listId) {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@android.support.annotation.NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if (listId != null && !listId.isEmpty()) {
                        Intent intent = new Intent(BaseActivity.this, ViewListActivity.class);
                        intent.putExtra("listId", listId);
                        BaseActivity.this.startActivity(intent);
                    }
                }
            }
        };
    }

    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

}
