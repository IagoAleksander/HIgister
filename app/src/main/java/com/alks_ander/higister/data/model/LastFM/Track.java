package com.alks_ander.higister.data.model.LastFM;

import com.alks_ander.higister.data.model.BaseItem;

public class Track extends BaseItem
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
}