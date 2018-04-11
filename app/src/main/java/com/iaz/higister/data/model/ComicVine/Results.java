package com.iaz.higister.data.model.ComicVine;

import android.os.Parcel;
import android.os.Parcelable;

import com.iaz.higister.data.model.BaseItem;

public class Results
{
    private Image image;

//    private String date_last_updated;

//    private null aliases;

//    private String store_date;

//    private String id;

    private String api_detail_url;

    private String site_detail_url;

//    private String has_staff_review;

//    private String cover_date;

//    private String date_added;

    private String description;

//    private String resource_type;

    private String name;

    private Volume volume;

    private String issue_number;

//    private null deck;

    public Image getImage ()
    {
        return image;
    }

    public void setImage (Image image)
    {
        this.image = image;
    }

//    public String getDate_last_updated ()
//    {
//        return date_last_updated;
//    }
//
//    public void setDate_last_updated (String date_last_updated)
//    {
//        this.date_last_updated = date_last_updated;
//    }

//    public null getAliases ()
//    {
//        return aliases;
//    }
//
//    public void setAliases (null aliases)
//    {
//        this.aliases = aliases;
//    }
//
//    public String getStore_date ()
//    {
//        return store_date;
//    }
//
//    public void setStore_date (String store_date)
//    {
//        this.store_date = store_date;
//    }
//
//    public String getId ()
//    {
//        return id;
//    }
//
//    public void setId (String id)
//    {
//        this.id = id;
//    }

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
//
//    public String getHas_staff_review ()
//    {
//        return has_staff_review;
//    }
//
//    public void setHas_staff_review (String has_staff_review)
//    {
//        this.has_staff_review = has_staff_review;
//    }
//
//    public String getCover_date ()
//    {
//        return cover_date;
//    }
//
//    public void setCover_date (String cover_date)
//    {
//        this.cover_date = cover_date;
//    }
//
//    public String getDate_added ()
//    {
//        return date_added;
//    }
//
//    public void setDate_added (String date_added)
//    {
//        this.date_added = date_added;
//    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }
//
//    public String getResource_type ()
//    {
//        return resource_type;
//    }
//
//    public void setResource_type (String resource_type)
//    {
//        this.resource_type = resource_type;
//    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Volume getVolume ()
    {
        return volume;
    }

    public void setVolume (Volume volume)
    {
        this.volume = volume;
    }

    public String getIssue_number ()
    {
        return issue_number;
    }

    public void setIssue_number (String issue_number)
    {
        this.issue_number = issue_number;
    }

//    public null getDeck ()
//    {
//        return deck;
//    }
//
//    public void setDeck (null deck)
//    {
//        this.deck = deck;
//    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [image = "+image+", date_last_updated = "+date_last_updated+", aliases = , store_date = "+store_date+", id = "+id+", api_detail_url = "+api_detail_url+", site_detail_url = "+site_detail_url+", has_staff_review = "+has_staff_review+", cover_date = "+cover_date+", date_added = "+date_added+", description = "+description+", resource_type = "+resource_type+", name = "+name+", volume = "+volume+", issue_number = "+issue_number+", deck = ]";
//    }
//
//    protected Results(Parcel in) {
//        image = (Image) in.readValue(Image.class.getClassLoader());
//        api_detail_url = in.readString();
//        site_detail_url = in.readString();
//        description = in.readString();
//        name = in.readString();
//        volume = (Volume) in.readValue(Volume.class.getClassLoader());
//        issue_number = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeValue(image);
//        dest.writeString(api_detail_url);
//        dest.writeString(site_detail_url);
//        dest.writeString(description);
//        dest.writeString(name);
//        dest.writeValue(volume);
//        dest.writeString(issue_number);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<Results> CREATOR = new Parcelable.Creator<Results>() {
//        @Override
//        public Results createFromParcel(Parcel in) {
//            return new Results(in);
//        }
//
//        @Override
//        public Results[] newArray(int size) {
//            return new Results[size];
//        }
//    };
}