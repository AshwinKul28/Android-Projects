package com.example.rugved.teacherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "name";
    static final String PREF_USER_ROLL= "tid";
//    static final String PREF_USER_YEAR= "year";
//    static final String PREF_USER_IMAGE= "img";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String name, String tid)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, name);
        editor.putString(PREF_USER_ROLL, tid);
//        editor.putString(PREF_USER_YEAR,Year);
//        editor.putString(PREF_USER_IMAGE,image);


        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");

    }
    public static String getUserRoll(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_ROLL, "");

    }
//    public static String getUserYear(Context ctx)
//    {
//        return getSharedPreferences(ctx).getString(PREF_USER_YEAR, "");
//
//    }
//    public static String getUserImage(Context ctx)
//    {
//        return getSharedPreferences(ctx).getString(PREF_USER_IMAGE, "");
//
//    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}