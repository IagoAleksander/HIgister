package com.iaz.HIgister.data.model.LastFM;


import org.simpleframework.xml.Root;

@Root(name = "results", strict = false)
public class Results
{

    private Trackmatches trackmatches;

    public Trackmatches getTrackmatches ()
    {
        return trackmatches;
    }

    public void setTrackmatches (Trackmatches trackmatches)
    {
        this.trackmatches = trackmatches;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [trackmatches = "+trackmatches+"]";
    }
}

	