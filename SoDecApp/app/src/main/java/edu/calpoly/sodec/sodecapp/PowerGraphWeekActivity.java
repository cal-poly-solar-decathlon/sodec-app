package edu.calpoly.sodec.sodecapp;

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
public class PowerGraphWeekActivity extends ActionBarActivity {

    private LineChartView mChart;
    private LineChartData mData;
    private String startTime;
    private String endTime;

    private static final int WEEK_VIEW = 2;

    private static final String DEFAULT_YAXIS_NAME = "Power Generated (kW)";
    private static final String DEFAULT_XAXIS_NAME = "Date";
    private static final String DEVICE = "s-temp-lr";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView( R.layout.power_graph_week_layout);

        startTime = TimestampUtils.getStartIsoForWeek();
        endTime = TimestampUtils.getIsoForNow();

        mChart = (LineChartView) findViewById(R.id.powerGeneratedChart);
        mData = new LineChartData();
        PowerGraphUtils.initPoints(mData, mChart, DEVICE, startTime, endTime);

        initData(WEEK_VIEW);
        initStyle(WEEK_VIEW);
        mChart.setLineChartData(mData);
    }

    // TODO: Need to get data from server; currently uses mock data for a week view.
    private void initData(int viewType) {
        List<PointValue> values = new ArrayList<PointValue>();
        List<Line> lines = new ArrayList<Line>();
        Line line;

        values.add(new PointValue(1, 5));
        values.add(new PointValue(2, 4));
        values.add(new PointValue(3, 11));
        values.add(new PointValue(4, 9));
        values.add(new PointValue(5, 20));
        values.add(new PointValue(6, 15));
        values.add(new PointValue(7, 2));

        line = new Line(values)
                .setColor(Color.BLUE)
                .setCubic(true);
        lines.add(line);
        mData.setLines(lines);
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
