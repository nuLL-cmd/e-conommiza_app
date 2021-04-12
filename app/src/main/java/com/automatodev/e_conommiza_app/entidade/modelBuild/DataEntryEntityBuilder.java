package com.automatodev.e_conommiza_app.entidade.modelBuild;

import com.automatodev.e_conommiza_app.entidade.model.DataEntryEntity;

import java.math.BigDecimal;

public class DataEntryEntityBuilder {

    private String nameLocal;
    private String category;
    private Long idPersp;
    private Long dataEntry;
    private String typeEntry;
    private BigDecimal valueEntry;


    public DataEntryEntity build(){
        return new DataEntryEntity(this.idPersp,this.nameLocal,this.category,this.dataEntry, this.typeEntry, this.valueEntry);
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

    public DataEntryEntityBuilder typeEntry(String typeEntry){
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
}
