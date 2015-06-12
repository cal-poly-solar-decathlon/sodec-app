package edu.calpoly.sodec.sodecapp;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by JustineDunham on 6/9/15.
 */
public class BannerLayout extends LinearLayout {

    private LinearLayout bannerLayout;
    private LinearLayout pageLayout;
    private LinearLayout logoLayout;
    private LinearLayout titleLayout;
    private TextView pageTitle;
    private TextView bannerText;
    private ImageView bannerLogo;

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
        super.setOrientation(LinearLayout.VERTICAL);
        LayoutParams mainParams = (LayoutParams) this.getLayoutParams();
        if (mainParams == null) {
            mainParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        this.setLayoutParams(mainParams);
        this.setWeightSum(10);

        initBanner(context);

        pageLayout = new LinearLayout(context);
        pageLayout.setOrientation(LinearLayout.HORIZONTAL);
        pageLayout.setBackgroundColor(Color.GRAY);
        pageLayout.setPadding(25, 25, 25, 25);


        LayoutParams pageParams = (LayoutParams) pageLayout.getLayoutParams();
        if (pageParams == null) {
            pageParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            //pageParams = new LayoutParams(0, 0);
        }
        pageParams.weight = 1.5f;
        pageLayout.setLayoutParams(pageParams);

        super.addView(bannerLayout);
        super.addView(pageLayout);
    }

    private void initBanner(Context context) {
        bannerLayout = new LinearLayout(context);
        bannerLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams bannerParams = (LayoutParams) bannerLayout.getLayoutParams();
        if (bannerParams == null) {
            bannerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        bannerParams.weight = 8.5f;
        bannerLayout.setLayoutParams(bannerParams);

        bannerLayout.setBackgroundColor(Color.rgb(86, 137, 53));
        bannerLayout.setWeightSum(10);

        titleLayout = new LinearLayout(bannerLayout.getContext());
        pageTitle = new TextView(titleLayout.getContext());
        LayoutParams titleParams = (LayoutParams) titleLayout.getLayoutParams();
        if (titleParams == null) {
            titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        //titleParams.weight = 3;
        titleLayout.setLayoutParams(titleParams);

        pageTitle.setLayoutParams(titleParams);
        pageTitle.setTextSize(40);
        pageTitle.setGravity(Gravity.CENTER);
        pageTitle.setTextColor(Color.WHITE);
        pageTitle.setPadding(0, 0, 0, 0);
        titleLayout.setOrientation(HORIZONTAL);
        titleLayout.addView(pageTitle);

        logoLayout = new LinearLayout(context);
        LayoutParams logoParams = (LayoutParams) logoLayout.getLayoutParams();
        if (logoParams == null) {
            logoParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
        //logoParams.weight = 7;
        //logoParams.gravity = Gravity.START;
        logoLayout.setLayoutParams(logoParams);

        bannerLogo = new ImageView(bannerLayout.getContext());
        bannerLogo.setLayoutParams(logoParams);
        bannerLogo.setImageResource(R.drawable.cpsolar_logo);
        bannerLogo.setAdjustViewBounds(true);
        bannerLogo.setPadding(25, 0, 0, 0);

        logoLayout.addView(bannerLogo);

        bannerLayout.addView(logoLayout);
        bannerLayout.addView(titleLayout);
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
