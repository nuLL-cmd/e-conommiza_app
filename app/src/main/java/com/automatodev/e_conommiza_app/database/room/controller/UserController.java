package com.automatodev.e_conommiza_app.database.room.controller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.DatabaseConfiguration;

import com.automatodev.e_conommiza_app.database.room.config.DataBaseConfig;
import com.automatodev.e_conommiza_app.model.UserEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

//Classe controller que contem os metodos de chamada direta ao banco de dados
public class UserController extends AndroidViewModel {
    private DataBaseConfig dataBaseConfig;

    //Configuração inicial do banco pela classe DataBaseConfig que neste projeto é a classe de configuração do banco de dados.
    public UserController(@NonNull Application application){
        super(application);
        this.dataBaseConfig = DataBaseConfig.getDataBaseConfig(application);

    }

    //Metodos de acesso direto as operações do banco de dados
    public Flowable<List<UserEntity>> getAllUsers(){
        return dataBaseConfig.userDao().getAllUsers();
    }

    //Metodos de acesso direto as operações do banco de dados
    public Flowable<UserEntity> getUserById(int id){
        return dataBaseConfig.userDao().getUserById(id);
    }

    //Metodos de acesso direto as operações do banco de dados
    public Completable addUser(UserEntity userEntity){
        return dataBaseConfig.userDao().addUser(userEntity);
    }


}
