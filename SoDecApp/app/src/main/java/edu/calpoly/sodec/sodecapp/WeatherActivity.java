package edu.calpoly.sodec.sodecapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.provider.forecastio.ForecastIOProviderType;


public class WeatherActivity extends ActionBarActivity {

    private TextView mBedroomWeatherView;
    private TextView mBathroomWeatherView;
    private TextView mLivingRoomWeatherView;
    private TextView mWeatherTitleView;

    private Button mViewTempButton;
    private Button mViewHumidityButton;
    private BannerLayout bannerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();
        config.ApiKey = "a3ef2cfa095204d246373287a54aa91a";
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.lang = "en"; // Use english
        config.maxResult = 5; // Max number of cities retrieved
        config.numDays = 6; // Max num of days in the forecast

        try {
            WeatherClient client = builder.attach(this)
                    .provider(new ForecastIOProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.StandardHttpClient.class)
                    .config(config)
                    .build();
        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }

        initLayout();
        initListeners();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTempInfo();
    }

    private void initLayout() {
        setContentView(R.layout.activity_weather);
        bannerLayout = new BannerLayout(this);

        View view = findViewById(R.id.weatherlayout);
        view.setBackgroundColor(Color.rgb(226, 231, 234));
        ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);
        bannerLayout.addView(view);
        parent.addView(bannerLayout);
        bannerLayout.setPageTitleText("Weather");
        mBedroomWeatherView = (TextView) this.findViewById(R.id.bedroomWeather);
        mBathroomWeatherView = (TextView) this.findViewById(R.id.bathroomWeather);
        mLivingRoomWeatherView = (TextView) this.findViewById(R.id.livingRoomWeather);
        mWeatherTitleView = (TextView) this.findViewById(R.id.weather_title);
        mViewTempButton = (Button) this.findViewById(R.id.button_view_temp);
        mViewHumidityButton = (Button) this.findViewById(R.id.button_view_humidity);

    }

    private void initListeners() {
        mViewHumidityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHumidityInfo();
            }
        });

        mViewTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTempInfo();
            }
        });
    }

    private void loadTempInfo() {
        mWeatherTitleView.setText("Inside Temperature");

        DatabaseUtils.getTempByID(Device.TEMP_BED, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mBedroomWeatherView.setText(WeatherUtils.formatTemp(Float.parseFloat(response)));
            }
        });

        DatabaseUtils.getTempByID(Device.TEMP_BATH, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mBathroomWeatherView.setText(WeatherUtils.formatTemp(Float.parseFloat(response)));
            }
        });

        DatabaseUtils.getTempByID(Device.TEMP_LIVINGROOM, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mLivingRoomWeatherView.setText(WeatherUtils.formatTemp(Float.parseFloat(response)));
            }
        });


    }

    private void loadHumidityInfo() {
        mWeatherTitleView.setText("Inside Humidity");

        DatabaseUtils.getTempByID(Device.HUM_BED, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mBedroomWeatherView.setText(WeatherUtils.formatHumidity(Float.parseFloat(response)));
            }
        });

        DatabaseUtils.getTempByID(Device.HUM_BATH, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mBathroomWeatherView.setText(WeatherUtils.formatHumidity(Float.parseFloat(response)));
            }
        });

        DatabaseUtils.getTempByID(Device.HUM_LIVINGROOM, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mLivingRoomWeatherView.setText(WeatherUtils.formatHumidity(Float.parseFloat(response)));
            }
        });

    }
}
