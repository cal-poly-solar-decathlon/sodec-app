package edu.calpoly.sodec.sodecapp;

import android.app.TabActivity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.content.Intent;

public class PowerGeneratedActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tabHoursGraph").setIndicator("Hours").setContent(new Intent(this,PowerGraphHoursActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabDaysGraph").setIndicator("Days").setContent(new Intent(this,PowerGraphDaysActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabWeeksGraph").setIndicator("Weeks").setContent(new Intent(this,PowerGraphWeeksActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabMonthsGraph").setIndicator("Months").setContent(new Intent(this, PowerGraphMonthsActivity.class )));
        mTabHost.setCurrentTab(0);
    }
}
