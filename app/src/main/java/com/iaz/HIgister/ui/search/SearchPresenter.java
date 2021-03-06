package com.iaz.HIgister.ui.search;


import com.iaz.HIgister.data.BackendManager;
import com.iaz.HIgister.data.DataManager;
import com.iaz.HIgister.injection.ConfigPersistent;
import com.iaz.HIgister.ui.base.BasePresenter;
import com.iaz.HIgister.util.Constants;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class SearchPresenter extends BasePresenter<SearchMvpView> {

//    private final DataManager mDataManager;
    private Disposable mDisposable;

    @Inject
    public SearchPresenter(DataManager dataManager) {
//        mDataManager = dataManager;
    }

    public SearchPresenter() {
    }

    @Override
    public void attachView(SearchMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void loadResults(int type, String text) {
        BackendManager backendManager = new BackendManager();


        switch (type) {
            case Constants.MOVIES:
            case Constants.TV_SERIES:
                backendManager.fetchMovies((RecyclerViewFragment2) getMvpView(), type, text);
                break;
            case Constants.BOOKS:
                backendManager.fetchBooks((RecyclerViewFragment2) getMvpView(), text);
                break;
            case Constants.ANIMES:
            case Constants.MANGAS:
                backendManager.fetchAnimes((RecyclerViewFragment2) getMvpView(), type, text);
                break;
            case Constants.MUSICS:
                backendManager.fetchMusics((RecyclerViewFragment2) getMvpView(), text);
                break;
            case Constants.COMICS:
                backendManager.fetchComics((RecyclerViewFragment2) getMvpView(), text);
                break;
        }
    }

}
