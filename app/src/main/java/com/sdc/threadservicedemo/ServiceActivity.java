package com.sdc.threadservicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ServiceActivity extends AppCompatActivity {

    private Button btn_start_service;
    private Button btn_stop_service;

    private Button btn_bind_service;
    private Button btn_unbind_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        btn_start_service = (Button) findViewById(R.id.btn_start_service);
        btn_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动服务。第一次启动服务，执行onCreate和onStartCommand方法。以后没有停止服务又启动服务，会执行onStartCommand方法
                //绑定服务后，再启动服务，会执行onStartCommand方法
                Intent intent = new Intent(ServiceActivity.this, MyService.class);
                startService(intent);
            }
        });

        btn_stop_service = (Button) findViewById(R.id.btn_stop_service);
        btn_stop_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //停止服务。会执行onDestory方法
                //在MyService中调用stopSelf()方法也可以停止服务
                //只绑定服务（没有解绑服务），然后停止服务（没有启动服务），会没有效果
                Intent intent = new Intent(ServiceActivity.this, MyService.class);
                stopService(intent);
            }
        });

        btn_bind_service = (Button) findViewById(R.id.btn_bind_service);
        btn_bind_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //绑定服务。直接绑定服务，没有启动服务，只会执行onCreate方法，不会执行onStartCommand方法
                //如果启动服务后，再绑定服务，不会再执行onCreate方法。而且直接停止服务没有效果，必须执行解绑服务，不用再执行停止服务。
                //只要有绑定服务，就必须解绑服务进行停止
                Intent intent = new Intent(ServiceActivity.this, MyService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
        });

        btn_unbind_service = (Button) findViewById(R.id.btn_unbind_service);
        btn_unbind_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //解绑服务。执行onDestory方法
                //如果启动服务（没有停止服务）后，解绑服务（没有绑定服务），程序会崩溃（做异常处理）
                try {
                    unbindService(connection);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private MyService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
