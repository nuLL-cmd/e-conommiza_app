package com.automatodev.e_conommiza_app.model;

import java.math.BigDecimal;


public class DataEntryEntity {
    private String nameLocal;
    private Long dateEntry;
    private String typeEntry;
    private BigDecimal valueEntry;

    public DataEntryEntity(String nameLocal, Long dateEntry, String typeEntry, BigDecimal valueEntry) {
        this.nameLocal = nameLocal;
        this.dateEntry = dateEntry;
        this.typeEntry = typeEntry;
        this.valueEntry = valueEntry;
    }

    public String getNameLocal() {
        return nameLocal;
    }

    public void setNameLocal(String nameLocal) {
        this.nameLocal = nameLocal;
    }

    public Long getDateEntry() {
        return dateEntry;
    }

    public void setDateEntry(Long dateEntry) {
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
