package com.automatodev.e_conommiza_app.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.automatodev.e_conommiza_app.entidade.model.UserEntity;
import com.automatodev.e_conommiza_app.entidade.modelBuild.UserEntityBuilder;

public class UserPreferences  {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public UserPreferences(Context context, String type){
        this.context = context;
        preferences = this.context.getSharedPreferences(type, Context.MODE_PRIVATE);
    }


    public UserEntity getUser(){

        return new UserEntityBuilder()
                .userName(preferences.getString("name",""))
                .userEmail(preferences.getString("email",""))
                .userUid(preferences.getString("uid",""))
                .urlPhoto(preferences.getString("urlPhoto",""))
                .build();


    }

    public void setUser(UserEntity userEntity){
        editor = preferences.edit();
        editor.putString("name",userEntity.getUserName());
        editor.putString("uid", userEntity.getUserUid());
        editor.putString("urlPhoto", userEntity.getUrlPhoto());
        editor.putString("email", userEntity.getUserEmail());
        editor.apply();
    }

    public void updateField(String field, String value){
        editor = preferences.edit();
        editor.putString(field, value);
        editor.apply();
    }


}
