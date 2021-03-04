package com.automatodev.e_conommiza_app.database.callback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public interface FirestoreGetCallback {
    void onSuccess(Task<DocumentSnapshot>task);
    void onFailure(Exception e);
}
