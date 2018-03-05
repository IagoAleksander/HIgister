package com.alks_ander.higister.data.model.Omdb;

import com.alks_ander.higister.data.model.BaseItem;

public class Search extends BaseItem
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
}
