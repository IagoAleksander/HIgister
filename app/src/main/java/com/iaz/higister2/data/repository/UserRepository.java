package com.iaz.higister.data.repository;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iaz.higister.data.model.User;
import com.iaz.higister.util.CompressorUtil;
import com.iaz.higister.util.Constants;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by alksander on 25/04/2018.
 */

public class UserRepository {

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    User user;
    private EventListener eventListener;
    private ListenerRegistration listenerRegistration;

    private ArrayList<User> allUsers = new ArrayList<>();

    public UserRepository() {

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void saveProfileInfo(Activity activity, User user, OnUpdateProfile onUpdateProfile) {
        // Add a new document with a generated ID

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("updateProfile", "DocumentSnapshot successfully written!");
                    addListener(activity, onUpdateProfile);
                })
                .addOnFailureListener(e -> {
                    Log.w("updateProfile", "Error writing document", e);
                    addListener(activity, onUpdateProfile);
                });
    }

    public void receiveProfileInfo(String uid, OnUpdateProfile onUpdateProfile) {

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);

                if (user != null) {
                    user.uid = documentSnapshot.getId();
                    onUpdateProfile.onSuccess(user);
                }
                else {
                    onUpdateProfile.onFailure("usuário nao existe");
                }
            } else {
                onUpdateProfile.onFailure("usuário nao existe");
            }
        })
                .addOnFailureListener(e -> Log.w("updateProfile", "Error writing document", e));
    }

    public void filterResult(String filter, OnGetUsers onGetUsers) {

        receiveAllUsers(new OnGetUsers() {
            @Override
            public void onSuccess(ArrayList<User> peopleList) {
                ArrayList<User> resulList = new ArrayList<>();

                for (User user : peopleList) {
                    if (user.getName() != null && !user.getName().isEmpty() && user.getName().toLowerCase().contains(filter.toLowerCase())) {
                        resulList.add(user);
                    }
                }
                onGetUsers.onSuccess(resulList);
            }

            @Override
            public void onFailure(String exception) {

            }
        });
    }

    public void receiveAllUsers(OnGetUsers onGetUsers) {
        allUsers.clear();

        CollectionReference colRef = db.collection("users");

        colRef.get()
                .addOnSuccessListener(documentSnapshots -> {
                    Log.d("receiveAllUsers: ", "success");
                    User tempUser;

                    for (DocumentSnapshot doc : documentSnapshots) {
                        tempUser = doc.toObject(User.class);

                        if (tempUser != null
                                && !doc.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            tempUser.uid = doc.getId();
                            allUsers.add(tempUser);
                        }
                    }
                    onGetUsers.onSuccess(allUsers);

                })
                .addOnFailureListener(e -> {});
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

    public void addListener(Activity activity, OnUpdateProfile onUpdateProfile) {
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
                .addSnapshotListener(activity, eventListener);
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

    public interface OnGetUsers {
        void onSuccess(ArrayList<User> user);

        void onFailure(String exception);
    }
}
