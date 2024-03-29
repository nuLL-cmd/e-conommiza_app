package com.automatodev.e_conommiza_app.database.sqlite.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;

import io.reactivex.Completable;

@Dao
public interface DataEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addDataEntry(DataEntryEntity dataEntryEntity);

    @Delete
     Completable deleteDataEntry(DataEntryEntity dataEntry);

}
