package com.example.ashik.photopandabeta;
/**
 * SharedPreferenceManager - Provides convenience methods and abstraction for storing data
 * in the Shared Preference.
 * @author - Rahul Padmakumar
 * @since - 2016-07-07
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressWarnings("unused")
public class SharedPreferenceManager {

    private final SharedPreferences mSettings;
    private final Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferenceManager(Context ctx, String prefFileName) {

        mSettings = ctx.getSharedPreferences(prefFileName,
                Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    /***
     * Set a value for the key
     */
    public void setValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /***
     * Set a int value for the key
     */
    public void setValue(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /***
     * Set a float value for the key
     */
    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }

    /***
     * Set a long value for the key
     */
    public void setValue(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /****
     * Gets the value from the settings stored natively on the device.
     * @param defaultValue
     *            Default value for the key, if one is not found.
     */
    public String getValue(String key, String defaultValue) {
        return mSettings.getString(key, defaultValue);
    }

    public int getIntValue(String key, int defaultValue) {
        return mSettings.getInt(key, defaultValue);
    }

    public long getLongValue(String key, long defaultValue) {
        return mSettings.getLong(key, defaultValue);
    }

    /****
     * Gets the value from the preferences stored natively on the device.
     *
     * @param defValue
     *            Default value for the key, if one is not found.
     */
    public boolean getValue(String key, boolean defValue) {
        return mSettings.getBoolean(key, defValue);
    }

    public void setValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /****
     * Clear all the preferences store in this Editor.
     */
    public void clear() {
        mEditor.clear().commit();
    }

    /**
     * Removes preference entry for the given key.
     *
     * @param key - Key of the field to be removed.
     */
    public void removeValue(String key) {
        if (mEditor != null) {
            mEditor.remove(key).commit();
        }
    }
}