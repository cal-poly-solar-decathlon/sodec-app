package edu.calpoly.sodec.sodecapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView.OnItemSelectedListener;

public class PowergraphActivity extends ActionBarActivity {

    private LineChartView mChart;
    private LineChartData mData;
    private RelativeLayout mLayout;
    private String pug;
    private String time;
    private String startTime;
    private String endTime;
    private String device;

    private String[] gens = {"s-elec-gen-main-array", "s-elec-gen-bifacial"};
    private String[] uses = {"s-elec-used-laundry",
            "s-elec-used-dishwasher",
            "s-elec-used-refrigerator",
            "s-elec-used-induction-stove",
            "s-elec-used-ewh-solar-water-heater",
            "s-elec-used-kitchen-receps-1",
            "s-elec-used-kitchen-receps-2",
            "s-elec-used-dining-receps-1",
            "s-elec-used-dining-receps-2",
            "s-elec-used-bathroom-receps",
            "s-elec-used-bedroom-receps-1",
            "s-elec-used-bedroom-receps-2",
            "s-elec-used-mechanical-receps",
            "s-elec-used-entry-receps",
            "s-elec-used-exterior-receps",
            "s-elec-used-grey-water-pump-recep",
            "s-elec-used-black-water-pump-recep",
            "s-elec-used-thermal-loop-pump-recep",
            "s-elec-used-water-supply-pump-recep",
            "s-elec-used-water-supply-booster-pump-recep",
            "s-elec-used-vehicle-charging-recep",
            "s-elec-used-heat-pump-recep",
            "s-elec-used-air-handler-recep"};

    private static final int DAY_VIEW = 0;

    private static final String DEFAULT_YAXIS_NAME = "Power Generated (kW)";
    private static final String DEFAULT_XAXIS_NAME = "Date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powergraph);

        Spinner susegen = (Spinner) findViewById(R.id.usegen);
        Spinner stime = (Spinner) findViewById(R.id.time);

        startTime = TimestampUtils.getStartIsoForDay();
        endTime = TimestampUtils.getIsoForNow();

        mChart = (LineChartView) findViewById(R.id.powerline);
        mData = new LineChartData();
        PowerGraphUtils.initPoints(mData, mChart, device, PowerGraphUtils.BASE_POWER, startTime, endTime);
        initStyle(DAY_VIEW);
        mChart.setLineChartData(mData);

        String[] items = new String[] { "Power Generated", "Power Used" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

        susegen.setAdapter(adapter);

        susegen.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                pug = (String) parent.getItemAtPosition(position);
                System.out.println("SUSEGEN: " + pug);

                switch(pug) {
                    case "Power Generated": device = "s-elec-gen-main-array";
                        break;
                    case "Power Used": device = "s-elec-used-laundry";
                        break;
                }

                PowerGraphUtils.initPoints(mData, mChart, device, PowerGraphUtils.BASE_POWER, startTime, endTime);
                initStyle(DAY_VIEW);
                mChart.setLineChartData(mData);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] items2 = new String[] { "Day", "Week", "Month", "Year" };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items2);

        stime.setAdapter(adapter2);

        stime.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                long startMillis = 0;
                long endMillis = 0;
                Log.v("item", (String) parent.getItemAtPosition(position));
                time = (String) parent.getItemAtPosition(position);
                System.out.println(time);

                switch (time) {
                    case "Day":
                        startTime = TimestampUtils.getStartIsoForDay();
                        break;
                    case "Week":
                        startMillis = TimestampUtils.getStartOfDay(-TimestampUtils.DAYS_PER_WEEK).getTimeInMillis();
                        break;
                    case "Month":
                        startMillis = TimestampUtils.getStartOfDay(-TimestampUtils.DAYS_PER_MONTH).getTimeInMillis();
                        break;
                    case "Year":
                        startMillis = TimestampUtils.getStartOfDay(-TimestampUtils.DAYS_PER_YEAR).getTimeInMillis();
                        break;
                }
                if (time.equals("Day")) {
                    PowerGraphUtils.initPoints(mData, mChart, device, PowerGraphUtils.BASE_POWER, startTime, endTime);
                }
                else {
                    endMillis = TimestampUtils.getEndOfDay(-1).getTimeInMillis();
                    PowerGraphUtils.initPointsFromCache(mData, mChart, device, startMillis, endMillis);
                }

                initStyle(DAY_VIEW);
                mChart.setLineChartData(mData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mLayout = (RelativeLayout) findViewById(R.id.powergraphLayout);

        mLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                startActivity(new Intent(PowergraphActivity.this, PowerActivity.class));
            }

            public void onSwipeLeft() {
            }

            public void onSwipeBottom() {
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_powergraph, menu);
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

    private void initStyle(int viewType) {
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
