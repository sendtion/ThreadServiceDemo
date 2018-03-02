package com.sdc.threadservicedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Android线程
 * Handler消息机制
 * Service服务
 */
public class MainActivity extends AppCompatActivity {

    private Button btn_change_text;
    private Button btn_use_service;
    private Button btn_example;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_change_text = (Button) findViewById(R.id.btn_change_text);
        btn_change_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeText();
            }
        });

        tv_content = (TextView) findViewById(R.id.tv_content);

        btn_use_service = (Button) findViewById(R.id.btn_use_service);
        btn_use_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(intent);
            }
        });

        btn_example = (Button) findViewById(R.id.btn_example);
        btn_example.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                startActivity(intent);
            }
        });


        //第一种启动方式
        new MyThread1().start();

        //第二种启动方式
        new Thread(new MyThread2()).start();

        //第三种启动方式，也是创建方式，更常用
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        new DownloadTask().execute();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //在handleMessage中更新UI
                    tv_content.setText((String)msg.obj);
                    break;
            }
        }
    };

    //改变文本操作
    private void changeText(){
        final String content = "我们都是中国人！";
        new Thread(new Runnable() {
            @Override
            public void run() {
                //睡眠1秒，再继续执行
                SystemClock.sleep(1000);
                //第一种方式，使用post切换到主线程更新UI
                tv_content.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_content.setText(content);
                    }
                });
                //第二种方式，使用runOnUiThread切换到主线程更细UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_content.setText(content);
                    }
                });
                //第三种方式，使用handler发送消息到主线程
                Message msg = new Message();
                msg.what = 1;
                msg.obj = content;
                handler.sendMessage(msg);
            }
        }).start();
    }

    //线程方式1（不推荐），继承Thread类，耦合性较高
    class MyThread1 extends Thread {

        @Override
        public void run() {
            super.run();
        }
    }

    //线程创建方式2（推荐），实现Runnable抽象类
    class MyThread2 implements Runnable {

        @Override
        public void run() {

        }
    }

    class DownloadTask extends AsyncTask<Void,Integer,Boolean> {
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("下载");
            progressDialog.setMessage("正在下载......");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //处理下载任务，处理完成后返回true
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("正在下载 " + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if (aBoolean){
                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
