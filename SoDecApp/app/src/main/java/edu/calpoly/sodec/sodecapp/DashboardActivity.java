package edu.calpoly.sodec.sodecapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.provider.yahooweather.YahooProviderType;

import de.tavendo.autobahn.WebSocketConnection;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class DashboardActivity extends ActionBarActivity {

    private LinearLayout mPowerGenGroup;
    private LinearLayout mPowerUseGroup;
    private PieChartView mPowerUseChart;
    private LineChartView mPowerGenChart;
    private LineChartData mPowerData;

    private RelativeLayout mLightsGroup;
    private TextView mNumLightsOn;

    private LinearLayout mTemperatureGroup;
    private TextView mGenAreaTemp;
    private TextView mInsideTemp;
    private TextView mOutsideTemp;
    private WeatherClient mWeather;

    private WebSocketConnection mSocketConnection;

    private static final String TAG = "Dashboard";
    private LightingUtils lightUtils = new LightingUtils();

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
        mPowerGenGroup = (LinearLayout) this.findViewById(R.id.dashPowerGen);
        mPowerGenChart = (LineChartView) findViewById(R.id.dashPowerGenChart);
        mPowerGenChart.setInteractive(false);

        mPowerUseGroup = (LinearLayout) this.findViewById(R.id.dashPowerUse);
        mPowerUseChart = (PieChartView) this.findViewById(R.id.dashPowerUseChart);
        mPowerUseChart.setInteractive(false);

        mLightsGroup = (RelativeLayout) this.findViewById(R.id.dashLightsGroup);
        mNumLightsOn = (TextView) this.findViewById(R.id.dashLightsOnCount);

        mTemperatureGroup = (LinearLayout) this.findViewById(R.id.dashTempGroup);
        mGenAreaTemp = (TextView) this.findViewById(R.id.dashTempGenArea);
        mInsideTemp = (TextView) this.findViewById(R.id.dashTempInside);
        mOutsideTemp = (TextView) this.findViewById(R.id.dashTempOutside);
    }

    private void loadPowerInfo() {
        // Stick with graph placeholder for now because the power section is a little cramped
        /*String startTime = TimestampUtils.getStartIsoForDay();
        String endTime = TimestampUtils.getIsoForNow();

        mPowerData = new LineChartData();
        PowerGraphUtils.initPoints(mPowerData, mPowerGenChart, PowerGeneratedActivity.DEVICE,
                PowerGraphUtils.BASE_POWER, startTime, endTime);*/
    }

    private void loadLightingInfo() {
        lightUtils.getAllLightData(new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mNumLightsOn.setText(lightUtils.numLightsOn.toString());
            }
        });
    }

    private void loadWeatherInfo() {
        DatabaseUtils.getInsideTemp(new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mInsideTemp.setText(DatabaseUtils.formatTemp(Float.parseFloat(response)) +
                        " " + getString(R.string.dash_temp_inside));
            }
        });

        DatabaseUtils.getOutsideTemp(new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mOutsideTemp.setText(DatabaseUtils.formatTemp(Float.parseFloat(response)) +
                        " " + getString(R.string.dash_temp_outside));
            }
        });

        DatabaseUtils.getGeneralAreaTemp(mWeather, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mGenAreaTemp.setText(DatabaseUtils.formatTemp(Float.parseFloat(response)) +
                        " " + getString(R.string.dash_temp_general));
            }
        });
    }

    private void initListeners() {
        mPowerGenGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, PowerGeneratedActivity.class));
            }
        });
        mPowerUseGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, PowerActivity.class));
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
