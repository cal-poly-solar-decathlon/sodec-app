package edu.calpoly.sodec.sodecapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Listens for the device to boot up, then makes sure the app is requesting
 * the various readings every so often.
 * */
public class BootReceiver extends BroadcastReceiver {

    public static final String ACTION_START_COLLECTION = "edu.calpoly.sodec.sodecapp.action.SCHEDULE_COLLECTION";

    public BootReceiver() { /* No set-up needed. */ }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent checkCache = new Intent(context, PowerCacheIntentService.class);
        checkCache.setAction(PowerCacheIntentService.ACTION_UPDATE);
        PendingIntent pending = PendingIntent.getService(context, 0,
                checkCache,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        Log.d("BootReceiver", "Scheduling caching");
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                0,
                AlarmManager.INTERVAL_HALF_DAY,
                pending
        );
    }
}
