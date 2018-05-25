package com.iaz.higister2.data.model;

import com.google.firebase.firestore.Exclude;

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
