package com.automatodev.e_conommiza_app.database.callback;

import com.google.firebase.firestore.DocumentReference;

public interface FirestoreSaveCallback {

    void onSuccess(DocumentReference documentReference);
    void onFailure(Exception e);
}
