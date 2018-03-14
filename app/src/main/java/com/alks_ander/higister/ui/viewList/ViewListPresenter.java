package com.alks_ander.higister.ui.viewList;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;

import com.alks_ander.higister.data.DataManager;
import com.alks_ander.higister.injection.ConfigPersistent;
import com.alks_ander.higister.ui.base.BasePresenter;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.alks_ander.higister.util.Constants.PERMISSION_WRITE_EXTERNAL;
import static com.alks_ander.higister.util.Constants.REQUEST_IMAGE_CAPTURE;
import static com.alks_ander.higister.util.Constants.REQUEST_IMAGE_CAPTURE_BANNER;
import static com.alks_ander.higister.util.Constants.SELECT_PICTURE;
import static com.alks_ander.higister.util.Constants.SELECT_PICTURE_BANNER;

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
