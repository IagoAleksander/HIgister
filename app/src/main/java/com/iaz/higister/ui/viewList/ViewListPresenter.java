package com.iaz.higister.ui.viewList;

import com.iaz.higister.data.DataManager;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class ViewListPresenter extends BasePresenter<ViewListMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;
    private ViewListActivity activity;

    @Inject
    public ViewListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ViewListMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void setActivity(ViewListActivity activity) {
        this.activity = activity;
    }

}
