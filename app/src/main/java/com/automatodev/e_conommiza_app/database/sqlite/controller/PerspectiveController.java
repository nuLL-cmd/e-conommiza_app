package com.automatodev.e_conommiza_app.database.sqlite.controller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.automatodev.e_conommiza_app.database.sqlite.config.DatabaseConfig;
import com.automatodev.e_conommiza_app.entidade.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entidade.response.PerspectiveWithData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class PerspectiveController extends AndroidViewModel {
    private DatabaseConfig databaseConfig;
    public PerspectiveController(@NonNull Application application) {
        super(application);

        databaseConfig = DatabaseConfig.getDatabaseConfig(application);
    }

    public Flowable<List<PerspectiveEntity>> getPerspectiveById(String uid){
        return databaseConfig.perspectiveDao().getPerspectiveById(uid);
    }

    public Flowable<List<PerspectiveEntity>> getAllPerspectives(){
        return databaseConfig.perspectiveDao().getAllPerspectives();
    }

    public Completable addPerspective(PerspectiveEntity perspectiveEntity){
        return databaseConfig.perspectiveDao().addPerspective(perspectiveEntity);
    }


    public Flowable<List<PerspectiveWithData>> getPerspectiveWithData(String uid){
        return databaseConfig.perspectiveDao().getPerspectiveWithData(uid);
    }

    public Completable updatePerspective(PerspectiveEntity perspectiveEntity){
        return databaseConfig.perspectiveDao().updatePerspective(perspectiveEntity);
    }


}
