package com.iaz.HIgister.data.model.GoodReads.bookDetails;

import com.iaz.HIgister.data.model.GoodReads.search.Work;

public class Book
{
    private String title;

    private String description;

    private String num_pages;

    private String small_image_url;

    private String image_url;

    private String publisher;

    private Authors authors;

    private String url;

    private String is_ebook;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNum_pages() {
        return num_pages;
    }

    public void setNum_pages(String num_pages) {
        this.num_pages = num_pages;
    }

    public String getSmall_image_url() {
        return small_image_url;
    }

    public void setSmall_image_url(String small_image_url) {
        this.small_image_url = small_image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Authors getAuthors() {
        return authors;
    }

    public void setAuthors(Authors authors) {
        this.authors = authors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIs_ebook() {
        return is_ebook;
    }

    public void setIs_ebook(String is_ebook) {
        this.is_ebook = is_ebook;
    }
}
