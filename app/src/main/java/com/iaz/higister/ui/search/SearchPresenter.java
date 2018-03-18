package com.iaz.higister.ui.search;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import com.iaz.higister.data.BackendManager;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;
import com.iaz.higister.util.RxUtil;

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

    public void loadResults(String type, String text) {
        checkViewAttached();
        RxUtil.dispose(mDisposable);
        BackendManager backendManager = new BackendManager();


        if (type.equals("movie") || type.equals("series"))
            backendManager.fetchMovies((RecyclerViewFragment2) getMvpView(), type, text);
        else if (type.equals("book"))
            backendManager.fetchBooks((RecyclerViewFragment2) getMvpView(), text);
        else if (type.equals("anime") || type.equals("manga")) {
            backendManager.fetchAnimes((RecyclerViewFragment2) getMvpView(), type, text);
        }
        else if (type.equals("music")){
            backendManager.fetchMusics((RecyclerViewFragment2) getMvpView(), text);
        }
        else if (type.equals("comics")){
            backendManager.fetchComics((RecyclerViewFragment2) getMvpView(), text);
        }
    }

}
