package com.najdi.android.najdiapp.utitility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static com.najdi.android.najdiapp.common.Constants.ARABIC_LAN;

public class PreferenceUtils {

    private static final String SHARED_PREF = "pref_auth_token";
    public static final String USER_ID_KEY = "user_new_id";
    public static final String USER_EMAIL_KEY = "user_email";
    public static final String USER_NAME_KEY = "user_name";
    public static final String USER_PHONE_NO_KEY = "phone_no";
    public static final String USER_PASSWORD = "password";
    public static final String USER_LOGIIN_TOKEN = "login_token";
    public static final String LOCALE_LANG = "locale";
    public static final String FCM_TOKEN_KEY = "fcm_token_key";
    public static final String USER_SELECTED_CITY = "user_selected_city";
    public static final String USER_LAT_LONG = "user_lat_long";
    public static final String IS_GUEST_USER = "is_guest_user";

    public static String getValueString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public static String getLangFromPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        return preferences.getString(PreferenceUtils.LOCALE_LANG, ARABIC_LAN);
    }

    public static void setValueString(Context context, String key, String value) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setValueBoolean(Context context, String key, boolean value) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public static boolean getValueBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
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

    public static void setValueLong(Context context, String key, long value) {
        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getValueLong(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);
        return preferences.getLong(key, -1);
    }
}
