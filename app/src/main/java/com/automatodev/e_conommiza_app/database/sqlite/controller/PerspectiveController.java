package com.automatodev.e_conommiza_app.database.sqlite.controller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.automatodev.e_conommiza_app.database.sqlite.config.DatabaseConfig;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entity.response.PerspectiveWithData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class PerspectiveController extends AndroidViewModel {
    private final DatabaseConfig databaseConfig;

    public PerspectiveController(@NonNull Application application) {
        super(application);

        databaseConfig = DatabaseConfig.getDatabaseConfig(application);
    }

    public Flowable<List<PerspectiveEntity>> getPerspectiveByUid(String uid){
        return databaseConfig.perspectiveDao().getPerspectiveByUid(uid);
    }

    public Single<PerspectiveEntity> getPerspectiveById(Long id){
        return databaseConfig.perspectiveDao().getPerspectiveById(id);
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
