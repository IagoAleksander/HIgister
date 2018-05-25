package com.iaz.higister2.data.model.LastFM;

import java.util.ArrayList;

public class Trackmatches
{
    private ArrayList<Track> track = new ArrayList<>();

    public ArrayList<Track> getTrack ()
    {
        return track;
    }

    public void setTrack (ArrayList<Track> track)
    {
        this.track = track;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [track = "+track+"]";
    }
}
