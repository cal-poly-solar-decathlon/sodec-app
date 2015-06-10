package edu.calpoly.sodec.sodecapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.provider.yahooweather.YahooProviderType;

import edu.calpoly.sodec.sodecapp.PowerCache.CacheManager;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class DashboardActivity extends ActionBarActivity {

    private CacheManager mCache;

    private PieChartView mPowerPieChart;
    private LineChartView mPowerLineChart;
    private LineChartData mPowerData;

    private RelativeLayout mLightsGroup;
    private TextView mNumLightsOn;

    private LinearLayout mInsightsGroup;

    private LinearLayout mTemperatureGroup;
    private TextView mGenAreaTemp;
    private TextView mInsideTemp;
    private TextView mOutsideTemp;
    private WeatherClient mWeather;

    private BannerLayout bannerLayout;

    private static final String TAG = "Dashboard";
    private LightingUtils lightUtils = new LightingUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bannerLayout = new BannerLayout(this);
        //bannerLayout.addView(R.layout.activity_main);

        setContentView(R.layout.activity_dashboard);
        View view = findViewById(R.id.toplayout);
        ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);
        bannerLayout.addView(view);
        parent.addView(bannerLayout);
        bannerLayout.setPageTitleText("Main Page");
        //setContentView(bannerLayout);

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
        initSensorCollection();
        mCache = CacheManager.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPowerInfo();
        loadLightingInfo();
        loadWeatherInfo();
    }

    private void initLayout() {
        //setContentView(R.layout.activity_dashboard);
        mPowerLineChart = (LineChartView) findViewById(R.id.dashPowerLineChart);
        mPowerPieChart = (PieChartView) this.findViewById(R.id.dashPowerPieChart);
        mLightsGroup = (RelativeLayout) this.findViewById(R.id.dashLightsGroup);
        mNumLightsOn = (TextView) this.findViewById(R.id.dashLightsOnCount);

        mTemperatureGroup = (LinearLayout) this.findViewById(R.id.dashTempGroup);
        mGenAreaTemp = (TextView) this.findViewById(R.id.dashTempGenArea);
        mInsideTemp = (TextView) this.findViewById(R.id.dashTempInside);
        mOutsideTemp = (TextView) this.findViewById(R.id.dashTempOutside);

        mInsightsGroup = (LinearLayout) this.findViewById(R.id.dashInsightsGroup);
    }

    private void loadPowerInfo() {
        // Stick with graph placeholder for now because the power section is a little cramped
        /*String startTime = TimestampUtils.getStartIsoForDay();
        String endTime = TimestampUtils.getIsoForNow();

        mPowerData = new LineChartData();
        PowerGraphUtils.initPoints(mPowerData, mPowerLineChart, PowerGeneratedActivity.DEVICE,
                PowerGraphUtils.BASE_POWER, startTime, endTime);*/
    }

    private void loadLightingInfo() {
        lightUtils.getAllLightData(new ServerConnection.ResponseCallback<String, String>() {
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
                mInsideTemp.setText(WeatherUtils.formatTemp(Float.parseFloat(response)) +
                        " " + getString(R.string.dash_temp_inside));
            }
        });

        DatabaseUtils.getOutsideTemp(new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mOutsideTemp.setText(WeatherUtils.formatTemp(Float.parseFloat(response)) +
                        " " + getString(R.string.dash_temp_outside));
            }
        });

        WeatherUtils.getGeneralAreaTemp(mWeather, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mGenAreaTemp.setText(WeatherUtils.formatTemp(Float.parseFloat(response)) +
                        " " + getString(R.string.dash_temp_general));
            }
        });
    }

    private void initListeners() {
        mPowerLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, PowergraphActivity.class));
            }
        });
        mPowerPieChart.setOnClickListener(new View.OnClickListener() {
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
        mInsightsGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, SuggestionsActivity.class));
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
                new Intent(PowerCacheIntentService.ACTION_UPDATE),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!sensorCollectionStarted) {
            sendBroadcast(new Intent(BootReceiver.ACTION_START_COLLECTION));
        }
    }
}
