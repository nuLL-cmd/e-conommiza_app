package com.automatodev.e_conommiza_app.enumarator;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author Marco Aurelio
 * @date 09/06/2021
 *
 * Enum for type register
 * */
public enum TypeEnum {

    INPUT("I","ENTRY"),
    OUTPUT("O", "OUTPUT"),
    DEFAULT("D","DEFAULT");

    private final String code;
    private final String description;

    private TypeEnum(String code, String description){
        this.code = code;
        this.description = description;

        try{
            Field field = this.getClass().getSuperclass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(this, this.code);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("logx","Error TypeEnum: "+e.getMessage());
        }
    }

    public String getCode(){
        return this.code;
    }

    public String getDescription(){
        return this.description;
    }
}
