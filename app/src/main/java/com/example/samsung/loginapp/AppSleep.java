package com.example.samsung.loginapp;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by samsung on 2/21/2018.
 */

public class AppSleep extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        SystemClock.sleep(3500);
    }
}
