package com.iaz.higister.ui.login;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iaz.higister.data.DataManager;
import com.iaz.higister.data.model.User;
import com.iaz.higister.data.repository.UserRepository;
import com.iaz.higister.injection.ConfigPersistent;
import com.iaz.higister.ui.base.BasePresenter;
import com.iaz.higister.ui.main.MainActivity;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

@ConfigPersistent
public class AuthPresenter extends BasePresenter<AuthMvpView> {

    private final DataManager mDataManager;
    private Disposable mDisposable;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    UserRepository userRepository = new UserRepository();

    @Inject
    public AuthPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(AuthMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mDisposable != null) mDisposable.dispose();
    }

    public void setAutenticationListener() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {

                userRepository.receiveProfileInfo(user.getUid(), new UserRepository.OnUpdateProfile() {
                    @Override
                    public void onSuccess(User user) {
                        Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                        getMvpView().getActivity().startActivity(intent);
                    }

                    @Override
                    public void onFailure(String exception) {

                        Log.d("auth", "onCheckUser: user does not exist");
                        userRepository.saveProfileInfo(getMvpView().getActivity(), new User(), new UserRepository.OnUpdateProfile() {
                            @Override
                            public void onSuccess(User user) {
                                Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                                getMvpView().getActivity().startActivity(intent);
                            }

                            @Override
                            public void onFailure(String exception) {
                                Log.d("auth", "onCreateNewUser:failure");
                            }
                        });
                    }
                });
                // User is signed in
                Log.d("auth", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("auth", "onAuthStateChanged:signed_out");
            }
        };
    }

    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void removeAuthStateListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getMvpView().getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<AuthResult> task) {
                        Log.d("acc created", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getMvpView().getActivity(), "falha",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signInWithEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getMvpView().getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("acc logged in", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getMvpView().getActivity(), "falha",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void handleFacebookAccessToken(AccessToken token) {
        Log.d("", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getMvpView().getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");

                            userRepository.receiveProfileInfo(FirebaseAuth.getInstance().getCurrentUser().getUid(), new UserRepository.OnUpdateProfile() {
                                @Override
                                public void onSuccess(User user) {
                                    Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                                    getMvpView().getActivity().startActivity(intent);
                                }

                                @Override
                                public void onFailure(String exception) {

                                    Profile profile = Profile.getCurrentProfile().getCurrentProfile();
                                    if (profile != null) {
                                        // user has logged in

                                        User newUser = new User();
                                        newUser.setName(profile.getName());
                                        newUser.setProfilePictureUri(profile.getProfilePictureUri(400,400).toString());

                                        Log.d("auth", "onCheckUser: user does not exist");
                                        userRepository.saveProfileInfo(getMvpView().getActivity(), newUser, new UserRepository.OnUpdateProfile() {
                                            @Override
                                            public void onSuccess(User user) {
                                                Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                                                intent.putExtra("loggedWithFacebook", 1);
                                                getMvpView().getActivity().startActivity(intent);
                                            }

                                            @Override
                                            public void onFailure(String exception) {
                                                Log.d("auth", "onCreateNewUser:failure");
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getMvpView().getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

}
