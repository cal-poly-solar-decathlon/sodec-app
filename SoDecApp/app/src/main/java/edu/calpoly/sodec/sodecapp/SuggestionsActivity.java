package edu.calpoly.sodec.sodecapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SuggestionsActivity extends ActionBarActivity {
    private double currInsideTemp;
    private double currOutsideTemp;
    private double trendInsideTemp;
    private double trendOutsideTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        this.loadTemp();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_suggestions, menu);
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

    private void loadTemp() {
        DatabaseUtils.getInsideTemp(new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                currInsideTemp = Double.parseDouble(WeatherUtils.formatTemp(Float.parseFloat(response)));
            }
        });

        DatabaseUtils.getOutsideTemp(new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                currOutsideTemp = Double.parseDouble(WeatherUtils.formatTemp(Float.parseFloat(response)));
            }
        });
    }
}
