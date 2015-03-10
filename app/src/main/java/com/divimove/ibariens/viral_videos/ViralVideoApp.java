package com.divimove.ibariens.viral_videos;

import android.app.Application;
import android.content.Context;

public class ViralVideoApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}