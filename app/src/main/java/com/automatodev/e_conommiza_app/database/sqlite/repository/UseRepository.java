package com.automatodev.e_conommiza_app.database.sqlite.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.automatodev.e_conommiza_app.model.UserEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface UseRepository {
    @Query("SELECT * FROM  tb_user")
    public Flowable<List<UserEntity>> getAllUsers();

    public @Query("SELECT * FROM tb_user WHERE user_uid = :uid")
    Flowable<UserEntity> getUserById(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable addUser(UserEntity userEntity);
}

