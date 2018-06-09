package com.iaz.HIgister.ui.listsTutorial;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.iaz.HIgister.injection.ConfigPersistent;
import com.iaz.HIgister.ui.base.BasePresenter;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.iaz.HIgister.util.Constants.PERMISSION_WRITE_EXTERNAL;
import static com.iaz.HIgister.util.Constants.REQUEST_IMAGE_CAPTURE;
import static com.iaz.HIgister.util.Constants.SELECT_PICTURE;

@ConfigPersistent
public class ListsTutorialPresenter extends BasePresenter<ListsTutorialMvpView> {

    private Disposable mDisposable;
    private ListsTutorialActivity activity;

    @Inject
    public ListsTutorialPresenter() {
    }

    @Override
    public void attachView(ListsTutorialMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void setActivity(ListsTutorialActivity activity) {

        this.activity = activity;
    }


    public void openDialogWindow() {
        EasyImage.openGallery(activity, SELECT_PICTURE);
    }

    public void getPhoto() {
        EasyImage.openCamera(activity, REQUEST_IMAGE_CAPTURE);
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@android.support.annotation.NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                Uri imageUri = Uri.fromFile(imageFiles.get(0));

                if (activity.fragmentPicture.isMenuVisible())
                    activity.fragmentPicture.callGlide(imageUri);

                else if (activity.fragmentItemPicture.isMenuVisible())
                    activity.fragmentItemPicture.callGlide(imageUri);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
            }
        });

    }

    public void requestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    getPhoto();
                    activity.fragmentPicture.dismissDialog();
                } else {
                    activity.fragmentPicture.showSnackBar("É necessário permitir o acesso à camera");
                }
            }
        }
    }
}
