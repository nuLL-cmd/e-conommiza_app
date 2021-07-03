package com.automatodev.e_conommiza_app.security;

import android.content.Context;
import android.util.Log;

import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.database.firebase.firestore.FirestoreService;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.automatodev.e_conommiza_app.preferences.UserPreferences;
import com.automatodev.e_conommiza_app.security.callback.FirebaseAuthCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FirebaseAuthentication {

    private final FirebaseAuth firebaseAuth;
    private final FirestoreService firestoreService;
    private final UserPreferences userPreferences;

    public FirebaseAuthentication(Context context) {

        userPreferences = new UserPreferences(context, "user");
        firebaseAuth = FirebaseAuth.getInstance();
        firestoreService = new FirestoreService();

    }

    public void loginWithEmail(String email, String password, FirebaseAuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true);

                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            callback.onFailure("Email ou senha invalidos!");
                            Log.d("logx", "Error loginWithEmail: " + e.getMessage());
                        } catch (FirebaseAuthEmailException e) {
                            callback.onFailure("Email com formato invalido!");
                            Log.d("logx", "Error loginWithEmail: " + e.getMessage());
                        } catch (FirebaseAuthInvalidUserException e) {
                            callback.onFailure("Usuario não encontrado!");
                            Log.d("logx", "Error loginWithEmail: " + e.getMessage());
                        } catch (Exception e) {
                            callback.onFailure("Algo deu errado em seu login, \nDa uma checada na sua conexão :D");
                            Log.d("logx", "Error loginWithEmail: " + e.getMessage());
                        }


                    }
                }).addOnFailureListener(e -> {
            Log.d("logx", "Error loginWithEmail: " + e.getMessage());
            callback.onFailure("Algo deu errado em seu login, \nDa uma checada na sua conexão :D");

        });
    }

    public void registerWithEmail(UserEntity user,LayoutDialogProgressBinding bindingProgress, String email, String password, FirebaseAuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.setUserUid(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                        bindingProgress.setInformation("Salvando dados na nuvem...");
                        firestoreService.saveUser(user, new FirestoreSaveCallback() {
                            @Override
                            public void onSuccess() {
                                userPreferences.setUser(user);
                                callback.onSuccess(true);

                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("logx", "Error saveUser: " + e.getMessage());
                                callback.onFailure("Algo deu errado, verifique seu email e/ou senha.\nDa uma olhada na sua conexão :D");
                            }
                        });

                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            callback.onFailure("Escolha uma senha mais forte");
                            Log.e("logx", "Error createUserWithEmailAndPassword: " + e.getMessage());
                        } catch (FirebaseAuthEmailException e) {
                            callback.onFailure("Email com formato invalido!");
                            Log.e("logx", "Error createUserWithEmailAndPassword: " + e.getMessage());
                        } catch (FirebaseAuthUserCollisionException e) {
                            callback.onFailure("Email ja cadastrado em outra conta!");
                            Log.e("logx", "Error createUserWithEmailAndPassword: " + e.getMessage());
                        } catch (Exception e) {
                            callback.onFailure( "Algo deu errado, verifique seu email e/ou senha.\nDa uma olhada na sua conexão :D");
                            Log.e("logx", "Error createUserWithEmailAndPassword: " + e.getMessage());

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Algo deu errado em seu login, \nDa uma checada na sua conexão :D");
                    Log.e("logx", "Error createUserWithEmailAndPassword: " + e.getMessage());
                });
    }

    public FirebaseUser getUser(){
        return firebaseAuth.getCurrentUser();
    }

    public void logout() {
        firebaseAuth.signOut();
    }
}
