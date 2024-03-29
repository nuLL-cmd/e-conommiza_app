package com.automatodev.e_conommiza_app.entity.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity(tableName = "tb_perspective")
public class PerspectiveEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_perspective")
    private Long idPerspective;

    private String month;

    @ColumnInfo(name = "user_uid")
    private String userUid;

    private int year;

    @ColumnInfo(name = "total_debit")
    private BigDecimal totalDebit;

    @ColumnInfo(name = "total_credit")
    private BigDecimal totalCredit;


    @Ignore
    private List<DataEntryEntity> itemsPerspective;

    public PerspectiveEntity() {
    }

    @Ignore
    public PerspectiveEntity(Long idPerspective, String month, int year) {
        this.month = month;
        this.year = year;
        this.idPerspective = idPerspective;
    }

    @Ignore
    public PerspectiveEntity(String month, String userUid, int year, BigDecimal totalDebit, BigDecimal totalCredit, List<DataEntryEntity> itemsPerspective) {
        this.month = month;
        this.userUid = userUid;
        this.year = year;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.itemsPerspective = itemsPerspective;
    }

    @Ignore
    public PerspectiveEntity(Long idPerspective, String month, String userUid, int year, BigDecimal totalDebit, BigDecimal totalCredit, List<DataEntryEntity> itemsPerspective) {
        this.idPerspective = idPerspective;
        this.month = month;
        this.userUid = userUid;
        this.year = year;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.itemsPerspective = itemsPerspective;
    }

    public Long getIdPerspective() {
        return idPerspective;
    }

    public void setIdPerspective(Long idPerspective) {
        this.idPerspective = idPerspective;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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
