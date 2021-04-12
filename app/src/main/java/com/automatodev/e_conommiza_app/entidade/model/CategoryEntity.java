package com.automatodev.e_conommiza_app.entidade.model;

import com.automatodev.e_conommiza_app.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryEntity implements Serializable {

    private int image;
    private String name;


    public CategoryEntity(String name, int image){
        this.name = name;
        this.image = image;
    }

    public static List<CategoryEntity> getCategories(){
        List<CategoryEntity> categoryEntities = new ArrayList<>();

        categoryEntities.add(new CategoryEntity("Saude", R.drawable.ic_medicine));
        categoryEntities.add(new CategoryEntity("Lanche",R.drawable.ic_food_24));
        categoryEntities.add(new CategoryEntity("Restaurante",R.drawable.ic_restaurant_24));
        categoryEntities.add(new CategoryEntity("Mercado",R.drawable.ic_market_24));
        categoryEntities.add(new CategoryEntity("Lazer",R.drawable.ic_play_24));
        categoryEntities.add(new CategoryEntity("Oficina",R.drawable.ic_workshop_24));
        categoryEntities.add(new CategoryEntity("Carro",R.drawable.ic_car_24));
        categoryEntities.add(new CategoryEntity("Moto",R.drawable.ic_bike_24));
        categoryEntities.add(new CategoryEntity("Taxi",R.drawable.ic_taxi_24));
        categoryEntities.add(new CategoryEntity("Office",R.drawable.ic_work_24));
        categoryEntities.add(new CategoryEntity("Hobby",R.drawable.ic_hobby_24));
        categoryEntities.add(new CategoryEntity("Estudos",R.drawable.ic_school_24));
        categoryEntities.add(new CategoryEntity("Diversos",R.drawable.ic_other_24));


        return categoryEntities;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
