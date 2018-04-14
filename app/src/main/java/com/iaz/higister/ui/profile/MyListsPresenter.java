package com.iaz.higister.ui.profile;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class MyListsPresenter extends BasePresenter<MyListsMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;
    ArrayList<UserList> list = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    public MyListsPresenter(DataManager dataManager) {
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

    public void receiveLists() {

        CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("createdLists");

        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                Log.d("receiveMyLists", "Error writing document");
                UserList tempList;

                list.clear();

                for (DocumentSnapshot doc : documentSnapshots) {
                    tempList = doc.toObject(UserList.class);
                    tempList.uid = doc.getId();
                    list.add(tempList);
                }

                getMvpView().updateData(list);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("receiveMyLists", "Error writing document", e);
            }
        });
    }
}
