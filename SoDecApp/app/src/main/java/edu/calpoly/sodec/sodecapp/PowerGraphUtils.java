package edu.calpoly.sodec.sodecapp;

import android.graphics.Color;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Christine on 3/11/15.
 */
public class PowerGraphUtils {
    public static void initPoints(final LineChartData mData, final LineChartView mChart, String device, String startTime, String endTime) {

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("device", device));
        params.add(new BasicNameValuePair("start", startTime));
        params.add(new BasicNameValuePair("end", endTime));

        new ServerConnection().getEventsInRange(new ServerConnection.ResponseCallback<String, String>() {

            /**
             *
             * @param response should be array of JSONObjects for events-in-range
             */
            @Override
            public void execute(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    List<PointValue> values = new ArrayList<PointValue>();
                    List<Line> lines = new ArrayList<Line>();
                    Line line;
                    int interval = array.length()/(array.length()/100);
                    try {
                        for (int i = 0, j = 0 ; i < 100 && j < array.length(); i++, j+=interval) {

                            JSONObject data = (JSONObject) array.get(j);
                            int reading = data.getInt("reading");
                            values.add(new PointValue(i, reading));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    line = new Line(values)
                            .setColor(Color.BLUE)
                            .setCubic(true);
                    lines.add(line);
                    mData.setLines(lines);
                    mChart.setLineChartData(mData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);





    }


}
