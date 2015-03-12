package edu.calpoly.sodec.sodecapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edu.calpoly.sodec.sodecapp.ServerConnection.ResponseCallback;

import java.util.Map;

/** Receives timed events indicating that sensor data should be collected from the server. */
public class SensorCollectionReceiver extends BroadcastReceiver {

    public static final String ACTION_COLLECT_SENSOR_DATA = "COLLECT_SENSOR_DATA";

    public SensorCollectionReceiver() { /* No set-up needed. */ }


    @Override
    public void onReceive(Context context, Intent intent) {
        new ServerConnection().getPowerGenerated(new ResponseCallback<String, String>() {
            @Override
            public void execute(String response) {

            }
            /*@Override
            public void execute(Map<String, String> json) {
                // TODO: We need to decide where we want to save the data locally and if there
                // is anything else we want to do at these polling intervals.
            }*/

        });
    }

}
