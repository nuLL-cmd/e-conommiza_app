package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreGetCallback;
import com.automatodev.e_conommiza_app.database.firebase.firestore.FirestoreService;
import com.automatodev.e_conommiza_app.database.sqlite.controller.PerspectiveController;
import com.automatodev.e_conommiza_app.databinding.ActivityMainBinding;
import com.automatodev.e_conommiza_app.entidade.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entidade.model.UserEntity;
import com.automatodev.e_conommiza_app.entidade.modelBuild.PerspectiveEntityBuilder;
import com.automatodev.e_conommiza_app.entidade.response.PerspectiveWithData;
import com.automatodev.e_conommiza_app.preferences.UserPreferences;
import com.automatodev.e_conommiza_app.security.firebaseAuth.Authentication;
import com.automatodev.e_conommiza_app.view.adapter.FragmentPageAdapter;
import com.automatodev.e_conommiza_app.view.utils.ComponentUtils;
import com.automatodev.e_conommiza_app.view.utils.FormatUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

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

    private ActivityMainBinding binding;
    public static List<PerspectiveEntity> perspectiveEntities;
    public List<PerspectiveEntity> perspectiveList;
    private FirestoreService firestoreService;
    private Authentication auth;
    private UserEntity userEntity;
    private ProgressDialog dialog;
    private FragmentPageAdapter fragmentAdapter;
    private ComponentUtils componentUtils;
    private Calendar c;
    private CompositeDisposable disposable;
    private PerspectiveController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        componentUtils = new ComponentUtils(this);
        dialog = new ProgressDialog(this);
        perspectiveList = new ArrayList<>();
        perspectiveEntities = new ArrayList<>();
        disposable = new CompositeDisposable();
        controller = new ViewModelProvider(this).get(PerspectiveController.class);
        fragmentAdapter = new FragmentPageAdapter(getSupportFragmentManager(), 0, perspectiveEntities);

        binding.txtMonthBalance.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.txtMonthBalance.setSelected(true);
            }
        });

        binding.viewPagerMain.setAdapter(fragmentAdapter);

        getUser();
        showData();
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
            componentUtils.showSnackbar("Você não tem nenhuma perspectiva cadastrada.\ncadastre uma perspecitve antes para adicionar um novo regisro", 2000);
            return;
        }

        if (!AddItemActivity.status) {
            try {
                int position = binding.viewPagerMain.getCurrentItem();
              /*  String perspective = perspectiveEntities.get(position).getMonth() + " / " + perspectiveEntities.get(position).getYear();
                long idPerspective = perspectiveEntities.get(position).getIdPerspective();*/
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
                intent.putExtra("urlPhoto", userEntity.getUrlPhoto());
                intent.putExtra("typeIntent", "new");
                startActivity(intent);
                binding.menu.close(true);
            } catch (Exception e) {
                componentUtils.showSnackbar("Dados ainda não carregados, aguarde sua conexão", 2000);
            }

        }


    }

    public void showData() {

        try {
            disposable.add(controller.getPerspectiveWithData(auth.getUser().getUid()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(dataList -> {
                perspectiveEntities.clear();
                perspectiveList.clear();

                for (PerspectiveWithData p : dataList) {
                    PerspectiveEntity perspectiveEntity = p.perspectiveEntity;
                    perspectiveEntity.setItemsPerspective(p.dataEntryEntities);
                    perspectiveEntities.add(perspectiveEntity);
                    perspectiveList.add(new PerspectiveEntity(perspectiveEntity.getIdPerspective(), perspectiveEntity.getMonth(), perspectiveEntity.getYear()));
                }


                fragmentAdapter.notifyDataSetChanged();


                if (perspectiveEntities.size() == 0) {
                    binding.txtPerspectiveMain.setText("Não há perspectivas");
                    binding.txtCreditMain.setVisibility(View.GONE);
                    binding.txtDebitMain.setVisibility(View.GONE);
                    binding.txtAmountPerspectiveMain.setText("Comece adicionando \n uma nova perspectiva!");

                } else {
                    //int positionTest = getPosition(perspectiveEntities);
                    binding.viewPagerMain.setCurrentItem(dataList.size(), true);
                    binding.txtCreditMain.setVisibility(View.VISIBLE);
                    binding.txtDebitMain.setVisibility(View.VISIBLE);
                    setDataViewPager(dataList.size() - 1);
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
            }));
        } catch (Exception e) {
            perspectiveList.clear();
            perspectiveEntities.clear();
            fragmentAdapter.notifyDataSetChanged();
            binding.txtPerspectiveMain.setText("Pproblema ao \ncarregar as perspectivas");
            binding.txtCreditMain.setVisibility(View.GONE);
            binding.txtDebitMain.setVisibility(View.GONE);
            binding.txtAmountPerspectiveMain.setText("Parece que tivemos um erro ao processar os dados.");

        }


    }

    public void getUser() {
        auth = new Authentication();
        String uid = auth.getUser().getUid();
        UserPreferences preferences = new UserPreferences(this, "user");
        userEntity = preferences.getUser();

        if (userEntity.getUserUid().equals(uid)) {
            binding.txtUserMain.setText(userEntity.getUserName());
        } else {
            firestoreService = new FirestoreService();
            try {
                firestoreService.getUser(uid, new FirestoreGetCallback() {
                    @Override
                    public void onSuccess(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                binding.spinktNameMain.setVisibility(View.GONE);
                                userEntity = snapshot.toObject(UserEntity.class);
                                preferences.setUser(userEntity);
                                binding.txtUserMain.setText(userEntity.getUserName());
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
        Toast.makeText(this, "Tivemos um problema ao carregar sessão... Saindo...", Toast.LENGTH_LONG).show();
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

        DatePickerDialog dialog = new DatePickerDialog(this, R.style.DialogTheme, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
        Date date1 = getDate();
        c.setTime(date1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(c.getTime().getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(c.getTime().getTime());
        dialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        binding.menu.close(true);
        dialog.setMessage("Aguarde...");
        dialog.setCancelable(false);

        try {
            String monthSelected = c.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "br"));
            PerspectiveEntity perspectiveEntity = new PerspectiveEntityBuilder()
                    .year(year)
                    .month(monthSelected.toUpperCase())
                    .userUid(userEntity.getUserUid())
                    .totalCredit(new BigDecimal("0.00"))
                    .totalDebit(new BigDecimal("0.00"))
                    .build();


            PerspectiveController pController = new ViewModelProvider(this).get(PerspectiveController.class);
            new CompositeDisposable().add(pController.addPerspective(perspectiveEntity).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(100);
                                    dialog.dismiss();
                                    componentUtils.showSnackbar("Dado inserido com sucesso", 2000);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();


                    }));

        } catch (Exception e) {
            componentUtils.showSnackbar("Dados ainda não carregados, aguarde sua conexão", 2000);
            dialog.dismiss();
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
                perspectiveEntities.get(fragmentAdapter.getIntemCount()).getYear());
        binding.txtCreditMain.setText(FormatUtils.numberFormat(perspectiveEntities.get(position).getTotalCredit()));
        binding.txtDebitMain.setText(FormatUtils.numberFormat(perspectiveEntities.get(position).getTotalDebit()));
        binding.txtAmountPerspectiveMain.setText("Saldo por perspectiva " + "\n\n R$ "
                + FormatUtils.decimalFormat(
                perspectiveEntities.get(position).getTotalCredit().subtract(perspectiveEntities.get(position).getTotalDebit())));
    }
}