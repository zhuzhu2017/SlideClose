package com.tongtong.slideclose;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends SlideActivity {

    private static final String TAG = "MainActivity";
    private View decorView;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(TAG);
        decorView = getWindow().getDecorView();
        width = getWindowManager().getDefaultDisplay().getWidth();
    }


    public void jump(View view) {
        ObjectAnimator.ofFloat(decorView, "translationX", 0, -width / 4).setDuration(300).start();
        startActivity(new Intent(this, SecondActivity.class));
    }

    @Override
    public boolean isNeedSlide() {
        return false;
    }
}
