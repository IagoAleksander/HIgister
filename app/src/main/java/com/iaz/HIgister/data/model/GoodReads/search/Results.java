package com.iaz.HIgister.data.model.GoodReads.search;

import org.simpleframework.xml.ElementList;

import java.util.ArrayList;

/**
 * Created by alksander on 02/03/2018.
 */


public class Results
{
    @ElementList(required = false, inline = true)
    private ArrayList<Work> work = new ArrayList<>();

    public ArrayList<Work> getWork ()
    {
        return work;
    }

    public void setWork (ArrayList<Work> work)
    {
        this.work = work;
    }
}
