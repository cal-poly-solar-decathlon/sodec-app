package edu.calpoly.sodec.sodecapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends ActionBarActivity {

    private WebSocketClient mServerConnection;

    private TextView mSensorReadingVw;

    // TODO: Update this when backend gets hosted, currently points to locally running backend
    private static final String SOCKET_URI = "ws://10.0.2.2:3001";

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
        URI connectionUri;

        try {
            connectionUri = new URI(SOCKET_URI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mServerConnection = new WebSocketClient(connectionUri) {
            @Override
            public void onOpen(ServerHandshake handshakeData) {
                Log.d("connectToServer", "open");
                displayReading("Connecting...");
            }

            // Just display incoming readings/messages initially for vertical prototype
            @Override
            public void onMessage(String message) {
                Log.d("connectToServer", message);
                displayReading(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("connectToServer", "close");
                displayReading("Closing the connection...");
            }

            @Override
            public void onError(Exception ex) {
                Log.d("connectToServer", "Error: " + ex.toString());
            }
        };
        mServerConnection.connect();
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
    protected void onDestroy() {
        if (mServerConnection != null) {
            mServerConnection.close();
            mServerConnection = null;
        }
        super.onDestroy();
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
