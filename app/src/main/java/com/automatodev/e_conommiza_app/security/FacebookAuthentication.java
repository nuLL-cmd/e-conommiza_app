package com.automatodev.e_conommiza_app.security;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.database.firebase.FirestoreService;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.UserEntityBuilder;
import com.automatodev.e_conommiza_app.preferences.UserPreferences;
import com.automatodev.e_conommiza_app.security.callback.FacebookAuthCallback;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FacebookAuthentication {

    private final Context context;
    private final FirebaseAuth firebaseAuth;
    private final CallbackManager callbackManager;


    public FacebookAuthentication(Context context) {
        this.context = context;

        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(context);
        this.callbackManager = CallbackManager.Factory.create();
        firebaseAuth = FirebaseAuth.getInstance();

    }


    public CallbackManager getCallbackManager() {
        return this.callbackManager;
    }


    public void loginFacebook(LayoutDialogProgressBinding bindingProgress, FacebookAuthCallback callback) {
        LoginButton loginButton = new LoginButton(context);
        loginButton.performClick();
        loginButton.setPermissions("email", "public_profile");
        LoginManager.getInstance().registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseWithFacebook(bindingProgress, loginResult.getAccessToken(), callback);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                callback.onFailure("Algo deu errado em seu login, \nDa uma checada na sua conexão :D");
                Log.e("logx", "Error loginFacebook: " + error.getLocalizedMessage());
            }
        });
    }

    private void firebaseWithFacebook(LayoutDialogProgressBinding bindingProgress, AccessToken idToken, FacebookAuthCallback callback) {
        bindingProgress.setIsLoading(true);
        bindingProgress.setInformation("Um momento...");
        AuthCredential credential = FacebookAuthProvider.getCredential(idToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                bindingProgress.setInformation("Verificando seu sdados...");
                callback.onSuccess(true);

            } else {
                try {
                    throw task.getException();
                } catch (Exception e) {
                    callback.onFailure("Algo deu errado em seu login, \nDa uma checada na sua conexão :D");
                    Log.e("logx", "Error firebaseWithFacebook: " + e.getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure("Algo deu errado em seu login, \nDa uma checada na sua conexão :D");
                Log.e("logx", "Error firebaseWithFacebook: " + e.getMessage());
            }
        });


    }


}
