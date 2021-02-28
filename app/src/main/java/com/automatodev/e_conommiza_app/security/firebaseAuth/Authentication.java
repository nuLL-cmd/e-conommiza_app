package com.automatodev.e_conommiza_app.security.firebaseAuth;

import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication  {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public Authentication(){
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void loginWithEmail(String email, String password, FirebaseAuthCallback callback){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    public void registerWithEmail(String email, String password, FirebaseAuthCallback callback){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback::onSuccess)
                .addOnFailureListener(callback::onFailure);
    }

    public FirebaseUser getUser(){
        return this.firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void logout(){
        firebaseAuth.signOut();
    }
}
