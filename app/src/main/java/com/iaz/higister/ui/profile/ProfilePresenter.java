package com.iaz.higister.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.model.User;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;
import com.iaz.higister.ui.login.AuthMvpView;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class ProfilePresenter extends BasePresenter<ProfileMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;
    User user ;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    public ProfilePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ProfileMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void saveProfileInfo(String name, String description, int age, ArrayList<String> interests) {
        // Add a new document with a generated ID

        user = new User();
        user.name = name;
        user.description = description;
        user.age = age;
        user.followersNumber = 300;
        user.interests = interests;
        user.listsCreatedNumber = 17;
        user.listsFavouritedNumber = 12;

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateProfile", "DocumentSnapshot successfully written!");
                        getMvpView().updateData(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateProfile", "Error writing document", e);
                    }
                });
    }

    public void receiveProfileInfo() {
        user = null;

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                getMvpView().updateData(user);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("updateProfile", "Error writing document", e);
            }
        });
    }
}
