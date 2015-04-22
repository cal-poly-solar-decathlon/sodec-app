package edu.calpoly.sodec.sodecapp;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.PieChartView;

public class PowerActivity extends ActionBarActivity {

    PieChartData mData;
    PieChartView mChart;

    String[] mRooms = {"Bedroom", "Kithcen", "Living Room", "Dining Room", "Bathroom",
            "Mechanical"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);

        mData = new PieChartData();
        mChart = (PieChartView) findViewById(R.id.roomPowerChart);

        setData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_power, menu);
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

    public ArrayList<SliceValue> generateSliceValues() {
        ArrayList<SliceValue> slices = (ArrayList<SliceValue>)mData.getValues();

        slices.add(new SliceValue(27.f, ChartUtils.COLOR_BLUE)); // bedroom
        slices.add(new SliceValue(50.f, ChartUtils.COLOR_VIOLET)); // kitchen
        slices.add(new SliceValue(20.f, ChartUtils.COLOR_GREEN)); // living room
        slices.add(new SliceValue(12.f, ChartUtils.COLOR_ORANGE)); // dining room
        slices.add(new SliceValue(30.f, ChartUtils.COLOR_RED)); // bathroom
        slices.add(new SliceValue(71.f, ChartUtils.DEFAULT_COLOR)); // mechanical

        return slices;
    }

    // sets the room pie chart with hardcoded data
    public void setData() {
        ArrayList<SliceValue> slices = generateSliceValues();

        for (int i = 0; i < mRooms.length; i++)
        {
            slices.get(i).setLabel(mRooms[i].toCharArray());
        }
        mData.setHasLabels(true);
        mData.setValues(slices);
        mChart.setPieChartData(mData);
    }
}
