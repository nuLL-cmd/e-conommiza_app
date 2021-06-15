package com.automatodev.e_conommiza_app.database.sqlite.utils;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.automatodev.e_conommiza_app.enumarator.TypeEnum;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author Marco Aurelio
 *
 * Classe que converte um bigdecimal para long afim de ser gravado no banco, e um long vindo do banco para bigdecimal na aplicação
 * isso se deve pois não é possivel guardar bigdecimal no room database
 * */
public class Converters {
    @TypeConverter
    public static BigDecimal fromLong(Long value)
    {
        return value == null ? null : new BigDecimal(value).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    @TypeConverter
    public static Long fromBigDecimal(BigDecimal value)
    {
        return value == null ? null : value.multiply(new BigDecimal(100)).longValueExact();
    }

    @TypeConverter
    public static String fromStringApplication(TypeEnum type){
        return type.getCode();
    }

    @TypeConverter
    public static TypeEnum fromStringDatabase(String type){

        return TypeEnum.valueOf(type);
    }

}
