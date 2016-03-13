package com.hyw.seven.testzarker;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity
{
    private static final String TAG = "test";
    private int i;

    @ViewById(R.id.viewflipper)
    ViewFlipper mViewFlipper;

    @ViewById(R.id.gridView)
    GridView mGridView;

    @ViewById(R.id.indicator_content)
    LinearLayout indicatorContent;

    private int viewflipperChildNum;
    private int[] imageResource = {R.mipmap.largecloudysunny, R.mipmap.largecloudy, R.mipmap.largerain, R.mipmap.largesnow, R.mipmap.largethundershower};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void initView()
    {
        viewflipperChildNum = imageResource.length;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LayoutInflater inflater = LayoutInflater.from(this);


        int w = (int) getResources().getDimension(R.dimen.guide_point_width);
        int margin = (int) getResources().getDimension(R.dimen.guide_point_margin);
        LinearLayout.LayoutParams lpl = new LinearLayout.LayoutParams(w, w);
        lpl.setMargins(margin, margin, margin, margin);

        Log.i(TAG, "w: " + w);

        for(int i = 0; i < viewflipperChildNum; i++)
        {
            ImageView im = new ImageView(this);
            im.setImageResource(imageResource[i]);
            im.setScaleType(ImageView.ScaleType.FIT_XY);
            im.setLayoutParams(lp);
            mViewFlipper.addView(im);

            View point = inflater.inflate(R.layout.indicator_point, null);
            point.setLayoutParams(lpl);

            if(i == 0)
                point.setBackgroundResource(R.mipmap.guidepointwhite);
            indicatorContent.addView(point);

        }

//        Log.i(TAG, "child num: " + indicatorContent.getChildCount());


        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));

    }
}
