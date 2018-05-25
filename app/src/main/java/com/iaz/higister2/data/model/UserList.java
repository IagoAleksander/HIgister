package com.iaz.higister.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

/**
 * Created by alksander on 17/03/2018.
 */

public class UserList implements Parcelable {

    @Exclude
    public String uid;

    private String name;
    private String description;
    private int type;
    private boolean visibleForEveryone;
    private boolean commentsEnabled;
    private String listPictureUri;
    private String creatorId;
    private String creatorName;
    private ArrayList<String> favoritedBy = new ArrayList<>();
    private ArrayList<String> likedBy = new ArrayList<>();
    private ArrayList<String> comments = new ArrayList<>();

    @Exclude
    private ArrayList<ListItem> listItems = new ArrayList<>();

    public UserList() {
    }

    protected UserList(Parcel in) {
        uid = in.readString();
        setName(in.readString());
        setDescription(in.readString());
        setType(in.readInt());
        setVisibleForEveryone(in.readByte() != 0);
        setCommentsEnabled(in.readByte() != 0);
        setListPictureUri(in.readString());
        setCreatorId(in.readString());
        setCreatorName(in.readString());
        setListItems(in.createTypedArrayList(ListItem.CREATOR));
        setFavoritedBy(in.createStringArrayList());
        setLikedBy(in.createStringArrayList());
        in.readStringList(getComments());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(getName());
        dest.writeString(getDescription());
        dest.writeInt(getType());
        dest.writeByte(isVisibleForEveryone() ? (byte) 1 : (byte) 0);
        dest.writeByte(isCommentsEnabled() ? (byte) 1 : (byte) 0);
        dest.writeString(getListPictureUri());
        dest.writeString(getCreatorId());
        dest.writeString(getCreatorName());
        dest.writeTypedList(getListItems());
        dest.writeStringList(getFavoritedBy());
        dest.writeStringList(getLikedBy());
        dest.writeStringList(getComments());

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserList> CREATOR = new Parcelable.Creator<UserList>() {
        @Override
        public UserList createFromParcel(Parcel in) {
            return new UserList(in);
        }

        @Override
        public UserList[] newArray(int size) {
            return new UserList[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isVisibleForEveryone() {
        return visibleForEveryone;
    }

    public void setVisibleForEveryone(boolean visibleForEveryone) {
        this.visibleForEveryone = visibleForEveryone;
    }

    public boolean isCommentsEnabled() {
        return commentsEnabled;
    }

    public void setCommentsEnabled(boolean commentsEnabled) {
        this.commentsEnabled = commentsEnabled;
    }

    public String getListPictureUri() {
        return listPictureUri;
    }

    public void setListPictureUri(String listPictureUri) {
        this.listPictureUri = listPictureUri;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public ArrayList<String> getFavoritedBy() {
        return favoritedBy;
    }

    public void setFavoritedBy(ArrayList<String> favoritedBy) {
        this.favoritedBy = favoritedBy;
    }

    public ArrayList<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(ArrayList<String> likedBy) {
        this.likedBy = likedBy;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<ListItem> getListItems() {
        return listItems;
    }

    public void setListItems(ArrayList<ListItem> listItems) {
        this.listItems = listItems;
    }
}
