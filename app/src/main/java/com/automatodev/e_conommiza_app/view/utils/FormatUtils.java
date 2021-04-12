package com.automatodev.e_conommiza_app.view.utils;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormatUtils {
    public static String format( Long date){
        Locale locale = new Locale("pt", "br");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        return dateFormat.format(date);
    }


}
