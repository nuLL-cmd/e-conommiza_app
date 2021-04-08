package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreGetCallback;
import com.automatodev.e_conommiza_app.database.firebase.firestore.FirestoreService;
import com.automatodev.e_conommiza_app.database.seed.MockFile;
import com.automatodev.e_conommiza_app.database.sqlite.controller.DataEntryController;
import com.automatodev.e_conommiza_app.database.sqlite.controller.PerspectiveController;
import com.automatodev.e_conommiza_app.databinding.ActivityMainBinding;
import com.automatodev.e_conommiza_app.entidade.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.entidade.model.UserEntity;
import com.automatodev.e_conommiza_app.entidade.response.PerspectiveWithData;
import com.automatodev.e_conommiza_app.security.firebaseAuth.Authentication;
import com.automatodev.e_conommiza_app.view.adapter.FragmentPageAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    public static boolean status;
    public static boolean refresh;

    private ActivityMainBinding binding;
    public static List<PerspectiveEntity> perspectiveEntities;
    private FirestoreService firestoreService;
    private Authentication auth;
    private UserEntity userEntity;
    private MockFile mockFile;
    private ProgressDialog dialog;
    private  FragmentPageAdapter fragmentAdapter;
    private int positionLocal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        mockFile = new MockFile();
        dialog = new ProgressDialog(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding.txtMonthBalance.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.txtMonthBalance.setSelected(true);
            }
        });

        perspectiveEntities = new ArrayList<>();

        fragmentAdapter = new FragmentPageAdapter(getSupportFragmentManager(), 0, perspectiveEntities);
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
            intent.putExtra("user", userEntity);
            startActivity(intent);
            binding.menu.close(true);
        }
    }

    public void actMainPerspective(View view) {
        dialog.setMessage("Aguarde...");
        dialog.setCancelable(false);
        dialog.show();


        PerspectiveController pController = new ViewModelProvider(this).get(PerspectiveController.class);
        new CompositeDisposable().add(pController.addPerspective(mockFile.getPerspectiveMock()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                                dialog.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    Toast.makeText(this, "Dado inserido com sucesso", Toast.LENGTH_LONG).show();

                }));
    }

    public void actMainProvento(View view) {
        dialog.setMessage("Aguarde...");
        dialog.setCancelable(false);
        dialog.show();
        DataEntryController dataEntryController = new ViewModelProvider(this).get(DataEntryController.class);
        new CompositeDisposable().add(dataEntryController.addDataEntry(mockFile.getDataEntryMock()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                                dialog.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    Toast.makeText(this, "Dado inserido com sucesso", Toast.LENGTH_LONG).show();
                }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void actMainItem(View view){
        if (!AddItemActivity.status){
            Intent intent = new Intent(this, AddItemActivity.class);
            startActivity(intent);
            binding.menu.close(true);
        }



    }

    public void showData() {
        CompositeDisposable disposable = new CompositeDisposable();
        PerspectiveController controller = new ViewModelProvider(this).get(PerspectiveController.class);
        disposable.add(controller.getPerspectiveWithData(auth.getUser().getUid()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(dataList -> {
            perspectiveEntities.clear();

            for (PerspectiveWithData p : dataList) {
                PerspectiveEntity perspectiveEntity = p.perspectiveEntity;
                perspectiveEntity.setItemsPerspective(p.dataEntryEntities);
                perspectiveEntities.add(perspectiveEntity);
            }


            fragmentAdapter.notifyDataSetChanged();

            if (perspectiveEntities.size() == 0) {
                binding.txtPerspectiveMain.setText("Não há perspectivas");
                binding.txtCreditMain.setVisibility(View.GONE);
                binding.txtDebitMain.setVisibility(View.GONE);
                binding.txtAmountPerspectiveMain.setText("Comece adicionando \n uma nova perspectiva!");

            } else {

                binding.txtCreditMain.setVisibility(View.VISIBLE);
                binding.txtDebitMain.setVisibility(View.VISIBLE);
                binding.txtPerspectiveMain.setText(perspectiveEntities.get(0).getMonth() + " / " +
                        perspectiveEntities.get(fragmentAdapter.getIntemCount()).getYear());
                binding.txtCreditMain.setText("R$ " + perspectiveEntities.get(0).getTotalCredit());
                binding.txtDebitMain.setText("R$ " + perspectiveEntities.get(0).getTotalDebit());
                binding.txtAmountPerspectiveMain.setText("Saldo - " + perspectiveEntities.get(0).getMonth() + "\nR$ " +
                        perspectiveEntities.get(0).getTotalCredit().subtract(perspectiveEntities.get(0).getTotalDebit()));
            }

            binding.viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                }

                @Override
                public void onPageSelected(int position) {
                    binding.txtPerspectiveMain.setText(perspectiveEntities.get(position).getMonth() + " / " +
                            perspectiveEntities.get(position).getYear());
                    binding.txtCreditMain.setText("R$ " + perspectiveEntities.get(position).getTotalCredit());
                    binding.txtDebitMain.setText("R$ " + perspectiveEntities.get(position).getTotalDebit());
                    binding.txtAmountPerspectiveMain.setText("Saldo - " + perspectiveEntities.get(position).getMonth() + "\nR$ " +
                            perspectiveEntities.get(position).getTotalCredit().subtract(perspectiveEntities.get(position).getTotalDebit()));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }));


    }

    public void getUser() {

        auth = new Authentication();
        String uid = auth.getUser().getUid();
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
                            binding.txtUserMain.setText(userEntity.getUserName());
                        }
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("logx", "Error getUser: " + e.getMessage());
                    e.printStackTrace();
                    errorLogout();
                }
            });

        } catch (Exception e) {
            Log.e("logx", "Exception getUser: " + e.getMessage());
            e.printStackTrace();
            errorLogout();
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

}