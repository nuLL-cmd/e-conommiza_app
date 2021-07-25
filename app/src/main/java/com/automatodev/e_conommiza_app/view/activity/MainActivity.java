package com.automatodev.e_conommiza_app.view.activity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreGetCallback;
import com.automatodev.e_conommiza_app.database.firebase.FirestoreService;
import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.database.sqlite.controller.PerspectiveController;
import com.automatodev.e_conommiza_app.databinding.ActivityMainBinding;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.PerspectiveEntityBuilder;
import com.automatodev.e_conommiza_app.entity.modelBuild.UserEntityBuilder;
import com.automatodev.e_conommiza_app.entity.response.PerspectiveWithData;
import com.automatodev.e_conommiza_app.preferences.UserPreferences;
import com.automatodev.e_conommiza_app.security.FirebaseAuthentication;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;
import com.automatodev.e_conommiza_app.utils.FormatUtils;
import com.automatodev.e_conommiza_app.view.adapter.FragmentPageAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.firestore.DocumentSnapshot;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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


@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static boolean status;
    public static boolean refresh;
    private boolean isShow = false;
    private boolean goToActualPerspective = true;

    private CompositeDisposable disposable;
    public  List<PerspectiveEntity> perspectiveEntities;
    private List<PerspectiveEntity> perspectiveList;
    private ActivityMainBinding binding;
    private FirestoreService firestoreService;
    private FirebaseAuthentication auth;
    private UserEntity userEntity;
    private FragmentPageAdapter fragmentAdapter;
    private ComponentUtils componentUtils;
    private Calendar c;
    private PerspectiveController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        componentUtils = new ComponentUtils(this);
        perspectiveList = new ArrayList<>();
        perspectiveEntities = new ArrayList<>();
        disposable = new CompositeDisposable();
        controller = new ViewModelProvider(this).get(PerspectiveController.class);
        fragmentAdapter = new FragmentPageAdapter( getSupportFragmentManager(), 0, perspectiveEntities);

        binding.viewPagerMain.setAdapter(fragmentAdapter);

        binding.setIsLoading(true);

        getUser();

    }


    @Override
    protected void onPause() {
        super.onPause();
        super.onPause();
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


    public void actMainProfile(View view) {

        if (!ProfileActivity.status) {
            Intent intent = new Intent(this, ProfileActivity.class);

            startActivity(intent);
            binding.menu.close(true);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void actMainItem(View view) {
        if (perspectiveEntities.size() == 0) {
            componentUtils.showSnackbar("Cadastre uma perspecitva antes de adicionar um novo regisro", 2000);
            return;
        }

        if (!AddItemActivity.status) {
            try {
                int position = binding.viewPagerMain.getCurrentItem();
                PerspectiveEntity perspectiveEntity = new PerspectiveEntityBuilder()
                        .idPerspective(perspectiveEntities.get(position).getIdPerspective())
                        .totalDebit(perspectiveEntities.get(position).getTotalDebit())
                        .totalCredit(perspectiveEntities.get(position).getTotalCredit())
                        .userUid(perspectiveEntities.get(position).getUserUid())
                        .month(perspectiveEntities.get(position).getMonth())
                        .year(perspectiveEntities.get(position).getYear())
                        .build();
                Intent intent = new Intent(this, AddItemActivity.class);
                intent.putExtra("perspective", perspectiveEntity);
                intent.putExtra("typeIntent", "new");
                startActivity(intent);
                binding.menu.close(true);
            } catch (Exception e) {
                componentUtils.showSnackbar("Dados ainda não carregados, aguarde sua conexão", 2000);
            }

        }

    }

    public void showData(String uid) {
        try {
            disposable.add(controller.getPerspectiveWithData(uid)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(dataList -> {

                        if (!isShow) {
                            binding.txtValueBalanceMain.animate().setDuration(300).alpha(0f).start();
                            binding.spinktBalanceMain.setVisibility(View.VISIBLE);
                        }


                        perspectiveEntities.clear();
                        perspectiveList.clear();

                        for (PerspectiveWithData p : dataList) {
                            PerspectiveEntity perspectiveEntity = p.perspectiveEntity;
                            perspectiveEntity.setItemsPerspective(p.dataEntryEntities);
                            perspectiveEntities.add(perspectiveEntity);
                            perspectiveList.add(new PerspectiveEntity(perspectiveEntity.getIdPerspective(), perspectiveEntity.getMonth(), perspectiveEntity.getYear()));

                        }


                        fragmentAdapter.notifyDataSetChanged();
                        int positionTest = getPosition(perspectiveEntities);

                        if (perspectiveEntities.size() == 0) {

                            binding.txtAmountPerspectiveMain.setText("Não há perspectivas");
                            binding.relativeNoContentMain.setVisibility(View.VISIBLE);
                            calculeBalance();

                        } else {

                            if (goToActualPerspective) {
                                binding.relativeNoContentMain.setVisibility(View.GONE);
                                binding.viewPagerMain.setCurrentItem(positionTest, true);
                                goToActualPerspective = false;
                                setDataViewPager(positionTest);

                            }else
                                setDataViewPager(binding.viewPagerMain.getCurrentItem());



                            calculeBalance();

                        }

                        binding.viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                setDataViewPager(position);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

                        binding.setIsLoading(false);

                    }));
        } catch (Exception e) {

            fragmentAdapter.notifyDataSetChanged();
            binding.txtPerspectiveMain.setText("Problema ao \ncarregar as perspectivas");
            binding.txtCreditMain.setVisibility(View.GONE);
            binding.txtDebitMain.setVisibility(View.GONE);
            binding.txtAmountPerspectiveMain.setText("Tivemos um erro ao processar os dados.");

        }


    }

    public void getUser() {
        auth = new FirebaseAuthentication(this);
        String uid = auth.getUser().getUid();
        UserPreferences preferences = new UserPreferences(this, "user");
        userEntity = preferences.getUser();

        if (userEntity.getUserUid().equals(uid)) {
            binding.txtUserMain.setText(userEntity.getUserName());
            stateRefreshUserName();
            showData(userEntity.getUserUid());

        } else {
            firestoreService = new FirestoreService();
            try {
                firestoreService.getUser(uid, new FirestoreGetCallback() {
                    @Override
                    public void onSuccess(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                userEntity = snapshot.toObject(UserEntity.class);
                                preferences.setUser(userEntity);
                                binding.txtUserMain.setText(userEntity.getUserName());
                                showData(userEntity.getUserUid());
                                stateRefreshUserName();
                            }else{

                                UserEntity userEntity = new UserEntityBuilder()
                                        .userName(auth.getUser().getDisplayName())
                                        .userEmail(auth.getUser().getEmail())
                                        .urlPhoto(auth.getUser().getPhotoUrl().toString())
                                        .userUid(auth.getUser().getUid())
                                        .build();

                                MainActivity.this.userEntity.setUserUid(auth.getUser().getUid());

                                if(userEntity.getUrlPhoto().contains("facebook"))
                                    userEntity.setUrlPhoto(userEntity.getUrlPhoto().concat("?type=large"));

                                showData(userEntity.getUserUid());
                                firestoreService.saveUser(userEntity, new FirestoreSaveCallback() {
                                    @Override
                                    public void onSuccess() {
                                        preferences.setUser(userEntity);
                                        binding.txtUserMain.setText(userEntity.getUserName());
                                        stateRefreshUserName();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        e.printStackTrace();
                                        errorLogout();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        errorLogout();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                errorLogout();
            }

        }

    }


    private void errorLogout() {
        Toast.makeText(this, "Problema ao carregar sua sessão... Saindo...", Toast.LENGTH_LONG).show();
        auth.logout();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refresh) {
            getUser();
            refresh = false;
        }


    }


    public void showPicker(View view) throws ParseException {
        c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog();
        dialog.setAccentColor(getResources().getColor(R.color.button_positive));
        Date date1 = getDate();
        c.setTime(date1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        dialog.setMinDate(c);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        dialog.setMaxDate(c);
        dialog.setVersion(DatePickerDialog.Version.VERSION_1);
        List<Calendar> datesDisable = new ArrayList<>();
        datesDisable.add(c);
        Calendar[] dates = datesDisable.toArray(new Calendar[datesDisable.size()]);
        dialog.setDisabledDays(dates);
        dialog.setSelectableDays(dates);
        dialog.vibrate(false);
        dialog.setCancelText("VOLTAR");
        dialog.setTitle("Nova perspectiva");
        dialog.setMenuVisibility(false);
        dialog.setOnDateSetListener(this);
        dialog.setHighlightedDays(dates);
        dialog.show(getFragmentManager(),"datePicker");

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int dayOfMonth) {

        try {
            String monthSelected = c.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "br"));

            PerspectiveEntity perspectiveEntity = new PerspectiveEntityBuilder()
                    .year(year)
                    .month(monthSelected.substring(0, 1).toUpperCase() + monthSelected.substring(1))
                    .userUid(userEntity.getUserUid())
                    .totalCredit(new BigDecimal("0.00"))
                    .totalDebit(new BigDecimal("0.00"))
                    .build();


            PerspectiveController pController = new ViewModelProvider(this).get(PerspectiveController.class);
            new CompositeDisposable().add(pController.addPerspective(perspectiveEntity).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        componentUtils.showSnackbar("Sucesso!", 500);

                    }));

        } catch (Exception e) {
            componentUtils.showSnackbar("Dados ainda não carregados, aguarde sua conexão", 2000);
        }

    }

    public Date getDate() throws ParseException {
        DateFormat format = new SimpleDateFormat("MMMM-yyyy", new Locale("pt", "br"));
        String pattern;
        Date date;
        c = Calendar.getInstance();

        if (perspectiveList.size() == 0) {
            String monthFormat = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "br"));
            int year = Calendar.getInstance().get(Calendar.YEAR);
            pattern = monthFormat + "-" + year;
            date = format.parse(pattern);
            date.setMonth(date.getMonth());

        } else {
            pattern = perspectiveList.get(perspectiveList.size() - 1).getMonth() + "-" + perspectiveList.get(perspectiveList.size() - 1).getYear();
            date = format.parse(pattern);
            date.setMonth(date.getMonth() + 1);

        }

        return date;
    }

    public int getPosition(List<PerspectiveEntity> perspectiveEntities) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("MMMM", new Locale("pt", "br"));
        int position = 0;
        String month;
        int year;
        Date date;
        Calendar calendar = Calendar.getInstance();

        for (PerspectiveEntity p : perspectiveEntities) {
            month = p.getMonth();
            date = dateFormat.parse(month);

            if (calendar.get(Calendar.MONTH) == date.getMonth() && calendar.get(Calendar.YEAR) == p.getYear()) {
                return position;
            }

            position++;
        }

        return 0;
    }

    public void setDataViewPager(int position) {
        binding.txtPerspectiveMain.setText(perspectiveEntities.get(position).getMonth() + " / " +
                perspectiveEntities.get(position).getYear());
        binding.txtCreditMain.setText(FormatUtils.numberFormat(perspectiveEntities.get(position).getTotalCredit()));
        binding.txtDebitMain.setText(FormatUtils.numberFormat(perspectiveEntities.get(position).getTotalDebit()));
        binding.txtAmountPerspectiveMain.setText("Saldo por perspectiva " + "\n\n R$ "
                + FormatUtils.decimalFormat(
                perspectiveEntities.get(position).getTotalCredit().subtract(perspectiveEntities.get(position).getTotalDebit())));
    }

    public void calculeBalance() {

        BigDecimal balance = new BigDecimal("0.00");
        BigDecimal value;

        for (PerspectiveEntity p : perspectiveEntities) {
            value = p.getTotalCredit().subtract(p.getTotalDebit());
            balance = balance.add(value);
        }

        final BigDecimal balanceFinal = balance;

        if (isShow) {
            binding.txtValueBalanceMain.animate().setDuration(500).alpha(0f).start();
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(350);
                        binding.spinktBalanceMain.post(() -> {
                            binding.txtValueBalanceMain.setText(FormatUtils.decimalFormat(balanceFinal));
                            binding.txtValueBalanceMain.animate().setDuration(500).alpha(1f).start();
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        } else {
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                        binding.spinktBalanceMain.post(() -> {
                            binding.spinktBalanceMain.setVisibility(View.GONE);
                            binding.txtValueBalanceMain.setText(FormatUtils.decimalFormat(balanceFinal));
                            binding.txtValueBalanceMain.animate().setDuration(500).alpha(1f).start();
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            isShow = true;

        }

    }

    private void stateRefreshUserName() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    binding.spinktNameMain.post(() -> {
                        binding.spinktNameMain.setVisibility(View.GONE);
                        binding.txtUserMain.animate().setDuration(500).alpha(1f).start();
                    });


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }



}