package com.iaz.HIgister.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

/**
 * Created by alksander on 17/03/2018.
 */

public class ListItem implements Parcelable{

    @Exclude
    public String uid;

    private String name;
    private String description;
    private int type;
    private BaseItem baseItem;
//    public Bitmap photo;

    public ListItem() {}

    protected ListItem(Parcel in) {
        uid = in.readString();
        setName(in.readString());
        setDescription(in.readString());
        setType(in.readInt());
        setBaseItem(in.readTypedObject(BaseItem.CREATOR));
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
        //TODO
        dest.writeTypedObject(getBaseItem(), PARCELABLE_WRITE_RETURN_VALUE);

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

    public BaseItem getBaseItem() {
        return baseItem;
    }

    public void setBaseItem(BaseItem baseItem) {
        this.baseItem = baseItem;
    }
}
