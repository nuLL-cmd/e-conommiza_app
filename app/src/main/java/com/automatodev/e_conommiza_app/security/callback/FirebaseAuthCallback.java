package com.automatodev.e_conommiza_app.security.callback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface FirebaseAuthCallback {

    void onSuccess(Task<AuthResult> task);
    void onFailure(Exception e);
}
