package com.iaz.higister.data.repository;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iaz.higister.data.model.User;
import com.iaz.higister.util.CompressorUtil;
import com.iaz.higister.util.Constants;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

/**
 * Created by alksander on 25/04/2018.
 */

public class UserRepository {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    User user ;
    private EventListener eventListener;
    private ListenerRegistration listenerRegistration;

    public UserRepository() {

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void saveProfileInfo(User user, OnUpdateProfile onUpdateProfile) {
        // Add a new document with a generated ID

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("updateProfile", "DocumentSnapshot successfully written!");
                    addListener(onUpdateProfile);
                })
                .addOnFailureListener(e -> {
                    Log.w("updateProfile", "Error writing document", e);
                    addListener(onUpdateProfile);
                });
    }

    public void receiveProfileInfo(OnUpdateProfile onUpdateProfile) {

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);

                onUpdateProfile.onSuccess(user);
            }
        })
                .addOnFailureListener(e -> Log.w("updateProfile", "Error writing document", e));
    }

    public void saveProfileImageOnStorage(String uri, final OnImageUpload onImageUpload) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final StorageReference storageReference = storage.getReference().child(Constants.PATH_USER_IMAGE + uid);

        CompressorUtil.compress(uri, new CompressorUtil.CompressListener() {
            @Override
            public void onCompressSuccess(File file) {
                UploadTask uploadTask = storageReference.putFile(Uri.parse("file://" + file.getPath()));
                uploadTask.
                        addOnFailureListener(exception -> onImageUpload.onFailure(exception.getMessage()))
                        .addOnSuccessListener(taskSnapshot -> {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            onImageUpload.onSuccess(downloadUrl);
                        });
            }

            @Override
            public void onCompressError() {
                onImageUpload.onFailure("Error on compressing");
            }
        });
    }

    public void addListener(OnUpdateProfile onUpdateProfile) {
//        if (listenerRegistration == null ) {

        if (eventListener == null) {
            eventListener = (EventListener<DocumentSnapshot>) (documentSnapshot, e) -> {
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);
                    onUpdateProfile.onSuccess(user);
                }
            };
        }
        listenerRegistration = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(eventListener);
//        }
    }

    public void removeListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    public interface OnImageUpload {
        void onSuccess(Uri uri);

        void onFailure(String exception);
    }

    public interface OnUpdateProfile {
        void onSuccess(User user);

        void onFailure(String exception);
    }
}
