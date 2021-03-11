package com.automatodev.e_conommiza_app.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;


@Entity(tableName = "tb_data_entry")
public class DataEntryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_data")
    private int idData;

    @ColumnInfo(name = "name_local")
    private String nameLocal;

    @ColumnInfo(name = "data_entry")
    private long dateEntry;

    @ColumnInfo(name = "type_entry")
    private String typeEntry;

    @ColumnInfo(name = "value_entry")
    private BigDecimal valueEntry;

    public DataEntryEntity(String nameLocal, long dateEntry, String typeEntry, BigDecimal valueEntry) {
        this.nameLocal = nameLocal;
        this.dateEntry = dateEntry;
        this.typeEntry = typeEntry;
        this.valueEntry = valueEntry;
    }

    public int getIdData() {
        return idData;
    }

    public void setIdData(int idData) {
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
