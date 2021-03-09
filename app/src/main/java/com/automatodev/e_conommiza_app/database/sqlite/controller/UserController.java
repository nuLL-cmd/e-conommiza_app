package com.automatodev.e_conommiza_app.database.sqlite.controller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.automatodev.e_conommiza_app.database.sqlite.config.DatabaseConfig;
import com.automatodev.e_conommiza_app.model.UserEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class UserController extends AndroidViewModel {
    private DatabaseConfig databaseConfig;
    public UserController(@NonNull Application application) {
        super(application);

        this.databaseConfig = DatabaseConfig.getDatabaseConfig(application);
    }


    public Flowable<List<UserEntity>> getAllUsers(){
        return databaseConfig.userRepository().getAllUsers();
    }

    public Flowable<UserEntity> getUserByUid(String uid){
        return this.databaseConfig.userRepository().getUserByUid(uid);
    }

    public Completable addUser(UserEntity userEntity){
        return this.databaseConfig.userRepository().addUser(userEntity);
    }
}
