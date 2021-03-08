package com.automatodev.e_conommiza_app.database.seed;

import com.automatodev.e_conommiza_app.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.model.PerspectiveEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockFile {

    private  List<PerspectiveEntity> perspectiveEntities = new ArrayList<>();

    private  List<DataEntryEntity> dataEntryEntities= new ArrayList<>();
    private  List<DataEntryEntity> dataEntryEntitiesTwo = new ArrayList<>();
    private  List<DataEntryEntity> dataEntryEntitiesTree = new ArrayList<>();


    public  List<PerspectiveEntity> getPerspectiveEntityLIst(){

        dataEntryEntities.add(new DataEntryEntity("Fujioka", 1614900139724L,"entry",new BigDecimal("2800.33")));
        dataEntryEntities.add(new DataEntryEntity("McDonalds", 1614900139724L,"noEntry",new BigDecimal("35.33")));
        dataEntryEntities.add(new DataEntryEntity("Valesi", 1614900139724L,"noEntry",new BigDecimal("48.50")));
        dataEntryEntities.add(new DataEntryEntity("Laura", 1614900139724L,"entry",new BigDecimal("250.00")));
        dataEntryEntities.add(new DataEntryEntity("Inter", 1614900139724L,"noEntry",new BigDecimal("300.00")));
        dataEntryEntities.add(new DataEntryEntity("Totality", 1614900139724L,"noEntry",new BigDecimal("500.00")));
        dataEntryEntities.add(new DataEntryEntity("Cinema", 1614900139724L,"noEntry",new BigDecimal("45.00")));
        dataEntryEntities.add(new DataEntryEntity("Mendanha", 1614900139724L,"noEntry",new BigDecimal("250.00")));
        dataEntryEntities.add(new DataEntryEntity("Venda notebook", 1614900139724L,"entry",new BigDecimal("400.00")));
        dataEntryEntities.add(new DataEntryEntity("Evolução motos", 1614900139724L,"noEntry",new BigDecimal("355.33")));
        dataEntryEntities.add(new DataEntryEntity("Combustivel", 1614900139724L,"noEntry",new BigDecimal("80.00")));
        dataEntryEntities.add(new DataEntryEntity("Pizzaria padrão", 1614900139724L,"noEntry",new BigDecimal("150.00")));
        dataEntryEntities.add(new DataEntryEntity("Mercado pago", 1614900139724L,"noEntry",new BigDecimal("199.00")));
        dataEntryEntities.add(new DataEntryEntity("Nubank", 1614900139724L,"noEntry",new BigDecimal("200.00")));
        dataEntryEntities.add(new DataEntryEntity("BurgerKing", 1614900139724L,"noEntry",new BigDecimal("28.00")));



        dataEntryEntitiesTwo.add(new DataEntryEntity("Fujioka", 1614900139724L,"entry",new BigDecimal("2800.33")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Valesi", 1614900139724L,"noEntry",new BigDecimal("35.33")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Lanche", 1614900139724L,"noEntry",new BigDecimal("48.50")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Laura", 1614900139724L,"entry",new BigDecimal("250.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Inter", 1614900139724L,"noEntry",new BigDecimal("300.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Totality", 1614900139724L,"noEntry",new BigDecimal("500.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Compra olx", 1614900139724L,"noEntry",new BigDecimal("45.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Mendanha", 1614900139724L,"noEntry",new BigDecimal("250.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Venda cooler", 1614900139724L,"entry",new BigDecimal("400.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Revisão fazer", 1614900139724L,"noEntry",new BigDecimal("355.33")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Combustivel", 1614900139724L,"noEntry",new BigDecimal("80.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Drogaria popular", 1614900139724L,"noEntry",new BigDecimal("150.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Mercado pago", 1614900139724L,"noEntry",new BigDecimal("199.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Nubank", 1614900139724L,"noEntry",new BigDecimal("200.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity("Barao lanches", 1614900139724L,"noEntry",new BigDecimal("28.00")));



        dataEntryEntitiesTree.add(new DataEntryEntity("Fujioka", 1614900139724L,"entry",new BigDecimal("2800.33")));
        dataEntryEntitiesTree.add(new DataEntryEntity("DiRoma", 1614900139724L,"noEntry",new BigDecimal("35.33")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Polo", 1614900139724L,"noEntry",new BigDecimal("48.50")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Laura", 1614900139724L,"entry",new BigDecimal("250.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Inter", 1614900139724L,"noEntry",new BigDecimal("300.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Totality", 1614900139724L,"noEntry",new BigDecimal("500.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Senago", 1614900139724L,"noEntry",new BigDecimal("45.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Mendanha", 1614900139724L,"noEntry",new BigDecimal("250.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Celg", 1614900139724L,"entry",new BigDecimal("400.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Internet", 1614900139724L,"noEntry",new BigDecimal("355.33")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Fone de ouvido João", 1614900139724L,"noEntry",new BigDecimal("80.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Cinema", 1614900139724L,"noEntry",new BigDecimal("150.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Feira de quinta", 1614900139724L,"noEntry",new BigDecimal("199.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("Nubank", 1614900139724L,"noEntry",new BigDecimal("200.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity("SSD olx", 1614900139724L,"noEntry",new BigDecimal("28.00")));


        perspectiveEntities.add(new PerspectiveEntity("Março",2021,new BigDecimal("1800.00"),new BigDecimal("3200.00"),dataEntryEntities));
        perspectiveEntities.add(new PerspectiveEntity("Abril",2021,new BigDecimal("1750.00"),new BigDecimal("3350.00"),dataEntryEntitiesTwo));
        perspectiveEntities.add(new PerspectiveEntity("Maio",2021,new BigDecimal("1850.00"),new BigDecimal("3250.00"),dataEntryEntitiesTree));
        perspectiveEntities.add(new PerspectiveEntity("Junho",2021,new BigDecimal("1900.00"),new BigDecimal("3300.00"),dataEntryEntities));
        perspectiveEntities.add(new PerspectiveEntity("Julho",2021,new BigDecimal("1630.00"),new BigDecimal("3400.00"),dataEntryEntitiesTwo));
        perspectiveEntities.add(new PerspectiveEntity("Agosto",2021,new BigDecimal("1550.00"),new BigDecimal("3150.00"),dataEntryEntitiesTree));
        perspectiveEntities.add(new PerspectiveEntity("Setembro",2021,new BigDecimal("1500.00"),new BigDecimal("3000.00"),dataEntryEntities));
        perspectiveEntities.add(new PerspectiveEntity("Outubro",2021,new BigDecimal("1600.00"),new BigDecimal("3050.00"),dataEntryEntitiesTwo));
        perspectiveEntities.add(new PerspectiveEntity("Novembro",2021,new BigDecimal("1720.00"),new BigDecimal("3300.00"),dataEntryEntitiesTree));
        perspectiveEntities.add(new PerspectiveEntity("Dezembro",2021,new BigDecimal("2000.00"),new BigDecimal("3200.00"),dataEntryEntities));



        return perspectiveEntities;

    }

}
