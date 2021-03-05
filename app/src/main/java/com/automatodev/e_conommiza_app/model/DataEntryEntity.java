package com.automatodev.e_conommiza_app.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter @AllArgsConstructor
public class DataEntryEntity {
    private String nameLocal;
    private Long dateEntry;
    private String typeEntry;
    private BigDecimal valueEntry;
}
