package edu.calpoly.sodec.sodecapp.PowerCache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.calpoly.sodec.sodecapp.Device;
import edu.calpoly.sodec.sodecapp.PowerCache.PowerContract.PowerGen;
import edu.calpoly.sodec.sodecapp.PowerCache.PowerContract.PowerUse;
import edu.calpoly.sodec.sodecapp.TimestampUtils;

public class CacheManager {
    private PowerDbHelper mDbHelper;

    private Map<Long, ContentValues> mGenBuffer;
    private Map<Long, ContentValues> mUseBuffer;
    
    // Maps every device name to its associated db column, since we have so many
    private Map<String, String> mDeviceToCol = null;

    private static CacheManager sInstance = null;
    private final BlockingQueue mCacheWorkQueue;
    private final ThreadPoolExecutor mCacheThreadPool;

    private static final int MIN_THREADS = 10;
    private static final int MAX_THREADS = 10;
    private static final int KEEP_ALIVE_TIME = 4;
    private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;

    public static final int MAX_DAYS = 365;

    private static final String TAG = "CacheManager";

    private CacheManager(Context context) {
        initDeviceToColMap();
        mDbHelper = new PowerDbHelper(context);
        mGenBuffer = new HashMap<>();
        mUseBuffer = new HashMap<>();

        mCacheWorkQueue = new LinkedBlockingQueue<Runnable>();
        mCacheThreadPool = new ThreadPoolExecutor(MIN_THREADS, MAX_THREADS, KEEP_ALIVE_TIME,
                KEEP_ALIVE_UNIT, mCacheWorkQueue);
    }

    public static CacheManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CacheManager(context);
        }
        return sInstance;
    }

    public void addTask(Runnable cacheTask) {
        mCacheThreadPool.execute(cacheTask);
    }

    public void updateGenTable() {
        updateDb(PowerGen.TABLE_NAME, mGenBuffer);
    }

    public void updateUseTable() {
        updateDb(PowerUse.TABLE_NAME, mUseBuffer);
    }

    /**
     * Flushes the given data buffer and saves any new data to the specified table.
     */
    private void updateDb(String tableName, Map<Long, ContentValues> dataToCache) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();

        for (ContentValues dayVals : dataToCache.values()) {
            db.insertWithOnConflict(
                    tableName,
                    null,
                    dayVals,
                    SQLiteDatabase.CONFLICT_IGNORE);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        dataToCache.clear();
    }

    public synchronized void addDayToGenBuffer(String device, ContentValues dayData) {
        addDayToBuffer(mGenBuffer, device, dayData);
    }

    public synchronized void addDayToUseBuffer(String device, ContentValues dayData) {
        addDayToBuffer(mUseBuffer, device, dayData);
    }

    private void addDayToBuffer(Map<Long, ContentValues> buffer, String device, ContentValues dayData) {
        long timestamp = dayData.getAsLong(PowerGen.COLUMN_BASE_TIMESTAMP);

        if (buffer.containsKey(timestamp)) {
            String insertCol = mDeviceToCol.get(device);

            buffer.get(timestamp).put(insertCol, (Long) dayData.get(insertCol));
        }
        else {
            buffer.put(timestamp, dayData);
        }
    }

    /**
     * Checks if power information has been cached for any previous day.
     * @return A Calendar instance for the most recent day that has cached data. In the case that
     *         there are no cached days, a Calendar instance for a year ago will be returned.
     *         (Because we only neeed a maximum of a year's worth of data.)
     */
    public Calendar getLastCachedDay() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder =
                PowerGen.COLUMN_BASE_TIMESTAMP + " DESC";
        Calendar lastDay = null;

        db.beginTransaction();
        // The days that are cached for power generation and usage should always be in sync so we
        // only check one table here
        Cursor dayCursor = db.query(
                PowerGen.TABLE_NAME,
                PowerGen.FULL_PROJECTION,
                null, null, null, null,
                sortOrder
        );

        if (dayCursor.moveToFirst()) {
            lastDay = new GregorianCalendar();
            lastDay.setTime(new Timestamp(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(
                    PowerGen.COLUMN_BASE_TIMESTAMP))));
        }
        else {
            Log.i(TAG, "No days cached yet.");
            lastDay = TimestampUtils.getStartOfDay(-MAX_DAYS - 1);
        }
        dayCursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        return lastDay;
    }

    public ArrayList<Long> getReadingsForDevice(String device, long oldestDay) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ArrayList<Long> readings = new ArrayList<>();
        String deviceCol = getColForDevice(device);

        String sortOrder = PowerUse.COLUMN_BASE_TIMESTAMP + " DESC";
        String[] projection = {PowerUse.COLUMN_BASE_TIMESTAMP, deviceCol};
        String table = device.equals(Device.GEN_BIFACIAL) || device.equals(Device.GEN_MAIN) ?
                PowerGen.TABLE_NAME : PowerUse.TABLE_NAME;

        db.beginTransaction();
        Cursor dayCursor = db.query(
                table,
                projection,
                null, null, null, null,
                sortOrder
        );

        if (dayCursor.moveToFirst()) {
            int deviceColIndex = dayCursor.getColumnIndexOrThrow(deviceCol);
            int timeColIndex = dayCursor.getColumnIndexOrThrow(PowerUse.COLUMN_BASE_TIMESTAMP);

            while (!dayCursor.isAfterLast() && dayCursor.getLong(timeColIndex) >= oldestDay) {
                readings.add(dayCursor.getLong(deviceColIndex));
                dayCursor.moveToNext();
            }
        }
        else {
            Log.i(TAG, "No cached readings found for device " + device);
        }
        dayCursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        return readings;
    }

    public void dumpUseCacheToConsole() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder =
                PowerUse.COLUMN_BASE_TIMESTAMP + " DESC";

        db.beginTransaction();
        Cursor dayCursor = db.query(
                PowerUse.TABLE_NAME,
                PowerUse.FULL_PROJECTION,
                null, null, null, null,
                sortOrder
        );

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        if (dayCursor.moveToFirst()) {
            do {
                Log.d(TAG, "----");
                Log.d(TAG, dateFormat.format(new Date(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(PowerUse.COLUMN_BASE_TIMESTAMP)))));
                Log.d(TAG, Long.toString(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(PowerUse.COLUMN_BASE_TIMESTAMP))));
                Log.d(TAG, Long.toString(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(PowerUse.COLUMN_USE_BEDROOM_RECEPS_1))));
                Log.d(TAG, Long.toString(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(PowerUse.COLUMN_USE_DISHWASHER))));
            } while (dayCursor.moveToNext());
        }
        else {
            Log.i(TAG, "Nothing found for power usage.");
        }
        dayCursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void dumpGenCacheToConsole() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sortOrder =
                PowerGen.COLUMN_BASE_TIMESTAMP + " DESC";

        db.beginTransaction();
        Cursor dayCursor = db.query(
                PowerGen.TABLE_NAME,
                PowerGen.FULL_PROJECTION,
                null, null, null, null,
                sortOrder
        );

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

        if (dayCursor.moveToFirst()) {
            do {
                Log.d(TAG, "----");
                Log.d(TAG, dateFormat.format(new Date(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(PowerGen.COLUMN_BASE_TIMESTAMP)))));
                Log.d(TAG, Long.toString(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(PowerGen.COLUMN_BASE_TIMESTAMP))));
                Log.d(TAG, Long.toString(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(PowerGen.COLUMN_GEN_MAIN))));
                Log.d(TAG, Long.toString(dayCursor.getLong(dayCursor.getColumnIndexOrThrow(PowerGen.COLUMN_GEN_BIFACIAL))));
            } while (dayCursor.moveToNext());
        }
        else {
            Log.i(TAG, "Nothing found for power generated.");
        }
        dayCursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public String getColForDevice(String device) {
        return mDeviceToCol.get(device);
    }

    private void initDeviceToColMap() {
        if (mDeviceToCol == null) {
            mDeviceToCol = new HashMap<>();
            mDeviceToCol.put(Device.GEN_MAIN, PowerGen.COLUMN_GEN_MAIN);
            mDeviceToCol.put(Device.GEN_BIFACIAL, PowerGen.COLUMN_GEN_BIFACIAL);
            mDeviceToCol.put(Device.DEVICE_LAUNDRY, PowerUse.COLUMN_USE_LAUNDRY);
            mDeviceToCol.put(Device.DEVICE_DISHWASHER, PowerUse.COLUMN_USE_DISHWASHER);

            mDeviceToCol.put(Device.DEVICE_REFRIGERATOR, PowerUse.COLUMN_USE_REFRIGERATOR);
            mDeviceToCol.put(Device.DEVICE_INDUCTION_STOVE, PowerUse.COLUMN_USE_INDUCTION_STOVE);
            mDeviceToCol.put(Device.DEVICE_EWH_SOLAR_WATER_HEATER, PowerUse.COLUMN_USE_EWH_SOLAR_WATER_HEATER);
            mDeviceToCol.put(Device.DEVICE_KITCHEN_RECEPS_1, PowerUse.COLUMN_USE_KITCHEN_RECEPS_1);
            mDeviceToCol.put(Device.DEVICE_KITCHEN_RECEPS_2, PowerUse.COLUMN_USE_KITCHEN_RECEPS_2);

            mDeviceToCol.put(Device.DEVICE_LIVING_RECEPS, PowerUse.COLUMN_USE_LIVING_RECEPS);
            mDeviceToCol.put(Device.DEVICE_DINING_RECEPS_1, PowerUse.COLUMN_USE_DINING_RECEPS_1);
            mDeviceToCol.put(Device.DEVICE_DINING_RECEPS_2, PowerUse.COLUMN_USE_DINING_RECEPS_2);
            mDeviceToCol.put(Device.DEVICE_BATHROOM_RECEPS, PowerUse.COLUMN_USE_BATHROOM_RECEPS);
            mDeviceToCol.put(Device.DEVICE_BEDROOM_RECEPS_1, PowerUse.COLUMN_USE_BEDROOM_RECEPS_1);

            mDeviceToCol.put(Device.DEVICE_BEDROOM_RECEPS_2 , PowerUse.COLUMN_USE_BEDROOM_RECEPS_2);
            mDeviceToCol.put(Device.DEVICE_MECHANICAL_RECEPS, PowerUse.COLUMN_USE_MECHANICAL_RECEPS);
            mDeviceToCol.put(Device.DEVICE_ENTRY_RECEPS, PowerUse.COLUMN_USE_ENTRY_RECEPS);
            mDeviceToCol.put(Device.DEVICE_EXTERIOR_RECEPS, PowerUse.COLUMN_USE_EXTERIOR_RECEPS);
            mDeviceToCol.put(Device.DEVICE_GREY_WATER_PUMP_RECEP, PowerUse.COLUMN_USE_GREY_WATER_PUMP_RECEP);

            mDeviceToCol.put(Device.DEVICE_BLACK_WATER_PUMP_RECEP, PowerUse.COLUMN_USE_BLACK_WATER_PUMP_RECEP);
            mDeviceToCol.put(Device.DEVICE_THERMAL_LOOP_PUMP_RECEP, PowerUse.COLUMN_USE_THERMAL_LOOP_PUMP_RECEP);
            mDeviceToCol.put(Device.DEVICE_WATER_SUPPLY_PUMP_RECEP, PowerUse.COLUMN_USE_WATER_SUPPLY_PUMP_RECEP);
            mDeviceToCol.put(Device.DEVICE_WATER_SUPPLY_BOOSTER_PUMP_RECEP, PowerUse.COLUMN_USE_WATER_SUPPLY_BOOSTER_PUMP_RECEP);
            mDeviceToCol.put(Device.DEVICE_VEHICLE_CHARGING_RECEP, PowerUse.COLUMN_USE_VEHICLE_CHARGING_RECEP);

            mDeviceToCol.put(Device.DEVICE_HEAT_PUMP_RECEP, PowerUse.COLUMN_USE_HEAT_PUMP_RECEP);
            mDeviceToCol.put(Device.DEVICE_AIR_HANDLER_RECEP, PowerUse.COLUMN_USE_AIR_HANDLER_RECEP);
        }
    }
}
