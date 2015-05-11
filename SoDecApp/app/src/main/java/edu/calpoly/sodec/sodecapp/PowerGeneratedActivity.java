package edu.calpoly.sodec.sodecapp;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.content.Intent;

public class PowerGeneratedActivity extends TabActivity {

    // TODO: We need to change this when eguage gets split into multiple devices
    public static final String DEVICE = "s-temp-lr";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tabDayGraph").setIndicator("Past Day").setContent(new Intent(this,PowerGraphDayActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabMonthGraph").setIndicator("Past Month").setContent(new Intent(this, PowerGraphMonthActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tabWeekGraph").setIndicator("Past Week").setContent(new Intent(this,PowerGraphWeekActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabYearGraph").setIndicator("Past Year").setContent(new Intent(this, PowerGraphYearActivity.class)));

        mTabHost.setCurrentTab(0);
/*
        mTabHost.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                startActivity(new Intent(PowerGeneratedActivity.this, PowerActivity.class));
            }

            public void onSwipeLeft() {
            }

            public void onSwipeBottom() {
            }
        });*/
    }
}
