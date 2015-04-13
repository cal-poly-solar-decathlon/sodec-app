package edu.calpoly.sodec.sodecapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Listens for the device to boot up, then makes sure the app is requesting
 * the various readings every so often.
 * */
public class BootReceiver extends BroadcastReceiver {

    public static final String ACTION_START_COLLECTION = "SCHEDULE_COLLECTION";

    public BootReceiver() { /* No set-up needed. */ }

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                new Intent(SensorCollectionReceiver.ACTION_COLLECT_SENSOR_DATA),
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                1*1000,
                10*1000,
                pending
        );
    }
}
