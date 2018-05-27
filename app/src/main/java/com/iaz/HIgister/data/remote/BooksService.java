package com.iaz.HIgister.data.remote;


import com.iaz.HIgister.data.model.GoodReads.search.GoodreadsResponse;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BooksService {

    String ENDPOINT = "https://www.goodreads.com/search/";
    String ENDPOINT2 = "https://www.goodreads.com/book/show/";

    @GET("index.xml?key=gsKNSne9BvGuORhjeBmkNg")
    @Headers({
            "Accept: application/xml",
            "Content-Type: application/xml"
    })
    Call<GoodreadsResponse> fetchBooks(
            @Query("q") String s
    );

    @GET("{id}.xml?key=gsKNSne9BvGuORhjeBmkNg")
    @Headers({
            "Accept: application/xml",
            "Content-Type: application/xml"
    })
    Call<com.iaz.HIgister.data.model.GoodReads.bookDetails.GoodreadsResponse> fetchBookDetails(
            @Path("id") int id
    );

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static BooksService newBooksService() {

            return new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(
                            SimpleXmlConverterFactory.createNonStrict(
                                    new Persister(new AnnotationStrategy())))
                    .client(new OkHttpClient())
                    .build()
                    .create(BooksService.class);
        }

        public static BooksService newBookDetailsService() {

            return new Retrofit.Builder()
                    .baseUrl(ENDPOINT2)
                    .addConverterFactory(
                            SimpleXmlConverterFactory.createNonStrict(
                                    new Persister(new AnnotationStrategy())))
                    .client(new OkHttpClient())
                    .build()
                    .create(BooksService.class);
        }
    }



}
