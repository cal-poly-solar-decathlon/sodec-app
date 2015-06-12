package edu.calpoly.sodec.sodecapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView.OnItemSelectedListener;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PowergraphActivity extends ActionBarActivity {

    private LineChartView mChart;
    private LineChartData mData;
    private RelativeLayout mLayout;
    private String pug;
    //private String time;
    private String length = "Day";
    //private String startTime;
    private String endTime;
    //private String device;
    private String type = "gens";

    private static final int SERIES_INTERVAL = 400;

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

        //startTime = TimestampUtils.getStartIsoForDay();
        endTime = TimestampUtils.getIsoForNow();

        mChart = (LineChartView) findViewById(R.id.powerline);
        mData = new LineChartData();
        //PowerGraphUtils.initPoints(mData, mChart, "s-elec-used-dishwasher", PowerGraphUtils.BASE_POWER, startTime, endTime);

        generator(gens, length);

        initStyle(DAY_VIEW);

        //mChart.setLineChartData(mData);

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
                    case "Power Generated": type = "gens";
                        break;
                    case "Power Used": type = "uses";
                        break;
                }

                //PowerGraphUtils.initPoints(mData, mChart, "s-elec-used-dishwasher", PowerGraphUtils.BASE_POWER, startTime, endTime);
                if (type.equals("gens"))
                    generator(gens, length);
                else
                    generator(uses, length);
                initStyle(DAY_VIEW);
                //mChart.setLineChartData(mData);
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
                Log.v("item", (String) parent.getItemAtPosition(position));
                length = (String) parent.getItemAtPosition(position);
                //System.out.println(time);
                /*switch (time) {
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
                }*/

                //PowerGraphUtils.initPoints(mData, mChart, "", PowerGraphUtils.BASE_POWER, startTime, endTime);
                if (type.equals("gens"))
                    generator(gens, length);
                else
                    generator(uses, length);
                initStyle(DAY_VIEW);
               // mChart.setLineChartData(mData);

                //PowerGraphUtils.initPointsTot();
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

    private void generator(final String[] source, String time) {
        String startTime = TimestampUtils.getStartIsoForDay();

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

        final List<PointValue> values = new ArrayList<PointValue>();
        final List<AtomicInteger> dev = new ArrayList<AtomicInteger>();
        final List<Line> lines = new ArrayList<Line>();

        for (int v=0; v < source.length; v++) {
            System.out.println(v);
            PowerGraphUtils.initPointsTot(new ServerConnection.ResponseCallback<String, String>() {
                @Override
                public void execute(String response) {
                    try {
                        JSONArray jsonResponse = new JSONArray(response);
                        int numDeltas = jsonResponse.length();
                        dev.add(new AtomicInteger());
                        for (int i = 0, j = 0; i < SERIES_INTERVAL && j < numDeltas; i++, j += SERIES_INTERVAL) {
                            if (values.size() > i) {
                                values.set(i, new PointValue(i,
                                        ((JSONArray) jsonResponse.get(j)).getInt(1)+values.get(i).getY()));
                                System.out.println("CRAP" + values.get(i));
                            }
                            else {
                            values.add(new PointValue(i,
                                    ((JSONArray) jsonResponse.get(j)).getInt(1)));
                            System.out.println("POOOP" + values.get(i));
                            }

                            if (dev.size() == source.length) {
                                Line line = new Line(values)
                                        .setColor(Color.BLUE)
                                        .setCubic(true);
                                lines.add(line);
                                mData.setLines(lines);
                                mChart.setLineChartData(mData);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // mNumLightsOn.setText(lightUtils.numLightsOn.toString());
                }
            }, mData, mChart, source[v], PowerGraphUtils.BASE_POWER, startTime, endTime);

        }
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
