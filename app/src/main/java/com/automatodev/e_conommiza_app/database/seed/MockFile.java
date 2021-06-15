package com.automatodev.e_conommiza_app.database.seed;

import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.PerspectiveEntityBuilder;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MockFile {

    private  List<PerspectiveEntity> perspectiveEntities = new ArrayList<>();

    private  List<DataEntryEntity> dataEntryEntities= new ArrayList<>();
    private  List<DataEntryEntity> dataEntryEntitiesTwo = new ArrayList<>();
    private  List<DataEntryEntity> dataEntryEntitiesTree = new ArrayList<>();


    public  List<PerspectiveEntity> getPerspectivesMock(){

/*        dataEntryEntities.add(new DataEntryEntity(1L,"Fujioka", 1614900139724L,"entry",new BigDecimal("2800.33")));
        dataEntryEntities.add(new DataEntryEntity(1L,"McDonalds", 1614900139724L,"noEntry",new BigDecimal("35.33")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Valesi", 1614900139724L,"noEntry",new BigDecimal("48.50")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Laura", 1614900139724L,"entry",new BigDecimal("250.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Inter", 1614900139724L,"noEntry",new BigDecimal("300.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Totality", 1614900139724L,"noEntry",new BigDecimal("500.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Cinema", 1614900139724L,"noEntry",new BigDecimal("45.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Mendanha", 1614900139724L,"noEntry",new BigDecimal("250.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Venda notebook", 1614900139724L,"entry",new BigDecimal("400.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Evolução motos", 1614900139724L,"noEntry",new BigDecimal("355.33")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Combustivel", 1614900139724L,"noEntry",new BigDecimal("80.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Pizzaria padrão", 1614900139724L,"noEntry",new BigDecimal("150.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Mercado pago", 1614900139724L,"noEntry",new BigDecimal("199.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"Nubank", 1614900139724L,"noEntry",new BigDecimal("200.00")));
        dataEntryEntities.add(new DataEntryEntity(1L,"BurgerKing", 1614900139724L,"noEntry",new BigDecimal("28.00")));



        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Fujioka", 1614900139724L,"entry",new BigDecimal("2800.33")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Valesi", 1614900139724L,"noEntry",new BigDecimal("35.33")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Lanche", 1614900139724L,"noEntry",new BigDecimal("48.50")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Laura", 1614900139724L,"entry",new BigDecimal("250.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Inter", 1614900139724L,"noEntry",new BigDecimal("300.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Totality", 1614900139724L,"noEntry",new BigDecimal("500.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Compra olx", 1614900139724L,"noEntry",new BigDecimal("45.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Mendanha", 1614900139724L,"noEntry",new BigDecimal("250.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Venda cooler", 1614900139724L,"entry",new BigDecimal("400.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Revisão fazer", 1614900139724L,"noEntry",new BigDecimal("355.33")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Combustivel", 1614900139724L,"noEntry",new BigDecimal("80.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Drogaria popular", 1614900139724L,"noEntry",new BigDecimal("150.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Mercado pago", 1614900139724L,"noEntry",new BigDecimal("199.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Nubank", 1614900139724L,"noEntry",new BigDecimal("200.00")));
        dataEntryEntitiesTwo.add(new DataEntryEntity(2L,"Barao lanches", 1614900139724L,"noEntry",new BigDecimal("28.00")));



        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Fujioka", 1614900139724L,"entry",new BigDecimal("2800.33")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"DiRoma", 1614900139724L,"noEntry",new BigDecimal("35.33")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Polo", 1614900139724L,"noEntry",new BigDecimal("48.50")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Laura", 1614900139724L,"entry",new BigDecimal("250.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Inter", 1614900139724L,"noEntry",new BigDecimal("300.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Totality", 1614900139724L,"noEntry",new BigDecimal("500.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Senago", 1614900139724L,"noEntry",new BigDecimal("45.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Mendanha", 1614900139724L,"noEntry",new BigDecimal("250.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Celg", 1614900139724L,"entry",new BigDecimal("400.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Internet", 1614900139724L,"noEntry",new BigDecimal("355.33")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Fone de ouvido João", 1614900139724L,"noEntry",new BigDecimal("80.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Cinema", 1614900139724L,"noEntry",new BigDecimal("150.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Feira de quinta", 1614900139724L,"noEntry",new BigDecimal("199.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"Nubank", 1614900139724L,"noEntry",new BigDecimal("200.00")));
        dataEntryEntitiesTree.add(new DataEntryEntity(3L,"SSD olx", 1614900139724L,"noEntry",new BigDecimal("28.00")));*/


        perspectiveEntities.add(new PerspectiveEntity("Março","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1800.00"),new BigDecimal("3200.00"),dataEntryEntities));
        perspectiveEntities.add(new PerspectiveEntity("Abril","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1750.00"),new BigDecimal("3350.00"),dataEntryEntitiesTwo));
        perspectiveEntities.add(new PerspectiveEntity("Maio","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1850.00"),new BigDecimal("3250.00"),dataEntryEntitiesTree));
        perspectiveEntities.add(new PerspectiveEntity("Junho","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1900.00"),new BigDecimal("3300.00"),dataEntryEntities));
        perspectiveEntities.add(new PerspectiveEntity("Julho","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1630.00"),new BigDecimal("3400.00"),dataEntryEntitiesTwo));
        perspectiveEntities.add(new PerspectiveEntity("Agosto","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1550.00"),new BigDecimal("3150.00"),dataEntryEntitiesTree));
        perspectiveEntities.add(new PerspectiveEntity("Setembro","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1500.00"),new BigDecimal("3000.00"),dataEntryEntities));
        perspectiveEntities.add(new PerspectiveEntity("Outubro","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1600.00"),new BigDecimal("3050.00"),dataEntryEntitiesTwo));
        perspectiveEntities.add(new PerspectiveEntity("Novembro","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("1720.00"),new BigDecimal("3300.00"),dataEntryEntitiesTree));
        perspectiveEntities.add(new PerspectiveEntity("Dezembro","QgL2IBmkNcPsGuBzgkasVCY0sCI2",2021,new BigDecimal("2000.00"),new BigDecimal("3200.00"),dataEntryEntities));



        return perspectiveEntities;

    }

    public DataEntryEntity getDataEntryMock(){
        return new DataEntryEntity(1L,"Cinema","Lazer", 1614900139724L, TypeEnum.OUTPUT,new BigDecimal("150.00"),0);

    }

    public PerspectiveEntity getPerspectiveMock(){
        Locale locale = new Locale("pt", "br");
        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
        PerspectiveEntity perspectiveEntity = new PerspectiveEntityBuilder().month(month.toUpperCase())
                .year(Calendar.getInstance().get(Calendar.YEAR))
                .userUid("QgL2IBmkNcPsGuBzgkasVCY0sCI2")
                .totalCredit(new BigDecimal("0.00"))
                .totalDebit(new BigDecimal("0.00"))
                .build();


        return perspectiveEntity;
    }


}
