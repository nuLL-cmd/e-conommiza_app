package com.automatodev.e_conommiza_app.entity.model;

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

        categoryEntities.add(new CategoryEntity("Amazon",R.drawable.ic_amazon));
        categoryEntities.add(new CategoryEntity("Casa",R.drawable.ic_casa));
        categoryEntities.add(new CategoryEntity("Carro",R.drawable.ic_carro));
        categoryEntities.add(new CategoryEntity("Cinema",R.drawable.ic_cinema));
        categoryEntities.add(new CategoryEntity("Despesas de casa",R.drawable.ic_despesas_casa));
        categoryEntities.add(new CategoryEntity("Estudos",R.drawable.ic_estudos));
        categoryEntities.add(new CategoryEntity("Fast-food",R.drawable.ic_lanche));
        categoryEntities.add(new CategoryEntity("Hobby",R.drawable.ic_hobby));
        categoryEntities.add(new CategoryEntity("Itaú",R.drawable.ic_itau));
        categoryEntities.add(new CategoryEntity("Inter",R.drawable.ic_inter));
        categoryEntities.add(new CategoryEntity("Mercado pago",R.drawable.ic_mercado_pago));
        categoryEntities.add(new CategoryEntity("Mercado",R.drawable.ic_mercado));
        categoryEntities.add(new CategoryEntity("Moto",R.drawable.ic_moto));
        categoryEntities.add(new CategoryEntity("Nubank",R.drawable.ic_nubank));
        categoryEntities.add(new CategoryEntity("Netflix",R.drawable.ic_netflix_logo));
        categoryEntities.add(new CategoryEntity("Oficina",R.drawable.ic_oficina));
        categoryEntities.add(new CategoryEntity("Outras entradas",R.drawable.ic_outras_entradas));
        categoryEntities.add(new CategoryEntity("Outras saídas",R.drawable.ic_outras_saidas));
        categoryEntities.add(new CategoryEntity("Restaurante",R.drawable.ic_restaurante));
        categoryEntities.add(new CategoryEntity("Saúde", R.drawable.ic_saude));
        categoryEntities.add(new CategoryEntity("Taxi",R.drawable.ic_taxi));
        categoryEntities.add(new CategoryEntity("Trabalho",R.drawable.ic_trabalho));
        categoryEntities.add(new CategoryEntity("Uber",R.drawable.ic_uber));

        return categoryEntities;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
