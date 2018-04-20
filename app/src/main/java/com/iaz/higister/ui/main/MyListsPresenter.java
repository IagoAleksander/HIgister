package com.iaz.higister.ui.main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.model.FavoritedList;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class MyListsPresenter extends BasePresenter<MyListsMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;
    ArrayList<FavoritedList> createdListsId = new ArrayList<>();
    ArrayList<UserList> createdLists = new ArrayList<>();
    ArrayList<String> favoritedListsId = new ArrayList<>();
    ArrayList<UserList> favoritedLists = new ArrayList<>();
    ArrayList<UserList> feedLists = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    MyListsPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MyListsMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    void receiveLists(OnUpdateLists onUpdateLists) {

        createdListsId.clear();

        CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("createdLists");

        colRef.get()
                .addOnSuccessListener(documentSnapshots -> {
                    Log.d("receiveMyLists: ", "success");
                    FavoritedList tempListId;

                    for (DocumentSnapshot doc : documentSnapshots) {
                        tempListId = doc.toObject(FavoritedList.class);
                        tempListId.uid = doc.getId();
                        createdListsId.add(tempListId);
                    }

                    populateLists(onUpdateLists);
                })
                .addOnFailureListener(onUpdateLists::onFailed);

    }

    void populateLists(OnUpdateLists onUpdateLists) {
        createdLists.clear();

        for (FavoritedList favoritedListIndex : createdListsId) {

            DocumentReference docRef = db.collection("lists").document(favoritedListIndex.uid);

            docRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.d("receiveFavoritedLists: ", "success");
                        UserList tempList;

                        if (documentSnapshot.exists()) {
                            tempList = documentSnapshot.toObject(UserList.class);
                            tempList.uid = documentSnapshot.getId();
                            createdLists.add(tempList);

                            onUpdateLists.onSuccess(createdLists);
                        }
                    })
                    .addOnFailureListener(e -> {

                    });
        }
    }

    void receiveFavorites(OnUpdateLists onUpdateLists) {

        favoritedListsId.clear();

        CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("favoritedLists");

        colRef.get()
                .addOnSuccessListener(documentSnapshots -> {
                    Log.d("receiveFavoritedLists: ", "success");

                    for (DocumentSnapshot doc : documentSnapshots) {
                        if (doc.exists())
                            favoritedListsId.add(doc.getId());
                    }

                    populateFavorites(onUpdateLists);
                })
                .addOnFailureListener(onUpdateLists::onFailed);
    }

    void populateFavorites(OnUpdateLists onUpdateLists) {
        favoritedLists.clear();

       for (String id : favoritedListsId) {

           DocumentReference docRef = db.collection("lists").document(id);

           docRef.get()
                   .addOnSuccessListener(documentSnapshot -> {
                       Log.d("receiveFavoritedLists: ", "success");
                       UserList tempList;

                       if (documentSnapshot.exists()) {
                           tempList = documentSnapshot.toObject(UserList.class);
                           tempList.uid = documentSnapshot.getId();
                           favoritedLists.add(tempList);

                           onUpdateLists.onSuccess(favoritedLists);
                       }
                   })
                   .addOnFailureListener(e -> {

                   });
       }
    }

    void receiveFeed(OnUpdateLists onUpdateLists) {

        feedLists.clear();

        CollectionReference colRef = db.collection("lists");

        colRef.get()
                .addOnSuccessListener(documentSnapshots -> {
                    Log.d("receiveFavoritedLists: ", "success");
                    UserList tempList;

                    for (DocumentSnapshot doc : documentSnapshots) {
                        tempList = doc.toObject(UserList.class);

                        if (!tempList.creatorId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            tempList.uid = doc.getId();
                            feedLists.add(tempList);
                        }
                    }

                    onUpdateLists.onSuccess(feedLists);
                })
                .addOnFailureListener(onUpdateLists::onFailed);
    }

    void favoriteList(UserList userList, OnListFavorited onListFavorited) {
        CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("favoritedLists");

        FavoritedList favoritedList = new FavoritedList();
        favoritedList.creatorId = userList.creatorId;

        colRef.document(userList.uid).set(favoritedList)
                .addOnSuccessListener(aVoid -> onListFavorited.onSuccess())
                .addOnFailureListener(e -> onListFavorited.onFailed(e));
    }

    void unfavoriteList(UserList userList, OnListRemoved onListRemoved) {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("favoritedLists").document(userList.uid);

        docRef.delete()
                .addOnSuccessListener(aVoid -> onListRemoved.onSuccess())
                .addOnFailureListener(e -> onListRemoved.onFailed(e.toString()));
    }

    void removeList(UserList userList, OnListRemoved onListRemoved) {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("createdLists").document(userList.uid);

        docRef.delete()
                .addOnSuccessListener(aVoid -> onListRemoved.onSuccess())
                .addOnFailureListener(e -> onListRemoved.onFailed(e.toString()));
    }

    interface OnUpdateLists {
        void onSuccess(ArrayList<UserList> userLists);

        void onFailed(Exception e);
    }

    interface OnListFavorited {
        void onSuccess();

        void onFailed(Exception e);
    }

    interface OnListRemoved {
        void onSuccess();

        void onFailed(String exception);
    }
}
