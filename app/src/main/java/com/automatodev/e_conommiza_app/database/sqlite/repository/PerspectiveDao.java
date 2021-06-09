package com.automatodev.e_conommiza_app.database.sqlite.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.automatodev.e_conommiza_app.entidade.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entidade.response.PerspectiveWithData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface PerspectiveDao {

    @Query("SELECT * FROM tb_perspective WHERE user_uid = :uid")
    Flowable<List<PerspectiveEntity>> getPerspectiveByUid(String uid);

    @Query("SELECT * FROM tb_perspective WHERE id_perspective = :id")
    Single<PerspectiveEntity> getPerspectiveById(Long id);

    @Transaction
    @Query("SELECT * FROM tb_perspective WHERE user_uid = :uid")
    Flowable<List<PerspectiveWithData>> getPerspectiveWithData(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addPerspective(PerspectiveEntity perspectiveEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updatePerspective(PerspectiveEntity perspectiveEntity);

}
