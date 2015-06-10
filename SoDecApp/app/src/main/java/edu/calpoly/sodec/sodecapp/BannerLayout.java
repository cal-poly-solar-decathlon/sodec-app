package edu.calpoly.sodec.sodecapp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by JustineDunham on 6/9/15.
 */
public class BannerLayout extends LinearLayout {

    private LinearLayout bannerLayout;
    private LinearLayout pageLayout;
    private TextView pageTitle;
    private TextView bannerText;

    public BannerLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        bannerLayout = new LinearLayout(context);
        bannerLayout.setOrientation(LinearLayout.HORIZONTAL);
        pageLayout = new LinearLayout(context);
        pageLayout.setOrientation(LinearLayout.HORIZONTAL);
        pageLayout.setBackgroundColor(Color.GRAY);

        super.setOrientation(LinearLayout.VERTICAL);

        //this.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        LayoutParams mainParams = (LayoutParams) this.getLayoutParams();
        if (mainParams == null) {
            mainParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        this.setLayoutParams(mainParams);
        this.setWeightSum(1.0f);

        LayoutParams bannerParams = (LayoutParams) bannerLayout.getLayoutParams();
        if (bannerParams == null) {
            bannerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        bannerParams.weight = 0.9f;
        bannerLayout.setLayoutParams(bannerParams);

        bannerLayout.setBackgroundColor(Color.GREEN);
        bannerLayout.setBackgroundColor(Color.rgb(0, 102, 51));
        bannerLayout.setWeightSum(1.0f);

        LayoutParams pageParams = (LayoutParams) pageLayout.getLayoutParams();
        if (pageParams == null) {
            pageParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            //pageParams = new LayoutParams(0, 0);
        }
        pageParams.weight = 0.1f;
        pageLayout.setLayoutParams(pageParams);

        pageTitle = new TextView(bannerLayout.getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 0.4f;
        pageTitle.setLayoutParams(params);
        pageTitle.setText("Page Title");
        pageTitle.setGravity(Gravity.CENTER_VERTICAL);
        pageTitle.setTextColor(Color.WHITE);
        pageTitle.setTextSize(24);

        bannerText = new TextView(bannerLayout.getContext());
        params.weight = 0.4f;
        bannerText.setLayoutParams(params);
        bannerText.setText("Cal Poly Solar Decathlon App");
        bannerText.setTextColor(Color.WHITE);
        bannerText.setGravity(Gravity.CENTER_VERTICAL);
        bannerText.setTextSize(30);
        bannerText.setPadding(20, 0, 0, 0);

        bannerLayout.addView(bannerText);
        bannerLayout.addView(pageTitle);
        super.addView(bannerLayout);
        super.addView(pageLayout);
    }

    @Override
    public void setOrientation(int orientation) {
        pageLayout.setOrientation(orientation);
    }

    @Override
    public void addView(View view) {
        pageLayout.addView(view);
    }

    public void setBannerText(String text) {
        bannerText.setText(text);
    }

    public void setPageTitleText(String text) {
        pageTitle.setText(text);
    }

}
