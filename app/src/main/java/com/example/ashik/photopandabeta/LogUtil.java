package com.example.ashik.photopandabeta;

import android.util.Log;

/**
 * Class for logging messages if BuildConfig is DEBUG.
 * @author Deena Philip
 * @since 2016-07-12
 */
public class LogUtil {

    /**
     * Logs debug message if in DEBUG mode.
     * @param tag tag of the log
     * @param message message to be logged
     */
    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (!StringHelper.isEmptyString(message))
                Log.d(tag, message);
        }
    }

    /**
     * Logs error message if in DEBUG mode.
     * @param tag tag of the log
     * @param message message to be logged
     */
    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (!StringHelper.isEmptyString(message))
                Log.e(tag, message);
        }
    }

    /**
     * Logs info message if in DEBUG mode.
     * @param tag tag of the log
     * @param message message to be logged
     */
    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (!StringHelper.isEmptyString(message))
                Log.i(tag, message);
        }
    }

    /**
     * Logs verbose message if in DEBUG mode.
     * @param tag tag of the log
     * @param message message to be logged
     */
    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (!StringHelper.isEmptyString(message))
                Log.v(tag, message);
        }
    }

    /**
     * Logs warning message if in DEBUG mode.
     * @param tag tag of the log
     * @param message message to be logged
     */
    public static void w(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (!StringHelper.isEmptyString(message))
                Log.w(tag, message);
        }
    }
}
