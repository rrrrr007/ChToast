package com.example.ch.myapplication;

import android.app.Activity;

/**
 * Created by ch on 2017/11/27.
 */

public abstract class BaseCustomToast implements MiguToast.CustomSet {
    /**
     * 是否需要自动消失
     */
    abstract boolean autoHide();

    /**
     * 多少秒后消失
     */
    abstract int duration();

    /**
     * 自定义的布局id
     */
    abstract int layoutId();

    /**
     * 自定义的布局的高度dp
     */
    abstract int layoutHeight();

    /**
     * 从顶部弹出还是从底部
     */
    abstract int type();

    /**
     * 背景颜色
     */

    abstract int background();

    /**
     * 是否需要底部阴影
     */

    abstract boolean hasLine();

    /**
     * 是否支持点击自己消失
     */

    abstract boolean hideBySelf();

    /**
     * 是否支持点击外部消失
     */

    abstract boolean hideByOut();

    /**
     * 初始化自定义View
     */
    private MiguToast.CustomSet customSet(){
        return this;
    }


    public void show(Activity activity) {
        MiguToast.builder(activity)
                .autoHide(autoHide())
                .setType(type())
                .background(background())
                .hasLine(hasLine())
                .setView(layoutId(), layoutHeight())
                .hideBySelf(hideBySelf())
                .hideByOut(hideByOut())
                .duration(duration())
                .initCustomView(customSet())
                .build();
    }
}
