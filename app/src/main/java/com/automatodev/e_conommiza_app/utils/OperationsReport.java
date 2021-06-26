package com.automatodev.e_conommiza_app.utils;

import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OperationsReport {

    private final List<PerspectiveEntity> perspectiveEntities;
    private int countDebit = 0;
    private int countCredit = 0;
    private int countTotal = 0;

    public OperationsReport(List<PerspectiveEntity> perspectiveEntities){
        this.perspectiveEntities = perspectiveEntities;
    }

    public Map<String, String> getPercentDebitCredit(String output, BigDecimal currentValue, BigDecimal beforeValue){
        Map<String, String> map = new HashMap<>();
        String txtResult;

        BigDecimal total = new BigDecimal("0.00");

        if ((currentValue.compareTo(new BigDecimal("0.00")) > 0) && (beforeValue.compareTo(new BigDecimal("0.00")) > 0)) {

            if (currentValue.compareTo(beforeValue) > 0) {

                total = total.add(currentValue.subtract(beforeValue));
                double percent = (total.doubleValue() / beforeValue.doubleValue()) * 100;
                txtResult = output + " a mais em relação ao último mês";
                map.put("title",txtResult );
                map.put("value",FormatUtils.percentFormat(percent, true));
                return  map;

            } else if (currentValue.compareTo(beforeValue) < 0) {

                total = total.add(beforeValue.subtract(currentValue));
                double percent = (total.doubleValue() / beforeValue.doubleValue()) * 100;
                txtResult = output + " a menos em relação ao último mês";
                map.put("title",txtResult );
                map.put("value",FormatUtils.percentFormat(percent, true));
                return  map;

            } else {
                txtResult = output + " em relação ao último mês";
                map.put("title",txtResult );
                map.put("value",FormatUtils.percentFormat((double)0, true));
                return  map;
            }

        } else if ((currentValue.compareTo(new BigDecimal("0.00")) > 0) && (beforeValue.compareTo(new BigDecimal("0.00")) == 0)) {

            txtResult = output + " a mais em relação ao último mês";
            map.put("title",txtResult );
            map.put("value",FormatUtils.percentFormat((double)100, true));
            return  map;

        } else if ((currentValue.compareTo(new BigDecimal("0.00")) == 0) && (beforeValue.compareTo(new BigDecimal("0.00")) > 0)) {

            txtResult = output + " a menos em relação ao último mês";
            map.put("title",txtResult );
            map.put("value",FormatUtils.percentFormat((double)100, true));
            return  map;
        } else {

            txtResult = output + " em relação ao último mês";
            map.put("title",txtResult );
            map.put("value",FormatUtils.percentFormat((double)0, true));
            return  map;
        }

    }

    public List<String> getPercentRegister() {

        this.perspectiveEntities.stream().forEach(p -> countDebit += p.getItemsPerspective().stream().filter(pers -> pers.getTypeEntry().equals(TypeEnum.OUTPUT)).count());
        this.perspectiveEntities.stream().forEach(p -> countCredit += p.getItemsPerspective().stream().filter(pers -> pers.getTypeEntry().equals(TypeEnum.INPUT)).count());
        this.perspectiveEntities.stream().forEach(p -> countTotal += p.getItemsPerspective().size());

        List<String> percents = new ArrayList<>();

        Double percentDebit = (double) (countDebit * 100) / (double) countTotal;
        Double percentCredit = (double) (countCredit * 100) / (double) countTotal;
        percents.add(FormatUtils.percentFormat(percentCredit, false));
        percents.add(FormatUtils.percentFormat(percentDebit,false));

        return percents;
    }

    public String getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "br"));
        if (month != null) {
            month = month.toLowerCase();
            return month;
        } else {

            return "nothing";
        }

    }

    public String getBeforeMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "br"));
        if (month != null) {
            month = month.toLowerCase();
            return month;
        } else {

            return "nothing";
        }

    }

    public int getYear(){
        return  Calendar.getInstance().get(Calendar.YEAR);
    }



}
