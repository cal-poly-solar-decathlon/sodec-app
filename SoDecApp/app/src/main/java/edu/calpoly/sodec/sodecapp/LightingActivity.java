package edu.calpoly.sodec.sodecapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LightingActivity extends ActionBarActivity {

    private LinearLayout pageLayout;
    private LinearLayout listLayout;
    private LinearLayout floorplanLayout;

    private List<TextView> lightTexts = new ArrayList<TextView>();
    private List<ImageView> lightImages = new ArrayList<ImageView>();

    private ScrollView view;
    private ImageView floorplanView;

    private LightingUtils lightUtils = new LightingUtils();

    @Override
    protected void onStart() {
        super.onStart();
        lightUtils.getAllLightData(new ServerConnection.ResponseCallback<String, String>() {
            @Override
            public void execute(String response) {
                reloadData();
                //recreate();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageLayout = new LinearLayout(this);
        pageLayout.setOrientation(LinearLayout.HORIZONTAL);
        pageLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        floorplanLayout = new LinearLayout(pageLayout.getContext());
        floorplanLayout.setGravity(Gravity.CENTER);

        view = new ScrollView(pageLayout.getContext());
        floorplanView = new ImageView(floorplanLayout.getContext());
        floorplanView.setImageResource(R.drawable.floorplan);
        floorplanView.setScaleType(ImageView.ScaleType.FIT_XY);

        listLayout = new LinearLayout(view.getContext());
        listLayout.setOrientation(LinearLayout.VERTICAL);

        view.addView(listLayout);


        setupLightList();

        //setupView();
        floorplanLayout.addView(floorplanView);

        pageLayout.addView(view);
        pageLayout.addView(floorplanLayout);
        setContentView(pageLayout);

        initSensorCollection();
        //AmbientLightDevice[] test = new AmbientLightDevice[ambientLights.size()];
        //AmbientLightDevice[] devices = ambientLights.keySet().toArray(test);

    }

    public void setupLightList() {
        for (LightDevice light : lightUtils.lights) {
            TextView ambLight = new TextView(listLayout.getContext());
            ImageView image = new ImageView(listLayout.getContext());
            LinearLayout labelLayout = new LinearLayout(listLayout.getContext());
            labelLayout.setOrientation(LinearLayout.HORIZONTAL);

            //image.setImageResource(R.drawable.light_on);

            /*int status = ambientLights.get(light);
            String lightStatus = "On";
            if (status == 0) {
                lightStatus = "Off";
                image.setImageResource(R.drawable.light_off);
            }*/
            if (light.isOn) {
                image.setImageResource(R.drawable.light_on);
            } else {
                image.setImageResource(R.drawable.light_off);
            }

            image.setScaleX(0.5f);
            image.setScaleY(0.5f);

            //text += lightStatus;
            ambLight.setText(light.description);
            ambLight.setGravity(Gravity.CENTER);

            lightImages.add(image);
            lightTexts.add(ambLight);

            labelLayout.addView(image);
            labelLayout.addView(ambLight);
            labelLayout.setGravity(Gravity.CENTER_VERTICAL);
            listLayout.addView(labelLayout);
        }
    }

    public void reloadData() {
        for (int idx = 0; idx < lightTexts.size(); idx++) {
            ImageView image = lightImages.get(idx);

            if (lightUtils.lights.get(idx).isOn) {
                image.setImageResource(R.drawable.light_on);
            } else {
                image.setImageResource(R.drawable.light_off);
            }
        }
    }

    private void initSensorCollection() {
        boolean sensorCollectionStarted = (PendingIntent.getBroadcast(this, 0,
                new Intent(SensorCollectionReceiver.ACTION_COLLECT_SENSOR_DATA),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!sensorCollectionStarted) {
            sendBroadcast(new Intent(BootReceiver.ACTION_START_COLLECTION));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lighting, menu);
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

}
