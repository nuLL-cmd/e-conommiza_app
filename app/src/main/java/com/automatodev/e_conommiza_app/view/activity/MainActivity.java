package com.automatodev.e_conommiza_app.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreGetCallback;
import com.automatodev.e_conommiza_app.database.firebase.firestore.FirestoreService;
import com.automatodev.e_conommiza_app.database.seed.MockFile;
import com.automatodev.e_conommiza_app.databinding.ActivityMainBinding;
import com.automatodev.e_conommiza_app.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.model.UserEntity;
import com.automatodev.e_conommiza_app.security.firebaseAuth.Authentication;
import com.automatodev.e_conommiza_app.view.adapter.FragmentPageAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    public static boolean status;

    private ActivityMainBinding binding;
    private FragmentPageAdapter fragmentAdapter;
    public static List<PerspectiveEntity> perspectiveEntities;
    private FirestoreService firestoreService;
    private Authentication auth;
    private UserEntity userEntity;
    public static boolean refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        binding.txtMonthBalance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.txtMonthBalance.setSelected(true);
                }
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void showData() {
        MockFile mockFile = new MockFile();
        perspectiveEntities = new ArrayList<>();
        perspectiveEntities.addAll(mockFile.getPerspectiveEntityLIst());


        // adapter = new PerspectiveAdapter(perspectiveEntities);
        fragmentAdapter = new FragmentPageAdapter(getSupportFragmentManager(), 0, perspectiveEntities);
        binding.viewPagerMain.setAdapter(fragmentAdapter);


        if (perspectiveEntities.size() == 0) {
            binding.txtPerspectiveMain.setText("Não há perspectivas");
            binding.txtCreditMain.setVisibility(View.GONE);
            binding.txtDebitMain.setVisibility(View.GONE);
            binding.txtAmountPerspectiveMain.setText("Comece adicionando \n uma nova perspectiva!");

        } else {
            binding.txtCreditMain.setVisibility(View.VISIBLE);
            binding.txtDebitMain.setVisibility(View.VISIBLE);
            binding.txtPerspectiveMain.setText(perspectiveEntities.get(fragmentAdapter.getIntemCount()).getMonth() + " / " +
                    perspectiveEntities.get(fragmentAdapter.getIntemCount()).getYear());
            binding.txtCreditMain.setText("R$ " + perspectiveEntities.get(fragmentAdapter.getIntemCount()).getTotalCredit());
            binding.txtDebitMain.setText("R$ " + perspectiveEntities.get(fragmentAdapter.getIntemCount()).getTotalDebit());
            binding.txtAmountPerspectiveMain.setText("Saldo - " + perspectiveEntities.get(fragmentAdapter.getIntemCount()).getMonth() + "\nR$ " +
                    perspectiveEntities.get(fragmentAdapter.getIntemCount()).getTotalCredit().subtract(perspectiveEntities.get(fragmentAdapter.getIntemCount()).getTotalDebit()));
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
    }

    public void getUser() {
        auth = new Authentication();
        String uid = auth.getUser().getUid();
        if (uid != null) {
            firestoreService = new FirestoreService();
            try {
                firestoreService.getUser(uid, new FirestoreGetCallback() {
                    @Override
                    public void onSuccess(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists())
                                userEntity = snapshot.toObject(UserEntity.class);
                            binding.txtUserMain.setText(userEntity.getUserName());
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

        } else
            errorLogout();

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