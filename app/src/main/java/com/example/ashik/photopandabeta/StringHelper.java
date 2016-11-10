package com.example.ashik.photopandabeta;

import android.text.Editable;

/**
 * StringHelper
 *
 * @author - Rahul Padmakumar
 * @since - 2016-07-14
 */

public class StringHelper {

    public static boolean isEmptyEditable(Editable editable) {

        if (editable != null) {
            return isEmptyString(editable.toString());
        } else {
            return true;
        }
    }

    public static boolean isEmptyString(String string) {

        return string == null || string.trim().isEmpty();
    }

    public static String getString(String s) {
        return isEmptyString(s) ? "" : s.trim();
    }

    public static String getTrimmedString(CharSequence text) {
        return text != null ? getTrimmedString(text.toString()) : "";
    }

    /**
     * Function to trim text and return empty String if given value is null.
     *
     * @param text given String to be trimmed
     * @return trimmed String value
     */
    public static String getTrimmedString(String text) {
        return text != null ? text.trim() : text;
    }
}
