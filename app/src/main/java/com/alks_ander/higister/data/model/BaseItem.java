package com.alks_ander.higister.data.model;

import android.graphics.Bitmap;

/**
 * Created by alksander on 04/03/2018.
 */

public class BaseItem {

    private String myType;
    private Bitmap bit;
    private int backgroundColor;

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
}
