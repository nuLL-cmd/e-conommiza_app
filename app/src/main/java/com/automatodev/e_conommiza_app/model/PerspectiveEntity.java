package com.automatodev.e_conommiza_app.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class PerspectiveEntity {

    private String namePespective;
    private BigDecimal totalPerspective;
    private BigDecimal totaldDebit;
    private BigDecimal totalCredit;

    private List<DataEntryEntity> itemsPerspective;



}
