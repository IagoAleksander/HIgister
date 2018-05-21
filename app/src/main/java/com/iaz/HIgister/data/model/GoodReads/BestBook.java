package com.iaz.HIgister.data.model.GoodReads;

/**
 * Created by alksander on 02/03/2018.
 */

public class BestBook {

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

    private BestBook () {

    }

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

//    protected BestBook(Parcel in) {
//        title = in.readString();
//        image_url = in.readString();
//        type = in.readString();
//        small_image_url = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(title);
//        dest.writeString(image_url);
//        dest.writeString(type);
//        dest.writeString(small_image_url);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Parcelable.Creator<BestBook> CREATOR = new Parcelable.Creator<BestBook>() {
//        @Override
//        public BestBook createFromParcel(Parcel in) {
//            return new BestBook(in);
//        }
//
//        @Override
//        public BestBook[] newArray(int size) {
//            return new BestBook[size];
//        }
//    };
}

