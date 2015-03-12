package edu.calpoly.sodec.sodecapp;

import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/** Handles HTTP and WebSocket communication with the server. */
public class ServerConnection {

    private static WebSocketConnection mSocketConnection = null;

    // Remote backend (may not be updated currently)
    //private static final String BASE_SERVER_URI = "http://192.227.237.2:3000";
    //private static final String SOCKET_URI = "ws://192.227.237.2:3001";

    // Local backend when using an emulator
    private static final String BASE_SERVER_URI = "http://calpolysolardecathlon.org:3000/srv";
    private static final String SOCKET_URI = "http://calpolysolardecathlon.org:3001/srv";

    // Server routes / endpoints
    private static final String POWER_ROUTE = "/power";
    private static final String LR_TEMP_ROUTE = "/s-temp-lr";
    private static final String LR_OCC_ROUTE = "/s-occ-lr";
    private static final String EVENTS_IN_RANGE_ROUTE = "/events-in-range";
    private static final String LATEST_EVENT_ROUTE = "/latest-event";

    // Status codes
    public static final int SUCCESS = 200;

    private static final String TAG = "ServerConnection";

    // For now we do not need to associate any state with this class. Some will probably be added
    // as we go though.
    public ServerConnection() { }

    /** Indicates commands to execute once a server response is received. */
    public interface ResponseCallback<K, V> {
        //public void execute(Map<K, V> json);
        //public void execute(JSONArray array);
        public void execute(String response);
    }

    public static WebSocketConnection getSocketConnection() {
        if (mSocketConnection == null || !mSocketConnection.isConnected()) {
            mSocketConnection = new WebSocketConnection();
            try {
                mSocketConnection.connect(SOCKET_URI, new WebSocketHandler() {
                    @Override
                    public void onOpen() {
                        mSocketConnection.sendTextMessage("One day, this will be useful information " +
                                "from the Solar Decathlon house.");
                    }

                    @Override
                    public void onTextMessage(String message) {
                        // TODO: We'll want to actually do something here once we start
                        // supporting events that have a push/notification component
                        Log.d(TAG, "Received message: " + message);
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        Log.d(TAG, "The connection was lost because " + reason);
                    }
                });
            } catch (WebSocketException e) {
                Log.d(TAG, "Error: " + e.toString());
            }
        }
        return mSocketConnection;
    }

    /** Retrieve the most recent amount of power generated. */
    public void getPowerGenerated(final ResponseCallback<String, String> onSuccess) {
        sendRequest(onSuccess, POWER_ROUTE, null);
    }

    /** Retrieve the living room temperature */
    public void getLivingRoomTemp(final ResponseCallback<String, String> onSuccess) {
        sendRequest(onSuccess, LR_TEMP_ROUTE, null);
    }

    /** Retrieve the events between a start time and end time */
    public void getEventsInRange(final ResponseCallback<String, String> onSuccess, final List<NameValuePair> parameter) {
        sendRequest(onSuccess, EVENTS_IN_RANGE_ROUTE, parameter);
    }

    /** Retrieve the events between a start time and end time */
    public void getLatestEvents(final ResponseCallback<String, String> onSuccess) {
        sendRequest(onSuccess, LATEST_EVENT_ROUTE, null);
    }

    /** Retrieve the living room occupancy */
    public void getLivingRoomOcc(final ResponseCallback<String, String> onSuccess) {
        sendRequest(onSuccess, LR_OCC_ROUTE, null);
    }

    private void sendRequest(final ResponseCallback<String, String> onSuccess, final String route, final List<NameValuePair> parameters) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String url = BASE_SERVER_URI + route;
                if(!url.endsWith("?"))
                    url += "?";
                if(parameters != null) {
                    String paramString = URLEncodedUtils.format(parameters, "utf-8");
                    url += paramString;
                }

                HttpGet request = new HttpGet(url);

                // CloseableHttpResponse response = null;
                // Map<String, String> jsonResponse = null;

                try (CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.execute(request);){

                    if (response.getStatusLine().getStatusCode() == SUCCESS) {
                        BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        StringBuilder responseString = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            responseString.append(line);
                        }

                        return responseString.toString();
                        /*jsonResponse = new ObjectMapper().readValue(
                                response.getEntity().getContent(), Map.class);
                         */
                    }
                    else {
                        /* Only 23 characters allowed */
                        Log.d(TAG + " " + route, "Error: " +
                                response.getStatusLine().getStatusCode());
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    onSuccess.execute(response);
                }
            }
        }.execute();
    }
}

