package edu.calpoly.sodec.sodecapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PowerUtils {
    private static final String TEMP = "status";

    public static void getPowerByID(String deviceId, final ServerConnection.ResponseCallback onFinish) {
        new ServerConnection().getLatestEvent(new ServerConnection.ResponseCallback<String, String>() {
            @Override
            public void execute(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    onFinish.execute(Float.toString(jsonResponse.getInt(TEMP)));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, deviceId);

    }
}
