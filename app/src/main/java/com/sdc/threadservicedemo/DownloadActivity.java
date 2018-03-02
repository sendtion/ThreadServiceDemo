package com.sdc.threadservicedemo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DownloadActivity extends AppCompatActivity {
    private static final String TAG = "DownloadActivity";

    private static final String DOWNLOAD_URL = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";

    private Button btn_start_download;
    private Button btn_pause_download;
    private Button btn_cancel_download;

    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        btn_start_download = (Button) findViewById(R.id.btn_start_download);
        btn_start_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadBinder == null){
                    return;
                } else {
                    downloadBinder.startDownload(DOWNLOAD_URL);
                }
            }
        });

        btn_pause_download = (Button) findViewById(R.id.btn_pause_download);
        btn_pause_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadBinder == null){
                    return;
                } else {
                    downloadBinder.pauseDownload();
                }
            }
        });

        btn_cancel_download = (Button) findViewById(R.id.btn_cancel_download);
        btn_cancel_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadBinder == null){
                    return;
                } else {
                    downloadBinder.cancelDownload();
                }
            }
        });

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent); //启动服务
        bindService(intent, connection, BIND_AUTO_CREATE); //绑定服务

        requestPermission();
    }

    private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 11:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "拒绝权限无法继续使用", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
