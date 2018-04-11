package com.iaz.higister.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.iaz.higister.data.model.ComicVine.Results;
import com.iaz.higister.data.model.GoodReads.BestBook;
import com.iaz.higister.data.model.LastFM.Track;
import com.iaz.higister.data.model.MyAnimeList.Result;
import com.iaz.higister.data.model.Omdb.Search;

/**
 * Created by alksander on 04/03/2018.
 */

public class BaseItem implements Parcelable{

    public String title;
    public String description;
    public String detailsUrl;
    private String myType;
    private Bitmap bit;
    public String imageUrl;

    protected BaseItem() {}

    public BaseItem(BestBook book) {
        this.title = book.getTitle();
//        this.description = book.getAuthor();
        this.myType = "book";
        this.imageUrl = book.getSmall_image_url();
    }

    public BaseItem(Result myAnimeListItem, String type) {
        this.title = myAnimeListItem.getTitle();
        this.description = myAnimeListItem.getDescription();
        this.myType = type;
        this.imageUrl = myAnimeListItem.getImage_url();
        this.detailsUrl = myAnimeListItem.getUrl();
    }

    public BaseItem(Results comicsVineItem) {
        this.title = comicsVineItem.getVolume().getName() +" " +comicsVineItem.getName();
        this.description = comicsVineItem.getDescription();
        this.myType = "comics";
        this.imageUrl = comicsVineItem.getImage().getSmall_url();
        this.detailsUrl = comicsVineItem.getSite_detail_url();
    }

    public BaseItem(Search omdbItem, String type) {
        this.title = omdbItem.getTitle();
        this.description = omdbItem.getYear();
        this.myType = type;
        this.imageUrl = omdbItem.getPoster();
        this.detailsUrl= "http://www.imdb.com.br/title/" +omdbItem.getImdbID() +"/";
    }

    public BaseItem(Track lastFmItem) {
        this.title = lastFmItem.getName();
        this.description = lastFmItem.getArtist();
        this.myType = "music";
        this.detailsUrl = lastFmItem.getUrl();
    }

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

    protected BaseItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        detailsUrl = in.readString();
        myType = in.readString();
//        bit = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(detailsUrl);
        dest.writeString(myType);
        //        dest.writeValue(bit);
        dest.writeString(imageUrl);

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
