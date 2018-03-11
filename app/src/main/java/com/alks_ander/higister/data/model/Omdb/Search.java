package com.alks_ander.higister.data.model.Omdb;

import android.os.Parcel;
import android.os.Parcelable;

import com.alks_ander.higister.data.model.BaseItem;

public class Search extends BaseItem implements Parcelable
{
    private String Year;

    private String Type;

    private String Poster;

    private String imdbID;

    private String Title;

    public String getYear ()
    {
        return Year;
    }

    public void setYear (String Year)
    {
        this.Year = Year;
    }

    public String getType ()
    {
        return Type;
    }

    public void setType (String Type)
    {
        this.Type = Type;
    }

    public String getPoster ()
    {
        return Poster;
    }

    public void setPoster (String Poster)
    {
        this.Poster = Poster;
    }

    public String getImdbID ()
    {
        return imdbID;
    }

    public void setImdbID (String imdbID)
    {
        this.imdbID = imdbID;
    }

    public String getTitle ()
    {
        return Title;
    }

    public void setTitle (String Title)
    {
        this.Title = Title;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Year = "+Year+", Type = "+Type+", Poster = "+Poster+", imdbID = "+imdbID+", Title = "+Title+"]";
    }

    protected Search(Parcel in) {
        super(in);
        Year = in.readString();
        Type = in.readString();
        Poster = in.readString();
        imdbID = in.readString();
        Title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Year);
        dest.writeString(Type);
        dest.writeString(Poster);
        dest.writeString(imdbID);
        dest.writeString(Title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Search> CREATOR = new Parcelable.Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };
}
