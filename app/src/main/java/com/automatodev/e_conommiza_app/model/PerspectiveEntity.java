package com.automatodev.e_conommiza_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "tb_perspective")
public class PerspectiveEntity implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "id_perspective")
    private long idPerspective;

    @ColumnInfo(name = "id_user")
    private long idUser;

    private String month;
    private int year;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    @Ignore
    private List<DataEntryEntity> itemsPerspective;

    public PerspectiveEntity(String month, int year, BigDecimal totalDebit, BigDecimal totalCredit, List<DataEntryEntity> itemsPerspective) {
        this.month = month;
        this.year = year;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.itemsPerspective = itemsPerspective;
    }

    protected PerspectiveEntity(Parcel in) {
        month = in.readString();
        year = in.readInt();
    }

    public static final Creator<PerspectiveEntity> CREATOR = new Creator<PerspectiveEntity>() {
        @Override
        public PerspectiveEntity createFromParcel(Parcel in) {
            return new PerspectiveEntity(in);
        }

        @Override
        public PerspectiveEntity[] newArray(int size) {
            return new PerspectiveEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(month);
        dest.writeInt(year);
    }
}
