package com.iaz.higister.ui.createList;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.model.UserList;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;
import com.iaz.higister.ui.viewList.ViewListActivity;
import com.iaz.higister.util.CompressorUtil;
import com.iaz.higister.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.iaz.higister.util.Constants.PERMISSION_WRITE_EXTERNAL;
import static com.iaz.higister.util.Constants.REQUEST_IMAGE_CAPTURE;
import static com.iaz.higister.util.Constants.REQUEST_IMAGE_CAPTURE_BANNER;
import static com.iaz.higister.util.Constants.SELECT_PICTURE;
import static com.iaz.higister.util.Constants.SELECT_PICTURE_BANNER;

@ConfigPersistent
public class CreateListPresenter extends BasePresenter<CreateListMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;
    private CreateListActivity activity;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Inject
    public CreateListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(CreateListMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void setActivity(CreateListActivity activity) {
        this.activity = activity;
    }

    public void openDialogWindow() {
        EasyImage.openGallery(activity, SELECT_PICTURE);
    }

    public void getPhoto() {
        EasyImage.openCamera(activity, REQUEST_IMAGE_CAPTURE);
    }

    public void openDialogWindowBanner() {
        EasyImage.openGallery(activity, SELECT_PICTURE_BANNER);
    }

    public void getPhotoBanner() {
        EasyImage.openCamera(activity, REQUEST_IMAGE_CAPTURE_BANNER);
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
                if (type == SELECT_PICTURE_BANNER || type == REQUEST_IMAGE_CAPTURE_BANNER) {
                    getMvpView().callGlideBanner(imageUri);
                } else {
                    getMvpView().callGlide(imageUri);
                }
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

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your camera");
                    Uri imageUri = activity.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    getMvpView().dismissDialog();
                } else {
                    getMvpView().showSnackBar("É necessário permitir o acesso à camera");
                }
            }
        }
    }

    public void updateList(UserList list, OnListUpdated onListUpdated) {
        db.collection("lists").document(list.uid).set(list)
                .addOnSuccessListener(documentReference -> {
                    Log.d("updateList: ", "success");
                    onListUpdated.onSuccess();
                })
                .addOnFailureListener(onListUpdated::onFailed);
    }

    interface OnListUpdated {
        void onSuccess();

        void onFailed(Exception exception);
    }

    public void saveListImageOnStorage(String uri, final OnImageUpload onImageUpload) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final StorageReference storageReference = storage.getReference().child(Constants.PATH_USER_IMAGE + uid);

        CompressorUtil.compress(getMvpView().getActivity(), uri, new CompressorUtil.CompressListener() {
            @Override
            public void onCompressSuccess(File file) {
                UploadTask uploadTask = storageReference.putFile(Uri.parse("file://" + file.getPath()));
                uploadTask.
                        addOnFailureListener(exception -> onImageUpload.onFailure(exception.getMessage()))
                        .addOnSuccessListener(taskSnapshot -> {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            onImageUpload.onSuccess(downloadUrl);
                        });
            }

            @Override
            public void onCompressError() {
                onImageUpload.onFailure("Error on compressing");
            }
        });
    }

    interface OnImageUpload {
        void onSuccess(Uri uri);

        void onFailure(String exception);
    }

}
