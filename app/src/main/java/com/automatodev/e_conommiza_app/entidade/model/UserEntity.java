package com.automatodev.e_conommiza_app.entidade.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;


public class UserEntity implements Serializable {


    private String userName;
    private String userEmail;
    private String urlPhoto;
    private String userUid;

    @ServerTimestamp
    private Date dateSince;


    public UserEntity(){

    }

    public UserEntity( String userName, String userEmail, String urlPhoto, String userUid, Date dateSince) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.urlPhoto = urlPhoto;
        this.userUid = userUid;
        this.dateSince = dateSince;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public Date getDateSince() {
        return dateSince;
    }

    public void setDateSince(Date dateSince) {
        this.dateSince = dateSince;
    }
}
