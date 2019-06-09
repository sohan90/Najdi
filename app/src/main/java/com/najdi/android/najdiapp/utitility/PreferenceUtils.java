package com.najdi.android.najdiapp.utitility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static final String SHARED_PREF = "pref_auth_token";
    public static final String USER_ID_KEY = "user_id";
    public static final String USER_EMAIL_KEY = "user_email";
    public static final String USER_NAME_KEY = "user_name";

    public static String getValueString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public static void setValueString(Context context, String key, String value) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static int getValueInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        return preferences.getInt(key, -1);
    }

    public static void setValueInt(Context context, String key, int value) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}