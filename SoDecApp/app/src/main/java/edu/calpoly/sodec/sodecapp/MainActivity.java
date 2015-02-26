package edu.calpoly.sodec.sodecapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.tavendo.autobahn.WebSocketConnection;
import edu.calpoly.sodec.sodecapp.ServerConnection.ResponseCallback;

import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private TextView mSensorReadingVw;
    private WebSocketConnection mSocketConnection;

    // Local backend when using an emulator
    private static final String SOCKET_URI = "ws://10.0.2.2:3001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorReadingVw = (TextView) this.findViewById(R.id.sensorReading);

        // TODO: Move this to a service or broadcast receiver at some point
        new ServerConnection().getPowerGenerated(new ResponseCallback<String, String>() {
            @Override
            public void execute(Map<String, String> response) {
                mSensorReadingVw.setText(response.get("power-consumption"));
            }
        });
        mSocketConnection = ServerConnection.getSocketConnection();
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
