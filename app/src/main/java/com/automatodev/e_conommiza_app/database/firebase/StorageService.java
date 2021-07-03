package com.automatodev.e_conommiza_app.database.firebase;

import android.net.Uri;

import com.automatodev.e_conommiza_app.database.firebase.callback.StorageCallback;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageService {

    private FirebaseStorage storage;
    public StorageService(){
        storage = FirebaseStorage.getInstance();
    }

    public void uploadPhoto(Uri uri, String userUid, StorageCallback callback){
        StorageReference reference = storage.getReference("profilePhoto").child(userUid);
        reference.putFile(uri)
                .addOnCompleteListener(task -> {
                    reference.getDownloadUrl()
                            .addOnSuccessListener(callback::onSuccess)
                            .addOnFailureListener(callback::onFailure);
                }).addOnFailureListener(callback::onFailure);

    }


}
