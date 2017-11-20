package com.tongtong.slideclose;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends SlideActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void jump(View view) {
        openActivity(SecondActivity.class);
    }

    @Override
    public boolean isNeedSlide() {
        return false;
    }
}
