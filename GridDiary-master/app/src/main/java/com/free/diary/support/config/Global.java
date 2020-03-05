package com.free.diary.support.config;

import android.content.Context;

import com.free.diary.DiaryApplication;


public class Global {

    public static Context getContext() {
        return DiaryApplication.getContext();
    }

    public class SubjectType {
        public static final String APP = "APP";
        public static final String CUSTOM = "CUSTOM";
    }
}
