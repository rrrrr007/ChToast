package com.example.ch.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ch on 2017/11/27.
 */

public class TestCustomToast extends BaseCustomToast {

    @Override
    boolean autoHide() {
        return false;
    }

    @Override
    int duration() {
        return 5;
    }

    @Override
    int layoutId() {
        return R.layout.custom_view;
    }

    @Override
    int layoutHeight() {
        return 200;
    }

    @Override
    int type() {
        return 2;
    }

    @Override
    int background() {
        return Color.parseColor("#FEECEC");
    }

    @Override
    boolean hasLine() {
        return true;
    }

    @Override
    boolean hideBySelf() {
        return false;
    }

    @Override
    boolean hideByOut() {
        return true;
    }

    @Override
    public void initView(final Activity activity, View view) {
        view.findViewById(R.id.iv_custom_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "点击icon", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.iv_custom_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "点击image", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.btn_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "点击btn", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.tv_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "点击tv", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
