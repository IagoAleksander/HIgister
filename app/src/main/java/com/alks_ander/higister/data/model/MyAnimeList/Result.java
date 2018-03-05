package com.alks_ander.higister.data.model.MyAnimeList;

import com.alks_ander.higister.data.model.BaseItem;

public class Result extends BaseItem
{
//    private String id;

    private String title;

    private String image_url;

    private String description;

    private String score;

    private String type;

    private String episodes;

//    private String members;

    private String url;

//    public String getId ()
//    {
//        return id;
//    }
//
//    public void setId (String id)
//    {
//        this.id = id;
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

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getScore ()
    {
        return score;
    }

    public void setScore (String score)
    {
        this.score = score;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getEpisodes ()
    {
        return episodes;
    }

    public void setEpisodes (String episodes)
    {
        this.episodes = episodes;
    }

//    public String getMembers ()
//    {
//        return members;
//    }
//
//    public void setMembers (String members)
//    {
//        this.members = members;
//    }

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
//        return "ClassPojo [id = "+id+", title = "+title+", image_url = "+image_url+", description = "+description+", score = "+score+", type = "+type+", episodes = "+episodes+", members = "+members+", url = "+url+"]";
//    }
}
