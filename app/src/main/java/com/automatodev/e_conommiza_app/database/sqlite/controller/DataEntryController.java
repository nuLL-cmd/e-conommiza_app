package com.automatodev.e_conommiza_app.database.sqlite.controller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.automatodev.e_conommiza_app.database.sqlite.config.DatabaseConfig;
import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;

import io.reactivex.Completable;

public class DataEntryController extends AndroidViewModel {

    private DatabaseConfig databaseConfig;

    public DataEntryController(@NonNull Application application) {
        super(application);

        databaseConfig = DatabaseConfig.getDatabaseConfig(application);
    }

    public Completable addDataEntry(DataEntryEntity dataEntryEntity){
        return databaseConfig.dataEntryDao().addDataEntry(dataEntryEntity);
    }
}
