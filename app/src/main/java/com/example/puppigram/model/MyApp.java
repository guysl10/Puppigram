package com.example.puppigram.model;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    public static Context context;

    // To publicize the application context.
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
