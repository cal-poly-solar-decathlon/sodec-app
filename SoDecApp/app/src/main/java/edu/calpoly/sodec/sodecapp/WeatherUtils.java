package edu.calpoly.sodec.sodecapp;

import android.util.Log;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class WeatherUtils {
    private static final float FAHR_CONV_MULT = 9f / 5f;
    private static final int FAHR_CONV_BASE = 32;

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
}
