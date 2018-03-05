package com.alks_ander.higister.ui.search;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import com.alks_ander.higister.data.BackendManager;
import com.alks_ander.higister.data.DataManager;
import com.alks_ander.higister.injection.ConfigPersistent;
import com.alks_ander.higister.ui.base.BasePresenter;
import com.alks_ander.higister.util.RxUtil;

@ConfigPersistent
public class SearchPresenter extends BasePresenter<SearchMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;

    @Inject
    public SearchPresenter(DataManager dataManager) {
        mDataManager = dataManager;
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

    public void loadResults(String type, String text) {
        checkViewAttached();
        RxUtil.dispose(mDisposable);
        BackendManager backendManager = new BackendManager();


        if (type.equals("movie") || type.equals("series"))
            backendManager.fetchMovies((SearchActivity) getMvpView(), type, text);
        else if (type.equals("book"))
            backendManager.fetchBooks((SearchActivity) getMvpView(), text);
        else if (type.equals("anime") || type.equals("manga")) {
            backendManager.fetchAnimes((SearchActivity) getMvpView(), type, text);
        }
        else if (type.equals("music")){
            backendManager.fetchMusics((SearchActivity) getMvpView(), text);
        }
        else if (type.equals("comics")){
            backendManager.fetchComics((SearchActivity) getMvpView(), text);
        }
    }

}
