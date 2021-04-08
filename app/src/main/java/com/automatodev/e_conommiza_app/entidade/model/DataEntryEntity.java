package com.automatodev.e_conommiza_app.entidade.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;


@Entity(tableName = "tb_data_entry")
public class DataEntryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_data")
    private Integer idData;

    @ColumnInfo(name =  "id_persp")
    private Long idPersp;

    @ColumnInfo(name = "name_local")
    private String nameLocal;

    @ColumnInfo(name = "data_entry")
    private long dateEntry;

    @ColumnInfo(name = "type_entry")
    private String typeEntry;

    @ColumnInfo(name = "value_entry")
    private BigDecimal valueEntry;


    public DataEntryEntity(Long idPerspective,String nameLocal, long dateEntry, String typeEntry, BigDecimal valueEntry) {
        this.idPersp = idPerspective;
        this.nameLocal = nameLocal;
        this.dateEntry = dateEntry;
        this.typeEntry = typeEntry;
        this.valueEntry = valueEntry;
    }


    public DataEntryEntity() {
    }

    public Long getIdPersp() {
        return idPersp;
    }

    public void setIdPersp(Long idPersp) {
        this.idPersp = idPersp;
    }

    public Integer getIdData() {
        return idData;
    }

    public void setIdData(Integer idData) {
        this.idData = idData;
    }

    public String getNameLocal() {
        return nameLocal;
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal = nameLocal;
    }

    public long getDateEntry() {
        return dateEntry;
    }

    public void setDateEntry(long dateEntry) {
        this.dateEntry = dateEntry;
    }

    public String getTypeEntry() {
        return typeEntry;
    }

    public void setTypeEntry(String typeEntry) {
        this.typeEntry = typeEntry;
    }

    public BigDecimal getValueEntry() {
        return valueEntry;
    }

    public void setValueEntry(BigDecimal valueEntry) {
        this.valueEntry = valueEntry;
    }
}
