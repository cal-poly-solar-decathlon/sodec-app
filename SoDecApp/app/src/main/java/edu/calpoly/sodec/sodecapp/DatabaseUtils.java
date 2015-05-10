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


    private static final String BASE_TIME = "baseTimestamp";
    private static final String BASE_STATUS = "baseStatus";
    private static final String SERIES_DATA = "seriesData";



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

    public static void getTrendByID(String deviceId, String usage, String startTime, String endTime) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("device", deviceId));
        params.add(new BasicNameValuePair("start", startTime));
        params.add(new BasicNameValuePair("end", endTime));

        new ServerConnection().getEventsInRange(new ServerConnection.ResponseCallback<String, String>() {

            /**
             * @param response Should include a base timestamp, base power generated/usage value,
             *                 and pairs of time deltas and power deltas.
             */
            @Override
            public void execute(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Timestamp baseTime = new Timestamp((int) jsonResponse.get(BASE_TIME));
                    int baseStatus = (int) jsonResponse.get(BASE_STATUS);
                    JSONArray dataDeltas = jsonResponse.getJSONArray(SERIES_DATA);
                    List<PointValue> values = new ArrayList<PointValue>();
                    List<PointValue> incrValues = new ArrayList<PointValue>();

                    int numDeltas = dataDeltas.length();
                    int status = baseStatus;
                    int delta;

                    for (int i = 0 ; i < numDeltas; i++) {
                        if (i != 0) {
                            delta = ((JSONArray) dataDeltas.get(i)).getInt(1);
                            status += delta;
                        }

                        values.add(new PointValue(i,status));
                    }
                    // Getting every 10 minutes assuming it's time deltas of 5 seconds
                    for (int i = 0; i < values.size(); i += 120) {
                        incrValues.add(values.get(i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);

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

    private int findSlopeOfBestFitLine(List<PointValue> values) {
        int xMean;
        int xSum = 0;
        int yMean;
        int ySum = 0;

        int sumNominator = 0;
        int sumDenominator = 0;


        for(int i = 0; i < values.size(); i ++) {
            xSum += (int) values.get(i).getX();
        }
        for(int i = 0; i < values.size(); i ++) {
            ySum += (int) values.get(i).getY();
        }
        xMean = xSum / values.size();
        yMean = ySum/values.size();

        for(int i = 0; i < values.size(); i++) {
            PointValue point = values.get(i);
            int productNominator = (int)((point.getX() - xMean) * (point.getY() - yMean));
            sumNominator += productNominator;

            int productDenominator = (int)((point.getX() - xMean) * (point.getX() - xMean));
            sumDenominator += productDenominator;
        }

        return sumNominator/sumDenominator;
    }


}
