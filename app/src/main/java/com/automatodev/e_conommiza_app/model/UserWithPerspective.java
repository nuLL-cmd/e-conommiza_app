package com.automatodev.e_conommiza_app.model;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithPerspective {

    @Embedded
    @Relation(parentColumn = "idUser", entityColumn = "idUser")
    private UserEntity userEntity;
    private List<PerspectiveEntity> perspectives;

}
