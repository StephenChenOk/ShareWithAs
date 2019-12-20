package com.chen.fy.sharewithas.applications;

import android.app.Application;
import android.content.Context;


public class ContextApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
