package com.automatodev.e_conommiza_app.database.sqlite.config;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.automatodev.e_conommiza_app.database.sqlite.utils.Converters;
import com.automatodev.e_conommiza_app.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.model.UserEntity;

@TypeConverters(Converters.class)
@Database(entities = {UserEntity.class, PerspectiveEntity.class}, version = 1, exportSchema = false)
public abstract class DatabaseConfig extends RoomDatabase {
    private static DatabaseConfig databaseConfig;

    public static synchronized DatabaseConfig getDatabaseConfig(Context context){
        if (databaseConfig == null){
            databaseConfig = Room.databaseBuilder(context,DatabaseConfig.class,"tb_econommiza").build();
        }

        return databaseConfig;
    }


}
