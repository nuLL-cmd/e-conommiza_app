package com.automatodev.e_conommiza_app.entity.modelBuild;

import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;

import java.math.BigDecimal;
import java.util.List;

public class PerspectiveEntityBuilder {

    private Long idPerspective;
    private String month;
    private String userUid;
    private Integer year;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private List<DataEntryEntity> itemsPerspective;


    public PerspectiveEntity build(){
        return new PerspectiveEntity(this.idPerspective,this.month, this.userUid, this.year, this.totalDebit, this.totalCredit, this.itemsPerspective);
    }

    public PerspectiveEntityBuilder month(String month){
        this.month = month;
        return this;
    }


    public PerspectiveEntityBuilder userUid(String userUid){
        this.userUid = userUid;
        return this;
    }


    public PerspectiveEntityBuilder year(Integer year){
        this.year = year;
        return this;
    }


    public PerspectiveEntityBuilder totalDebit(BigDecimal totalDebit){
        this.totalDebit = totalDebit;
        return this;
    }


    public PerspectiveEntityBuilder totalCredit(BigDecimal totalCredit){
        this.totalCredit = totalCredit;
        return this;
    }

    public PerspectiveEntityBuilder itemsPerspective(List<DataEntryEntity> itemsPerspective){
        this.itemsPerspective = itemsPerspective;
        return this;
    }
    public PerspectiveEntityBuilder idPerspective(Long idPerspective){
        this.idPerspective= idPerspective;
        return this;
    }
}
