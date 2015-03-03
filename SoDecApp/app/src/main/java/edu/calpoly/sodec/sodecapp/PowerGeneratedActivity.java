package edu.calpoly.sodec.sodecapp;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class PowerGeneratedActivity extends ActionBarActivity {

    private LineChartView mChart;
    private LineChartData mData;

    private static final int HOUR_VIEW = 0;
    private static final int DAY_VIEW = 1;
    private static final int WEEK_VIEW = 2;
    private static final int MONTH_VIEW = 3;

    private static final String DEFAULT_YAXIS_NAME = "Power Generated (kW)";
    private static final String DEFAULT_XAXIS_NAME = "Date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_generated);

        mChart = (LineChartView) findViewById(R.id.powerGeneratedChart);
        mData = new LineChartData();
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
        values.add(new PointValue(2, 10));
        values.add(new PointValue(3, 7));
        values.add(new PointValue(4, 8));
        values.add(new PointValue(5, 16));
        values.add(new PointValue(6, 9));
        values.add(new PointValue(7, 7));

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
