package com.automatodev.e_conommiza_app.entity.modelBuild;

import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;

import java.math.BigDecimal;

public class DataEntryEntityBuilder {

    private String nameLocal;
    private String category;
    private Long idPersp;
    private Long dataEntry;
    private TypeEnum typeEntry;
    private BigDecimal valueEntry;
    private Integer payment;


    public DataEntryEntity build(){
        return new DataEntryEntity(this.idPersp,this.nameLocal,this.category,this.dataEntry, this.typeEntry, this.valueEntry, this.payment);
    }


    public DataEntryEntityBuilder nameLocal(String nameLocal){
        this.nameLocal = nameLocal;
        return this;
    }


    public DataEntryEntityBuilder idPersp(Long idPerspective){
        this.idPersp = idPerspective;
        return this;
    }


    public DataEntryEntityBuilder dataEntry(Long dataEntry){
        this.dataEntry = dataEntry;
        return this;
    }

    public DataEntryEntityBuilder typeEntry(TypeEnum typeEntry){
        this.typeEntry = typeEntry;
        return this;
    }

    public DataEntryEntityBuilder valueEntry(BigDecimal valueEntry){
        this.valueEntry = valueEntry;
        return this;
    }

    public DataEntryEntityBuilder category(String category){
        this.category = category;
        return this;
    }

    public DataEntryEntityBuilder category(Integer payment){
        this.payment = payment;
        return this;
    }
}
