package com.alks_ander.higister.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alksander on 04/03/2018.
 */

public class BaseItem implements Parcelable{

    private String myType;
    private Bitmap bit;
    private int backgroundColor;

    protected BaseItem() {}

    public String getMyType() {
        return myType;
    }

    public void setMyType(String myType) {
        this.myType = myType;
    }

    public Bitmap getBit() {
        return bit;
    }

    public void setBit(Bitmap bit) {
        this.bit = bit;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    protected BaseItem(Parcel in) {
        myType = in.readString();
        bit = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        backgroundColor = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(myType);
        dest.writeValue(bit);
        dest.writeInt(backgroundColor);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BaseItem> CREATOR = new Parcelable.Creator<BaseItem>() {
        @Override
        public BaseItem createFromParcel(Parcel in) {
            return new BaseItem(in);
        }

        @Override
        public BaseItem[] newArray(int size) {
            return new BaseItem[size];
        }
    };
}
