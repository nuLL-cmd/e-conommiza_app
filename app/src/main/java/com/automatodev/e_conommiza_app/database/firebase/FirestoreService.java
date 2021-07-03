package com.automatodev.e_conommiza_app.database.firebase;

import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreGetCallback;
import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FirestoreService {

    private FirebaseFirestore firebaseFirestore;

    public FirestoreService() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void saveUser(UserEntity userEntity, FirestoreSaveCallback callback) {
        if (userEntity != null) {
            firebaseFirestore.collection("users").document(userEntity.getUserUid())
                    .set(userEntity)
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(callback::onFailure);
        }
    }

    public void updateUser(String uid, Map<String, Object> data, FirestoreSaveCallback callback) {
        DocumentReference dr = firebaseFirestore.collection("users")
                .document(uid);

        dr.update(data)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(callback::onFailure);
    }

    public void getUser(String uid, FirestoreGetCallback callback) {
        DocumentReference reference = firebaseFirestore.collection("users").document(uid);
        reference.get()
                .addOnCompleteListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

}
