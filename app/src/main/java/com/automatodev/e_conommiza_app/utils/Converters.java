package com.automatodev.e_conommiza_app.utils;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

//Classe para conversão de tipos, pois a biblioteca de persistencia do room, não aceita Objetos como propriedades nas classes de modelo.
//É necessario dois metodos para cada conversão de tipo
public class Converters {

    //Anotação typeConverter, referencia que estes metodos serão usados na conversão de tipo da classe pojo
    @TypeConverter
    public static BigDecimal fromLong(Long value) {
        return value == null ? null : new BigDecimal(value).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    //Anotação typeConverter, referencia que estes metodos serão usados na conversão de tipo da classe pojo
    @TypeConverter
    public static Long fromBigDecimal(BigDecimal value) {
        return value == null ? null : value.multiply(new BigDecimal(100)).longValueExact();
    }

}
