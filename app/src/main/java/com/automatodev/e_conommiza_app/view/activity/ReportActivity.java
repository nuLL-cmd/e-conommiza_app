package com.automatodev.e_conommiza_app.view.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.databinding.ActivityReportBinding;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;
import com.automatodev.e_conommiza_app.utils.OperationsReport;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ReportActivity extends AppCompatActivity {

    public static boolean status;
    private ActivityReportBinding binding;
    private int[] colorsPercentCredit = new int[]{Color.parseColor("#256fff"), Color.parseColor("#00c853")};
    private int[] colorsPercentDebit = new int[]{Color.parseColor("#FF9800"), Color.parseColor("#e65100")};

    OperationsReport operationsReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        populeReport();
    }

    private void setDataPieChart(PieChart pieChart, BigDecimal currentMonth, BigDecimal beforeMonth, String percent, String type) {

        float current = (currentMonth.compareTo(new BigDecimal("0.00"))) == 0 ? 0.0f : currentMonth.floatValue();
        float before = (beforeMonth.compareTo(new BigDecimal("0.00"))) == 0 ? 0.0f : beforeMonth.floatValue();


        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(10, 0, 5, 0);

        pieChart.setDragDecelerationFrictionCoef(0.95f);


        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        pieChart.getLegend().setEnabled(false);


        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(50f);
        pieChart.setEntryLabelTextSize(12f);

        

        pieChart.setCenterText(percent);
        pieChart.setCenterTextColor(type.equals(TypeEnum.INPUT.getDescription()) ? Color.parseColor("#256fff") : Color.parseColor("#e65100"));
        pieChart.setCenterTextSize((current == 0.0f && before == 0.0f) ? 50f : 15f);


        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(current));
        entries.add(new PieEntry(before));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setValueFormatter(formatValue());
        dataSet.setDrawIcons(false);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        dataSet.setColors(type.equals(TypeEnum.INPUT.getDescription()) ? colorsPercentCredit : colorsPercentDebit);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);

        pieChart.highlightValues(null);

        pieChart.animate();

        pieChart.invalidate();

    }


    @Override
    protected void onStart() {
        super.onStart();
        status = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        status = false;
    }

    public void actReportProfile(View view) {
        NavUtils.navigateUpFromSameTask(ReportActivity.this);
    }

    private void populeReport() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<PerspectiveEntity> perspectiveEntities = (List<PerspectiveEntity>) bundle.getSerializable("data");
            if (perspectiveEntities != null) {
                operationsReport = new OperationsReport(perspectiveEntities);

                populeCardPercentInput(perspectiveEntities);
                populeCardPercentOutput(perspectiveEntities);
                populeCardPercentItems();
                populeCardAllBalance();
            }
        }
    }


    public void populeCardPercentInput(List<PerspectiveEntity> perspectiveEntities) {

        BigDecimal creditCurrentMont = new BigDecimal("0.00");
        BigDecimal creditBeforeMont = new BigDecimal("0.00");

        for (PerspectiveEntity p : perspectiveEntities) {
            if ((p.getMonth().toLowerCase().equals(operationsReport.getCurrentMonth())) && (p.getYear() == operationsReport.getYear()))
                creditCurrentMont = creditCurrentMont.add(p.getTotalCredit());

            if ((p.getMonth().toLowerCase().equals(operationsReport.getBeforeMonth())) && (p.getYear() == operationsReport.getYear())) {
                creditBeforeMont = creditBeforeMont.add(p.getTotalCredit());
            }
        }

        Map<String, String> map = operationsReport.getPercentDebitCredit("Proventos", creditCurrentMont, creditBeforeMont);

        if (map != null) {
            binding.lblPercentCarInputReport.setText(Objects.requireNonNull(map.get("title")));
            binding.txtCurrentMonthInputReport.setText(operationsReport.getCurrentMonth().substring(0, 1).toUpperCase() + operationsReport.getCurrentMonth().substring(1));
            binding.txtBeforeMonthInputReport.setText(operationsReport.getBeforeMonth().substring(0, 1).toUpperCase() + operationsReport.getBeforeMonth().substring(1));
            binding.txtCurrentMonthInputReport.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.button_positive), PorterDuff.Mode.SRC);
            binding.txtBeforeMonthInputReport.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.green_00c853), PorterDuff.Mode.SRC);

            setDataPieChart(binding.pieChartInputReport, creditCurrentMont, creditBeforeMont, Objects.requireNonNull(map.get("value")), TypeEnum.INPUT.getDescription());

        } else {

            binding.lblPercentCarInputReport.setText("0%");
            binding.txtCurrentMonthInputReport.setText(operationsReport.getCurrentMonth().substring(0, 1).toUpperCase() + operationsReport.getCurrentMonth().substring(1));
            binding.txtBeforeMonthInputReport.setText(operationsReport.getBeforeMonth().substring(0, 1).toUpperCase() + operationsReport.getBeforeMonth().substring(1));
            binding.txtCurrentMonthInputReport.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.button_positive), PorterDuff.Mode.SRC);
            binding.txtBeforeMonthInputReport.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.green_00c853), PorterDuff.Mode.SRC);

            setDataPieChart(binding.pieChartInputReport, creditCurrentMont, creditBeforeMont, "0%", TypeEnum.INPUT.getDescription());
        }
    }

    private void populeCardPercentOutput(List<PerspectiveEntity> perspectiveEntities) {

        BigDecimal debitCurrentMont = new BigDecimal("0.00");
        BigDecimal debitBeforeMont = new BigDecimal("0.00");

        for (PerspectiveEntity p : perspectiveEntities) {
            if ((p.getMonth().toLowerCase().equals(operationsReport.getCurrentMonth())) && (p.getYear() == operationsReport.getYear()))
                debitCurrentMont = debitCurrentMont.add(p.getTotalDebit());

            if ((p.getMonth().toLowerCase().equals(operationsReport.getBeforeMonth())) && (p.getYear() == operationsReport.getYear())) {
                debitBeforeMont = debitBeforeMont.add(p.getTotalDebit());
            }
        }

        Map<String, String> map = operationsReport.getPercentDebitCredit("Gastos", debitCurrentMont, debitBeforeMont);


        if (map != null) {
            binding.lblPercentCarOutputReport.setText(Objects.requireNonNull(map.get("title")));
            binding.txtCurrentMonthOutputReport.setText(operationsReport.getCurrentMonth().substring(0, 1).toUpperCase() + operationsReport.getCurrentMonth().substring(1));
            binding.txtBeforeMonthOutputReport.setText(operationsReport.getBeforeMonth().substring(0, 1).toUpperCase() + operationsReport.getBeforeMonth().substring(1));
            binding.txtCurrentMonthOutputReport.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.orange_FF9800), PorterDuff.Mode.SRC);
            binding.txtBeforeMonthOutputReport.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.red_e65100), PorterDuff.Mode.SRC);

            setDataPieChart(binding.pieChartOutputReport, debitCurrentMont, debitBeforeMont, Objects.requireNonNull(map.get("value")), TypeEnum.OUTPUT.getDescription());

        } else {

            binding.lblPercentCarOutputReport.setText("0%");
            binding.txtCurrentMonthOutputReport.setText(operationsReport.getCurrentMonth().substring(0, 1).toUpperCase() + operationsReport.getCurrentMonth().substring(1));
            binding.txtBeforeMonthOutputReport.setText(operationsReport.getBeforeMonth().substring(0, 1).toUpperCase() + operationsReport.getBeforeMonth().substring(1));
            binding.txtCurrentMonthOutputReport.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.orange_FF9800), PorterDuff.Mode.SRC);
            binding.txtBeforeMonthOutputReport.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.red_e65100), PorterDuff.Mode.SRC);

            setDataPieChart(binding.pieChartOutputReport, debitCurrentMont, debitBeforeMont, "0%", TypeEnum.OUTPUT.getDescription());
        }
    }

    private void populeCardPercentItems() {

        binding.txtPercentCreditReport.setText(operationsReport.getPercentRegister().get(0));
        binding.txtPercentDebitReport.setText(operationsReport.getPercentRegister().get(1));
    }


    private void populeCardAllBalance() {
        List<BigDecimal> allBalanceList = operationsReport.getAllBalance();

        Log.d("logx", "Total balance size: " + allBalanceList.size());

    }

    private ValueFormatter formatValue() {
        final Locale locale = new Locale("pt", "br");
        final NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return format.format(value);
            }
        };
    }


}