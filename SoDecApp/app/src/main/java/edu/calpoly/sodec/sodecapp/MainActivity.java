package edu.calpoly.sodec.sodecapp;

import android.app.PendingIntent;
import android.content.Intent;
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

        // Still keeping this here until we merge in the power generation display
        new ServerConnection().getPowerGenerated(new ResponseCallback<String, String>() {
            @Override
            public void execute(Map<String, String> response) {
                mSensorReadingVw.setText(response.get("power-consumption"));
            }
        });
        mSocketConnection = ServerConnection.getSocketConnection();
        initSensorCollection();
    }

    /**
     * Broadcast the need to schedule sensor data collection if it's not already initialized.
     *
     * Based on Chris Knight's answer to http://stackoverflow.com/questions/4556670/how-to-check-if-alarmmanager-already-has-an-alarm-set
     * */
    private void initSensorCollection() {
        boolean sensorCollectionStarted = (PendingIntent.getBroadcast(this, 0,
                new Intent(SensorCollectionReceiver.ACTION_COLLECT_SENSOR_DATA),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!sensorCollectionStarted) {
            sendBroadcast(new Intent(BootReceiver.ACTION_START_COLLECTION));
        }
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
