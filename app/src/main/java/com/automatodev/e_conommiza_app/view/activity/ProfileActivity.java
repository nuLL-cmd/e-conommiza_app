package com.automatodev.e_conommiza_app.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;

import com.automatodev.e_conommiza_app.R;
import com.automatodev.e_conommiza_app.database.firebase.callback.FirestoreSaveCallback;
import com.automatodev.e_conommiza_app.database.firebase.callback.StorageCallback;
import com.automatodev.e_conommiza_app.database.firebase.firestore.FirestoreService;
import com.automatodev.e_conommiza_app.database.seed.MockFile;
import com.automatodev.e_conommiza_app.database.firebase.storage.StorageService;
import com.automatodev.e_conommiza_app.databinding.ActivityProfileTwoBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogAboutBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogLogoutBinding;
import com.automatodev.e_conommiza_app.databinding.LayoutDialogProgressBinding;
import com.automatodev.e_conommiza_app.model.PerspectiveEntity;
import com.automatodev.e_conommiza_app.model.UserEntity;
import com.automatodev.e_conommiza_app.security.firebaseAuth.Authentication;
import com.automatodev.e_conommiza_app.view.adapter.ItemsProfileAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.iceteck.silicompressorr.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import lombok.SneakyThrows;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileTwoBinding binding;
    private Authentication auth;
    private FirestoreService firestoreService;
    private StorageService storageService;
    private UserEntity user;
    private List<PerspectiveEntity> perspectiveEntities;
    private Uri uriInternal;
    private Uri uriExternal;

    public static boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileTwoBinding.inflate(getLayoutInflater());
        View viewBinding = binding.getRoot();
        setContentView(viewBinding);

        setSupportActionBar(binding.toolbarMenuProfile);

        auth = new Authentication();
        firestoreService = new FirestoreService();
        storageService = new StorageService();

        getUser();
        showData();

        binding.lblSinceProfile.setTexts(new String[]{"Você tem", "Você pode consultar um", "Organize seus gastos ", "Seu bolso agradeçe"});
        binding.txtSinceProfile.setTexts(new String[]{perspectiveEntities.size() + " perspectivas cadastradas", "relatorio clicando no gráfico", "em proventos e despessas", "esta boa ação"});

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                Snackbar.make(binding.relativeDaddyProfile,
                        "Para usar este recurso, você precisa dessa permissão!", Snackbar.LENGTH_LONG).show();
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

    @SneakyThrows
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
        LayoutDialogProgressBinding bindingProgress = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_progress, binding.relativeDaddyProfile, false);
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
                            Snackbar.make(binding.relativeDaddyProfile
                                    , "Tivemos um problema ao salvar seus dados\n Tente novamente."
                                    , Snackbar.LENGTH_LONG).show();
                            Log.e("logx", "Erro: " + e.getMessage());
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
                    Snackbar.make(binding.relativeDaddyProfile
                            , "Tivemos um problema ao salvar seus dados\n Tente novamente."
                            , Snackbar.LENGTH_LONG).show();
                    Log.e("logx", "Erro: " + e.getMessage());
                    Log.e("logx", "Error uploadPhoto: " + e.getMessage());

                }
            });

        } else
            Snackbar.make(binding.relativeDaddyProfile, "Nenhuma alteração detectada!", Snackbar.LENGTH_LONG).show();

    }

    public void getUser() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("user");
            binding.imageUserProfile.setAlpha(0f);
            try {
                Glide.with(ProfileActivity.this).load(user.getUrlPhoto())
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                binding.imageUserProfile.animate().setDuration(300).alpha(1f).start();
                                return false;
                            }
                        })
                        .into(binding.imageUserProfile);

            } catch (Exception e) {
                Log.e("logx", "Error loadImge getUser: " + e.getMessage());
                Toast.makeText(this, "Houve complicações ao carregar seu perfil, favor feche a aplicação e tente novamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAbout:
                about();
                break;
            case R.id.itemExit:
                logout();
                break;
            case R.id.itemFeedback:
                feedback();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void feedback() {
        final String appPackageName = getApplicationContext().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void actProfileMain(View view) {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void showData() {
        MockFile mockFile = new MockFile();
        perspectiveEntities = mockFile.getPerspectiveEntityLIst();
        ItemsProfileAdapter adapter = new ItemsProfileAdapter(perspectiveEntities);

        binding.recyclerItemsProfile.hasFixedSize();
        binding.recyclerItemsProfile.setAdapter(adapter);

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


    public void about() {
        LayoutDialogAboutBinding layoutBinding = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_about, binding.relativeDaddyProfile, false);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(layoutBinding.getRoot());
        dialog.show();
        layoutBinding.btnCloseDialogAbout.setOnClickListener(view1 -> dialog.dismiss());
    }

    public void logout() {
        AlertDialog dialogLogout = new AlertDialog.Builder(this).create();
        dialogLogout.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutDialogLogoutBinding bindingLogout = DataBindingUtil.inflate(getLayoutInflater().from(this), R.layout.layout_dialog_logout, binding.relativeDaddyProfile, false);
        dialogLogout.setView(bindingLogout.getRoot());
        dialogLogout.show();
        bindingLogout.btnYesDialogLogout.setOnClickListener(v -> {
            auth.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        bindingLogout.btnCancelDialogLogout.setOnClickListener(v1 -> dialogLogout.dismiss());

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
}