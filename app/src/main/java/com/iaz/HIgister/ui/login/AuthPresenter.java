package com.iaz.HIgister.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SignUpEvent;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iaz.HIgister.data.DataManager;
import com.iaz.HIgister.data.model.User;
import com.iaz.HIgister.data.repository.UserRepository;
import com.iaz.HIgister.injection.ConfigPersistent;
import com.iaz.HIgister.ui.base.BasePresenter;
import com.iaz.HIgister.ui.intro.IntroActivity;
import com.iaz.HIgister.ui.main.MainActivity;
import com.iaz.HIgister.ui.viewList.ViewListActivity;
import com.iaz.HIgister.util.DialogFactory;

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
    public Dialog mDialog;

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

    public void setAutenticationListener(String listId) {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser userF = firebaseAuth.getCurrentUser();

            if (userF != null) {

                userRepository.receiveProfileInfo(userF.getUid(), new UserRepository.OnUpdateProfile() {
                    @Override
                    public void onSuccess(User user) {

                        if (listId != null && !listId.isEmpty()) {
                            Intent intent = new Intent(getMvpView().getActivity(), ViewListActivity.class);
                            intent.putExtra("listId", listId);
                            getMvpView().getActivity().startActivity(intent);
                        } else {
                            DialogFactory.finalizeDialog(mDialog, true, "Logged in", () -> {
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getMvpView().getActivity());
                                Boolean showTutorial = sharedPref.getBoolean("showIntro", true);

                                if (showTutorial) {
                                    Intent intent = new Intent(getMvpView().getActivity(), IntroActivity.class);
                                    intent.putExtra("user", user);
                                    getMvpView().getActivity().startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                                    getMvpView().getActivity().startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(String exception) {

                        Log.d("auth", "onCheckUser: user does not exist");

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getMvpView().getActivity());
                        Boolean showTutorial = sharedPref.getBoolean("showIntro", true);

                        if (showTutorial) {
                            Intent intent = new Intent(getMvpView().getActivity(), IntroActivity.class);
                            getMvpView().getActivity().startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                            getMvpView().getActivity().startActivity(intent);
                        }
                    }
                });
                // User is signed in
                Log.d("auth", "onAuthStateChanged:signed_in:" + userF.getUid());
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
                            DialogFactory.finalizeDialogOnClick(mDialog, false, "Sorry, an error occurred on create account", () -> {
                            });
                            Answers.getInstance().logSignUp(new SignUpEvent()
                                    .putMethod("Email")
                                    .putSuccess(false));
                        }
                        else {
                            Answers.getInstance().logSignUp(new SignUpEvent()
                                    .putMethod("Email")
                                    .putSuccess(true));
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
                            DialogFactory.finalizeDialogOnClick(mDialog, false, "Sorry, an error occurred on login", () -> {
                            });
                            Answers.getInstance().logLogin(new LoginEvent()
                                    .putMethod("Email")
                                    .putSuccess(false));
                        }
                        else {
                            Answers.getInstance().logLogin(new LoginEvent()
                                    .putMethod("Email")
                                    .putSuccess(true));
                        }
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
                            Answers.getInstance().logLogin(new LoginEvent()
                                    .putMethod("Facebook")
                                    .putSuccess(true));

                            userRepository.receiveProfileInfo(FirebaseAuth.getInstance().getCurrentUser().getUid(), new UserRepository.OnUpdateProfile() {
                                @Override
                                public void onSuccess(User user) {

                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getMvpView().getActivity());
                                    Boolean showTutorial = sharedPref.getBoolean("showIntro", true);

                                    if (showTutorial) {
                                        Intent intent = new Intent(getMvpView().getActivity(), IntroActivity.class);
                                        intent.putExtra("user", user);
                                        getMvpView().getActivity().startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                                        intent.putExtra("user", user);
                                        getMvpView().getActivity().startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFailure(String exception) {

                                    Profile profile = Profile.getCurrentProfile().getCurrentProfile();
                                    if (profile != null) {
                                        // user has logged in

                                        User newUser = new User();
                                        newUser.setName(profile.getName());
                                        newUser.setProfilePictureUri(profile.getProfilePictureUri(400, 400).toString());

                                        Log.d("auth", "onCheckUser: user does not exist");
                                        userRepository.saveProfileInfo(getMvpView().getActivity(), newUser, new UserRepository.OnUpdateProfile() {
                                            @Override
                                            public void onSuccess(User user) {

                                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getMvpView().getActivity());
                                                Boolean showTutorial = sharedPref.getBoolean("showIntro", true);

                                                if (showTutorial) {
                                                    Intent intent = new Intent(getMvpView().getActivity(), IntroActivity.class);
                                                    intent.putExtra("user", user);
                                                    getMvpView().getActivity().startActivity(intent);
                                                }
                                                else {
                                                    Intent intent = new Intent(getMvpView().getActivity(), MainActivity.class);
                                                    intent.putExtra("loggedWithFacebook", 1);
                                                    getMvpView().getActivity().startActivity(intent);
                                                }
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
                            Answers.getInstance().logLogin(new LoginEvent()
                                    .putMethod("Facebook")
                                    .putSuccess(false)
                                    .putCustomAttribute("reason", "authentication failed"));
                        }
                    }
                });
    }

}
