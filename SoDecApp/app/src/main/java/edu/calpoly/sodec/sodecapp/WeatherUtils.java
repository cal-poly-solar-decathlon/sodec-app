package edu.calpoly.sodec.sodecapp;

public class WeatherUtils {
    private static final float FAHR_CONV_MULT = 9f / 5f;
    private static final int FAHR_CONV_BASE = 32;

    public static float celsiusToFahrenheit(float degreesCelsius) {
        return FAHR_CONV_MULT * degreesCelsius + FAHR_CONV_BASE;
    }
}
