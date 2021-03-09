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


@Entity(tableName = "tb_perspective")
public class PerspectiveEntity implements Parcelable{

    @PrimaryKey
    @ColumnInfo(name = "id_perspective")
    private long idPerspective;

    @ColumnInfo(name = "id_user")
    private long idUser;

    private String month;
    private int year;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;

    public PerspectiveEntity() {
    }

    @Ignore
    private List<DataEntryEntity> itemsPerspective;

    public PerspectiveEntity(String month, int year, BigDecimal totalDebit, BigDecimal totalCredit, List<DataEntryEntity> itemsPerspective) {
        this.month = month;
        this.year = year;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.itemsPerspective = itemsPerspective;
    }

    public PerspectiveEntity(){}


    protected PerspectiveEntity(Parcel in) {
        idPerspective = in.readLong();
        idUser = in.readLong();
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

    public long getIdPerspective() {
        return idPerspective;
    }

    public void setIdPerspective(long idPerspective) {
        this.idPerspective = idPerspective;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public List<DataEntryEntity> getItemsPerspective() {
        return itemsPerspective;
    }

    public void setItemsPerspective(List<DataEntryEntity> itemsPerspective) {
        this.itemsPerspective = itemsPerspective;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idPerspective);
        dest.writeLong(idUser);
        dest.writeString(month);
        dest.writeInt(year);
    }

    public long getIdPerspective() {
        return idPerspective;
    }

    public void setIdPerspective(long idPerspective) {
        this.idPerspective = idPerspective;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public List<DataEntryEntity> getItemsPerspective() {
        return itemsPerspective;
    }

    public void setItemsPerspective(List<DataEntryEntity> itemsPerspective) {
        this.itemsPerspective = itemsPerspective;
    }
}
