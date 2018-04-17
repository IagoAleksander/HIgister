package com.iaz.higister.ui.main;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iaz.higister.data.DataManager;
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
    ArrayList<UserList> list = new ArrayList<>();

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

        CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("createdLists");

        colRef.get()
                .addOnSuccessListener(documentSnapshots -> {
                    Log.d("receiveMyLists: ", "success");
                    UserList tempList;

                    list.clear();

                    for (DocumentSnapshot doc : documentSnapshots) {
                        tempList = doc.toObject(UserList.class);
                        tempList.uid = doc.getId();
                        list.add(tempList);
                    }

                    onUpdateLists.onSuccess(list);
                })
                .addOnFailureListener(onUpdateLists::onFailed);
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

    interface OnListRemoved {
        void onSuccess();

        void onFailed(String exception);
    }
}
