package com.automatodev.e_conommiza_app.database.firestore;

import androidx.annotation.NonNull;

import com.automatodev.e_conommiza_app.database.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.model.UserEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class FirestoreService {

    private FirebaseFirestore firebaseFirestore;

    public FirestoreService(){
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void saveUser(UserEntity userEntity, FirestoreSaveCallback callback){
      if (userEntity != null){
          firebaseFirestore.collection("users").document(userEntity.getUid())
                  .set(userEntity)
                  .addOnSuccessListener(aVoid -> callback.onSuccess())
                  .addOnFailureListener(callback::onFailure);
      }
    }

    public void updateUser(String uid, Map<String, Object> data, FirestoreSaveCallback callback){
        DocumentReference dr = firebaseFirestore.collection("users")
                .document(uid);

            dr.update(data)
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(callback::onFailure);
    }

}
