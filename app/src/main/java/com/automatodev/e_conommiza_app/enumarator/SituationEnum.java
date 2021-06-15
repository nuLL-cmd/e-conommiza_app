package com.automatodev.e_conommiza_app.enumarator;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author Marco Aurelioi
 * @date  09/06/2021
 *
 * Enum for situations of registers
 */
public enum SituationEnum {

    PAY("P","PAY"),
    BLANK("B","BLANK"),
    FROZEN("F", "FROZEN");

    private final String code;
    private final String description;

    private SituationEnum(String code, String description){
        this.code = code;
        this.description = description;

        try{
            Field field = this.getClass().getSuperclass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(this, this.code);
        }catch(Exception e){
            e.printStackTrace();
            Log.e("logx","Error SituationEnum: "+e.getMessage());
        }
    }

    public String getDescription(){
        return this.description;
    }

    public String getCode(){
        return this.code;
    }
}
