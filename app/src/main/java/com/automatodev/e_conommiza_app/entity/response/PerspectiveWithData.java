package com.automatodev.e_conommiza_app.entity.response;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;

import java.util.List;

public class PerspectiveWithData {

    @Embedded
    public PerspectiveEntity perspectiveEntity;

    @Relation(parentColumn = "id_perspective",entityColumn = "id_persp")
    public List<DataEntryEntity> dataEntryEntities;
}
