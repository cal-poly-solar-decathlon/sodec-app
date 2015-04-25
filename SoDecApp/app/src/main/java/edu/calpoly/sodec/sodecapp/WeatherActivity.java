package edu.calpoly.sodec.sodecapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.provider.forecastio.ForecastIOProviderType;


public class WeatherActivity extends ActionBarActivity {
    private static final String DEVICE_OUTSIDE = "s-temp-out";
    private static final String DEVICE_BED = "s-temp-bed";
    private static final String DEVICE_BATH = "s-temp-bath";
    private static final String DEVICE_LIVING_ROOM = "s-temp-lr";
    private TextView mBedroomTempTextView;
    private TextView mBathroomTempTextVIew;
    private TextView mLivingRoomTempTextView;


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

        mBedroomTempTextView = (TextView) this.findViewById(R.id.bedroomTemp);
        mBathroomTempTextVIew = (TextView) this.findViewById(R.id.bathroomTemp);
        mLivingRoomTempTextView = (TextView) this.findViewById(R.id.livingRoomTemp);


    }

    private void loadTempInfo() {
        WeatherUtils.getTempByID(DEVICE_BED, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mBedroomTempTextView.setText(WeatherUtils.formatTemp(Float.parseFloat(response)));
            }
        });

        WeatherUtils.getTempByID(DEVICE_BATH, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mBathroomTempTextVIew.setText(WeatherUtils.formatTemp(Float.parseFloat(response)));
            }
        });

        WeatherUtils.getTempByID(DEVICE_LIVING_ROOM, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mLivingRoomTempTextView.setText(WeatherUtils.formatTemp(Float.parseFloat(response)));
            }
        });

    }
}
