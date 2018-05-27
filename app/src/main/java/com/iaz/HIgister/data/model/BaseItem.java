package com.iaz.HIgister.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.iaz.HIgister.data.model.ComicVine.Results;
import com.iaz.HIgister.data.model.GoodReads.bookDetails.Author;
import com.iaz.HIgister.data.model.GoodReads.bookDetails.Book;
import com.iaz.HIgister.data.model.GoodReads.search.BestBook;
import com.iaz.HIgister.data.model.LastFM.ImageLast;
import com.iaz.HIgister.data.model.LastFM.Track;
import com.iaz.HIgister.data.model.MyAnimeList.Result;
import com.iaz.HIgister.data.model.Omdb.movieDetails.MovieDetails;
import com.iaz.HIgister.data.model.Omdb.search.Search;
import com.iaz.HIgister.util.Constants;

/**
 * Created by alksander on 04/03/2018.
 */

public class BaseItem implements Parcelable {

    public String title;
    public String description;
    public String detailsUrl;
    private int myType;
    private Bitmap bit;
    public String imageUrl;

    @Exclude
    public String id;

    protected BaseItem() {
    }

    public BaseItem(Search omdbItem, int type) {
        this.id = omdbItem.getImdbID();
        this.title = omdbItem.getTitle();
        this.description = omdbItem.getYear();
        this.myType = type;
        this.imageUrl = omdbItem.getPoster();
        this.detailsUrl = "http://www.imdb.com.br/title/" + omdbItem.getImdbID() + "/";
    }

    public BaseItem(MovieDetails omdbItem, int type) {
        this.title = omdbItem.getTitle();

        this.description = omdbItem.getPlot()
                + "<br><br><b>Runtime: </b> " + omdbItem.getRuntime()
                + "<br><br><b>Genre: </b> " + omdbItem.getGenre()
                + "<br><br><b>Actors:</b> " + omdbItem.getActors()
                + "<br><br><b>Director:</b> " + omdbItem.getDirector()
                + "<br><br><b>Production:</b> " + omdbItem.getProduction();

        this.myType = type;
        this.imageUrl = omdbItem.getPoster();
        this.detailsUrl = "http://www.imdb.com.br/title/" + omdbItem.getImdbID() + "/";
    }

    public BaseItem(BestBook book) {
        this.id = Integer.toString(book.getId());
        this.title = book.getTitle();
//        this.description = book.getAuthor();
        this.myType = Constants.BOOKS;
        this.imageUrl = book.getImage_url();
    }

    public BaseItem(Book book) {
        this.title = book.getTitle();

        StringBuilder authorText = new StringBuilder();

        if (book.getAuthors().getAuthor().size() > 0) {
            authorText.append("<br><br><b>Authors:</b> ");
            for (Author author : book.getAuthors().getAuthor()) {
                if (author.equals(book.getAuthors().getAuthor().get(book.getAuthors().getAuthor().size() - 1)))
                    authorText.append(author.getName());
                else {
                    authorText.append(author.getName()).append(", ");
                }
            }
        } else {
            authorText.append("<br><br><b>Author:</b> " + book.getAuthors().getAuthor().get(0).getName());
        }
        this.description = book.getDescription()
                + "<br><br><b>Number of pages:</b> " + book.getNum_pages()
                + authorText
                + "<br><br><b>Publisher:</b> " + book.getPublisher();
        this.myType = Constants.BOOKS;
        this.imageUrl = book.getImage_url();
        this.detailsUrl = book.getUrl();
    }

    public BaseItem(Result myAnimeListItem, int type) {
        this.title = myAnimeListItem.getTitle();

        if (type == Constants.ANIMES) {
            this.description = myAnimeListItem.getDescription()
                    + "<br><br><b>Episodes:</b> " + myAnimeListItem.getEpisodes();
        } else {
            this.description = myAnimeListItem.getDescription();
        }
        this.myType = type;
        this.imageUrl = myAnimeListItem.getImage_url();
        this.detailsUrl = myAnimeListItem.getUrl();
    }

    public BaseItem(Results comicsVineItem) {

        if (comicsVineItem.getName() != null)
            this.title = comicsVineItem.getVolume().getName() + " " + comicsVineItem.getName();
        else
            this.title = comicsVineItem.getVolume().getName();

        this.description = comicsVineItem.getDescription()
                + "<br><br><b>Issue Number:</b> " + comicsVineItem.getIssue_number();

        this.myType = Constants.COMICS;
        this.imageUrl = comicsVineItem.getImage().getSmall_url();
        this.detailsUrl = comicsVineItem.getSite_detail_url();
    }

    public BaseItem(Track lastFmItem) {
        this.title = lastFmItem.getName();

        if (lastFmItem.getArtist() != null && !lastFmItem.getArtist().isEmpty())
            this.description = "Artist: " + lastFmItem.getArtist();

        this.myType = Constants.MUSICS;

        if (lastFmItem.getImage() != null) {
            for (ImageLast image : lastFmItem.getImage())
                if (image.getImageSizeDescription().equals("large"))
                    this.imageUrl = image.getImageUrl();

            if (this.imageUrl == null || this.imageUrl.isEmpty())
                for (ImageLast image : lastFmItem.getImage())
                    if (image.getImageSizeDescription().equals("medium"))
                        this.imageUrl = image.getImageUrl();

            if (this.imageUrl == null || this.imageUrl.isEmpty())
                for (ImageLast image : lastFmItem.getImage())
                    if (image.getImageSizeDescription().equals("extralarge"))
                        this.imageUrl = image.getImageUrl();

            if (this.imageUrl == null || this.imageUrl.isEmpty())
                this.imageUrl = lastFmItem.getImage()[0].getImageUrl();
        }

        this.detailsUrl = lastFmItem.getUrl();
    }

    public int getMyType() {
        return myType;
    }

    public void setMyType(int myType) {
        this.myType = myType;
    }

    public Bitmap getBit() {
        return bit;
    }

    public void setBit(Bitmap bit) {
        this.bit = bit;
    }

    protected BaseItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        detailsUrl = in.readString();
        myType = in.readInt();
//        bit = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(detailsUrl);
        dest.writeInt(myType);
        //        dest.writeValue(bit);
        dest.writeString(imageUrl);

    }

    @SuppressWarnings("unused")
    public static final Creator<BaseItem> CREATOR = new Creator<BaseItem>() {
        @Override
        public BaseItem createFromParcel(Parcel in) {
            return new BaseItem(in);
        }

        @Override
        public BaseItem[] newArray(int size) {
            return new BaseItem[size];
        }
    };
}
