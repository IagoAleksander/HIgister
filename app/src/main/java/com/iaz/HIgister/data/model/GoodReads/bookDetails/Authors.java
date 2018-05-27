package com.iaz.HIgister.data.model.GoodReads.bookDetails;

import org.simpleframework.xml.ElementList;

import java.util.ArrayList;

public class Authors
{
    @ElementList(required = false, inline = true)
    private ArrayList<Author> author;

    public ArrayList<Author> getAuthor ()
    {
        return author;
    }

    public void setAuthor (ArrayList<Author> author)
    {
        this.author = author;
    }
}