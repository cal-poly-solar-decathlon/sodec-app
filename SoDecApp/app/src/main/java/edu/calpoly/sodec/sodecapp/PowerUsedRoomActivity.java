package edu.calpoly.sodec.sodecapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;


public class PowerUsedRoomActivity extends ActionBarActivity {

    private LineChartView mChart;
    private LineChartData mData;
    private String pug;
    private String time;
    private String startTime;
    private String endTime;
    private String device;

    private static final String DEFAULT_YAXIS_NAME = "Power Used (kW)";
    private static final String DEFAULT_XAXIS_NAME = "Date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_used_room);

        TextView title = (TextView) findViewById(R.id.title);
        Spinner stime = (Spinner) findViewById(R.id.time);

        startTime = TimestampUtils.getStartIsoForDay();
        endTime = TimestampUtils.getIsoForNow();

        Intent intent = getIntent();
        String room = intent.getStringExtra("room");

        // need to fix this so it gets ALL devices in a room, not just one
        switch(room) {
            case "Bedroom":
                device = Device.POW_USE_BEDROOM_RECEPS_1;
                break;
            case "Kitchen":
                device = Device.POW_USE_DISHWASHER;
                break;
            case "Living Room":
                device = Device.POW_USE_LIVING_RECEPS;
                break;
            case "Dining Room":
                device = Device.POW_USE_DINING_RECEPS_2;
                break;
            case "Bathroom":
                device = Device.POW_USE_BATHROOM_RECEPS;
                break;
            case "Mechanical":
                device = Device.POW_USE_HEAT_PUMP_RECEP;
                break;
            default:
        }

        title.setText(room);

        mChart = (LineChartView) findViewById(R.id.powerUsedGraph);
        mData = new LineChartData();
        PowerGraphUtils.initPoints(mData, mChart, device, PowerGraphUtils.BASE_POWER, startTime, endTime);
        initStyle();
        mChart.setLineChartData(mData);

        String[] items2 = new String[] { "Day", "Week", "Month", "Year" };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items2);

        stime.setAdapter(adapter2);

        stime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                time = (String) parent.getItemAtPosition(position);
                System.out.println(time);

                switch (time) {
                    case "Day":
                        startTime = TimestampUtils.getStartIsoForDay();
                        break;
                    case "Week":
                        startTime = TimestampUtils.getStartIsoForWeek();
                        break;
                    case "Month":
                        startTime = TimestampUtils.getStartIsoForMonth();
                        break;
                    case "Year":
                        startTime = TimestampUtils.getStartIsoForYear();
                        break;
                }

                PowerGraphUtils.initPoints(mData, mChart, device, PowerGraphUtils.BASE_POWER, startTime, endTime);
                initStyle();
                mChart.setLineChartData(mData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_power_used_room, menu);
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

    private void initStyle() {
        mData.setAxisYLeft(new Axis()
                .setName(DEFAULT_YAXIS_NAME)
                .setHasLines(true)
                .setTextColor(Color.BLACK));
        mData.setAxisXBottom(new Axis()
                .setName(DEFAULT_XAXIS_NAME)
                .setFormatter(new SimpleAxisValueFormatter()
                        .setPrependedText("Mar ".toCharArray()))
                .setTextColor(Color.BLACK));
    }
}
