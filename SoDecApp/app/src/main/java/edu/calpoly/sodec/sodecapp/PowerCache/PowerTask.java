package edu.calpoly.sodec.sodecapp.PowerCache;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import edu.calpoly.sodec.sodecapp.Device;
import edu.calpoly.sodec.sodecapp.ServerConnection;

public class PowerTask implements Runnable {

    // Shared by other threads doing the same type of caching (i.e. power generated)
    private AtomicInteger mTasksCompleted;

    private String mDevice;
    private long mStartMillis;
    private long mEndMillis;
    private int mType;

    // Usually a task will be fetching power data for a single day or a whole week
    private int mNumDays;

    // The day limit across all threads doing the same type of caching (associated with |mTasksCompleted|)
    private int mMaxCombinedDays;

    private CacheManager mCacheManager;

    private static final String SERIES_DATA = "seriesData";
    private static final long MILLIS_PER_SEC = TimeUnit.SECONDS.toMillis(1);
    private static final long MILLIS_PER_DAY = TimeUnit.DAYS.toMillis(1);
    private static final int SENSOR_READINGS_PER_DAY = 5760;
    // We want to save readings every 15 minutes which comes out to every 60 readings
    private static final int SAVE_INCREMENT = 60;

    public static final int TYPE_GET_GEN = 1;
    public static final int TYPE_GET_USE = 2;

    private static final String TAG = "PowerTask";

    public PowerTask(int taskType, String device, int numDays, long dayStart, long dayEnd,
                     int maxCombinedDays, AtomicInteger tasksCompleted, Context context) {
        mType = taskType;
        mDevice = device;
        mStartMillis = dayStart;
        mEndMillis = dayEnd;
        mNumDays = numDays;
        mMaxCombinedDays = maxCombinedDays;
        mTasksCompleted = tasksCompleted;
        mCacheManager = CacheManager.getInstance(context);
    }

    @Override
    public void run() {
        List<NameValuePair> params = new LinkedList<NameValuePair>();

        params.add(new BasicNameValuePair("device", mDevice));
        params.add(new BasicNameValuePair("start", Long.toString(mStartMillis / MILLIS_PER_SEC)));
        params.add(new BasicNameValuePair("end", Long.toString(mEndMillis / MILLIS_PER_SEC)));
        new ServerConnection().getEventsInRangeSynch(new ServerConnection.ResponseCallback<String, String>() {

            @Override
            public void execute(String response) {
                try {
                    bufferDays(new JSONObject(response).getJSONArray(SERIES_DATA));
                } catch (JSONException e) {
                    //Log.d("PowerTask", "No events found.");
                }
            }
        }, params);
    }

    private void bufferDays(JSONArray dayDeltas) {
        long startTime = mStartMillis;
        ContentValues dayData = null;

        for (int day = 0; day < mNumDays; day++, startTime += MILLIS_PER_DAY) {
            dayData = new ContentValues();

            dayData.put(PowerContract.PowerGen.COLUMN_BASE_TIMESTAMP, startTime);
            try {
                dayData.put(mCacheManager.getColForDevice(mDevice), getAggregatedPowerJson(day, dayDeltas));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            if (mType == TYPE_GET_GEN) {
                mCacheManager.addDayToGenBuffer(mDevice, dayData);
            }
            else {
                mCacheManager.addDayToUseBuffer(mDevice, dayData);
            }
            checkForFinalTask();
        }
    }

    /** Determine if this was the last task/day to be cached. Update the database if so. */
    private void checkForFinalTask() {
        int tasksCompleted = mTasksCompleted.incrementAndGet();

        if (tasksCompleted >= mMaxCombinedDays * Device.GEN_DEVICES.length &&
                mType == TYPE_GET_GEN) {
            Log.i(TAG, "Caching done for power generated");
            mCacheManager.updateGenTable();
        }
        else if (tasksCompleted >= mMaxCombinedDays * Device.USE_DEVICES.length &&
                mType == TYPE_GET_USE) {
            Log.i(TAG, "Caching done for power usage");
            mCacheManager.updateUseTable();
        }
    }

    /**
     * Produces a JSON array of power values. The first value will always be the total power for
     * the specified day. The rest of the values will be readings from 15 minute intervals
     * throughout the day.
     * */
    private String getAggregatedPowerJson(int day, JSONArray genDeltas) throws JSONException {
        long total = 0;
        long readingVal = 0;
        JSONArray aggPower = new JSONArray();

        // Reserve first spot for the total
        aggPower.put(0, 0);
        for (int reading = SENSOR_READINGS_PER_DAY * day; reading < SENSOR_READINGS_PER_DAY * (day+1)
                && reading < genDeltas.length(); reading++) {
            readingVal = ((JSONArray) genDeltas.get(reading)).getLong(1);
            total += readingVal;

            if ((reading+1) % SAVE_INCREMENT == 0) {
                aggPower.put(readingVal);
            }
        }
        aggPower.put(0, total);

        return aggPower.toString();
    }
}
