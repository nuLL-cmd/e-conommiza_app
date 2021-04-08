package com.automatodev.e_conommiza_app.database.sqlite.utils;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

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

}
