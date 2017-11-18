package com.tongtong.slideclose;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allen on 2017/11/18.
 */

public class MyApp extends Application {

    public static List<Activity> mActivities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
