package edu.calpoly.sodec.sodecapp;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.content.Intent;

public class PowerUsedActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tabUsedDayGraph").setIndicator("Past Day").setContent(new Intent(this,PowerUseGraphDayActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabUsedMonthGraph").setIndicator("Past Month").setContent(new Intent(this,PowerUseGraphMonthActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabUsedWeekGraph").setIndicator("Past Week").setContent(new Intent(this,PowerUseGraphWeekActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabUsedYearGraph").setIndicator("Past Year").setContent(new Intent(this, PowerUseGraphYearActivity.class)));
        mTabHost.setCurrentTab(0);
    }
}
