package com.iaz.higister2.data.remote;

import com.iaz.higister2.data.model.GoodReads.GoodreadsResponse;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface BooksService {

    String ENDPOINT = "https://www.goodreads.com/search/";

    @GET("index.xml?key=gsKNSne9BvGuORhjeBmkNg")
    @Headers({
            "Accept: application/xml",
            "Content-Type: application/xml"
    })
    Call<GoodreadsResponse> fetchBooks(
            @Query("q") String s
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
    }



}
