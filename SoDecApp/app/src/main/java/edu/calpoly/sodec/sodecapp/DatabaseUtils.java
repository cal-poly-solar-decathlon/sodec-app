package edu.calpoly.sodec.sodecapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseUtils {


    private static final String TEMP = "status";
    // All temperatures we receive are in tenths of degrees
    private static final float TEMP_CONV_MULT = 1f / 10f;




    public static void getInsideTemp(final ServerConnection.ResponseCallback onFinish) {
        final AtomicInteger tempCount = new AtomicInteger(0);
        final AtomicReference<Float> tempSum = new AtomicReference<>(0f);
        final String[] insideDevices = {Device.TEMP_BATH, Device.TEMP_BED, Device.TEMP_LIVINGROOM};

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
        }, Device.TEMP_OUTSIDE);
    }




}
