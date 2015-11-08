package com.studygradle;

import android.app.Application;

/**
 * Created by grass on 11/8/15.
 */
public class StudyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        StartupLoader.loadStartupClasses(this);

    }
}
