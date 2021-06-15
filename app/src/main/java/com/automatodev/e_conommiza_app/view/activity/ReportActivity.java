package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.View;

import com.automatodev.e_conommiza_app.databinding.ActivityReportBinding;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;
import com.automatodev.e_conommiza_app.utils.FormatUtils;
import com.automatodev.e_conommiza_app.utils.OperationsReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ReportActivity extends AppCompatActivity {

    public static boolean status;
    private ActivityReportBinding binding;

    OperationsReport operationsReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        populeReport();
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

    public void populeReport() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<PerspectiveEntity> perspectiveEntities = (List<PerspectiveEntity>) bundle.getSerializable("data");
            if (perspectiveEntities != null) {
                operationsReport = new OperationsReport(perspectiveEntities);

                populeCardPercentInput(perspectiveEntities);
                populeCardPercentOutput(perspectiveEntities);
                populeCardPercentItems(perspectiveEntities);
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
        if (map != null){
            binding.lblPercentCardOneReport.setText(Objects.requireNonNull(map.get("title")));
            binding.txtPercentCardOneReport.setText(Objects.requireNonNull(map.get("value")));

        }
    }

    public void populeCardPercentOutput(List<PerspectiveEntity> perspectiveEntities) {

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
        if (map != null){
            binding.lblPercentCardTwoReport.setText(Objects.requireNonNull(map.get("title")));
            binding.txtPercentCardTwoReport.setText(Objects.requireNonNull(map.get("value")));

        }
    }

    public void populeCardPercentItems(List<PerspectiveEntity> perspectiveEntities){

        binding.txtPercentCreditReport.setText(operationsReport.getPercentRegister().get(0));
        binding.txtPercentDebitReport.setText(operationsReport.getPercentRegister().get(1));
    }




}