package com.iaz.HIgister.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.iaz.HIgister.data.model.ComicVine.Results;
import com.iaz.HIgister.data.model.GoodReads.BestBook;
import com.iaz.HIgister.data.model.LastFM.Track;
import com.iaz.HIgister.data.model.MyAnimeList.Result;
import com.iaz.HIgister.data.model.Omdb.Search;
import com.iaz.HIgister.util.Constants;

/**
 * Created by alksander on 04/03/2018.
 */

public class BaseItem implements Parcelable{

    public String title;
    public String description;
    public String detailsUrl;
    private int myType;
    private Bitmap bit;
    public String imageUrl;

    protected BaseItem() {}

    public BaseItem(BestBook book) {
        this.title = book.getTitle();
//        this.description = book.getAuthor();
        this.myType = Constants.BOOKS;
        this.imageUrl = book.getImage_url();
    }

    public BaseItem(Result myAnimeListItem, int type) {
        this.title = myAnimeListItem.getTitle();
        this.description = myAnimeListItem.getDescription();
        this.myType = type;
        this.imageUrl = myAnimeListItem.getImage_url();
        this.detailsUrl = myAnimeListItem.getUrl();
    }

    public BaseItem(Results comicsVineItem) {
        this.title = comicsVineItem.getVolume().getName() +" " +comicsVineItem.getName();
        this.description = comicsVineItem.getDescription();
        this.myType = Constants.COMICS;
        this.imageUrl = comicsVineItem.getImage().getSmall_url();
        this.detailsUrl = comicsVineItem.getSite_detail_url();
    }

    public BaseItem(Search omdbItem, int type) {
        this.title = omdbItem.getTitle();
        this.description = omdbItem.getYear();
        this.myType = type;
        this.imageUrl = omdbItem.getPoster();
        this.detailsUrl= "http://www.imdb.com.br/title/" +omdbItem.getImdbID() +"/";
    }

    public BaseItem(Track lastFmItem) {
        this.title = lastFmItem.getName();
        this.description = lastFmItem.getArtist();
        this.myType = Constants.MUSICS;
        this.detailsUrl = lastFmItem.getUrl();
    }

    public int getMyType() {
        return myType;
    }

    public void setMyType(int myType) {
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
        myType = in.readInt();
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
        dest.writeInt(myType);
        //        dest.writeValue(bit);
        dest.writeString(imageUrl);

    }

    @SuppressWarnings("unused")
    public static final Creator<BaseItem> CREATOR = new Creator<BaseItem>() {
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
