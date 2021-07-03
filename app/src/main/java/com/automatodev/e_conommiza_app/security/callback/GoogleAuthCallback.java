package com.automatodev.e_conommiza_app.security.callback;

public interface GoogleAuthCallback {
    void onSuccess(boolean response);
    void onFailure(String errorMessage);
}
