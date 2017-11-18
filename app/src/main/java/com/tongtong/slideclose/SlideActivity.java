package com.tongtong.slideclose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tongtong.slideclose.slide.SlidingLayout;

/**
 * 侧滑Activity基类
 * Created by allen on 2017/11/18.
 */

public abstract class SlideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.mActivities.add(this);
        if (isNeedSlide()) {
            SlidingLayout slidingLayout = new SlidingLayout(this);
            slidingLayout.bindActivity(this);
        }
    }

    public boolean isNeedSlide() {
        return true;    //默认都允许侧滑
    }

}
