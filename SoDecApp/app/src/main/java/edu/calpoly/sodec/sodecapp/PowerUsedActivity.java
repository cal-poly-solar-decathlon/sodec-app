package edu.calpoly.sodec.sodecapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class PowerUsedActivity extends TabActivity {

    private BannerLayout bannerLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bannerLayout = new BannerLayout(this);
        //bannerLayout.addView(R.layout.activity_main);

        View view = findViewById(R.id.powerUsedLayout);
        ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);
        bannerLayout.addView(view);
        parent.addView(bannerLayout);
        bannerLayout.setPageTitleText("Power Usage");
        TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tabUsedDayGraph").setIndicator("Past Day").setContent(new Intent(this,PowerUseGraphDayActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabUsedMonthGraph").setIndicator("Past Month").setContent(new Intent(this,PowerUseGraphMonthActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabUsedWeekGraph").setIndicator("Past Week").setContent(new Intent(this,PowerUseGraphWeekActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("tabUsedYearGraph").setIndicator("Past Year").setContent(new Intent(this, PowerUseGraphYearActivity.class)));
        mTabHost.setCurrentTab(0);
    }
}
