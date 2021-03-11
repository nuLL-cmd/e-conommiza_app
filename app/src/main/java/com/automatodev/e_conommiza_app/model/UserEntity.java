package com.automatodev.e_conommiza_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;


public class UserEntity implements Serializable {


    private int idUser;


    private String userName;


    private String userEmail;


    private String urlPhoto;


    private String userUid;

    @ServerTimestamp
    private Date dateSince;


    protected UserEntity(Parcel in) {
        idUser = in.readInt();
        userName = in.readString();
        userEmail = in.readString();
        urlPhoto = in.readString();
        userUid = in.readString();
        dateSince = new Date((in.readLong()));
    }

    public UserEntity(){

    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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
