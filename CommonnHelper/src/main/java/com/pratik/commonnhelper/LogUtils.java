package com.pratik.commonnhelper;

import android.util.Log;

public class LogUtils {

    public static void logD(final String tag, String message,boolean debugMode) {
        if (debugMode) {
            Log.d(tag, message);
        }
    }

    public static void logV(final String tag, String message,boolean debugMode) {
        if (debugMode) {
            Log.v(tag, message);
        }
    }

    public static void logI(final String tag, String message,boolean debugMode) {
        if (debugMode) {
            Log.i(tag, message);
        }
    }

    public static void logW(final String tag, String message,boolean debugMode) {
        if (debugMode) {
            Log.w(tag, message);
        }
    }

    public static void logE(final String tag, String message,boolean debugMode) {
        if (debugMode) {
            Log.e(tag, message);
        }
    }

}