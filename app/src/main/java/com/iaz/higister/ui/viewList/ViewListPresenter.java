package com.iaz.higister.ui.viewList;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class ViewListPresenter extends BasePresenter<ViewListMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;
    private ViewListActivity activity;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    public ViewListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ViewListMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void setActivity(ViewListActivity activity) {
        this.activity = activity;
    }

    public void getListItems (String listID) {


        CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("createdLists").document(listID).collection("listItems");

        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                ArrayList<ListItem> listItems = new ArrayList<>();

                for (DocumentSnapshot document : task.getResult()) {
                    ListItem listItem = document.toObject(ListItem.class);
                    listItems.add(listItem);
                }
                getMvpView().updateData(listItems);
            } else {
                Log.w("get list items", "Error receiving document", task.getException());
            }
        });
    }

}
