package com.iaz.HIgister.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

/**
 * Created by alksander on 17/03/2018.
 */

public class FavoritedList {

    @Exclude
    public String uid;
    private String creatorId;

    @ServerTimestamp
    Timestamp time;

    public FavoritedList() {}

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
