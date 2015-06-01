package edu.calpoly.sodec.sodecapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Methods for dealing with timestamps
 */
public class TimestampUtils {

    private static final int END_MILLIS = 999;
    private static final int END_SECONDS = 59;
    private static final int END_MINUTES = 59;
    private static final int END_HOURS = 23;

    public static final int DAYS_PER_WEEK = 7;
    // Generally we will always want the past 30 days regardless of actual month length
    public static final int DAYS_PER_MONTH = 30;
    public static final int DAYS_PER_YEAR = 365;

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss"
     */
    public static String getIsoForNow() {
        return Long.toString(System.currentTimeMillis()/1000);
    }

    public static String getStartIsoForMonth() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        Date lastMonth = cal.getTime();
        return Long.toString(lastMonth.getTime()/1000);
    }

    public static String getStartIsoForWeek() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        Date lastWeek = cal.getTime();
        return Long.toString(lastWeek.getTime()/1000);
    }

    public static String getStartIsoForDay(){
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDay = cal.getTime();
        return Long.toString(lastDay.getTime()/1000);
    }

    /**
     * @param dayDifference Difference between today and the desired day. (0 for today, -1 for yesterday, etc)
     * @return Calendar instance at the beginning of the specified day.
     */
    public static Calendar getStartOfDay(int dayDifference) {
        Calendar dayStart = new GregorianCalendar();

        dayStart.add(Calendar.DAY_OF_MONTH, dayDifference);
        dayStart.set(Calendar.HOUR_OF_DAY, 0);
        dayStart.set(Calendar.MINUTE, 0);
        dayStart.set(Calendar.SECOND, 0);
        dayStart.set(Calendar.MILLISECOND, 0);

        return dayStart;
    }

    /**
     * @param dayDifference Difference between today and the desired day. (0 for today, -1 for yesterday, etc)
     * @return Calendar instance at the end of the specified day.
     */
    public static Calendar getEndOfDay(int dayDifference) {
        Calendar dayEnd = new GregorianCalendar();

        dayEnd.add(Calendar.DAY_OF_MONTH, dayDifference);
        dayEnd.set(Calendar.HOUR_OF_DAY, END_HOURS);
        dayEnd.set(Calendar.MINUTE, END_MINUTES);
        dayEnd.set(Calendar.SECOND, END_SECONDS);
        dayEnd.set(Calendar.MILLISECOND, END_MILLIS);

        return dayEnd;
    }

    public static String getStartIsoForYear(){
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -365);
        Date lastDay = cal.getTime();
        return Long.toString(lastDay.getTime()/1000);
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date
     *            Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    private static String getISO(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return dateFormat.format(date);
    }

    /**
     * Private constructor: class cannot be instantiated
     */
    private TimestampUtils() {
    }
}