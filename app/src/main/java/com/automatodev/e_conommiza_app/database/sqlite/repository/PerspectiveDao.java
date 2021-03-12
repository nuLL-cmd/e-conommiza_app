package com.automatodev.e_conommiza_app.database.sqlite.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.automatodev.e_conommiza_app.model.PerspectiveEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface PerspectiveDao {

    @Query("SELECT * FROM tb_perspective WHERE user_uid = :uid")
    Flowable<List<PerspectiveEntity>> getPerspectiveById(String uid);

    @Query("SELECT * FROM tb_perspective")
    Flowable<List<PerspectiveEntity>> getAllPerspectives();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addPerspective(PerspectiveEntity perspectiveEntity);


}
