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
import java.util.stream.Collectors;

public class OperationsReport {

    private final List<PerspectiveEntity> perspectiveEntities;
    private int countDebit = 0;
    private int countCredit = 0;
    private int countFrozen = 0;
    private int countPay = 0;
    private int countTotal = 0;

    public OperationsReport(List<PerspectiveEntity> perspectiveEntities) {
        this.perspectiveEntities = perspectiveEntities;
    }

    public Map<String, String> getPercentDebitCredit(String output, BigDecimal currentValue, BigDecimal beforeValue) {
        Map<String, String> map = new HashMap<>();
        String txtResult;

        BigDecimal total = new BigDecimal("0.00");

        if ((currentValue.compareTo(new BigDecimal("0.00")) > 0) && (beforeValue.compareTo(new BigDecimal("0.00")) > 0)) {

            if (currentValue.compareTo(beforeValue) > 0) {

                total = total.add(currentValue.subtract(beforeValue));
                double percent = (total.doubleValue() / beforeValue.doubleValue()) * 100;
                txtResult = output + " a mais em relação ao último mês";
                map.put("title", txtResult);
                map.put("value", FormatUtils.percentFormatDouble(percent, true));
                return map;

            } else if (currentValue.compareTo(beforeValue) < 0) {

                total = total.add(beforeValue.subtract(currentValue));
                double percent = (total.doubleValue() / beforeValue.doubleValue()) * 100;
                txtResult = output + " a menos em relação ao último mês";
                map.put("title", txtResult);
                map.put("value", FormatUtils.percentFormatDouble(percent, true));
                return map;

            } else {
                txtResult = output + " em relação ao último mês";
                map.put("title", txtResult);
                map.put("value", FormatUtils.percentFormatDouble((double) 0, true));
                return map;
            }

        } else if ((currentValue.compareTo(new BigDecimal("0.00")) > 0) && (beforeValue.compareTo(new BigDecimal("0.00")) == 0)) {

            txtResult = output + " a mais em relação ao último mês";
            map.put("title", txtResult);
            map.put("value", FormatUtils.percentFormatDouble((double) 100, true));
            return map;

        } else if ((currentValue.compareTo(new BigDecimal("0.00")) == 0) && (beforeValue.compareTo(new BigDecimal("0.00")) > 0)) {

            txtResult = output + " a menos em relação ao último mês";
            map.put("title", txtResult);
            map.put("value", FormatUtils.percentFormatDouble((double) 100, true));
            return map;
        } else {

            txtResult = output + " em relação ao último mês";
            map.put("title", txtResult);
            map.put("value", FormatUtils.percentFormatDouble((double) 0, true));

            return map;
        }

    }

    public List<Float> getPercentRegister() {

        this.perspectiveEntities.stream().forEach(p -> countTotal += p.getItemsPerspective().size());
        this.perspectiveEntities.stream().forEach(p -> countDebit += p.getItemsPerspective().stream().filter(pers -> pers.getTypeEntry().equals(TypeEnum.OUTPUT)).count());
        this.perspectiveEntities.stream().forEach(p -> countCredit += p.getItemsPerspective().stream().filter(pers -> pers.getTypeEntry().equals(TypeEnum.INPUT)).count());
        this.perspectiveEntities.stream().forEach(p -> countFrozen += p.getItemsPerspective().stream().filter(pers -> pers.getPayment().equals(2)).count());
        this.perspectiveEntities.stream().forEach(p -> countPay += p.getItemsPerspective().stream().filter(pers -> pers.getPayment().equals(1)).count());


        List<Float> percents = new ArrayList<>();

        double percentDebit = countTotal != 0 ? (double) (countDebit * 100) / (double) countTotal : 0.0;
        double percentCredit = countTotal != 0 ? (double) (countCredit * 100) / (double) countTotal : 0.0;
        double percentFrozent = countTotal != 0 ? (double) (countFrozen * 100) / (double) countTotal : 0.0;
        double percentPay = countTotal != 0 ? (double) (countPay * 100) / (double) countTotal : 0.0;
        percents.add((float) percentCredit);
        percents.add((float) percentDebit);
        percents.add((float) percentFrozent);
        percents.add((float) percentPay);

        return percents;
    }

    public String getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "br"));
        if (month != null) {
            month = month.toLowerCase();
            return month;
        } else {

            return "N/A";
        }

    }

    public List<String> getNamePerspectives() {

        return perspectiveEntities.stream().map(perspectiveEntity -> perspectiveEntity.getMonth()).collect(Collectors.toList());
    }

    public String getBeforeMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "br"));
        if (month != null) {
            month = month.toLowerCase();
            return month;
        } else {

            return "N/A";
        }

    }

    public int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }


    public Map<String, Object> getAllBalance() {

        Map<String, Object> map = new HashMap<>();

        List<String> perspectives;
        List<BigDecimal> allBalance;


        allBalance = this.perspectiveEntities.stream().filter(p -> (p.getTotalCredit().subtract(p.getTotalDebit())
                .compareTo(new BigDecimal("0.00")) > 0)).map(p -> p.getTotalCredit().subtract(p.getTotalDebit()))
                .collect(Collectors.toList());

        perspectives = this.perspectiveEntities.stream().filter(p -> (p.getTotalCredit().subtract(p.getTotalDebit())
                .compareTo(new BigDecimal("0.00")) > 0))
                .map(PerspectiveEntity::getMonth).collect(Collectors.toList());

        map.put("perspectives",perspectives);
        map.put("values",allBalance);

        return map;
    }

}
