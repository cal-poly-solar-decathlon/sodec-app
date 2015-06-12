package edu.calpoly.sodec.sodecapp;


import android.util.Log;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import java.text.DecimalFormat;
import java.util.List;

public class WeatherUtils {

    private static final String TAG = "WeatherUtils";
    private static final float FAHR_CONV_MULT = 9f / 5f;
    private static final int FAHR_CONV_BASE = 32;
    private static final String DEGREE_NOTATION = "°F";
    private static final String PERCENT_NOTATION = "%";

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
                                    onFinish.execute(Double.toString(WeatherUtils.celsiusToFahrenheit(
                                            cWeather.weather.temperature.getTemp())));
                                }

                                @Override
                                public void onWeatherError(WeatherLibException ex) {
                                    Log.d(WeatherUtils.TAG, "weather error " + ex.getMessage());
                                }

                                @Override
                                public void onConnectionError(Throwable t) {
                                    Log.d(WeatherUtils.TAG, "connection error " + t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onWeatherError(WeatherLibException ex) {
                Log.d(WeatherUtils.TAG, "weather error " + ex.getMessage());
            }

            @Override
            public void onConnectionError(Throwable t) {
                Log.d(WeatherUtils.TAG, "connection error " + t.getMessage());
            }
        });
    }

    public static float celsiusToFahrenheit(float degreesCelsius) {
        return WeatherUtils.FAHR_CONV_MULT * degreesCelsius + WeatherUtils.FAHR_CONV_BASE;
    }

    public static String formatTemp(float degrees) {
        return new DecimalFormat("#.#").format(degrees) + WeatherUtils.DEGREE_NOTATION;
    }

    public static String formatHumidity(float percentage) {
        return new DecimalFormat("#.#").format(percentage) + WeatherUtils.PERCENT_NOTATION;
    }
}
