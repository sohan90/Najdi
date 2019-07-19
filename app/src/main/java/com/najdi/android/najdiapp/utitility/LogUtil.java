package com.najdi.android.najdiapp.utitility;


import android.util.Log;

import com.najdi.android.najdiapp.BuildConfig;


/**
 */
public class LogUtil {

    private static boolean isDebugEnabled = BuildConfig.DEBUG;

    /**
     * @param tag     TAG
     * @param message Message
     */
    public static void d(String tag, String message) {
        if (isDebugEnabled && message != null)
            Log.d(tag, message);
    }

    /**
     * @param tag     TAG
     * @param message MESSAGE
     */
    public static void i(String tag, String message) {
        if (isDebugEnabled && message != null)
            Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        if (isDebugEnabled && message != null)
            Log.e(tag, message);
    }
}
