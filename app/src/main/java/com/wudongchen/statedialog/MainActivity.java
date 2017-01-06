package com.wudongchen.statedialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.wudongchen.library.BKStateDialog;
import com.wudongchen.library.StateView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BKStateDialog(MainActivity.this, StateView.State.SUCCESS).setMessage("执行成功").show();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BKStateDialog(MainActivity.this, StateView.State.WARN).setMessage("执行成功").show();
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BKStateDialog(MainActivity.this, StateView.State.FAILUE).setMessage("执行成功").show();
            }
        });
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BKStateDialog(MainActivity.this, StateView.State.FORBID).setMessage("执行成功").show();
            }
        });
        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BKStateDialog(MainActivity.this, StateView.State.ERROR).setMessage("执行成功").setOnFinish(new BKStateDialog.Callback() {
                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this, "关闭Dialog回调", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
    }
}
