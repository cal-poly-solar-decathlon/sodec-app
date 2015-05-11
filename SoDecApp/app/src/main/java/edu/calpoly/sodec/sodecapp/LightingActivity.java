package edu.calpoly.sodec.sodecapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LightingActivity extends ActionBarActivity {

    private LinearLayout pageLayout;
    private LinearLayout listLayout;
    private FrameLayout floorplanLayout;

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
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Integer orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            pageLayout.setOrientation(LinearLayout.VERTICAL);
        } else {
            pageLayout.setOrientation(LinearLayout.HORIZONTAL);
        }*/

        //setupLightList();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setupLightList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        floorplanLayout = new FrameLayout(this);

        floorplanView = new ImageView(floorplanLayout.getContext());
        floorplanView.setImageResource(R.drawable.floorplan);
        floorplanView.setScaleType(ImageView.ScaleType.FIT_XY);
        floorplanLayout.addView(floorplanView);

        setContentView(floorplanLayout);

        initSensorCollection();
    }

    public void setupLightList() {
        for (LightDevice light : lightUtils.lights) {
            ImageView image = new ImageView(floorplanLayout.getContext());

            if (light.isOn) {
                image.setImageResource(R.drawable.light_on);
            } else {
                image.setImageResource(R.drawable.light_off);
            }

            image.setScaleX(0.05f);
            image.setScaleY(0.05f);

            double width = (double) floorplanLayout.getWidth();
            double height = (double) floorplanLayout.getHeight();
            int xTrans = (int) ((((double) light.location.x) / 100.0) * width);
            xTrans = xTrans - ((int) (width / 2));
            int yTrans = (int) (((double) (light.location.y) / 100.0) * height);
            yTrans = yTrans - ((int) (height / 2));

            image.setTranslationX(xTrans);
            image.setTranslationY(-yTrans);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(image.getWidth(), image.getHeight());
            params.leftMargin = xTrans;
            params.bottomMargin = yTrans;
            params.gravity = Gravity.TOP;

            lightImages.add(image);

            floorplanLayout.addView(image);
        }
    }

    public void reloadData() {
        for (int idx = 0; idx < lightImages.size(); idx++) {
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
