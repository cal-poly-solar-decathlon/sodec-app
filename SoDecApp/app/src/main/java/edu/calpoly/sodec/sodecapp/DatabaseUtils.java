package edu.calpoly.sodec.sodecapp;

import android.graphics.Color;
import android.util.Log;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;

public class DatabaseUtils {
    private static final float FAHR_CONV_MULT = 9f / 5f;
    private static final int FAHR_CONV_BASE = 32;

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
    private static final String TAG = "WeatherUtils";

    private static final String DEGREE_NOTATION = "°F";
    private static final String PERCENT_NOTATION = "%";


    public static float celsiusToFahrenheit(float degreesCelsius) {
        return FAHR_CONV_MULT * degreesCelsius + FAHR_CONV_BASE;
    }

    public static String formatTemp(float degrees) {
        return new DecimalFormat("#.#").format(degrees) + DEGREE_NOTATION;
    }

    public static String formatHumidity(float percentage) {
        return new DecimalFormat("#.#").format(percentage) + PERCENT_NOTATION;
    }

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

                            onFinish.execute(Float.toString(celsiusToFahrenheit(tempAvg)));
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

                        onFinish.execute(Float.toString(celsiusToFahrenheit(jsonResponse.getInt(TEMP) * TEMP_CONV_MULT)));

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
                            celsiusToFahrenheit(jsonResponse.getInt(TEMP) * TEMP_CONV_MULT)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, DEVICE_OUTSIDE);
    }

    /* Fetches the temperature provided by a Weather Provider instead of a house sensor */
    public static void getGeneralAreaTemp(final WeatherClient weather, final ServerConnection.ResponseCallback onFinish) {
        // TODO: Look for alternative ways to get weather info by lat/long
        //       Currently, none of the supported providers play nicely with lat/long
        weather.searchCity("San Luis Obispo", new WeatherClient.CityEventListener() {
            @Override
            public void onCityListRetrieved(List<City> cities) {
                if (cities.size() > 0) {
                    weather.getCurrentCondition(new WeatherRequest(cities.get(0).getId()),
                            new WeatherClient.WeatherEventListener() {
                                @Override
                                public void onWeatherRetrieved(CurrentWeather cWeather) {
                                    onFinish.execute(Double.toString(celsiusToFahrenheit(
                                            cWeather.weather.temperature.getTemp())));
                                    /*mCurrentTemperature.setText(WeatherUtils.celsiusToFahrenheit(
                                            cWeather.weather.temperature.getTemp()) +
                                            getString(R.string.degree_notation));*/
                                }

                                @Override
                                public void onWeatherError(WeatherLibException ex) {
                                    Log.d(TAG, "weather error " + ex.getMessage());
                                }

                                @Override
                                public void onConnectionError(Throwable t) {
                                    Log.d(TAG, "connection error " + t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onWeatherError(WeatherLibException ex) {
                Log.d(TAG, "weather error " + ex.getMessage());
            }

            @Override
            public void onConnectionError(Throwable t) {
                Log.d(TAG, "connection error " + t.getMessage());
            }
        });
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
