package com.automatodev.e_conommiza_app.security;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.database.firebase.FirestoreService;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.UserEntityBuilder;
import com.automatodev.e_conommiza_app.preferences.UserPreferences;
import com.automatodev.e_conommiza_app.security.callback.GoogleAuthCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleAuthentication {

    private final Context context;
    private final FirebaseAuth firebaseAuth;


    public GoogleAuthentication(Context context) {
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void firebaseAuthWithGoogle(LayoutDialogProgressBinding bindingProgress, String idToken, GoogleAuthCallback callback) {
        bindingProgress.setIsLoading(true);
        bindingProgress.setInformation("Um momento...");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener((Activity) context, task -> {
            if (task.isSuccessful()) {

                bindingProgress.setInformation("Verificando seu sdados...");
                callback.onSuccess(true);

            } else {

                try {
                    throw task.getException();
                } catch (Exception e) {
                    callback.onFailure("Algo deu errado em seu login, \nDa uma checada na sua conexão :D");
                    Log.e("logx", "Error googleLogin: " + e.getMessage());
                }

            }
        }).addOnFailureListener(e -> {
            callback.onFailure("Algo deu errado em seu login, \nDa uma checada na sua conexão :D");
            Log.e("logx", "Error googleLogin: " + e.getMessage());
        }).addOnCanceledListener(() ->{
            callback.onFailure("Operação cancelada!");
            Log.e("logx", "Login cancelado");
        });

    }


}
