package com.iaz.higister.data.repository;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iaz.higister.data.model.FavoritedList;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.ui.main.UserListsAdapter;
import com.iaz.higister.util.CompressorUtil;
import com.iaz.higister.util.Constants;

import java.io.File;
import java.util.ArrayList;

public class ListRepository {
    private FirebaseFirestore db;
    private FirebaseStorage storage;


    public ArrayList<FavoritedList> createdListsId = new ArrayList<>();
    private ArrayList<UserList> createdLists = new ArrayList<>();
    public ArrayList<String> favoritedListsId = new ArrayList<>();
    private ArrayList<UserList> favoritedLists = new ArrayList<>();
    private ArrayList<UserList> feedLists = new ArrayList<>();

    public ListRepository() {

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

//        reference = database.getReference("groups/");
    }

    public void saveList(UserList list) {
        if (list.listPictureUri != null && !list.listPictureUri.contains("http")) {
            saveListImageOnStorage(list.listPictureUri, new OnImageUpload() {
                @Override
                public void onSuccess(Uri uri) {
                    list.listPictureUri = uri.toString();
                    saveListInfo(list);
                }

                @Override
                public void onFailure(String exception) {

                }
            });
        } else {
            saveListInfo(list);
        }
    }

    public void saveListInfo(UserList list) {
        db.collection("lists").add(list)
                .addOnSuccessListener(documentReference -> {
                    Log.d("createList", "DocumentSnapshot successfully written!");
                    list.uid = documentReference.getId();

                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("createdLists").document(list.uid).set(list)
                            .addOnSuccessListener(docRef -> {
                                Log.d("createList", "DocumentSnapshot successfully written!");
                            })
                            .addOnFailureListener(e ->
                                    Log.w("createList", "Error writing document", e));

                    saveItem(list, 0, new OnItemSaved() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailed(Exception exception) {

                        }
                    });
                })
                .addOnFailureListener(e ->
                        Log.w("createList", "Error writing document", e));
    }


    public void saveListImageOnStorage(String uri, final OnImageUpload onImageUpload) {

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

    public void updateList(UserList list, OnListUpdated onListUpdated) {
        if (list.listPictureUri != null && !list.listPictureUri.contains("http")) {
            saveListImageOnStorage(list.listPictureUri, new OnImageUpload() {
                @Override
                public void onSuccess(Uri uri) {
                    list.listPictureUri = uri.toString();
                    updateListInfo(list, onListUpdated);
                }

                @Override
                public void onFailure(String exception) {

                }
            });
        } else {
            saveListInfo(list);
        }
    }

    public void updateListInfo(UserList list, OnListUpdated onListUpdated) {
        db.collection("lists").document(list.uid).set(list)
                .addOnSuccessListener(documentReference -> {
                    Log.d("updateList: ", "success");
                    onListUpdated.onSuccess();
                })
                .addOnFailureListener(onListUpdated::onFailed);
    }

    public void saveItem(UserList list, int position, OnItemSaved onItemSaved) {


        db.collection("lists").document(list.uid).collection("listItems").add(list.listItems.get(position))
                .addOnSuccessListener(documentReference -> {

                    Log.d("saveItem: ", "success");
                    onItemSaved.onSuccess();

                })
                .addOnFailureListener(onItemSaved::onFailed);

    }

    public void updateItem(UserList list, int position, OnItemUpdated onItemUpdated) {

        DocumentReference docRef = db.collection("lists").document(list.uid).collection("listItems")
                .document(list.listItems.get(position).uid);

        docRef.set(list.listItems.get(position))
                .addOnSuccessListener(documentReference -> {

                    Log.d("updateListItem", "DocumentSnapshot successfully written!");
                    onItemUpdated.onSuccess();
                })
                .addOnFailureListener(onItemUpdated::onFailed);


    }

    public void receiveLists(OnUpdateLists onUpdateLists) {

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

    public void populateLists(OnUpdateLists onUpdateLists) {
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

    public void receiveFavorites(OnUpdateLists onUpdateLists) {

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

    public void populateFavorites(OnUpdateLists onUpdateLists) {
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

        if (favoritedListsId.isEmpty()) {
            onUpdateLists.onSuccess(favoritedLists);
        }
    }

    public void addListener(Activity activity, String type, OnUpdateLists onUpdateLists) {

        if (type.equals("created")) {
            CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("createdLists");

            colRef.addSnapshotListener(activity, (documentSnapshots, e) ->
                    receiveLists(onUpdateLists));

        } else if (type.equals("favorited")) {
            CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("favoritedLists");

            colRef.addSnapshotListener(activity, (documentSnapshots, e) -> {
                receiveFavorites(onUpdateLists);
            });
        } else {
            CollectionReference colRef = db.collection("lists");

            colRef.addSnapshotListener(activity, (documentSnapshots, e) -> receiveFeed(onUpdateLists));
        }
    }

    public void receiveFeed(OnUpdateLists onUpdateLists) {

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

    public void favoriteList(UserList userList, OnListFavorited onListFavorited) {
        CollectionReference colRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("favoritedLists");

        FavoritedList favoritedList = new FavoritedList();
        favoritedList.creatorId = userList.creatorId;

        colRef.document(userList.uid).set(favoritedList)
                .addOnSuccessListener(aVoid -> onListFavorited.onSuccess(userList.uid))
                .addOnFailureListener(e -> onListFavorited.onFailed(e));

        userList.favoritedBy.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
        setFavoritedBy(userList);
    }

    public void setFavoritedBy(UserList list) {
        db.collection("lists").document(list.uid).set(list);
    }

    public void unfavoriteList(UserList userList, OnListRemoved onListRemoved) {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("favoritedLists").document(userList.uid);

        docRef.delete()
                .addOnSuccessListener(aVoid -> onListRemoved.onSuccess(userList.uid))
                .addOnFailureListener(e -> onListRemoved.onFailed(e.toString()));

        userList.favoritedBy.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
        setFavoritedBy(userList);
    }

    public void removeList(UserList userList, OnListRemoved onListRemoved) {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("createdLists").document(userList.uid);

        docRef.delete()
                .addOnSuccessListener(aVoid -> onListRemoved.onSuccess(userList.uid))
                .addOnFailureListener(e -> onListRemoved.onFailed(e.toString()));
    }

    public interface OnItemSaved {
        void onSuccess();

        void onFailed(Exception exception);
    }

    public interface OnItemUpdated {
        void onSuccess();

        void onFailed(Exception exception);
    }

    public interface OnListUpdated {
        void onSuccess();

        void onFailed(Exception exception);
    }

    public interface OnUpdateLists {
        void onSuccess(ArrayList<UserList> userLists);

        void onFailed(Exception e);
    }

    public interface OnListFavorited {
        void onSuccess(String uid);

        void onFailed(Exception e);
    }

    public interface OnListRemoved {
        void onSuccess(String uid);

        void onFailed(String exception);
    }

    public interface OnImageUpload {
        void onSuccess(Uri uri);

        void onFailure(String exception);
    }


//    public void findGroupById(String groupId, final GroupRepository.OnFindGroup onFindGroup) {
//
//        final DatabaseReference ref = reference.child(groupId);
//        ref.keepSynced(true);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Group group = dataSnapshot.getValue(Group.class);
//                if (group != null && !group.getWasDeletedStatus()) {
//                    group.setUid(dataSnapshot.getKey());
//                    onFindGroup.onFindGroupSuccess(group);
//                } else {
//                    onFindGroup.onFindGroupFailed("");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                onFindGroup.onFindGroupFailed(databaseError.toString());
//            }
//        });
//    }
//
//    public void getAllGroupsByTag(final String tagId, final OnGetAllGroupsByTag onGetAllGroupsByTag) {
//        reference.orderByChild("tags/" + tagId).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Group> groups = new ArrayList<>();
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Group group = data.getValue(Group.class);
//                    if (group != null && !group.getWasDeletedStatus()) {
//                        group.setUid(data.getKey());
//                        groups.add(group);
//                    }
//                }
//                onGetAllGroupsByTag.onGetAllGroupsByTagSuccess(groups);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                onGetAllGroupsByTag.onGetAllGroupsByTagFailure(databaseError);
//            }
//        });
//    }
//
//    public void getAllGroupsOfUser(final String userId, final OnGetAllGroupsOfUser onGetAllGroupsOfUser) {
//        reference.orderByChild("members/" + userId).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Group> groups = new ArrayList<>();
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Group group = data.getValue(Group.class);
//                    if (group != null && !group.getWasDeletedStatus()) {
//                        group.setUid(data.getKey());
//                        groups.add(group);
//                    }
//                }
//                onGetAllGroupsOfUser.onGetAllGroupsOfUserSuccess(groups);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                onGetAllGroupsOfUser.onGetAllGroupsOfUserFailure(databaseError);
//            }
//        });
//    }
//
//    public void getGroupById(final String groupId, final OnGetGroupById onGetGroupById) {
//        database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("groups/" + groupId);
//
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    Group group = dataSnapshot.getValue(Group.class);
//                    if (group != null && !group.getWasDeletedStatus()) {
//                        group.setUid(dataSnapshot.getKey());
//                        onGetGroupById.OnGetGroupByIdSuccess(group);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

}
