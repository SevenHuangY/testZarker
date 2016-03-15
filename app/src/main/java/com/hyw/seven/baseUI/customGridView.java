package com.hyw.seven.baseUI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridView;

/**
 * Created by HuangYW on 2016/3/15.
 */
public class customGridView extends GridView
{
    private static final String TAG = "test";

    public customGridView(Context context)
    {
        super(context);
    }

    public customGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        Log.i(TAG, "height size: " + MeasureSpec.getSize(expandSpec));
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
