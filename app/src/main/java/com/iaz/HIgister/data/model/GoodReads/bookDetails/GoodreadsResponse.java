package com.iaz.HIgister.data.model.GoodReads.bookDetails;

/**
 * Created by alksander on 26/05/2018.
 */


public class GoodreadsResponse {
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "ClassPojo [book = " + book + "]";
    }
}