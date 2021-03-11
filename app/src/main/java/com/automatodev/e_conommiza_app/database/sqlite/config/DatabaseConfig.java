package com.automatodev.e_conommiza_app.database.sqlite.config;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.automatodev.e_conommiza_app.database.sqlite.repository.DataEntryDao;
import com.automatodev.e_conommiza_app.database.sqlite.repository.PerspectiveDao;
import com.automatodev.e_conommiza_app.database.sqlite.utils.Converters;
import com.automatodev.e_conommiza_app.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.model.PerspectiveEntity;

@TypeConverters(Converters.class)
@Database(entities = {PerspectiveEntity.class, DataEntryEntity.class}, version = 2, exportSchema = false)
public abstract class DatabaseConfig extends RoomDatabase {
    private static DatabaseConfig databaseConfig;

    public static synchronized DatabaseConfig getDatabaseConfig(Context context){
        if (databaseConfig == null){
            databaseConfig = Room.databaseBuilder(context,DatabaseConfig.class,"tb_econommiza").build();
        }

        return databaseConfig;
    }


    public abstract DataEntryDao dataEntryDao();
    public abstract PerspectiveDao perspectiveDao();
}
