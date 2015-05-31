package edu.calpoly.sodec.sodecapp;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.PieChartView;

public class PowerActivity extends ActionBarActivity {

    public static final String DEVICE_LAUNDRY = "s-elec-used-laundry";
    public static final String DEVICE_DISHWASHER = "s-elec-used-dishwasher";
    public static final String DEVICE_REFRIGERATOR = "s-elec-used-refrigerator";
    public static final String DEVICE_INDUCTION_STOVE = "s-elec-used-induction-stove";
    public static final String DEVICE_EWH_SOLAR_WATER_HEATER = "s-elec-used-ewh-solar-water-heater";
    public static final String DEVICE_KITCHEN_RECEPS_1 = "s-elec-used-kitchen-receps-1";
    public static final String DEVICE_KITCHEN_RECEPS_2 = "s-elec-used-kitchen-receps-2";
    public static final String DEVICE_LIVING_RECEPS = "s-elec-used-living-receps";
    public static final String DEVICE_DINING_RECEPS_1 = "s-elec-used-dining-receps-1";
    public static final String DEVICE_DINING_RECEPS_2 = "s-elec-used-dining-receps-2";
    public static final String DEVICE_BATHROOM_RECEPS = "s-elec-used-bathroom-receps";
    public static final String DEVICE_BEDROOM_RECEPS_1 = "s-elec-used-bedroom-receps-1";
    public static final String DEVICE_BEDROOM_RECEPS_2 = "s-elec-used-bedroom-receps-2";
    public static final String DEVICE_MECHANICAL_RECEPS = "s-elec-used-mechanical-receps";
    public static final String DEVICE_ENTRY_RECEPS = "s-elec-used-entry-receps";
    public static final String DEVICE_EXTERIOR_RECEPS = "s-elec-used-exterior-receps";
    public static final String DEVICE_GREY_WATER_PUMP_RECEP = "s-elec-used-grey-water-pump-recep";
    public static final String DEVICE_BLACK_WATER_PUMP_RECEP = "s-elec-used-black-water-pump-recep";
    public static final String DEVICE_THERMAL_LOOP_PUMP_RECEP = "s-elec-used-thermal-loop-pump-recep";
    public static final String DEVICE_WATER_SUPPLY_PUMP_RECEP = "s-elec-used-water-supply-pump-recep";
    public static final String DEVICE_WATER_SUPPLY_BOOSTER_PUMP_RECEP = "s-elec-used-water-supply-booster-pump-recep";
    public static final String DEVICE_VEHICLE_CHARGING_RECEP = "s-elec-used-vehicle-charging-recep";
    public static final String DEVICE_HEAT_PUMP_RECEP = "s-elec-used-heat-pump-recep";
    public static final String DEVICE_AIR_HANDLER_RECEP = "s-elec-used-air-handler-recep";

    public static final String[] USE_DEVICES = {
            DEVICE_LAUNDRY, DEVICE_DISHWASHER, DEVICE_REFRIGERATOR, DEVICE_INDUCTION_STOVE,
            DEVICE_EWH_SOLAR_WATER_HEATER, DEVICE_KITCHEN_RECEPS_1, DEVICE_KITCHEN_RECEPS_2,
            DEVICE_LIVING_RECEPS, DEVICE_DINING_RECEPS_1, DEVICE_DINING_RECEPS_2,
            DEVICE_BATHROOM_RECEPS, DEVICE_BEDROOM_RECEPS_1, DEVICE_BEDROOM_RECEPS_2,
            DEVICE_MECHANICAL_RECEPS, DEVICE_ENTRY_RECEPS, DEVICE_EXTERIOR_RECEPS,
            DEVICE_GREY_WATER_PUMP_RECEP, DEVICE_BLACK_WATER_PUMP_RECEP, DEVICE_THERMAL_LOOP_PUMP_RECEP,
            DEVICE_WATER_SUPPLY_PUMP_RECEP, DEVICE_WATER_SUPPLY_BOOSTER_PUMP_RECEP,
            DEVICE_VEHICLE_CHARGING_RECEP, DEVICE_HEAT_PUMP_RECEP, DEVICE_AIR_HANDLER_RECEP
    };

    private Float bedroom_total;
    private Float bathroom_total;
    private Float living_total;
    private Float kitchen_total;
    private Float mech_total;
    private Float dining_total;

    private TextView kitchen_power;
    private TextView bathroom_power;
    private TextView bedroom_power;
    private TextView living_power;
    private TextView dining_power;
    private TextView mech_power;

    private PieChartData mData;
    private PieChartView mChart;

    private RelativeLayout mLayout;

    private String[] mRooms = {"Bedroom", "Kitchen", "Living Room", "Dining Room", "Bathroom",
                               "Mechanical"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        super.onCreate(savedInstanceState);

        living_total = new Float(0.0);
        bathroom_total = new Float(0.0);
        bedroom_total = new Float(0.0);
        kitchen_total = new Float(0.0);
        dining_total = new Float(0.0);
        mech_total = new Float(0.0);

        initLayout();
        mData = new PieChartData();
        mChart = (PieChartView) findViewById(R.id.roomPowerChart);
        mLayout = (RelativeLayout) findViewById(R.id.PowerLayout);

        setData();

        mLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
            }

            public void onSwipeLeft() {
                startActivity(new Intent(PowerActivity.this, PowergraphActivity.class));
            }

            public void onSwipeBottom() {
            }
        });
    }

    private void initLayout() {
        setContentView(R.layout.activity_power);

        bedroom_power = (TextView) this.findViewById(R.id.bedroom_power);
        bathroom_power = (TextView) this.findViewById(R.id.bathroom_power);
        living_power = (TextView) this.findViewById(R.id.living_power);
        kitchen_power = (TextView) this.findViewById(R.id.kitchen_power);
        mech_power = (TextView) this.findViewById(R.id.mech_power);
        dining_power = (TextView) this.findViewById(R.id.dining_power);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_power, menu);
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

    @Override
    protected void onStart() {
        super.onStart();
        loadPowerInfo();
    }

    private void loadPowerInfo() {
        PowerUtils.getPowerByID(DEVICE_LAUNDRY, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                //living_power.setText(response);
                living_total += Float.parseFloat(response);
                living_power.setText(living_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_DISHWASHER, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                kitchen_total += Float.parseFloat(response);
                kitchen_power.setText(kitchen_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_REFRIGERATOR, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                kitchen_total += Float.parseFloat(response);
                kitchen_power.setText(kitchen_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_INDUCTION_STOVE, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                kitchen_total += Float.parseFloat(response);
                kitchen_power.setText(kitchen_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_EWH_SOLAR_WATER_HEATER, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_KITCHEN_RECEPS_1, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                kitchen_total += Float.parseFloat(response);
                kitchen_power.setText(kitchen_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_KITCHEN_RECEPS_2, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                kitchen_total += Float.parseFloat(response);
                kitchen_power.setText(kitchen_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_LIVING_RECEPS, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                living_total += Float.parseFloat(response);
                living_power.setText(living_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_DINING_RECEPS_1, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                dining_total += Float.parseFloat(response);
                dining_power.setText(dining_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_DINING_RECEPS_2, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                dining_total += Float.parseFloat(response);
                dining_power.setText(dining_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_BATHROOM_RECEPS, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                bathroom_total += Float.parseFloat(response);
                bathroom_power.setText(bathroom_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_BEDROOM_RECEPS_1, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                bedroom_total += Float.parseFloat(response);
                bedroom_power.setText(bedroom_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_BEDROOM_RECEPS_2, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                bedroom_total += Float.parseFloat(response);
                bedroom_power.setText(bathroom_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_MECHANICAL_RECEPS, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_ENTRY_RECEPS, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
            }
        });

        PowerUtils.getPowerByID(DEVICE_EXTERIOR_RECEPS, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
            }
        });

        PowerUtils.getPowerByID(DEVICE_GREY_WATER_PUMP_RECEP, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_BLACK_WATER_PUMP_RECEP, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_THERMAL_LOOP_PUMP_RECEP, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_WATER_SUPPLY_PUMP_RECEP, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_WATER_SUPPLY_BOOSTER_PUMP_RECEP, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_VEHICLE_CHARGING_RECEP, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
            }
        });

        PowerUtils.getPowerByID(DEVICE_HEAT_PUMP_RECEP, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });

        PowerUtils.getPowerByID(DEVICE_AIR_HANDLER_RECEP, new ServerConnection.ResponseCallback() {
            @Override
            public void execute(String response) {
                mech_total += Float.parseFloat(response);
                mech_power.setText(mech_total.toString());
                setData();
            }
        });


    }

    public ArrayList<SliceValue> generateSliceValues() {
        ArrayList<SliceValue> slices = (ArrayList<SliceValue>)mData.getValues();
        slices.clear();
        slices.add(new SliceValue(bedroom_total.floatValue(), ChartUtils.COLOR_BLUE)); // bedroom
        slices.add(new SliceValue(kitchen_total.floatValue(), ChartUtils.COLOR_VIOLET)); // kitchen
        slices.add(new SliceValue(living_total.floatValue(), ChartUtils.COLOR_GREEN)); // living room
        slices.add(new SliceValue(dining_total.floatValue(), ChartUtils.COLOR_ORANGE)); // dining room
        slices.add(new SliceValue(bathroom_total.floatValue(), ChartUtils.COLOR_RED)); // bathroom
        slices.add(new SliceValue(mech_total.floatValue(), Color.parseColor("#FF6699"))); // mechanical

        return slices;
    }

    // sets the room pie chart with hardcoded data
    public void setData() {
        ArrayList<SliceValue> slices = generateSliceValues();

        for (int i = 0; i < mRooms.length; i++)
        {
            slices.get(i).setLabel(mRooms[i].toCharArray());
        }
        mData.setHasLabels(true);
        mData.setValues(slices);
        mChart.setPieChartData(mData);

        mChart.setValueTouchEnabled(true);
        mChart.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
                Intent intent = new Intent(PowerActivity.this, PowerUsedRoomActivity.class);
                intent.putExtra("room", mRooms[i]);
                startActivity(intent);
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }
}
