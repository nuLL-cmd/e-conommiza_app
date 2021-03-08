package com.automatodev.e_conommiza_app.database.firebase.callback;

public interface FirestoreSaveCallback {

    void onSuccess();
    void onFailure(Exception e);
}
