package com.example.ch.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by ch on 2017/11/22.
 */

public class MiguToast {
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#5E5E5E");//字体颜色
    private static final int DEFAULT_BACKGROUND = Color.parseColor("#ffffff");//背景色
    public static final int DEFAULT_TYPE_FROM_TOP = 1;//1.从顶部弹出   2从底部弹出；
    public static final int DEFAULT_TYPE_FROM_BOTTOM = 2;

    private static final int DEFAULT_ICON_RESOURCE = -1;//图片资源id
    private static final String DEFAULT_CONTENT = "ch";//显示内容
    private static final int DEFAULT_LAYOUT_ID = R.layout.toast;//默认布局id(可自定义)
    private static final int DEFAULT_LAYOUT_HEIGHT = 44;//布局高度dp（需要传自定义布局高度）(不包括状态栏)
    private static final int DEFAULT_DURATION = 3;//间隔3秒

    private static final boolean DEFAULT_AUTO_HIDE = true;//是否自动消失
    private static final boolean DEFAULT_HAS_LINE = true;//是否需要底部阴影
    private static final boolean DEFAULT_HIDE_BY_SELF = true;//点击自己消失;
    private static final boolean DEFAULT_HIDE_BY_OUT = false;//点击外部消失;

    private static final MiguToast mInstance = new MiguToast();

    private MiguToast() {
    }

    public static Builder builder(Activity activity) {
        return new Builder(activity);
    }

    private Handler mHandler = new Handler();

    private void cancel(ViewGroup group) {
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
            makeExistingViewAndRemove(group);
        }
    }

    private void show(Builder builder) {
        final Activity activity = builder.activity.get();
        final ViewGroup group = (ViewGroup) activity.getWindow().getDecorView();
        View view = LayoutInflater.from(activity).inflate(builder.layoutId, null);
        final LinearLayout layout = new LinearLayout(activity);
        final int type = builder.type;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, type == 2 ? convertToDp(builder.height, activity) : getStatusBarHeight(activity) + convertToDp(builder.height, activity));
        layoutParams.gravity = (type == 2 ? Gravity.BOTTOM : Gravity.TOP);
        layout.setLayoutParams(layoutParams);
        layout.setPadding(0, type == 2 ? 0 : getStatusBarHeight(activity), 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && builder.hasLine) {
            layout.setElevation(convertToDp(2, activity));
        }
        layout.setBackgroundColor(builder.background);
        cancel(group);
        if (builder.bySelf) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 2) {
                        layout.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.from_bottom_hide));
                    } else {
                        layout.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.from_top_hide));
                    }
                    group.removeView(layout);
                }
            });
        }
        if (builder.byOut) {
            final LinearLayout out = new LinearLayout(activity);
            FrameLayout.LayoutParams outParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            out.setLayoutParams(outParams);
            out.setId(R.id.outId);
            out.setClickable(true);
            out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 2) {
                        layout.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.from_bottom_hide));
                    } else {
                        layout.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.from_top_hide));
                    }
                    group.removeView(layout);
                    group.removeView(out);
                }
            });
            group.addView(out);
        }
        if (builder.layoutId == DEFAULT_LAYOUT_ID) {
            ImageView iv = (ImageView) view.findViewById(R.id.toast_iv);
            if (builder.iconResource == -1) {
                iv.setVisibility(View.GONE);
            } else {
                iv.setImageDrawable(ContextCompat.getDrawable(activity, builder.iconResource));
            }

            TextView tv = (TextView) view.findViewById(R.id.toast_tv);
            tv.setText(builder.content);
            tv.setTextColor(builder.textColor);
        } else {
            //自定义处理
            if (builder.set != null) {
                builder.set.initView(activity, view);
            }
        }
        layout.addView(view);
        layout.setId(R.id.mainId);
        group.addView(layout, layoutParams);
        if (type == 2) {
            layout.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.from_bottom_show));
        } else {
            layout.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.from_top_show));
        }
        if (builder.autoHide) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    group.removeView(layout);
                }
            }, builder.duration * 1000);
        }
    }

    private int getStatusBarHeight(Activity activity) {
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;

        return statusBarHeight;
    }

    private int convertToDp(float sizeInDp, Activity activity) {
        float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (sizeInDp * scale + 0.5f);
    }

    public void makeExistingViewAndRemove(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getId() == R.id.mainId || child.getId() == R.id.outId) {
                parent.removeView(child);
            }
            if (child instanceof ViewGroup) {
                makeExistingViewAndRemove((ViewGroup) child);
            }
        }
    }

    /**
     * ————————————————公共方法—————————————————————
     * 一般提示
     */

    public static void showNomalNotice(Activity activity, String url) {
        builder(activity).content(url).build();
    }

    /**
     * 成功提示
     */

    public static void showSuccessNotice(Activity activity, String url) {
        builder(activity).content(url).iconResource(R.drawable.icon_toast_success).build();
    }

    /**
     * 失败提示
     */

    public static void showFailNotice(Activity activity, String url) {
        builder(activity).content(url)
                .iconResource(R.drawable.icon_toast_fail)
                .textColor(Color.parseColor("#FB2F2F"))
                .background(Color.parseColor("#FEECEC"))
                .hasLine(false)
                .build();
    }

    /**
     * 手动关闭
     */

    public static void hide(Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        mInstance.cancel(vp);
    }


    public static class Builder {
        private final WeakReference<Activity> activity;
        private int type = DEFAULT_TYPE_FROM_TOP;
        private boolean autoHide = DEFAULT_AUTO_HIDE;
        private int duration = DEFAULT_DURATION;
        private int background = DEFAULT_BACKGROUND;
        private int layoutId = DEFAULT_LAYOUT_ID;
        private int height = DEFAULT_LAYOUT_HEIGHT;
        private boolean hasLine = DEFAULT_HAS_LINE;
        private boolean bySelf = DEFAULT_HIDE_BY_SELF;
        private boolean byOut = DEFAULT_HIDE_BY_OUT;
        private CustomSet set;
        //默认布局属性
        private int textColor = DEFAULT_TEXT_COLOR;
        private int iconResource = DEFAULT_ICON_RESOURCE;
        private String content = DEFAULT_CONTENT;


        public Builder(@NonNull Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        public Builder background(int background) {
            this.background = background;
            return this;
        }

        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder iconResource(int iconResource) {
            this.iconResource = iconResource;
            return this;
        }

        public Builder content(@NonNull String content) {
            this.content = content;
            return this;
        }

        public Builder setView(int layoutId, int height) {
            this.layoutId = layoutId;
            this.height = height;
            return this;
        }

        public Builder duration(int seconds) {
            this.duration = seconds;
            return this;
        }

        public Builder initCustomView(CustomSet customSet) {
            this.set = customSet;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder autoHide(boolean is) {
            this.autoHide = is;
            return this;
        }

        public Builder hasLine(boolean hasLine) {
            this.hasLine = hasLine;
            return this;
        }

        public Builder hideBySelf(boolean is) {
            this.bySelf = is;
            return this;
        }

        public Builder hideByOut(boolean is) {
            this.byOut = is;
            return this;
        }

        public void build() {
            mInstance.show(this);
        }
    }

    interface CustomSet {
        void initView(Activity activity, View view);
    }
}
