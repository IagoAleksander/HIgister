package com.alks_ander.higister.data.model.ComicVine;

public class Volume
{
    private String id;

    private String api_detail_url;

    private String site_detail_url;

    private String name;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getApi_detail_url ()
    {
        return api_detail_url;
    }

    public void setApi_detail_url (String api_detail_url)
    {
        this.api_detail_url = api_detail_url;
    }

    public String getSite_detail_url ()
    {
        return site_detail_url;
    }

    public void setSite_detail_url (String site_detail_url)
    {
        this.site_detail_url = site_detail_url;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", api_detail_url = "+api_detail_url+", site_detail_url = "+site_detail_url+", name = "+name+"]";
    }
}
