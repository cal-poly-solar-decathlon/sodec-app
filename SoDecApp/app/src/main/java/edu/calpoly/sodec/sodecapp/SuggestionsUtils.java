package edu.calpoly.sodec.sodecapp;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

public class SuggestionsUtils {
    private static final String BASE_TIME = "baseTimestamp";
    private static final String BASE_STATUS = "baseStatus";
    private static final String SERIES_DATA = "seriesData";

    public static void getTrendByID(String deviceId, String startTime, String endTime) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("device", deviceId));
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
                    Timestamp baseTime = new Timestamp((int) jsonResponse.get(BASE_TIME));
                    int baseStatus = (int) jsonResponse.get(BASE_STATUS);
                    JSONArray dataDeltas = jsonResponse.getJSONArray(SERIES_DATA);
                    List<PointValue> values = new ArrayList<PointValue>();
                    List<PointValue> incrValues = new ArrayList<PointValue>();

                    int numDeltas = dataDeltas.length();
                    int status = baseStatus;
                    int delta;

                    for (int i = 0 ; i < numDeltas; i++) {
                        if (i != 0) {
                            delta = ((JSONArray) dataDeltas.get(i)).getInt(1);
                            status += delta;
                        }

                        values.add(new PointValue(i,status));
                    }
                    // Getting every 10 minutes assuming it's time deltas of 5 seconds
                    for (int i = 0; i < values.size(); i += 120) {
                        incrValues.add(values.get(i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);

    }
    private double findSlopeOfBestFitLine(List<PointValue> values) {
        double xMean;
        double xSum = 0;
        double yMean;
        double ySum = 0;

        double sumNominator = 0;
        double sumDenominator = 0;


        for(int i = 0; i < values.size(); i ++) {
            xSum += (double) values.get(i).getX();
        }
        for(int i = 0; i < values.size(); i ++) {
            ySum += (double) values.get(i).getY();
        }
        xMean = xSum / values.size();
        yMean = ySum/values.size();

        for(int i = 0; i < values.size(); i++) {
            PointValue point = values.get(i);
            double productNominator = ((point.getX() - xMean) * (point.getY() - yMean));
            sumNominator += productNominator;

            double productDenominator = ((point.getX() - xMean) * (point.getX() - xMean));
            sumDenominator += productDenominator;
        }

        return sumNominator/sumDenominator;
    }
}
