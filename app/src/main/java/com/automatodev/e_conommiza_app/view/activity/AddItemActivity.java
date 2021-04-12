package com.automatodev.e_conommiza_app.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.sqlite.controller.DataEntryController;
import com.automatodev.e_conommiza_app.databinding.ActivityAddItemBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogCalendarBinding;
import com.automatodev.e_conommiza_app.entidade.model.CategoryEntity;
import com.automatodev.e_conommiza_app.entidade.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.entidade.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.view.adapter.CategoryAdapter;
import com.automatodev.e_conommiza_app.view.adapter.PerspectiveSpinnerAdapter;
import com.automatodev.e_conommiza_app.view.utils.ComponentUtils;
import com.automatodev.e_conommiza_app.view.utils.FormatUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {
    private ActivityAddItemBinding binding;
    private List<PerspectiveEntity> perspectiveEntities;
    private ComponentUtils componentUtils;

    private DataEntryEntity data;

    private boolean isSelected = false;
    private boolean positive = false;
    private boolean negative = false;
    public static boolean status;

    private String nameEntry;
    private String categoryEntry;
    private String typeEntiry;
    private BigDecimal valueEntry;
    private Long dateEntry;
    private Long idPerspective;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        componentUtils = new ComponentUtils(this);
        perspectiveEntities = new ArrayList<>();
        data = new DataEntryEntity();
        getData();
        inflateSpinnerCategory();

    }

    @Override
    protected void onStart() {
        super.onStart();
        status = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ;
        status = false;
    }


    public void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            perspectiveEntities = (List<PerspectiveEntity>) bundle.getSerializable("perspects");
            inflateSpinnerPerspective(perspectiveEntities);
        } else {
            Toast.makeText(this, "Você não tem nenhuma perspectiva cadastrada.\ncadastre uma perspecitve antes para adicionar um novo regisro", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    public void showCalendar(View view) throws ParseException {
        binding.btnDateItem.setBackground(getResources().getDrawable(R.drawable.bg_button_blue_noeffects));

        LayoutDialogCalendarBinding calendarBinding = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_calendar, binding.relativeDaddyItem, false);
        AlertDialog alertCalendar = new AlertDialog.Builder(this).create();
        alertCalendar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertCalendar.setView(calendarBinding.getRoot());
        alertCalendar.show();


        DateFormat format = new SimpleDateFormat("MMMM-yyyy", new Locale("pt", "br"));
        Date date = format.parse("ABRIL-2021");
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendarBinding.calendarLayoutCalendar.setMinDate(c.getTime().getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarBinding.calendarLayoutCalendar.setMaxDate(c.getTime().getTime());


        calendarBinding.calendarLayoutCalendar.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            binding.btnDateItem.setText(FormatUtils.format(calendar.getTime().getTime()));
            dateEntry = calendar.getTime().getTime();
            alertCalendar.dismiss();

        });

    }

    public void inflateSpinnerPerspective(List<PerspectiveEntity> perspectiveEntities) {
        PerspectiveSpinnerAdapter adapter = new PerspectiveSpinnerAdapter(this, perspectiveEntities);
        binding.spinnerPerspectiveItem.setAdapter(adapter);
        binding.spinnerPerspectiveItem.setSelection(adapter.getCount() - 1);

        binding.spinnerPerspectiveItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idPerspective = perspectiveEntities.get(position).getIdPerspective();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void inflateSpinnerCategory() {
        CategoryAdapter adapter = new CategoryAdapter(this, CategoryEntity.getCategories());
        binding.spinnerCategoryItem.setAdapter(adapter);
        binding.spinnerCategoryItem.setSelection(0);

        binding.spinnerCategoryItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryEntry = CategoryEntity.getCategories().get(position).getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void setPositive(View view) {

        if (!isSelected || negative) {
            componentUtils.fadeViewEffect(new ImageButton[]{binding.btnUpItem, binding.btnDownItem},
                    new Integer[]{R.drawable.bg_button_positive_green, R.drawable.ic_up_48_fff
                            , R.drawable.bg_button_neutral, R.drawable.ic_down_48_ee0005}, 120, true);

            negative = false;
            positive = true;
            typeEntiry = "entry";
            isSelected = true;

        } else {
            componentUtils.fadeViewEffect(new ImageButton[]{binding.btnUpItem, null},
                    new Integer[]{R.drawable.bg_button_neutral, R.drawable.ic_up_48_8bc34a
                            , null, null}, 120, false);

            isSelected = false;
            positive = false;
            typeEntiry = null;


        }

    }

    public void setNegative(View view) {
        if (!isSelected || positive) {
            componentUtils.fadeViewEffect(new ImageButton[]{binding.btnDownItem, binding.btnUpItem},
                    new Integer[]{R.drawable.bg_button_negative_red, R.drawable.ic_down_48_fff
                            , R.drawable.bg_button_neutral, R.drawable.ic_up_48_8bc34a}, 120, true);

            negative = true;
            positive = false;
            typeEntiry = "exit";
            isSelected = true;

        } else {
            componentUtils.fadeViewEffect(new ImageButton[]{binding.btnDownItem, null},
                    new Integer[]{R.drawable.bg_button_neutral, R.drawable.ic_down_48_ee0005
                            , null, null}, 120, false);

            isSelected = false;
            negative = false;
            typeEntiry = null;


        }

    }

    public void saveData(View view) {
        if (validateFields()) {
            componentUtils.showSnackbar("Existem campos que serem preenchidos!",2000);
            return;
        }

        if (typeEntiry == null) {
           componentUtils.showSnackbar("Você precisa informar o tipo do registro!", 2000);

            return;
        }

        if (dateEntry == null) {
           componentUtils.showSnackbar("Necessário informar uma data para o registro",2000);
            binding.btnDateItem.setBackground(getResources().getDrawable(R.drawable.bg_button_red_noshadow_global));
            return;
        }

        nameEntry = binding.edtNameItem.getText().toString();
        valueEntry = BigDecimal.valueOf(binding.edtPriceNew.getRawValue() / 100.00);

        data.setNameLocal(nameEntry);
        data.setValueEntry(valueEntry);
        data.setTypeEntry(typeEntiry);
        data.setValueEntry(valueEntry);
        data.setCategory(categoryEntry);
        data.setIdPersp(idPerspective);
        data.setDateEntry(dateEntry);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Aguarde...");
        dialog.setCancelable(false);
        dialog.show();
        DataEntryController dataEntryController = new ViewModelProvider(this).get(DataEntryController.class);
        new CompositeDisposable().add(dataEntryController.addDataEntry(data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(800);
                                dialog.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                  componentUtils.showSnackbar("Dado inserido com sucesso", 1200);
                }));

    }

    public Boolean validateFields() {
        int count = 0;
        ComponentUtils componentUtils = new ComponentUtils(this);
        EditText fields[] = new EditText[2];
        fields[0] = binding.edtNameItem;
        fields[1] = binding.edtPriceNew;
        for (EditText e : fields) {
            if (e.getText().toString().trim().isEmpty() || e.getText().toString().equals("R$ 0,00")) {
                e.setBackgroundResource(R.drawable.bg_edt_global_error);
                componentUtils.onTextListener(e);
                count++;
            }
        }
        return count != 0;
    }

}