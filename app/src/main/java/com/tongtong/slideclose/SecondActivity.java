package com.tongtong.slideclose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by allen on 2017/11/18.
 */

public class SecondActivity extends SlideActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void jump2(View view) {
        openActivity(ThirdActivity.class);
    }

}
