package com.automatodev.e_conommiza_app.view.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
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
import com.automatodev.e_conommiza_app.utils.CustomMarkerView;
import com.automatodev.e_conommiza_app.utils.FormatUtils;
import com.automatodev.e_conommiza_app.utils.OperationsReport;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class ReportActivity extends AppCompatActivity {

    public static boolean status;
    private int indexMaxValue;
    private ActivityReportBinding binding;
    private final int[] colorsPercentCredit = new int[]{Color.parseColor("#256fff"), Color.parseColor("#00c853")};
    private final int[] colorsPercentDebit = new int[]{Color.parseColor("#FF9800"), Color.parseColor("#e65100")};

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

        if (type.equals(TypeEnum.INPUT.getDescription()))
            pieChart.animateY(1400, Easing.EaseInOutQuad);
        else
            pieChart.animateX(1400, Easing.EaseInOutQuad);

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


        dataSet.setValueFormatter(formatValue("cash"));
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

    private void setDataHorizontalBarChart(HorizontalBarChart horizontalBarChart, List<Float> percents) {

        float percentCredit = percents.get(0);
        float percentDebit = percents.get(1);
        float percentFrozen = percents.get(2);
        float percentPay = percents.get(3);

        binding.txtInputReport.setText("Proventos " + FormatUtils.percentFormatFloat(percentCredit, true));
        binding.txtOutputReport.setText("Despesas " + FormatUtils.percentFormatFloat(percentDebit, true));
        binding.txtFrozenReport.setText("Congelados " + FormatUtils.percentFormatFloat(percentFrozen, true));
        binding.txtPayReport.setText("Pagos " + FormatUtils.percentFormatFloat(percentPay, true));


        horizontalBarChart.getDescription().setText("");
        horizontalBarChart.getLegend().setEnabled(false);
        horizontalBarChart.setPinchZoom(false);
        horizontalBarChart.setDoubleTapToZoomEnabled(false);
        horizontalBarChart.setDrawValueAboveBar(false);
        horizontalBarChart.setTouchEnabled(false);
        horizontalBarChart.setDragEnabled(false);
        horizontalBarChart.setScaleEnabled(false);
        horizontalBarChart.setScaleXEnabled(false);
        horizontalBarChart.setScaleYEnabled(false);


        XAxis xAxis = horizontalBarChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(4);
        xAxis.setValueFormatter(formatValuString(new String[]{"", "", "", ""}));


        YAxis yLeft = horizontalBarChart.getAxisLeft();
        yLeft.setAxisMaximum(100f);
        yLeft.setAxisMinimum(0f);
        yLeft.setEnabled(false);


        YAxis yRight = horizontalBarChart.getAxisRight();
        yRight.setDrawAxisLine(true);
        yRight.setDrawGridLines(false);
        yRight.setEnabled(false);


        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, percentPay));
        entries.add(new BarEntry(1f, percentFrozen));
        entries.add(new BarEntry(2f, percentDebit));
        entries.add(new BarEntry(3f, percentCredit));


        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setBarShadowColor(Color.argb(40, 150, 150, 150));
        dataSet.setValueFormatter(formatValue("percent"));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(15f);
        dataSet.setColors(Color.parseColor("#256fff"), Color.parseColor("#ffca28"), Color.parseColor("#e65100"), Color.parseColor("#00c853"));

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.8f);


        horizontalBarChart.setData(data);
        horizontalBarChart.setDrawBarShadow(true);
        horizontalBarChart.invalidate();
        horizontalBarChart.setDrawValueAboveBar(false);
        horizontalBarChart.animateY(2500);

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

        setDataHorizontalBarChart(binding.horizontalBarChartReport, operationsReport.getPercentRegister());
    }


    private void populeCardAllBalance() {

        operationsReport.getAllBalance();
        List<String> allPerspectives = (List<String>) operationsReport.getAllBalance().get("perspectives");
        List<BigDecimal> allBalance = (List<BigDecimal>) operationsReport.getAllBalance().get("values");

        assert allBalance != null;
        setDataLineChart(allBalance, allPerspectives);
    }

    private ValueFormatter formatValue(String type) {
        final Locale locale = new Locale("pt", "br");
        final NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (type.equals("cash"))
                    return format.format(value);
                else
                    return "";
            }
        };
    }


    private ValueFormatter formatValuString(String[] values) {
        final Locale locale = new Locale("pt", "br");
        final NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return values[(int) value];
            }
        };
    }

    public void setDataLineChart(List<BigDecimal> balances, List<String> months) {

        /*
         * Lista do tipo Entry que recebe um entry passando como prametro os valores do eixo X e eixo y.
         * O parametro no eixo x é o que define a linha tenua do grafico (LineChart)
         * */
        List<Entry> entries = new ArrayList<>();

        if (balances.size() != 0) {
            for (int i = 0; i < balances.size(); i++) {
                entries.add(new Entry(i, balances.get(i).floatValue()));
            }

            /*
             * Configurações de dados do eixo x - Os comentários estão respectivos as linhas de código
             *
             *  1 - Inicia o eixo X
             *  2 - Dados(labels) do eixo X no Bottom do grafico
             *  3 - Seta a cor dos dados(labels) do eixo X
             *  4 - Tanho do texto(labels) do eixo X
             *  5 - Margem lateral do texto(labels)
             *  6 - Margem de altura do text(labels)
             *  7 - Habilita ou desabilita as informaçoes ou configurações para este eixo.
             *  8 - desabilita as linhas na vertical do gráfico
             *  9 - Espessura das linhas dos eixos do gráfico
             *  10 -
             *  11 -
             *  12 - Seta um ValueFormater personalizado para este eixo
             *
             * */

            /*1*/
            XAxis xAxis = binding.chart.getXAxis();
            /*2*/
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            /*3*/
            xAxis.setTextColor(Color.parseColor("#256fff"));
            /*4*/
            xAxis.setTextSize(10);
            /*5*/
            xAxis.setYOffset(10f);
            /*6*/
            xAxis.setXOffset(-10f);
            /*7*/
            xAxis.setEnabled(false);
            /*8*/
            //xAxis.setDrawGridLines(false);
            /*9*/
            xAxis.setAxisLineWidth(1);
            /*10*/
            xAxis.setGranularity(1f);
            /*11*/
            xAxis.setGridColor(Color.WHITE);
            /*12*/
            //xAxis.setValueFormatter(formatData(mxData));


            /*
             * Configurações de dados do eixo y direito - Os comentários estão respectivos as linhas de código
             *
             *  1 - Inicia o eixo Y do lado direito do gráfico
             *  2 - Desabilita a visualização dos dados no eixo Y do lado direito do gráfico
             *  3 -
             *
             * */

            /*1*/
            YAxis yr = binding.chart.getAxisRight();
            /*2*/
            yr.setEnabled(false);
            /*3*/
            yr.setGranularity(1f);

            /*
             * Configurações de dados do eixo y esquerdo - Os comentários estão respectivos as linhas de código
             *
             *  1 - Inicia o eixo Y do lado esquerdo do grafico
             *  2 - Desabilita a visualização dos dados no eixo Y do lado direito do gráfico.
             *
             * */

            /*1*/
            YAxis yl = binding.chart.getAxisLeft();
            /*2*/
            yl.setEnabled(false);


            /*
             * Configurações do dataset - Os comentários estão respectivos as linhas de código
             *
             *  1 - Incicia um novo LineDataSet passando a lista de Entrys e uma label para o grafico
             *  2 - Preenchimento de cor dentro / a baixo da linha tenua do gráfico
             *  3 - Ativa a linha tenua serrilhada
             *  4 - Valores do eixo Y em cada nó da linha tenua do gráfico
             *  5 - Cor do texto do valor em cada nó da linha tenua do gráfico
             *  6 - Tamanho do texto do valor em cada nó da linha tenua do gráfico
             *  7 - Seta um ValueFormatter para formatar o valor em cada nó da linha tenua do gráfico
             *  8 - Espessura da linha em forma de cruz  que linka os dados do eixo y ao eixo x
             *  9 - Cor da linha em fomra de cruz do gráfico
             *  10 - Cor da bolinha exibida dentro do nó na linha tenua do gráfico
             *  11 - Diametro de cada nó da linha tenua do gráfico
             *  12 - Diametro da bolinha dentro do nó na linha tenua do gráfico
             *  13 - Cor de cada nó da linha tenua do gráfico
             *  14 - Parameetriza o tipo de curva que o gráfico fara em cada nó encontrado
             *  15 - Para o CUBIC (tipo de curva do gráfico) parametriza o arredondamento da curva do gráfico - quanto maior mais redondinho
             *  16 - Cor da linha tenua e da label do gráfico
             *  17 -  Gradiente (preenchimento) dentro / abaixo da linha tenua do gráfico
             *  18 - Espessura da linha tenua do gráfico
             * */

            /*1*/
            LineDataSet lineDataSet = new LineDataSet(entries, "Saldo das perspectivas");
            /*2*/
            lineDataSet.setDrawFilled(true);
            /*3*/
            //lineDataSet.enableDashedLine(0, 18, 0); //linha tenua serrilhada
            /*4*/
            lineDataSet.setDrawValues(false);
            /*5*/
            lineDataSet.setValueTextColor(Color.parseColor("#5A5A5A"));
            /*6*/
            lineDataSet.setValueTextSize(8);
            /*7*/
            //lineDataSet.setValueFormatter(formatValue());
            /*8*/
            lineDataSet.setHighlightLineWidth(0.4f);
            /*9*/
            lineDataSet.setHighLightColor(Color.parseColor("#1E88E5"));
            /*10*/
            lineDataSet.setCircleHoleColor(Color.parseColor("#ffffff"));
            /*11*/
            lineDataSet.setCircleRadius(4f);
            /*12*/
            lineDataSet.setCircleHoleRadius(2f);
            /*13*/
            lineDataSet.setCircleColor(Color.parseColor("#00c853"));
            /*14*/
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            /*15*/
            lineDataSet.setCubicIntensity(0.2f);
            /*16*/
            lineDataSet.setColor(Color.parseColor("#1E88E5")); //Cor da linha tenua e da label do grafico
            /*17*/
            lineDataSet.setFillDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{Color.parseColor("#00c853"), Color.parseColor("#5EC187"), Color.parseColor("#00FA5544")}));
            /*18*/
            lineDataSet.setLineWidth(2);


            /*
             * Configurações do Data (LineData) - Os comentários estão respectivos as linhas de código
             *
             *  1 - Incicia um novo LineData
             *  2 - seta o dataSet pelo método addDataSet passando o lineDataSet como parâmetro
             *
             * */

            /*1*/
            LineData lineData = new LineData();
            /*2*/
            lineData.addDataSet(lineDataSet);


            /*
             * Configurações do Chart (Gráfico) - Os comentários estão respectivos as linhas de código
             *
             *  1 - Seta o lineData no binding.chart(grafico)
             *  2 - Animação sentido X da linha tenua do gráfico passando (tempo em milesegundos)
             *  3 - Desabilita o double toque para zoom do gráfico
             *  4 - Inicia / atualiza o gráfico
             *  5 - Seta um texto para NoDataText do gráfico
             *  6 - Seta uma cor de texto para NoDataText do gráfico
             *  7 - Desabilita o zoom total do gráfico
             *  8 - Desabilita a descrição do gráfico
             *  9 - Remove as bordas do gráfico
             *  10 - Habilita / desabilita o toque no gráfico (pode usar o setOnClickListener() para colocar ações no toque)
             *  11 - Configura um zoon no gráfico,  neste caso apenas no eixo X
             *  12 - MarginBottom da label do gráfico
             *  13 - Desabilita ou habilita a legenda
             * */

            /*1*/
            binding.chart.setData(lineData);
            /*2*/
            binding.chart.animateX(1000);
            /*3*/
            binding.chart.setDoubleTapToZoomEnabled(false);
            /*4*/
            binding.chart.invalidate();
            /*5*/
            binding.chart.setNoDataText("Não há dados a serem exibidos!");
            /*6*/
            binding.chart.setNoDataTextColor(R.color.gray_CDCDCD);
            /*7*/
            binding.chart.setScaleEnabled(true);
            /*8*/
            binding.chart.getDescription().setEnabled(false);
            /*9*/
            binding.chart.setDrawBorders(false);
            /*10*/
            binding.chart.setTouchEnabled(true);
            /*11*/
            binding.chart.zoom(1f, 1f, 1f, 1f);
            /*12*/
            binding.chart.setExtraBottomOffset(10f);
            /*13*/
            binding.chart.getLegend().setEnabled(false);

            /*
             * Configurações um MarkerView personalizado
             *
             *  1 - Incicia o markerVIew com seus parâmetros de context, layout, lista de texto para legenda do dado.
             *  2 - Configura o gráfico (chart) no markerView
             *  3 - Configura o markerView como ativo
             *  4 - Configura o markerVIew no gráfico (chart)
             * */

            /*1*/
            CustomMarkerView mk = new CustomMarkerView(this, R.layout.layout_marker_view, months);
            /*2*/
            mk.setChartView(binding.chart);
            /*3*/
            mk.setActivated(true);
            /*4*/
            binding.chart.setMarker(mk);


            /*
            *  Traz o index do maior valor da lista de balanços atravez da variavel indexMaxValue
            * */
            IntStream.range(0, balances.size()).boxed().max(Comparator.comparing(balances::get)).ifPresent(index -> indexMaxValue = index);

            /*
            *
            * Mostra o markerView programaticamente na posição de maior valor da lista
            *
            * */
            //
            float x = binding.chart.getData().getDataSets().get(0).getEntryForIndex(indexMaxValue).getX();
            float y = binding.chart.getData().getDataSets().get(0).getEntryForIndex(indexMaxValue).getY();
            binding.chart.highlightValue(x, y, 0, true);
        }

        binding.chart.invalidate();
        binding.chart.setNoDataText("Não há dados a serem exibidos!");
        binding.chart.setNoDataTextColor(R.color.color_gray_5A5A5A);

    }


}