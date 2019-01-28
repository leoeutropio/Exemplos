package com.example.devmakerdiego.rms;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.devmakerdiego.rms.webservice.daosharedpreferences.LocalDbImplement;
import com.example.devmakerdiego.rms.webservice.model.LoginResponse;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import io.fabric.sdk.android.Fabric;
import java.util.List;

/**
 * Created by DevmakerDiego on 14/11/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        String[] permissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};

        Dexter.withActivity(this).withPermissions(permissions).withListener(
                new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()) {
                            mostrarMain();
                        }else if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this,R.style.AppTheme_AlertDialog);
                            builder.setTitle("Permissão");
                            builder.setMessage("Precisamos da sua permissão para continuar.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SplashActivity.this.finish();
                                    startActivity(SplashActivity.this.getIntent());
                                }
                            }).show();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this,R.style.AppTheme_AlertDialog);
        builder.setTitle("Permissão");
        builder.setMessage("Precisamos da sua permissão para acessar sua câmera, sua galeria e sua localização.");

        builder.setPositiveButton("CONFIGURAÇÕES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });

        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101){
            this.finish();
            startActivity(this.getIntent());
        }
    }

    private void mostrarMain() {
        LoginResponse loginResponse = new LocalDbImplement<LoginResponse>(this).getDefault(LoginResponse.class);

        if (loginResponse == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 1250);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainLogadoActivity.class));
                    finish();
                }
            }, 1250);
        }
    }
}