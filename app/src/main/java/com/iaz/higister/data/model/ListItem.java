package com.iaz.higister.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

/**
 * Created by alksander on 17/03/2018.
 */

public class ListItem implements Parcelable{

    @Exclude
    public String uid;

    public String name;
    public String description;
    public int type;
    public BaseItem baseItem;
//    public Bitmap photo;

    public ListItem() {}

    protected ListItem(Parcel in) {
        uid = in.readString();
        name = in.readString();
        description = in.readString();
        type = in.readInt();
        baseItem = in.readTypedObject(BaseItem.CREATOR);
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
        dest.writeTypedObject(baseItem, PARCELABLE_WRITE_RETURN_VALUE);

    }

    @SuppressWarnings("unused")
    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

}
