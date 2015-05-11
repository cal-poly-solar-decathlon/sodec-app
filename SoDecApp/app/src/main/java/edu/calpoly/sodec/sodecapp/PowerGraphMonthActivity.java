package edu.calpoly.sodec.sodecapp;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Kyle on 3/9/2015.
 */
public class PowerGraphMonthActivity extends ActionBarActivity {

    private LineChartView mChart;
    private LineChartData mData;
    private String startTime;
    private String endTime;

    private static final int MONTH_VIEW = 1;
    private static final String DEVICE = "egauge";

    private static final String DEFAULT_YAXIS_NAME = "Power Generated (kW)";
    private static final String DEFAULT_XAXIS_NAME = "Date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.power_graph_month_layout);

        startTime = TimestampUtils.getStartIsoForMonth();
        endTime = TimestampUtils.getIsoForNow();

        mChart = (LineChartView) findViewById(R.id.powerGeneratedChart);
        mData = new LineChartData();
        PowerGraphUtils.initPoints(mData, mChart, DEVICE, PowerGraphUtils.BASE_POWER, startTime, endTime);

        initStyle(MONTH_VIEW);
        mChart.setLineChartData(mData);

        mChart.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                startActivity(new Intent(PowerGraphMonthActivity.this, PowerActivity.class));
            }

            public void onSwipeLeft() {
            }

            public void onSwipeBottom() {
            }
        });
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
