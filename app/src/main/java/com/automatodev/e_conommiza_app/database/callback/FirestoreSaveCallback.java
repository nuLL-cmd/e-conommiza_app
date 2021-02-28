package com.automatodev.e_conommiza_app.database.callback;

import com.google.firebase.firestore.DocumentReference;

public interface FirestoreSaveCallback {

    void onSuccess();
    void onFailure(Exception e);
}
