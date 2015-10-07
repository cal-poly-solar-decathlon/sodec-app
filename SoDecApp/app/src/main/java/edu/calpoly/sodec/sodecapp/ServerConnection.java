package edu.calpoly.sodec.sodecapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/** Handles HTTP and WebSocket communication with the server. */
public class ServerConnection {

    private static WebSocketConnection mSocketConnection = null;

    private static final String BASE_SERVER_URI = "http://calpolysolardecathlon.org:8080/srv";
    private static final String SOCKET_URI = "http://calpolysolardecathlon.org:3001/srv";

    // Server routes / endpoints
    private static final String EVENTS_IN_RANGE_ROUTE = "/events-in-range";
    private static final String LATEST_EVENT_ROUTE = "/latest-event";

    /*public enum LightDevices {
        AMB_BED ("Bedroom", "s-amb-bed"),
        AMB_MECH ("Mechanical Room", "s-amb-mech"),
        AMB_LR ("Living Room", "s-amb-lr"),
        AMB_BATH ("Bathroom", "s-amb-bath"),

        private String id;
        private String label;
        LightDevices(String label, String id) {
            this.label = label;
            this.id = id;
        }

        public String getLabel() {
            return this.label;
        }

        public String getId() {
            return this.id;
        }
    }*/

    // Status codes
    public static final int SUCCESS = 200;

    private static final String TAG = "ServerConnection";

    // For now we do not need to associate any state with this class. Some will probably be added
    // as we go though.
    public ServerConnection() { }

    /** Indicates commands to execute once a server response is received. */
    public interface ResponseCallback<K, V> {
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
                        Log.i("ServerConnection", "web socket opened");
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

    /** Retrieve the events between a start time and end time */
    public void getEventsInRange(final ResponseCallback<String, String> onSuccess, final List<NameValuePair> parameter) {
        sendAsynchRequest(onSuccess, EVENTS_IN_RANGE_ROUTE, parameter);
    }

    public void getEventsInRangeSynch(final ResponseCallback<String, String> onSuccess, final List<NameValuePair> parameter) {
        sendSynchRequest(onSuccess, EVENTS_IN_RANGE_ROUTE, parameter);
    }

    /** Retrieve the events between a start time and end time */
    public void getLatestEvent(final ResponseCallback<String, String> onSuccess, String device) {
        List params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("device", device));
        sendAsynchRequest(onSuccess, LATEST_EVENT_ROUTE, params);
    }

    /*public void getAmbientLight(final ResponseCallback<String, String> onSuccess, AmbientLightDevice device) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("device", device.getId()));
        sendAsynchRequest(onSuccess, LATEST_EVENT_ROUTE, params);
    }*/

    // maybe replace with call to getLatestEvent?
    public void getLight(final ResponseCallback<String, String> onSuccess, final edu.calpoly.sodec.sodecapp.LightDevice device) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("device", device.id));
        sendAsynchRequest(onSuccess, LATEST_EVENT_ROUTE, params);
    }

    private void sendAsynchRequest(final ResponseCallback<String, String> onSuccess, final String route, final List<NameValuePair> parameters) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return sendRequest(onSuccess, route, parameters);
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    onSuccess.execute(response);
                }
            }
        }.execute();
    }

    private void sendSynchRequest(final ResponseCallback<String, String> onSuccess, final String route, final List<NameValuePair> parameters) {
        String response = sendRequest(onSuccess, route, parameters);

        if (response != null) {
            onSuccess.execute(response);
        }
    }

    // success callback is dead code...
    private String sendRequest(final ResponseCallback<String, String> onSuccess, final String route, final List<NameValuePair> parameters) {
        String url = BASE_SERVER_URI + route;
        HttpGet request;

        if(!url.endsWith("?")) {
            url += "?";
        }
        if(parameters != null) {
            String paramString = URLEncodedUtils.format(parameters, "utf-8");
            url += paramString;
        }

        request = new HttpGet(url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == SUCCESS) {
                BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder responseString = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    responseString.append(line);
                }

                return responseString.toString();
            }
            else {
                Log.d(TAG + " " + route, "Error: " + response.getStatusLine().getStatusCode());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

