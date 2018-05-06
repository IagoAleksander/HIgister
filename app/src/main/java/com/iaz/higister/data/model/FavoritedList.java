package com.iaz.higister.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

/**
 * Created by alksander on 17/03/2018.
 */

public class FavoritedList {

    @Exclude
    public String uid;
    private String creatorId;

    public FavoritedList() {}

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
