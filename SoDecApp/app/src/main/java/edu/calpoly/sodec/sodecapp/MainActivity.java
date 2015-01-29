package edu.calpoly.sodec.sodecapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends ActionBarActivity {

    private WebSocketConnection mServerConnection;

    private TextView mSensorReadingVw;

    // Remote backend
    private static final String SOCKET_URI = "ws://192.227.237.2:8080";
    // Local backend when using an emulator
    //private static final String SOCKET_URI = "ws://10.0.2.2:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorReadingVw = (TextView) this.findViewById(R.id.sensorReading);
        connectToServer();
    }

    /**
     * Connects to the SoDec backend using a web socket.
     */
    private void connectToServer() {

        mServerConnection = new WebSocketConnection();
        try {
            mServerConnection.connect(SOCKET_URI, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    mServerConnection.sendTextMessage("One day, this will be useful information " +
                            "from the Solar Decathlon house.");
                }

                @Override
                public void onTextMessage(String message) {
                    displayReading("The server says... " + message);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d("Server Connection", "The connection was lost because " + reason);
                }
            });
        } catch (WebSocketException e) {
            Log.d("Server Connection", "Error: " + e.toString());
        }
    }

    /**
     * Show the given reading.
     */
    private void displayReading(final String reading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSensorReadingVw.setText(reading);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
