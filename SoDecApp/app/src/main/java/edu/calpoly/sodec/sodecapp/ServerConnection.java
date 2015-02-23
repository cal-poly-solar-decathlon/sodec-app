package edu.calpoly.sodec.sodecapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class ServerConnection {

    // Remote backend (may not be updated currently)
    //private static final String BASE_SERVER_URI = "http://192.227.237.2:3000";
    // Local backend when using an emulator
    private static final String BASE_SERVER_URI = "http://10.0.2.2:3000";

    public static final int SUCCESS = 200;

    // For now we do not need to associate any state with this class. Some will probably be added
    // as we go though.
    public ServerConnection() { }

    /** Indicates commands to execute once a server response is received. */
    public interface ResponseCallback<K, V> {
        public void execute(Map<K, V> json);
    }

    /** Retrieve the most recent amount of power generated. */
    public void getPowerGenerated(final ResponseCallback<String, String> onSuccess) {
        new AsyncTask<Void, Void, Map>() {

            @Override
            protected Map doInBackground(Void... params) {

                HttpGet request = new HttpGet(BASE_SERVER_URI + "/power");
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = null;
                Map<String, String> jsonResponse = null;

                try {
                    response = httpClient.execute(request);

                    if (response.getStatusLine().getStatusCode() == SUCCESS) {
                        jsonResponse = new ObjectMapper().readValue(
                                response.getEntity().getContent(), Map.class);
                    }
                    else {
                        Log.d("ServerConnection power", "Error: " +
                                response.getStatusLine().getStatusCode());
                    }
                    response.close();
                    httpClient.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return jsonResponse;
            }

            @Override
            protected void onPostExecute(Map response) {
                if (response != null) {
                    onSuccess.execute(response);
                }
            }
        }.execute();
    }
}
