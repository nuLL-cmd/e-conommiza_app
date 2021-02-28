package com.automatodev.e_conommiza_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserEntity implements Parcelable {

    private String userName;
    private String email;
    private String urlPhoto;
    @ServerTimestamp
    private Date dateSince;


    protected UserEntity(Parcel in) {
        userName = in.readString();
        email = in.readString();
        urlPhoto = in.readString();
        dateSince = new Date((in.readLong()));
    }

    public UserEntity(){

    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(urlPhoto);
        dest.writeLong(dateSince.getTime());
    }
}
