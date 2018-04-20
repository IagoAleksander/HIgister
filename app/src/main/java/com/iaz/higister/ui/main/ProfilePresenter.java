package com.iaz.higister.ui.main;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.model.User;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;
import com.iaz.higister.util.CompressorUtil;
import com.iaz.higister.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
public class ProfilePresenter extends BasePresenter<ProfileMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;
    User user ;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Inject
    public ProfilePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ProfileMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void openDialogWindow() {
        EasyImage.openGallery(getMvpView().getFragment(), SELECT_PICTURE);
    }

    public void getPhoto() {
        EasyImage.openCamera(getMvpView().getFragment(), REQUEST_IMAGE_CAPTURE);
    }

    public void saveProfileInfo(String name, String description, int age, ArrayList<String> interests, Uri uri) {
        // Add a new document with a generated ID

        user = new User();
        user.name = name;
        user.description = description;
        user.age = age;
        user.followersNumber = 300;
        user.interests = interests;
        user.listsCreatedNumber = 17;
        user.listsFavouritedNumber = 12;

        if (uri != null)
            user.profilePictureUri = uri.toString();

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateProfile", "DocumentSnapshot successfully written!");
                        getMvpView().updateData(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateProfile", "Error writing document", e);
                    }
                });
    }

    public void receiveProfileInfo() {
        user = null;

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);
                    getMvpView().updateData(user);
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("updateProfile", "Error writing document", e);
            }
        });
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, getMvpView().getFragment().activity, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@android.support.annotation.NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                Uri imageUri = Uri.fromFile(imageFiles.get(0));
//                if (type == SELECT_PICTURE_BANNER || type == REQUEST_IMAGE_CAPTURE_BANNER) {
//                    getMvpView().callGlideBanner(imageUri);
//                } else {
                    getMvpView().callGlide(imageUri);
//                }
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
                    Uri imageUri = getMvpView().getFragment().activity.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    getMvpView().getFragment().startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//                    getMvpView().dismissDialog();
                } else {
                    getMvpView().showSnackBar("É necessário permitir o acesso à camera");
                }
            }
        }
    }

    public void saveProfileImageOnStorage(String uri, final OnImageUpload onImageUpload) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final StorageReference storageReference = storage.getReference().child(Constants.PATH_USER_IMAGE + uid);

        CompressorUtil.compress(getMvpView().getFragment().getContext(), uri, new CompressorUtil.CompressListener() {
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
