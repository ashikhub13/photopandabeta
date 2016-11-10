package com.example.ashik.photopandabeta;

import android.text.format.DateUtils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * DateFormatHelper: Helper class for all date related operations
 *
 * @author - Rahul Padmkaumar
 * @since - 2016-07-04
 */

public class DateFormatHelper {

    public static final String FORMAT_TIME_24_HR = "HH:mm";
    public static final String FORMAT_TIME_12_HR = "hh:mm a";
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String COMMON_DATE_FORMAT ="MMM dd, yyyy";
    public static final String FORMAT_HHMMAMMMddyyyy = "hh:mm a, MMM dd, yyyy";
    public static final String FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_MONTH_NAME = "MMM";
    public static final String FORMAT_DAY_NAME = "EEE";
    public static final String FORMAT_D_MMM_YYYY = "d MMM yyyy";
    public static final String FORMAT_DEFAULT = FORMAT_YYYY_MM_DD;
    public static final String FORMAT_YYYY_MM_DD_MM_SS ="yyyy-MM-dd hh:mm:ss";
    public static final String FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String TIMEZONE_UTC = "UTC";
    public static final String TIMEZONE_IST = "GMT+05:30";

    /**
     * Function to format the date.
     * @return Date in specified format in string.
     */
    public static String getMillisecInString(@SuppressWarnings("SameParameterValue") String format){
        return getDateString(new Date(), format);
    }

    /**
     * Function to format date using the specified format.
     * @param date Date to be formatted
     * @param format string format used for formatting
     * @return formatted date string
     */
    public static String getDateString(Date date, String format) {
        try {
            return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
        } catch (NullPointerException exception) {
            LogUtil.e("DateFormatHelper", exception.getMessage());
        }
        return "";
    }

    public static String getDateString(Date date) {
        return getDateString(date, FORMAT_DEFAULT);
    }

    /**
     * Function to parse Date from a date string using the specified date format.
     * @param dateString date string to be parsed
     * @param format date format used for parsing
     * @return Date object
     */
    public static Date getDate(String dateString, @SuppressWarnings("SameParameterValue") String format) {
        try {
            return new SimpleDateFormat(format, Locale.ENGLISH).parse(dateString);
        } catch (ParseException|NullPointerException e) {
            LogUtil.e(DateFormatHelper.class.getSimpleName(), e.getMessage());
        }
        return (new Date());
    }

    public static Date getDate(String dateString) {
        return getDate(dateString, FORMAT_DEFAULT);
    }

    /**
     * Returns the time as String in the specified format.
     * @param hours hours in int
     * @param minutes minutes in int
     * @param format specified String format
     * @return time as String e.g "10:30 AM"
     */
    public static String getTimeString(int hours, int minutes, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        return getDateString(calendar.getTime(), format);
    }

    public static Date getTime(String time) {
        return getDate(time, FORMAT_TIME_12_HR);
    }

    public static String getTimeString(int hours, int minutes) {
        return getTimeString(hours, minutes, FORMAT_TIME_12_HR);
    }

    /**
     * Checks if two dates are equal based on Year and Day of Year.
     * @param date1 first date
     * @param date2 second date
     * @return whether the dates are equal or not
     */
    public static boolean areDatesEqual(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        if (date1 == null || date2 == null) {
            return false;
        }
        calendar1.setTime(date1);
        calendar2.setTime(date2);

        return ((calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR))
                && (calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)));
    }

    public static boolean isToday(Date date) {
        return areDatesEqual(date, new Date());
    }

    public static String getDateString(String date, String reqFormat) {
        Date reqDate = getDate(date);
        return getDateString(reqDate, reqFormat);
    }

    public static String getDateString(String date, String givenFormat, String reqFormat) {
        Date reqDate = getDate(date, givenFormat);
        return getDateString(reqDate, reqFormat);
    }

    public static boolean areMonthAndYearEqual(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        if (date1 == null || date2 == null) {
            return false;
        }
        calendar1.setTime(date1);
        calendar2.setTime(date2);

        return ((calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH))
                && (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)));
    }

    public static String getMonthAndYear(Date date1) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        return getMonthForInt(calendar1.get(Calendar.MONTH))+" "+ calendar1.get(Calendar.YEAR);
    }

    public static String getMonthForInt( int num ) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if ( num >= 0 && num <= 11 ) {
            month = months[ num ];
        }
        return month;
    }

    /**
     * Function to convert UTC time to the specified time zone.
     * @param utcTime time to be converted
     * @param localTimeZone specified time zone
     * @return localTime in String
     */
    public static String convertUtcToLocalTime(String utcTime, String localTimeZone) {
        String time = convertUtcToLocalTime(utcTime, localTimeZone, FORMAT_TIME_12_HR);
        String date = convertUtcToLocalTime(utcTime, localTimeZone, COMMON_DATE_FORMAT);
        return date + " " + time;
    }

    public static String convertUtcToLocalTime(String utcTime, String localTimeZone, String requiredDateFormat) {
        if (!StringHelper.isEmptyString(utcTime)) {
            SimpleDateFormat utcFormat = new SimpleDateFormat(FORMAT_UTC, Locale.ENGLISH);
            utcFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
            Date date;
            try {
                date = utcFormat.parse(utcTime);
                SimpleDateFormat istFormat = new SimpleDateFormat(requiredDateFormat, Locale.ENGLISH);
                istFormat.setTimeZone(TimeZone.getTimeZone(localTimeZone));
                return istFormat.format(date);
            } catch (ParseException e) {
                LogUtil.e(DateFormatHelper.class.getSimpleName(), e.getMessage());
            }
            return "";
        } else {
            return "";
        }
    }

    public static String getIstFromUtc(String utcTime) {
        return convertUtcToLocalTime(utcTime, TIMEZONE_IST);
    }

    public static String getRelativeTimeFromUtc(String utcTime) {
        if (!StringHelper.isEmptyString(utcTime)) {
            SimpleDateFormat utcFormat = new SimpleDateFormat(FORMAT_UTC, Locale.ENGLISH);
            utcFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
            Date date;
            try {
                date = utcFormat.parse(utcTime);
                return DateUtils.getRelativeTimeSpanString(
                        date.getTime(),
                        new Date().getTime(),
                        DateUtils.SECOND_IN_MILLIS).toString();
            } catch (ParseException e) {
                LogUtil.e(DateFormatHelper.class.getSimpleName(), e.getMessage());
            }
            return "";
        } else {
            return "";
        }
    }

    public static String getIstFromUtc(String utcTime, String requiredFormat) {
        return convertUtcToLocalTime(utcTime, TIMEZONE_IST, requiredFormat);
    }
}
