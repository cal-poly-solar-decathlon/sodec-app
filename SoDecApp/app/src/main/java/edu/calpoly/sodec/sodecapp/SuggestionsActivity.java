package edu.calpoly.sodec.sodecapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SuggestionsActivity extends ActionBarActivity {
    private static final String DEVICE_OUTSIDE = "s-temp-out";
    private static final String DEVICE_BED = "s-temp-bed";
    private static final String DEVICE_BATH = "s-temp-bath";
    private static final String DEVICE_LIVING_ROOM = "s-temp-lr";


    private EditText inputPreferredTemp;
    private TextView preferredTemp;
    private TextView windowSuggestion;

    private double preferredTempValue;
    private double currInsideTemp;
    private double currOutsideTemp;
    private double trendBedroomTemp;
    private double trendBathroomTemp;
    private double trendLivingRoomTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        this.loadTemp();
        this.initLayout();
        this.inputPreferredTemp.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView wrappedTextValue, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    preferredTempValue = Double.parseDouble(wrappedTextValue.getText().toString());
                    preferredTemp.setText(wrappedTextValue.getText().toString());
                    loadSuggestion();
                    handled = true;
                }
                return handled;
            }
        });
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

    private void initLayout() {
        setContentView(R.layout.activity_suggestions);
        inputPreferredTemp = (EditText) this.findViewById(R.id.inputPreferredTemp);
        preferredTemp = (TextView) this.findViewById(R.id.textPreferredTemp);
        windowSuggestion = (TextView) this.findViewById(R.id.textWindowSuggestion);

    }

    private void loadTemp() {
        DatabaseUtils.getInsideTemp(new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                currInsideTemp = Double.parseDouble(response);
            }
        });

        DatabaseUtils.getOutsideTemp(new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                currOutsideTemp = Double.parseDouble(response);
            }
        });

        trendBathroomTemp = SuggestionsUtils.getTrendByID(DEVICE_BATH, TimestampUtils.getStartIsoForDay() ,TimestampUtils.getIsoForNow());
        trendBedroomTemp = SuggestionsUtils.getTrendByID(DEVICE_BED, TimestampUtils.getStartIsoForDay(),TimestampUtils.getIsoForNow());
        trendLivingRoomTemp = SuggestionsUtils.getTrendByID(DEVICE_LIVING_ROOM,TimestampUtils.getStartIsoForDay() ,TimestampUtils.getIsoForNow());

    }

    private void loadSuggestion() {
        if (this.currInsideTemp > this.preferredTempValue && this.currOutsideTemp < this.currInsideTemp)
        {
            this.windowSuggestion.setText("Open your windows");
        }
        else {
            this.windowSuggestion.setText(("Close your windows"));
            //TODO: Needs refining. 
        }
    }
}
