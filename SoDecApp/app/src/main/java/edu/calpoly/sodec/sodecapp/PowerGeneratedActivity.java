package edu.calpoly.sodec.sodecapp;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.content.Intent;

public class PowerGeneratedActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tabHoursGraph").setIndicator("Hours").setContent(new Intent(this,PowerGraphDayActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabDaysGraph").setIndicator("Days").setContent(new Intent(this,PowerGraphMonthActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabWeeksGraph").setIndicator("Weeks").setContent(new Intent(this,PowerGraphWeekActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabMonthsGraph").setIndicator("Months").setContent(new Intent(this, PowerGraphYearActivity.class)));
        mTabHost.setCurrentTab(0);
    }
}
