package com.hyw.seven.baseUI;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HuangYW on 2016/3/14.
 */
public class customViewPager extends ViewPager
{
    public customViewPager(Context context)
    {
        super(context);
    }

    public customViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // 解决ViewPager的layout_height = Wrap_Content时，占据全屏的问题
        if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST)
        {
            int height = 0;
            for (int i = 0; i < getChildCount(); i++)
            {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height)
                    height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
