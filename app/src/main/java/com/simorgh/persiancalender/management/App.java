package com.simorgh.persiancalender.management;

import android.app.Application;

import com.simorgh.persiancalender.model.CalenderManager;

import java.util.Calendar;
import java.util.TimeZone;

public class App extends Application {
    public static final String TAG = "debug13";

    public static CalenderManager cm;

    @Override
    public void onCreate() {
        super.onCreate();

        // init the calender manger
        cm = new CalenderManager(Calendar.getInstance(TimeZone.getDefault()));

    }
}
