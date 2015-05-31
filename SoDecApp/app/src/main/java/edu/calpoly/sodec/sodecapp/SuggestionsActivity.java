package edu.calpoly.sodec.sodecapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class SuggestionsActivity extends ActionBarActivity {
    private static final String DEVICE_OUTSIDE = "s-temp-out";
    private static final String DEVICE_BED = "s-temp-bed";
    private static final String DEVICE_BATH = "s-temp-bath";
    private static final String DEVICE_LIVING_ROOM = "s-temp-lr";


    private EditText editTextInputPreferredTemp;
    private TextView textViewPreferredTemp;
    private TextView textViewWindowSuggestion;
    private Button buttonSubmitPreferredTemp;
    private ImageView imageViewWindowSuggestion;
    private ImageView imageViewTempTrend;

    private double preferredTempValue;
    private double currInsideTemp;
    private double currOutsideTemp;
    private double trendBedroomTemp;
    private double trendBathroomTemp;
    private double trendLivingRoomTemp;
    private double trendOverallInsideTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        this.initLayout();
        this.loadTemp();


        buttonSubmitPreferredTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSuggestion();
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
        editTextInputPreferredTemp = (EditText) this.findViewById(R.id.editTextInputPreferredTemp);
        textViewWindowSuggestion = (TextView) this.findViewById(R.id.textViewWindowSuggestion);
        textViewPreferredTemp = (TextView) this.findViewById(R.id.textViewPrefTemp);
        buttonSubmitPreferredTemp = (Button) this.findViewById((R.id.buttonSubmitPreferredTemp));
        imageViewWindowSuggestion = (ImageView) this.findViewById(R.id.imageViewWindowSuggestion);
        imageViewTempTrend = (ImageView) this.findViewById(R.id.imageViewTempTrend);
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


        this.trendBathroomTemp = SuggestionsUtils.getTrendByID(DEVICE_BATH, TimestampUtils.getStartIsoForDay() ,TimestampUtils.getIsoForNow());
        this.trendBedroomTemp = SuggestionsUtils.getTrendByID(DEVICE_BED, TimestampUtils.getStartIsoForDay(),TimestampUtils.getIsoForNow());
        this.trendLivingRoomTemp = SuggestionsUtils.getTrendByID(DEVICE_LIVING_ROOM,TimestampUtils.getStartIsoForDay() ,TimestampUtils.getIsoForNow());
        this.trendOverallInsideTemp = (this.trendBathroomTemp+this.trendBedroomTemp+this.trendLivingRoomTemp)/3;

        if (this.trendOverallInsideTemp > 0) {
            this.imageViewTempTrend.setImageResource(R.drawable.temp_rising);
        } else {
            this.imageViewTempTrend.setImageResource(R.drawable.temp_falling);

        }
    }

    private void loadSuggestion() {
        this.preferredTempValue = Double.parseDouble(editTextInputPreferredTemp.getText().toString());
        this.textViewPreferredTemp.setText(Double.toString(this.preferredTempValue));

        // It's too hot inside and it's colder outside, or it's too cold inside and it's hotter outside.
        if (this.currInsideTemp > this.preferredTempValue && this.currOutsideTemp < this.currInsideTemp ||
            this.currInsideTemp < this.preferredTempValue && this.currOutsideTemp > this.currInsideTemp)
        {
            this.textViewWindowSuggestion.setText("Open your window.");
            this.imageViewWindowSuggestion.setImageResource(R.drawable.open_window);
        }

        else {
            this.textViewWindowSuggestion.setText(("Close your windows"));
            this.imageViewWindowSuggestion.setImageResource(R.drawable.closed_window);

            //TODO: Needs refining. 
        }
    }
}
