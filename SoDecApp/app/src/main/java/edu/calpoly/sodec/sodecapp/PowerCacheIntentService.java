package edu.calpoly.sodec.sodecapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import edu.calpoly.sodec.sodecapp.PowerCache.CacheManager;
import edu.calpoly.sodec.sodecapp.PowerCache.PowerTask;

/**
 * Handles all updating for the power generated and usage cache.
 */
public class PowerCacheIntentService extends IntentService {
    private CacheManager mCache;
    private AtomicInteger mUseDaysFinished;
    private AtomicInteger mGenDaysFinished;

    private static final int WEEK_INCREMENT = 7;

    public static final String ACTION_UPDATE = "edu.calpoly.sodec.sodecapp.action.UPDATE_CACHE";

    private static final String TAG = "PowerCacheIntentService";

    public PowerCacheIntentService() {
        super("CacheIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Checking if there is new data to cache...");

        if (intent != null) {
            mCache = CacheManager.getInstance(this);
            mUseDaysFinished = new AtomicInteger(0);
            mGenDaysFinished = new AtomicInteger(0);
            cacheMissingDays();
        }
    }

    private void cacheMissingDays() {
        Calendar dayToStopBefore = mCache.getLastCachedDay();
        // If we need to cache more than two days, we'll fetch a week's worth of data at a time
        int dayIncr = dayToStopBefore.before(TimestampUtils.getStartOfDay(-2)) ? WEEK_INCREMENT : 1;
        Calendar calDayStart = TimestampUtils.getStartOfDay(-dayIncr);
        Calendar calDayEnd = TimestampUtils.getEndOfDay(-1);
        int numDaysToCache = calcDaysToCache(dayToStopBefore, TimestampUtils.getStartOfDay(-1));

        if (numDaysToCache == 0) {
            Log.i(TAG, "Nothing new to cache yet.");
            return;
        }

        while (calDayStart.after(dayToStopBefore)) {
            dispatchPowerTasks(PowerTask.TYPE_GET_GEN, Device.POW_GEN_DEVICES, dayIncr,
                    calDayStart.getTimeInMillis(), calDayEnd.getTimeInMillis(), numDaysToCache);
            dispatchPowerTasks(PowerTask.TYPE_GET_USE, Device.POW_USE_DEVICES, dayIncr,
                    calDayStart.getTimeInMillis(), calDayEnd.getTimeInMillis(), numDaysToCache);
            calDayStart.add(Calendar.DAY_OF_MONTH, -dayIncr);
            calDayEnd.add(Calendar.DAY_OF_MONTH, -dayIncr);
        }
        Log.i(TAG, "Finished dispatching power tasks.");
    }

    private int calcDaysToCache(Calendar lastCachedDay, Calendar startingDay) {
        int numDays = 0;

        while (lastCachedDay.before(startingDay)) {
            numDays++;
            startingDay.add(Calendar.DAY_OF_MONTH, -1);
        }
        return numDays;
    }

    private void dispatchPowerTasks(int taskType, String[] devices, int dayIncr, long dayStart,
                                    long dayEnd, int numDaysToCache) {
        for (final String device : devices) {
            mCache.addTask(new PowerTask(taskType, device, dayIncr, dayStart, dayEnd, numDaysToCache,
                    taskType == PowerTask.TYPE_GET_GEN ? mGenDaysFinished : mUseDaysFinished, this));
        }
    }
}
