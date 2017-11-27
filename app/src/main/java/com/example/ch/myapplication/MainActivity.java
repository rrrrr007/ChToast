package com.example.ch.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn1, btn2, btn3, btn4,btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setText("一般提示");
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setText("成功提示");
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setText("错误提示");
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setText("从下方弹出成功提示");
        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setText("自定义提示");
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                MiguToast.showNomalNotice(this,"一般提示");
                break;
            case R.id.btn2:
                MiguToast.showSuccessNotice(this,"成功提示");
                break;
            case R.id.btn3:
                MiguToast.showFailNotice(this,"错误提示");
                break;
            case R.id.btn4:
                MiguToast.builder(this).setType(MiguToast.DEFAULT_TYPE_FROM_BOTTOM)
                        .iconResource(R.drawable.ic_success).content("我来自底部").textColor(Color.parseColor("#ffffff"))
                        .background(Color.GREEN).build();
                break;
            case R.id.btn5:
                new TestCustomToast().show(this);
                break;
        }
    }
}
