package com.automatodev.e_conommiza_app.view.utils;

import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormatUtils {
    public static String format( Long date){
        Locale locale = new Locale("pt", "br");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        return dateFormat.format(date);
    }

    public static String numberFormat(BigDecimal value){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(value);
    }

    public static String decimalFormat(BigDecimal value){
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(value);
    }


}
