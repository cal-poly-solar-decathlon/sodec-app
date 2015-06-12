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

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class PowerGraphUtils {

    private static final int SERIES_INTERVAL = 400;
    private static final String BASE_TIME = /*"baseTimestamp"*/"timestamp";
    public static final String BASE_POWER = /*"baseStatus"*/"generation"; // power generated
    public static final String BASE_USAGE = /*"baseUsage"*/"usage"; // power used
    private static final String SERIES_DATA = "seriesData";

    public static void initPoints(final LineChartData mData, final LineChartView mChart,
                                  String device, final String usage, String startTime, String endTime) {

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("device", device));
        params.add(new BasicNameValuePair("start", startTime));
        params.add(new BasicNameValuePair("end", endTime));

        new ServerConnection().getEventsInRange(new ServerConnection.ResponseCallback<String, String>() {

            /**
             * @param response Should include a base timestamp, base power generated/usage value,
             *                 and pairs of time deltas and power deltas.
             */
            @Override
            public void execute(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                   //Timestamp baseTime = new Timestamp((int) jsonResponse.get(BASE_TIME));
                   // int basePower = (int) jsonResponse.get(usage);
                    JSONArray dataDeltas = jsonResponse.getJSONArray(SERIES_DATA);


                    List<PointValue> values = new ArrayList<PointValue>();
                    List<Line> lines = new ArrayList<Line>();
                    Line line;
                    int numDeltas = dataDeltas.length();

                    for (int i = 0, j = 0 ; i < SERIES_INTERVAL && j < numDeltas; i++, j+=SERIES_INTERVAL) {
                        values.add(new PointValue(i,
                                ((JSONArray) dataDeltas.get(j)).getInt(1)));
                                //System.out.println(values.get(i));
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

    public static void initPointsTot(final ServerConnection.ResponseCallback<String, String> resp, final LineChartData mData, final LineChartView mChart,
                                  String device, final String usage, String startTime, String endTime) {

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("device", device));
        params.add(new BasicNameValuePair("start", startTime));
        params.add(new BasicNameValuePair("end", endTime));

        new ServerConnection().getEventsInRange(new ServerConnection.ResponseCallback<String, String>() {

            /**
             * @param response Should include a base timestamp, base power generated/usage value,
             *                 and pairs of time deltas and power deltas.
             */
            @Override
            public void execute(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    //Timestamp baseTime = new Timestamp((int) jsonResponse.get(BASE_TIME));
                    // int basePower = (int) jsonResponse.get(usage);
                    JSONArray dataDeltas = jsonResponse.getJSONArray(SERIES_DATA);
                    resp.execute(dataDeltas.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }
}
