package com.iaz.higister.data;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import com.iaz.higister.data.model.BaseItem;
import com.iaz.higister.data.model.ComicVine.ComicVineResponse;
import com.iaz.higister.data.model.ComicVine.Results;
import com.iaz.higister.data.model.GoodReads.GoodreadsResponse;
import com.iaz.higister.data.model.GoodReads.Work;
import com.iaz.higister.data.model.LastFM.LastFmResponse;
import com.iaz.higister.data.model.LastFM.Track;
import com.iaz.higister.data.model.MyAnimeList.MyAnimeListResponse;
import com.iaz.higister.data.model.MyAnimeList.Result;
import com.iaz.higister.data.model.Omdb.OmdbResponse;
import com.iaz.higister.data.model.Omdb.Search;
import com.iaz.higister.data.remote.AnimesService;
import com.iaz.higister.data.remote.BooksService;
import com.iaz.higister.data.remote.ComicsService;
import com.iaz.higister.data.remote.MoviesService;
import com.iaz.higister.data.remote.MusicsService;
import com.iaz.higister.ui.search.RecyclerViewFragment2;
import com.iaz.higister.ui.search.SearchActivity;
import com.iaz.higister.util.Constants;

/**
 * Created by alksander on 01/03/2018.
 */

public class BackendManager {

    public void fetchMovies(final RecyclerViewFragment2 activity, int type, String text) {

        String typeString;

        if (type == Constants.MOVIES) {
            typeString = "movie";
        }
        else {
            typeString = "series";
        }

        MoviesService.Creator.newMoviesService().fetchMovies(typeString, text).enqueue(
                new Callback<OmdbResponse>() {
                    @Override
                    public void onResponse(Call<OmdbResponse> call, Response<OmdbResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body().getSearch() != null) {

                            for (Search search : response.body().getSearch())
                                results.add(new BaseItem(search, type));
                        }

                        activity.showItems(results, type);
                    }

                    @Override
                    public void onFailure(Call<OmdbResponse> call, Throwable t) {
                        Timber.d(t.toString());
                    }
                }

        );
    }

    public void fetchAnimes(final RecyclerViewFragment2 activity, int type, String text) {

        String typeString;

        if (type == Constants.ANIMES) {
            typeString = "anime";
        }
        else {
            typeString = "manga";
        }

        AnimesService.Creator.newAnimesService().fetchAnimes(typeString, text).enqueue(
                new Callback<MyAnimeListResponse>() {
                    @Override
                    public void onResponse(Call<MyAnimeListResponse> call, Response<MyAnimeListResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getResult().size() > 10)
                                for (Result result : response.body().getResult().subList(0, 10))
                                    results.add(new BaseItem(result, type));
                            else
                                for (Result result : response.body().getResult())
                                    results.add(new BaseItem(result, type));

                        }
                            activity.showItems(results, type);
                    }

                    @Override
                    public void onFailure(Call<MyAnimeListResponse> call, Throwable t) {
                        Timber.d(t.toString());
                    }
                }

        );
    }

    public void fetchBooks(final RecyclerViewFragment2 activity, String text) {

        BooksService.Creator.newBooksService().fetchBooks(text).enqueue(
                new Callback<GoodreadsResponse>() {
                    @Override
                    public void onResponse(Call<GoodreadsResponse> call, Response<GoodreadsResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getSearch().getResults().getWork().size() > 10) {
                                for (Work work : response.body().getSearch().getResults().getWork().subList(0, 10))
                                    results.add(new BaseItem(work.getBest_book()));
                            } else {
                                for (Work work : response.body().getSearch().getResults().getWork())
                                    results.add(new BaseItem(work.getBest_book()));
                            }
                        }

                        activity.showItems(results, Constants.BOOKS);
                    }

                    @Override
                    public void onFailure(Call<GoodreadsResponse> call, Throwable t) {
                        Timber.d(t.toString());
                    }
                }

        );
    }

    public void fetchMusics(final RecyclerViewFragment2 activity, String text) {

        MusicsService.Creator.newMusicsService().fetchMusics(text).enqueue(
                new Callback<LastFmResponse>() {
                    @Override
                    public void onResponse(Call<LastFmResponse> call, Response<LastFmResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getResults().getTrackmatches().getTrack().size() > 10) {
                                for (Track track : response.body().getResults().getTrackmatches().getTrack().subList(0, 10))
                                    results.add(new BaseItem(track));
                            } else {
                                for (Track track : response.body().getResults().getTrackmatches().getTrack())
                                    results.add(new BaseItem(track));
                            }
                        }

                        activity.showItems(results, Constants.MUSICS);
                    }

                    @Override
                    public void onFailure(Call<LastFmResponse> call, Throwable t) {
                        Timber.d(t.toString());
                    }
                }

        );
    }

    public void fetchComics(final RecyclerViewFragment2 activity, String text) {

        ComicsService.Creator.newComicsService().fetchComics(text).enqueue(
                new Callback<ComicVineResponse>() {
                    @Override
                    public void onResponse(Call<ComicVineResponse> call, Response<ComicVineResponse> response) {

                        ArrayList<BaseItem> results = new ArrayList<>();

                        if (response.body() != null) {
                            if (response.body().getResults().size() > 10) {
                                for (Results result : response.body().getResults().subList(0, 10))
                                    results.add(new BaseItem(result));
                            } else {
                                for (Results result : response.body().getResults())
                                    results.add(new BaseItem(result));
                            }
                        }

                        activity.showItems(results, Constants.COMICS);
                    }

                    @Override
                    public void onFailure(Call<ComicVineResponse> call, Throwable t) {
                        Timber.d(t.toString());
                    }
                }

        );
    }
}
