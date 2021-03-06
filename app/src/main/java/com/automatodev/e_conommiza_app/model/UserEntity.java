package com.automatodev.e_conommiza_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(tableName = "users")
public class UserEntity implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "id_user")
    private long idUser;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "user_email")
    private String userEmail;

    @ColumnInfo(name = "url_photo")
    private String urlPhoto;

    @ColumnInfo(name = "user_uid")
    private String userUid;

    @Ignore
    @ServerTimestamp
    private Date dateSince;


    protected UserEntity(Parcel in) {
        idUser = in.readLong();
        userName = in.readString();
        userEmail = in.readString();
        urlPhoto = in.readString();
        userUid = in.readString();
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
        dest.writeLong(idUser);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(urlPhoto);
        dest.writeString(userUid);
        dest.writeLong(dateSince.getTime());
    }
}
