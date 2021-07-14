package com.automatodev.e_conommiza_app.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.sqlite.controller.DataEntryController;
import com.automatodev.e_conommiza_app.database.sqlite.controller.PerspectiveController;
import com.automatodev.e_conommiza_app.databinding.ActivityAddItemBinding;
import com.automatodev.e_conommiza_app.entity.model.CategoryEntity;
import com.automatodev.e_conommiza_app.entity.model.DataEntryEntity;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.enumarator.TypeEnum;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;
import com.automatodev.e_conommiza_app.utils.DrawableHelper;
import com.automatodev.e_conommiza_app.utils.FormatUtils;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private ActivityAddItemBinding binding;
    private ComponentUtils componentUtils;
    private DataEntryEntity data;
    private PerspectiveEntity perspectiveEntity;
    private CategoryEntity categoryEntity;

    private boolean isSelected = false;
    private boolean positive = false;
    private boolean negative = false;
    public static int resourceCategory = 0;
    public static String nameCategory;
    public static boolean status;

    String typeIntent;
    private String perspectiveDate;
    private String nameEntry;
    private String categoryEntry;
    private TypeEnum typeEntry;
    private Integer payment;
    private BigDecimal valueEntry;
    private Long dateEntry;
    private Long idPerspective;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnBackItem.getBackground().setColorFilter(ContextCompat.getColor(this, android.R.color.transparent), PorterDuff.Mode.SRC);

        componentUtils = new ComponentUtils(this);

        getData();
        ;


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
        resourceCategory = 0;
        nameCategory = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resourceCategory != 0) {
            binding.txtCategoryItem.setText(nameCategory);
            categoryEntity = new CategoryEntity(nameCategory,resourceCategory);
            binding.imageItemItem.setImageResource(resourceCategory);
            categoryEntry = nameCategory;
            if (typeEntry != null && resourceCategory != R.drawable.ic_uber){
                checkResourceChangeColor(categoryEntity.getImage(),
                        typeEntry.equals(TypeEnum.INPUT) ? Color.parseColor("#00c853") : Color.parseColor("#e65100"));
            }


        }
    }

    public void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            typeIntent = bundle.getString("typeIntent");
            perspectiveEntity = (PerspectiveEntity) bundle.getSerializable("perspective");
            data = (DataEntryEntity) bundle.getSerializable("data");

            if (typeIntent.equals("edit") && data != null) {
                binding.txtWindowItem.setText("Editar item");

                if (perspectiveEntity != null && data != null)
                    populeData(data);

            } else if (perspectiveEntity != null) {
                data = new DataEntryEntity();
                idPerspective = perspectiveEntity.getIdPerspective();
                perspectiveDate = perspectiveEntity.getMonth() + " / " + perspectiveEntity.getYear();
                payment = 0;
                binding.txtPerspectiveItem.setText(perspectiveDate);
            } else {
                Toast.makeText(this, "Tivemos um problema ao carregar os dados.\nReinicie o app e tente novamente.", Toast.LENGTH_LONG).show();
                finish();
            }

        } else {
            Toast.makeText(this, "Você não tem nenhuma perspectiva cadastrada.\ncadastre uma perspecitve antes para adicionar um novo regisro", Toast.LENGTH_LONG).show();
            finish();
        }

    }


    private void populeData(DataEntryEntity data) {
        if (data != null) {
            idPerspective = perspectiveEntity.getIdPerspective();
            perspectiveDate = perspectiveEntity.getMonth() + " / " + perspectiveEntity.getYear();
            dateEntry = data.getDateEntry();
            typeEntry = data.getTypeEntry();
            categoryEntry = data.getCategory();
            nameEntry = data.getNameLocal();
            valueEntry = data.getValueEntry();
            payment = data.getPayment();
            binding.txtPerspectiveItem.setText(perspectiveDate);
            binding.edtNameItem.setText(nameEntry);
            binding.edtPriceNew.setText(String.valueOf(data.getValueEntry()));

            categoryEntity = CategoryEntity.getCategories().stream()
                    .filter(category -> category.getName().equals(categoryEntry))
                    .findFirst().orElse(null);

            if (categoryEntity != null) {
                binding.txtCategoryItem.setText(categoryEntity.getName());
                binding.imageItemItem.setImageResource(categoryEntity.getImage());
            }

            if (typeEntry.getCode().equals(TypeEnum.INPUT.toString())) {

                componentUtils.stateColorComponent(new View[]{
                        binding.getRoot(), binding.btnUpItem, binding.btnDownItem,
                        binding.appbarItem, binding.imageItemItem
                }, new Integer[]{R.color.green_00c853, R.drawable.ic_up_48_fff, R.drawable.ic_down_48_ee0005, R.drawable.bg_edt_green}, false);

                checkResourceChangeColor(categoryEntity.getImage(), Color.parseColor("#00c853"));

                perspectiveEntity.setTotalCredit(perspectiveEntity.getTotalCredit().subtract(valueEntry));
                isSelected = true;
                positive = true;

            } else {
                componentUtils.stateColorComponent(new View[]{
                        binding.getRoot(), binding.btnDownItem, binding.btnUpItem,
                        binding.appbarItem, binding.imageItemItem
                }, new Integer[]{R.color.red_e65100, R.drawable.ic_down_48_fff, R.drawable.ic_up_48_8bc34a, R.drawable.bg_edt_orange}, false);

                checkResourceChangeColor(categoryEntity.getImage(), Color.parseColor("#e65100"));
                perspectiveEntity.setTotalDebit(perspectiveEntity.getTotalDebit().subtract(valueEntry));
                isSelected = true;
                negative = true;
            }

            binding.btnDateItem.setText(FormatUtils.format(dateEntry));

        }
    }


    public void showCalendar(View view) throws ParseException {


        DateFormat format = new SimpleDateFormat("MMMM / yyyy", new Locale("pt", "br"));
        Date date = format.parse(perspectiveDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        DatePickerDialog dateDialog = new DatePickerDialog(this, R.style.DatePickerDefaultTheme, this,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


        if (dateEntry != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(dateEntry));
            dateDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        }

        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        dateDialog.getDatePicker().setMinDate(c.getTime().getTime());
        dateDialog.show();

    }


    public void setPositive(View view) {

        if (!isSelected || negative) {

            componentUtils.stateColorComponent(new View[]{
                    binding.getRoot(), binding.btnUpItem, binding.btnDownItem,
                    binding.appbarItem, binding.imageItemItem
            }, new Integer[]{R.color.green_00c853, R.drawable.ic_up_48_fff, R.drawable.ic_down_48_ee0005, R.drawable.bg_edt_green}, false);

            if (categoryEntity != null)
                checkResourceChangeColor(categoryEntity.getImage(), Color.parseColor("#00c853"));

            negative = false;
            positive = true;
            typeEntry = TypeEnum.INPUT;
            isSelected = true;

        } else {
            componentUtils.stateColorComponent(new View[]{binding.btnUpItem
                    , binding.appbarItem, binding.imageItemItem}, new Integer[]{R.drawable.ic_up_48_8bc34a}, true);

            if (categoryEntity != null)
                checkResourceChangeColor(categoryEntity.getImage(), Color.parseColor("#256fff"));

            isSelected = false;
            positive = false;
            typeEntry = null;
        }


    }

    public void setNegative(View view) {
        if (!isSelected || positive) {

            componentUtils.stateColorComponent(new View[]{
                    binding.getRoot(), binding.btnDownItem, binding.btnUpItem,
                    binding.appbarItem, binding.imageItemItem
            }, new Integer[]{R.color.red_e65100, R.drawable.ic_down_48_fff, R.drawable.ic_up_48_8bc34a, R.drawable.bg_edt_orange}, false);

            if (categoryEntity != null)
                checkResourceChangeColor(categoryEntity.getImage(), Color.parseColor("#e65100"));

            negative = true;
            positive = false;
            typeEntry = TypeEnum.OUTPUT;
            isSelected = true;

        } else {
            componentUtils.stateColorComponent(new View[]{binding.btnDownItem
                    , binding.appbarItem, binding.imageItemItem}, new Integer[]{R.drawable.ic_down_48_ee0005}, true);

            if (categoryEntity != null)
                checkResourceChangeColor(categoryEntity.getImage(), Color.parseColor("#256fff"));

            isSelected = false;
            negative = false;
            typeEntry = null;

        }

    }

    public void saveData(View view) {


        if (validateFields()) {
            componentUtils.showSnackbar("Existem campos que serem preenchidos!", 800);
            return;
        }

        if (typeEntry == null) {
            componentUtils.showSnackbar("Você precisa informar o tipo do registro!", 800);
            binding.appbarItem.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.orange_f68059), PorterDuff.Mode.SRC);
            getWindow().setStatusBarColor(getResources().getColor(R.color.orange_f68059));


            return;
        }

        if (dateEntry == null) {
            componentUtils.showSnackbar("Informe uma data para o registro", 800);
            return;
        }

        if (categoryEntry == null) {
            componentUtils.showSnackbar("O registro precisa de uma categoria", 800);
            return;
        }

        nameEntry = binding.edtNameItem.getText().toString();
        valueEntry = BigDecimal.valueOf(binding.edtPriceNew.getRawValue() / 100.00);

        data.setNameLocal(nameEntry);
        data.setValueEntry(valueEntry);
        data.setTypeEntry(typeEntry);
        data.setCategory(categoryEntry);
        data.setIdPersp(idPerspective);
        data.setDateEntry(dateEntry);
        data.setPayment(payment);

        if (typeEntry.getCode().equals(TypeEnum.INPUT.toString()))
            perspectiveEntity.setTotalCredit(perspectiveEntity.getTotalCredit().add(valueEntry));
        else
            perspectiveEntity.setTotalDebit(perspectiveEntity.getTotalDebit().add(valueEntry));

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Aguarde...");
        dialog.setCancelable(false);
        dialog.show();
        DataEntryController dataEntryController = new ViewModelProvider(this).get(DataEntryController.class);
        new CompositeDisposable().add(dataEntryController.addDataEntry(data).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    PerspectiveController perspectiveController = new ViewModelProvider(this).get(PerspectiveController.class);
                    new CompositeDisposable().add(perspectiveController.updatePerspective(perspectiveEntity).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(500);
                                            dialog.dismiss();
                                            finish();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }));

                }));

    }

    public Boolean validateFields() {
        int count = 0;
        ComponentUtils componentUtils = new ComponentUtils(this);
        EditText fields[] = new EditText[2];
        fields[0] = binding.edtNameItem;
        fields[1] = binding.edtPriceNew;
        for (EditText e : fields) {
            if (e.getText().toString().trim().isEmpty() || e.getText().toString().equals("R$ 0,00") || e.getText().toString().equals("$0.00")) {
                e.setBackgroundResource(R.drawable.bg_edt_global_error);
                componentUtils.onTextListener(e);
                count++;
            }
        }
        return count != 0;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        binding.btnDateItem.setText(FormatUtils.format(calendar.getTime().getTime()));
        dateEntry = calendar.getTime().getTime();
    }

    public void actAddItemsCategory(View viw) {
        if (!CategoryActivity.status) {
            Intent intent = new Intent(this, CategoryActivity.class);
            if (typeEntry != null)
                intent.putExtra("color", typeEntry.equals(TypeEnum.INPUT) ? R.color.green_00c853 : R.color.red_e65100);
            binding.edtPriceNew.clearFocus();
            startActivity(intent);
        }

    }

    public void actAddItemMain(View view) {
        NavUtils.navigateUpFromSameTask(AddItemActivity.this);
    }


    public void checkResourceChangeColor(int resource, int newColor) {

        VectorChildFinder vector = new VectorChildFinder(this, resource, binding.imageItemItem);

        VectorDrawableCompat.VFullPath path = vector.findPathByName("modify");
        if (path != null) {
            path.setFillColor(newColor);
            path.setFillAlpha(1f);

        }

    }


}