package com.automatodev.e_conommiza_app.view.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.database.firebase.callback.StorageCallback;
import com.automatodev.e_conommiza_app.database.firebase.FirestoreService;
import com.automatodev.e_conommiza_app.database.firebase.StorageService;
import com.automatodev.e_conommiza_app.database.sqlite.controller.PerspectiveController;
import com.automatodev.e_conommiza_app.databinding.ActivityProfileTwoBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogAboutBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogChoiseGlobalBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.entity.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entity.model.UserEntity;
import com.automatodev.e_conommiza_app.entity.modelBuild.PerspectiveEntityBuilder;
import com.automatodev.e_conommiza_app.entity.response.PerspectiveWithData;
import com.automatodev.e_conommiza_app.preferences.UserPreferences;
import com.automatodev.e_conommiza_app.security.FirebaseAuthentication;
import com.automatodev.e_conommiza_app.utils.FormatUtils;
import com.automatodev.e_conommiza_app.view.adapter.ItemsProfileAdapter;
import com.automatodev.e_conommiza_app.utils.ComponentUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.iceteck.silicompressorr.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.automatodev.e_conommiza_app.R.*;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityProfileTwoBinding binding;
    private FirebaseAuthentication auth;
    private FirestoreService firestoreService;
    private StorageService storageService;
    private Uri uriInternal;
    private Uri uriExternal;
    private ComponentUtils componentUtils;
    private CompositeDisposable disposable;
    private UserPreferences preferences;
    private Calendar c;
    private List<PerspectiveEntity> perspectiveList;
    private ItemsProfileAdapter adapter;


    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private Integer registerCount = 0;


    public static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileTwoBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        componentUtils = new ComponentUtils(this);
        setSupportActionBar(binding.toolbarMenuProfile);

        disposable = new CompositeDisposable();


        auth = new FirebaseAuthentication(this);
        firestoreService = new FirestoreService();
        storageService = new StorageService();

        perspectiveList = new ArrayList<>();

        adapter = new ItemsProfileAdapter(perspectiveList);
        binding.recyclerItemsProfile.hasFixedSize();
        binding.recyclerItemsProfile.setAdapter(adapter);

        getUser();


        binding.swipeRefreshProfile.setColorSchemeResources(color.button_positive);
        binding.swipeRefreshProfile.setRefreshing(true);
        binding.swipeRefreshProfile.setOnRefreshListener(() -> binding.swipeRefreshProfile.setRefreshing(false));

        String[] phraseOe = {"Verificando"};
        String[] phraseTo = {"seus dados"};
        binding.txtFadeOneProfile.setTexts(phraseOe);
        binding.txtFadeTwoProfile.setTexts(phraseTo);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                componentUtils.showSnackbar("Para usar este recurso, você precisa dessa permissão!", 2000);
            else
                pickLib(binding.relativeDaddyProfile);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {
                uriInternal = data.getData();
                binding.imageUserProfile.setAlpha(0f);
                Glide.with(this).load(uriInternal).addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.imageUserProfile.animate().setDuration(300).alpha(1f).start();
                        new Thread() {
                            @Override
                            public void run() {
                                uriExternal = resizeImage(uriInternal);
                            }
                        }.start();

                        return false;
                    }
                }).into(binding.imageUserProfile);
            }
        }
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

    @Override
    public void onBackPressed() {
        disposable.dispose();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case id.itemAbout:
                about();
                break;
            case id.itemExit:
                logout();
                break;
            case id.itemFeedback:
                feedback();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Uri resizeImage(Uri uri) {
        try {
            File file = new File(FileUtils.getPath(this, uri));
            try {
                file = new Compressor(this).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Uri.fromFile(file);
        } catch (Exception e) {
            return uriExternal = uri;

        }
    }

    public void updateUser(View view) {
        AlertDialog dialogProgress = new AlertDialog.Builder(this).create();
        dialogProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutDialogProgressBinding bindingProgress = DataBindingUtil.inflate(getLayoutInflater().from(this), layout.layout_dialog_progress, binding.relativeDaddyProfile, false);
        dialogProgress.setView(bindingProgress.getRoot());
        Map<String, Object> map = new HashMap<>();
        if (uriExternal != null) {
            bindingProgress.setInformation("Enviando arquivo...");
            bindingProgress.setIsLoading(true);
            dialogProgress.show();
            storageService.uploadPhoto(uriExternal, auth.getUser().getUid(), new StorageCallback() {
                @Override
                public void onSuccess(Uri uri) {
                    bindingProgress.setInformation("Atualizando dados...");
                    map.put("urlPhoto", uri.toString());
                    preferences = new UserPreferences(ProfileActivity.this, "user");
                    preferences.updateField("urlPhoto", uri.toString());

                    firestoreService.updateUser(auth.getUser().getUid(), map, new FirestoreSaveCallback() {
                        @Override
                        public void onSuccess() {
                            MainActivity.refresh = true;
                            bindingProgress.setInformation("Sucesso!!");
                            bindingProgress.setIsLoading(false);
                            bindingProgress.setStatus(true);

                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        sleep(1200);
                                        dialogProgress.dismiss();
                                        uriExternal = null;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            bindingProgress.setInformation("Ops! Algo deu errado!");
                            bindingProgress.setIsLoading(false);
                            bindingProgress.setStatus(true);
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        sleep(2000);
                                        dialogProgress.dismiss();
                                        uriExternal = null;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                            componentUtils.showSnackbar("Tivemos um problema ao salvar seus dados\n Tente novamente."
                                    , 2000);
                            Log.e("logx", "OnFailure updateUser: " + e.getMessage());
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    bindingProgress.setInformation("Ops! Algo deu errado!");
                    bindingProgress.setIsLoading(false);
                    bindingProgress.setStatus(true);
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(2000);
                                dialogProgress.dismiss();
                                uriExternal = null;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    componentUtils.showSnackbar("Tivemos um problema ao salvar seus dados\n Tente novamente."
                            , 2000);
                    Log.e("logx", "OnFailure uploadPhoto: " + e.getMessage());

                }
            });

        } else
            componentUtils.showSnackbar("Nenhuma alteração detectada!", 2000);

    }

    public void getUser() {
        UserPreferences preferences = new UserPreferences(this, "user");
        UserEntity userEntity = preferences.getUser();

        if (!userEntity.getUserUid().equals("")) {
            binding.imageUserProfile.setAlpha(0f);
            Glide.with(ProfileActivity.this).load(userEntity.getUrlPhoto())
                    .addListener(componentUtils.listenerFadeImage(binding.imageUserProfile, 300)).into(binding.imageUserProfile);
            showData(userEntity.getUserUid());

        } else {
            componentUtils.showSnackbar("Houve um problema ao carregar suas perspectivas, favor feche a aplicação e tente novamente", 1500);
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1500);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        finish();

                    }
                }
            }.start();

        }
    }

    public void actProfileMain(View view) {
        disposable.dispose();
        NavUtils.navigateUpFromSameTask(this);
    }

    public void showData(String uid) {
        try {
            PerspectiveController perspectiveController = new ViewModelProvider(this).get(PerspectiveController.class);
            disposable = new CompositeDisposable();
            disposable.add(perspectiveController.getPerspectiveWithData(uid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(perspectivies -> {
                if (perspectivies != null) {
                    perspectiveList.clear();
                    for (PerspectiveWithData p : perspectivies) {
                        PerspectiveEntity perspectiveEntity = p.perspectiveEntity;
                        perspectiveEntity.setItemsPerspective(p.dataEntryEntities);
                        perspectiveList.add(perspectiveEntity);

                    }

                    phraseArray(perspectiveList);

                    adapter.notifyDataSetChanged();

                    if(perspectivies.size() == 0)
                        binding.relativeNoContentProfile.setVisibility(View.VISIBLE);
                    else{
                        binding.txtPerspectiveProfile.setText("Perspectivas cadastradas");
                        binding.relativeNoContentProfile.setVisibility(View.GONE);
                    }

                    adapter.setOnItemClickListener(position -> {

                    });
                }
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(450);
                            binding.swipeRefreshProfile.setRefreshing(false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }));


        } catch (Exception e) {
            componentUtils.showSnackbar("Houve um problema ao carregar suas perspectivas, favor feche a aplicação e tente novamente", 1500);
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1500);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        finish();

                    }
                }
            }.start();
        }
    }

    public void about() {
        LayoutDialogAboutBinding layoutBinding = DataBindingUtil.inflate(getLayoutInflater().from(this), layout.layout_dialog_about, binding.relativeDaddyProfile, false);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(layoutBinding.getRoot());
        dialog.show();
        layoutBinding.btnCloseDialogAbout.setOnClickListener(view1 -> dialog.dismiss());
    }

    public void logout() {
        AlertDialog dialogLogout = new AlertDialog.Builder(this).create();
        dialogLogout.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutDialogChoiseGlobalBinding bindingLogout = DataBindingUtil.inflate(getLayoutInflater().from(this), layout.layout_dialog_choise_global, binding.relativeDaddyProfile, false);
        bindingLogout.lblTitleDialogLogout.setText("Sair");
        bindingLogout.lblMessageDialogLogout.setText("Deseja fazer logout do aplicativo?");
        dialogLogout.setView(bindingLogout.getRoot());
        dialogLogout.show();
        bindingLogout.btnYesDialogLogout.setOnClickListener(v -> {
            auth.logout();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        bindingLogout.btnCancelDialogLogout.setOnClickListener(v1 -> dialogLogout.dismiss());

    }

    public void feedback() {
        final String appPackageName = getApplicationContext().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void pickLib(View view) {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{permission}, 100);
        else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        }
    }

    public void phraseArray(List<PerspectiveEntity> perspectivies) {
        registerCount = 0;
        totalDebit = new BigDecimal("0.00");
        totalCredit = new BigDecimal("0.00");


        if (perspectivies.size() != 0) {

            String[] phraseOne = new String[10];
            String[] phraseTwo = new String[10];


            perspectivies.stream().forEach(perspectiveEntity -> {
                totalDebit = totalDebit.add(perspectiveEntity.getTotalDebit());
                totalCredit = totalCredit.add(perspectiveEntity.getTotalCredit());
            });

            BigDecimal bigDebit = perspectivies.stream().map(PerspectiveEntity::getTotalDebit).max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
            BigDecimal bigCredit = perspectivies.stream().map(PerspectiveEntity::getTotalCredit).max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);


            perspectivies.stream().forEach(perspectiveEntity -> registerCount += perspectiveEntity.getItemsPerspective().size());

            phraseOne[0] = "Você tem";
            phraseTwo[0] = perspectivies.size() + " perspectivas";

            phraseOne[1] = "Seus proventos somam";
            phraseTwo[1] = FormatUtils.numberFormat(totalCredit) + " reais";

            phraseOne[2] = "Você possui um total";
            phraseTwo[2] = "de " + registerCount + " itens cadastrados";

            phraseOne[3] = "Você chegou a receber";
            phraseTwo[3] = FormatUtils.numberFormat(bigCredit) + "\nem um mês";

            phraseOne[4] = "Porém gastou";
            phraseTwo[4] = FormatUtils.numberFormat(bigDebit) + "\ndentro de um mês";

            phraseOne[5] = "Suas despesas somam";
            phraseTwo[5] = FormatUtils.numberFormat(totalDebit) + " reais";

            phraseOne[6] = "Continue gerindo ";
            phraseTwo[6] = "seus gastos pelo app";

            phraseOne[7] = "Usando as perspectivas";
            phraseTwo[7] = "voce se controla";

            phraseOne[8] = "Seu bolso";
            phraseTwo[8] = "agradece";

            phraseOne[9] = "Confia!";
            phraseTwo[9] = "\uD83D\uDE0E";


            binding.txtFadeOneProfile.setTexts(phraseOne);
            binding.txtFadeTwoProfile.setTexts(phraseTwo);


        } else {
            String[] phraseOne = {"Você não tem", "Com as perspectivas", "Organiza seus gastos ", "Fazendo isso", "Confia!"};
            String[] phraseTwo = {"nehuma pespectiva.", "você controla e", "por categoria ", "seu bolso agradece", ":D"};
            binding.txtFadeOneProfile.setTexts(phraseOne);
            binding.txtFadeTwoProfile.setTexts(phraseTwo);
        }
    }

    public void showPicker(View view) throws ParseException {
        c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(this, R.style.DatePickerDefaultTheme, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
        Date date1 = getDate();
        c.setTime(date1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(c.getTime().getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(c.getTime().getTime());
        dialog.show();
    }

    public Date getDate() throws java.text.ParseException {
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        try {
            String monthSelected = c.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "br"));

            PerspectiveEntity perspectiveEntity = new PerspectiveEntityBuilder()
                    .year(year)
                    .month(monthSelected.substring(0, 1).toUpperCase() + monthSelected.substring(1))
                    .userUid(FirebaseAuth.getInstance().getCurrentUser().getUid())
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
                                    componentUtils.showSnackbar("Dado inserido com sucesso", 500);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();


                    }));

        } catch (Exception e) {
            componentUtils.showSnackbar("Dados ainda não carregados, aguarde sua conexão", 1700);
        }

    }

    public void actProfileReport(View view) {
        if (perspectiveList.size() == 0) {
            componentUtils.showSnackbar("Você não tem dados para gerar um relatório.", 1700);
        } else {
            if (!ReportActivity.status) {
                Intent intent = new Intent(this, ReportActivity.class);
                intent.putExtra("data", (Serializable) perspectiveList);
                startActivity(intent);
            }
        }
    }


}