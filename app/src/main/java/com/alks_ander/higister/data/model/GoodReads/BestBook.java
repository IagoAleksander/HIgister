package com.alks_ander.higister.data.model.GoodReads;

import com.alks_ander.higister.data.model.BaseItem;

/**
 * Created by alksander on 02/03/2018.
 */

public class BestBook extends BaseItem{

//    private Id id;
//
//    private Author author;

    private String title;

    private String image_url;

    private String type;

    private String small_image_url;

//    public Id getId ()
//    {
//        return id;
//    }
//
//    public void setId (Id id)
//    {
//        this.id = id;
//    }
//
//    public Author getAuthor ()
//    {
//        return author;
//    }
//
//    public void setAuthor (Author author)
//    {
//        this.author = author;
//    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getImage_url ()
    {
        return image_url;
    }

    public void setImage_url (String image_url)
    {
        this.image_url = image_url;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getSmall_image_url ()
    {
        return small_image_url;
    }

    public void setSmall_image_url (String small_image_url)
    {
        this.small_image_url = small_image_url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = , author = , title = "+title+", image_url = "+image_url+", type = "+type+", small_image_url = "+small_image_url+"]";
    }
}

