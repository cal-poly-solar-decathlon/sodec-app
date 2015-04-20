package edu.calpoly.sodec.sodecapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.model.Weather;
import com.survivingwithandroid.weather.lib.provider.yahooweather.YahooProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

public class DashboardActivity extends ActionBarActivity {

    private LinearLayout mPowerGroup;
    private LineChartView mPowerChart;
    private LineChartData mPowerData;

    private RelativeLayout mLightsGroup;
    private TextView mNumLightsOn;

    private LinearLayout mTemperatureGroup;
    private TextView mCurrentTemperature;
    private WeatherClient mWeather;

    private WebSocketConnection mSocketConnection;

    private static final String TAG = "Dashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();

        config.ApiKey = "a3ef2cfa095204d246373287a54aa91a";
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.lang = "en";
        config.maxResult = 5;
        config.numDays = 6;

        try {
            mWeather = builder.attach(this)
                    .provider(new YahooProviderType())
                    .httpClient(WeatherDefaultClient.class)
                    .config(config)
                    .build();
        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }

        initLayout();
        initListeners();
        mSocketConnection = ServerConnection.getSocketConnection();
        initSensorCollection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPowerInfo();
        loadLightingInfo();
        loadWeatherInfo();
    }

    private void initLayout() {
        setContentView(R.layout.activity_dashboard);
        mPowerGroup = (LinearLayout) this.findViewById(R.id.dashPowerGroup);
        mPowerChart = (LineChartView) findViewById(R.id.dashPowerChart);
        mPowerChart.setInteractive(false);

        mLightsGroup = (RelativeLayout) this.findViewById(R.id.dashLightsGroup);
        mNumLightsOn = (TextView) this.findViewById(R.id.dashLightsOnCount);

        mTemperatureGroup = (LinearLayout) this.findViewById(R.id.dashTempGroup);
        mCurrentTemperature = (TextView) this.findViewById(R.id.dashTempCurrent);
    }

    private void loadPowerInfo() {
        String startTime = TimestampUtils.getStartIsoForDay();
        String endTime = TimestampUtils.getIsoForNow();

        mPowerData = new LineChartData();
        PowerGraphUtils.initPoints(mPowerData, mPowerChart, PowerGeneratedActivity.DEVICE,
                PowerGraphUtils.BASE_POWER, startTime, endTime);
    }

    private void loadLightingInfo() {
        LightingUtils.getNumLightsOn(new ServerConnection.ResponseCallback<String, String>() {
            @Override
            public void execute(String response) {
                mNumLightsOn.setText(response);
            }
        });
    }

    private void loadWeatherInfo() {
        // TODO: Look for alternative ways to get weather info by lat/long
        //       Currently, none of the supported providers play nicely with lat/long
        mWeather.searchCity("San Luis Obispo", new WeatherClient.CityEventListener() {
            @Override
            public void onCityListRetrieved(List<City> cities) {
                if (cities.size() > 0) {
                    mWeather.getCurrentCondition(new WeatherRequest(cities.get(0).getId()),
                            new WeatherClient.WeatherEventListener() {
                        @Override
                        public void onWeatherRetrieved(CurrentWeather cWeather) {
                            mCurrentTemperature.setText(WeatherUtils.celsiusToFahrenheit(
                                    cWeather.weather.temperature.getTemp()) +
                                    getString(R.string.degree_notation));
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

    private void initListeners() {
        mPowerGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, PowerGeneratedActivity.class));
            }
        });
        mLightsGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, LightingActivity.class));
            }
        });
        mTemperatureGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, WeatherActivity.class));
            }
        });
    }

    /**
     * Broadcast the need to schedule sensor data collection if it's not already initialized.
     *
     * Based on Chris Knight's answer to http://stackoverflow.com/questions/4556670/how-to-check-if-alarmmanager-already-has-an-alarm-set
     * */
    private void initSensorCollection() {
        boolean sensorCollectionStarted = (PendingIntent.getBroadcast(this, 0,
                new Intent(SensorCollectionReceiver.ACTION_COLLECT_SENSOR_DATA),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!sensorCollectionStarted) {
            sendBroadcast(new Intent(BootReceiver.ACTION_START_COLLECTION));
        }
    }
}
