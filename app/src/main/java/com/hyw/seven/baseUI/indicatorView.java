package com.hyw.seven.baseUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.hyw.seven.testzarker.R;

/**
 * Created by seven on 2016/3/13.
 */
public class indicatorView extends LinearLayout
{
    private static final String TAG = "test";

    private int indicatorWidth;
    private int indicatorMargin;
    private int indicatorNum;
    private int indicatorSrc, currentIndicatorSrc;
    private Context context;


    public indicatorView(Context context)
    {
        this(context, null);
    }

    public indicatorView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public indicatorView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        this.context = context;

        TypedArray ar = context.obtainStyledAttributes(attrs, R.styleable.indicator);

        indicatorWidth = (int) ar.getDimension(R.styleable.indicator_indicatorWidth, 20);
        indicatorMargin = (int) ar.getDimension(R.styleable.indicator_indicatorMargin, 5);
        indicatorNum = ar.getInt(R.styleable.indicator_indicatorNum, 0);
        indicatorSrc = ar.getResourceId(R.styleable.indicator_indicatorSrc, 0);
        currentIndicatorSrc = ar.getResourceId(R.styleable.indicator_currentIndicatorSrc, 0);

        ar.recycle();

        addIndicator(indicatorNum);
    }

    public void addIndicator(int num)
    {
        if(num <= 0 || currentIndicatorSrc <= 0 || indicatorSrc <= 0)
            return;

        Log.i(TAG, "addIndicator");
        removeAllViews();

        LinearLayout.LayoutParams lp = new LayoutParams(indicatorWidth, indicatorWidth);
        lp.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
        for(int i = 0; i < num; i++)
        {
            View view = new View(context);
            view.setLayoutParams(lp);
            if(i == 0)
                view.setBackgroundResource(currentIndicatorSrc);
            else
                view.setBackgroundResource(indicatorSrc);
            addView(view);
        }
    }

    public void setIndicator(int index)
    {
        int num = getChildCount();
        for (int i = 0; i < num; i++)
        {
            View view = getChildAt(i);
            if(i == index)
                view.setBackgroundResource(currentIndicatorSrc);
            else
                view.setBackgroundResource(indicatorSrc);
        }
    }
}
