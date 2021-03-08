package com.automatodev.e_conommiza_app.database.firebase.callback;

import android.net.Uri;

public interface StorageCallback {
    void onSuccess(Uri uri);
    void onFailure(Exception e);
}
