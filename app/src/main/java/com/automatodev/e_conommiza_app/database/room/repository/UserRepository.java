package com.automatodev.e_conommiza_app.database.room.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.automatodev.e_conommiza_app.model.UserEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

//Anotação informando qeu esta é uma classe Dao para o RoomDatabase
@Dao

//Interface que serve como repositorio, contendo as operações do banco de dados, é idela separar as operações de acordo com as classes (entidades)
public interface UserRepository {

    //Anotação query, com o parametro do select a ser usado
    @Query("SELECT * FROM tb_user")
    Flowable<List<UserEntity>> getAllUsers();

    //Anotação query, com o parametro do select a ser usado
    @Query("SELECT * FROM tb_user WHERE user_uid = :id")
    Flowable<UserEntity> getUserById(int id);

    //A anotação insert possui um parametro de confligo, o usado a baixo, substitui caso a chave primaria seja a mesma.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addUser(UserEntity userEntity);
}
