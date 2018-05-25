package com.iaz.higister2.data.model.LastFM;

public class LastFmResponse
{
    private Results results;

    public Results getResults ()
    {
        return results;
    }

    public void setResults (Results results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+"]";
    }
}