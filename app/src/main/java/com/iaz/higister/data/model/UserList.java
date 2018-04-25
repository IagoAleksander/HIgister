package com.iaz.higister.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

/**
 * Created by alksander on 17/03/2018.
 */

public class UserList implements Parcelable{

    @Exclude
    public String uid;

    public String name;
    public String description;
    public int type;
    public boolean visibleForEveryone;
    public boolean commentsEnabled;
    public String listPictureUri;
    public String creatorId;
    public ArrayList<String> favoritedBy = new ArrayList<>();

    @Exclude
    public ArrayList<ListItem> listItems = new ArrayList<>();

    public UserList() {}

    protected UserList(Parcel in) {
        uid = in.readString();
        name = in.readString();
        description = in.readString();
        type = in.readInt();
        visibleForEveryone = in.readByte() != 0;
        commentsEnabled = in.readByte() != 0;
        listPictureUri = in.readString();
        creatorId = in.readString();
        listItems = in.createTypedArrayList(ListItem.CREATOR);
        favoritedBy = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(type);
        dest.writeByte(visibleForEveryone ? (byte) 1 : (byte) 0);
        dest.writeByte(commentsEnabled ? (byte) 1 : (byte) 0);
        dest.writeString(listPictureUri);
        dest.writeString(creatorId);
        dest.writeTypedList(listItems);
        dest.writeStringList(favoritedBy);

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

}
