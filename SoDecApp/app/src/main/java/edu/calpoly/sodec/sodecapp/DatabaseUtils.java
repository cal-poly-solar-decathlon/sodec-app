package edu.calpoly.sodec.sodecapp;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import lecho.lib.hellocharts.model.PointValue;

public class DatabaseUtils {


    private static final String DEVICE_OUTSIDE = "s-temp-out";
    private static final String DEVICE_BED = "s-temp-bed";
    private static final String DEVICE_BATH = "s-temp-bath";
    private static final String DEVICE_LIVING_ROOM = "s-temp-lr";
    private static final String TEMP = "status";
    // All temperatures we receive are in tenths of degrees
    private static final float TEMP_CONV_MULT = 1f / 10f;




    public static void getInsideTemp(final ServerConnection.ResponseCallback onFinish) {
        final AtomicInteger tempCount = new AtomicInteger(0);
        final AtomicReference<Float> tempSum = new AtomicReference<>(0f);
        final String[] insideDevices = {DEVICE_BATH, DEVICE_BED, DEVICE_LIVING_ROOM};

        for (String device : insideDevices) {
            new ServerConnection().getLatestEvent(new ServerConnection.ResponseCallback<String, String>() {
                @Override
                public void execute(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        tempSum.set(tempSum.get() + jsonResponse.getInt(TEMP) * TEMP_CONV_MULT);
                        if (tempCount.incrementAndGet() >= insideDevices.length) {
                            float tempAvg = tempSum.get() / (float) insideDevices.length;

                            onFinish.execute(Float.toString(WeatherUtils.celsiusToFahrenheit(tempAvg)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, device);
        }
    }

    public static void getTempByID(String deviceId, final ServerConnection.ResponseCallback onFinish) {
        new ServerConnection().getLatestEvent(new ServerConnection.ResponseCallback<String, String>() {
                @Override
                public void execute(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        onFinish.execute(Float.toString(WeatherUtils.celsiusToFahrenheit(jsonResponse.getInt(TEMP) * TEMP_CONV_MULT)));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, deviceId);

    }


    public static void getOutsideTemp(final ServerConnection.ResponseCallback onFinish) {
        new ServerConnection().getLatestEvent(new ServerConnection.ResponseCallback<String, String>() {
            @Override
            public void execute(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    onFinish.execute(Float.toString(
                            WeatherUtils.celsiusToFahrenheit(jsonResponse.getInt(TEMP) * TEMP_CONV_MULT)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DEVICE_OUTSIDE);
    }




}
