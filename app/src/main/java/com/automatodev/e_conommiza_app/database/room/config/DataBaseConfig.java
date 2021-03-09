package com.automatodev.e_conommiza_app.database.room.config;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.automatodev.e_conommiza_app.database.room.repository.UserRepository;
import com.automatodev.e_conommiza_app.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.model.UserEntity;
import com.automatodev.e_conommiza_app.utils.Converters;


/*
    Classe de configuração do banco de dados room database
    A classe é do tipo abstrata e extende de RoomDatabase, a cima possui a anotação @Database que possui os seguintes parametros:
    * entities = Um array de classes envolvidas na persistencia (são as classes que representam as tabelas do banco)
    * version = Versão do banco de dados.
    * exportSchema = Ainda não sei tenho que verificar ao certo o que é, porem coloco false
 */

//Classe converter que possui a conversão de um objeto para um tipo aceitavel no room, pois a biblioteca do rom não aceita objetos na persistencia.
@TypeConverters(Converters.class)

//Informações do banco de dados(Classes que serão as tabelas, versão e exportação de schema)
@Database(entities = {UserEntity.class, PerspectiveEntity.class}, version = 1, exportSchema = false)

public abstract class DataBaseConfig extends RoomDatabase {

    //Declaração de um objeto statico do tipo DatabaseConfig (a propria classe)
    private static DataBaseConfig dataBaseConfig;

    //Metodo statico synchronized que recebe o contexo para a inicialização do room database
    public static synchronized DataBaseConfig getDataBaseConfig(Context context) {
        if (dataBaseConfig == null)
            dataBaseConfig = Room.databaseBuilder(context, DataBaseConfig.class, "db_econommiza").build();

        return dataBaseConfig;
    }


    //Metodo abstrato representado por uma interface que contem as operações do banco
    public abstract UserRepository userDao();
}
