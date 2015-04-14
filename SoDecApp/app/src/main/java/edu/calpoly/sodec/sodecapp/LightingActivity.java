package edu.calpoly.sodec.sodecapp;

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
import java.util.HashMap;

import edu.calpoly.sodec.sodecapp.ServerConnection.AmbientLightDevice;

public class LightingActivity extends ActionBarActivity {

    private HashMap<AmbientLightDevice, Integer> ambientLights = new HashMap<AmbientLightDevice, Integer>();
    private ArrayList<TextView> ambientTextViews;
    private LinearLayout pageLayout;
    private LinearLayout listLayout;
    private LinearLayout floorplanLayout;
    private ScrollView view;
    private ImageView floorplanView;

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

        ambientTextViews = new ArrayList<TextView>();

        LightingUtils.getAllAmbientLightData(ambientLights);

        AmbientLightDevice[] test = new AmbientLightDevice[ambientLights.size()];
        AmbientLightDevice[] devices = ambientLights.keySet().toArray(test);
        for (AmbientLightDevice light : devices) {
            TextView ambLight = new TextView(listLayout.getContext());
            ImageView image = new ImageView(listLayout.getContext());
            LinearLayout labelLayout = new LinearLayout(listLayout.getContext());
            labelLayout.setOrientation(LinearLayout.HORIZONTAL);

            image.setImageResource(R.drawable.light_on);
            String text = light.getLabel() + " - ";

            int status = ambientLights.get(light);
            String lightStatus = "On";
            if (status == 0) {
                lightStatus = "Off";
                image.setImageResource(R.drawable.light_off);
            }

            image.setScaleX(0.5f);
            image.setScaleY(0.5f);

            text += lightStatus;
            ambLight.setText(text);
            ambLight.setGravity(Gravity.CENTER);

            labelLayout.addView(image);
            labelLayout.addView(ambLight);
            labelLayout.setGravity(Gravity.CENTER_VERTICAL);
            listLayout.addView(labelLayout);
        }

        floorplanLayout.addView(floorplanView);

        pageLayout.addView(view);
        pageLayout.addView(floorplanLayout);
        setContentView(pageLayout);
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
