package com.iaz.HIgister.ui.viewList;

import android.util.Log;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iaz.HIgister.data.DataManager;
import com.iaz.HIgister.data.model.ListItem;
import com.iaz.HIgister.data.model.UserList;
import com.iaz.HIgister.injection.ConfigPersistent;
import com.iaz.HIgister.ui.base.BasePresenter;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.util.DynamicLinkUtil;

import java.util.ArrayList;

import javax.inject.Inject;

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

    public void getListItems(String listID) {


        CollectionReference colRef = db.collection("lists").document(listID).collection("listItems");

        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                ArrayList<ListItem> listItems = new ArrayList<>();

                for (DocumentSnapshot document : task.getResult()) {
                    ListItem listItem = document.toObject(ListItem.class);
                    listItem.uid = document.getId();
                    listItems.add(listItem);
                }
                getMvpView().updateData(listItems);
            } else {
                Log.w("get list items", "Error receiving document", task.getException());
            }
        });
    }

    public void removeListItem(UserList list, int position, OnListItemRemoved onListItemRemoved) {

        DocumentReference docRef = db.collection("lists").document(list.uid).collection("listItems").document(list.getListItems().get(position).uid);

        docRef.delete()
                .addOnSuccessListener(documentReference -> onListItemRemoved.onSuccess())
                .addOnFailureListener(onListItemRemoved::onFailed);


    }

    public void shareListToFacebook(UserList list) {

        ShareDialog shareDialog;
        shareDialog = new ShareDialog(activity);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setQuote("Higister List: " +list.getName() +"\n\n" +"by " +list.getCreatorName())
                .setContentUrl(DynamicLinkUtil.createDynamicLinkToList(list.uid))
                .build();
        shareDialog.show(content);
    }

    interface OnListItemRemoved {
        void onSuccess();

        void onFailed(Exception exception);
    }

}
