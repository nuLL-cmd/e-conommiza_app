package com.automatodev.e_conommiza_app.entidade.modelBuild;

import com.automatodev.e_conommiza_app.entidade.model.UserEntity;

import java.util.Date;

public class UserEntityBuilder {

    private String userName;
    private String userEmail;
    private String urlPhoto;
    private String userUid;
    private Date dateSince;


    public UserEntity build(){
        return new UserEntity( this.userName, this.userEmail, this.urlPhoto, this.userUid, this.dateSince);
    }

    public UserEntityBuilder userName(String userName){
        this.userName = userName;
        return this;
    }

    public UserEntityBuilder userEmail(String userEmail){
        this.userEmail = userEmail;
        return this;
    }

    public UserEntityBuilder urlPhoto(String urlPhoto){
        this.urlPhoto = urlPhoto;
        return this;
    }

    public UserEntityBuilder userUid(String userUid){
        this.userUid = userUid;
        return this;
    }

    public UserEntityBuilder dateSince(Date dateSince){
        this.dateSince = dateSince;
        return this;
    }


}
