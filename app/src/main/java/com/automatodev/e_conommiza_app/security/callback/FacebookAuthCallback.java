package com.automatodev.e_conommiza_app.security.callback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface FacebookAuthCallback {
    void onSuccess(boolean response);
    void onFailure(String errorMessage);
}
