package com.iaz.higister.data.model.LastFM;

public class Track
{
//    private String listeners;

    private String mbid;

    private String name;

//    private Image[] image;

//    private String streamable;

    private String artist;

    private String url;

//    public String getListeners ()
//    {
//        return listeners;
//    }
//
//    public void setListeners (String listeners)
//    {
//        this.listeners = listeners;
//    }

    public String getMbid ()
    {
        return mbid;
    }

    public void setMbid (String mbid)
    {
        this.mbid = mbid;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

//    public Image[] getImage ()
//    {
//        return image;
//    }
//
//    public void setImage (Image[] image)
//    {
//        this.image = image;
//    }

//    public String getStreamable ()
//    {
//        return streamable;
//    }
//
//    public void setStreamable (String streamable)
//    {
//        this.streamable = streamable;
//    }

    public String getArtist ()
    {
        return artist;
    }

    public void setArtist (String artist)
    {
        this.artist = artist;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [listeners = "+listeners+", mbid = "+mbid+", name = "+name+", image = , streamable = "+streamable+", artist = "+artist+", url = "+url+"]";
//    }

//    protected Track(Parcel in) {
//        mbid = in.readString();
//        name = in.readString();
//        artist = in.readString();
//        url = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(mbid);
//        dest.writeString(name);
//        dest.writeString(artist);
//        dest.writeString(url);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
//        @Override
//        public Track createFromParcel(Parcel in) {
//            return new Track(in);
//        }
//
//        @Override
//        public Track[] newArray(int size) {
//            return new Track[size];
//        }
//    };
}