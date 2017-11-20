package com.tongtong.slideclose;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tongtong.slideclose.slide.SlidingLayout;

/**
 * 侧滑Activity基类
 * Created by allen on 2017/11/18.
 */

public abstract class SlideActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private View decorView;
    private int width;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.mActivities.add(this);
        setTitle(TAG);
        decorView = getWindow().getDecorView();
        width = getWindowManager().getDefaultDisplay().getWidth();
        if (isNeedSlide()) {
            SlidingLayout slidingLayout = new SlidingLayout(this);
            slidingLayout.bindActivity(this);
        }
    }

    public boolean isNeedSlide() {
        return true;    //默认都允许侧滑
    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle bundle) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ObjectAnimator.ofFloat(decorView, "translationX", 0, -width / 4).setDuration(300).start();
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyApp.mActivities.size() > 1) {
            Activity activity = MyApp.mActivities.get(MyApp.mActivities.size() - 2);
            View decorView = activity.getWindow().getDecorView();
            float translationX = decorView.getTranslationX();
            if (translationX < 0) {
                ObjectAnimator.ofFloat(decorView, "translationX", translationX, 0).setDuration(300).start();
            }
        }
        MyApp.mActivities.remove(this);
    }
}
