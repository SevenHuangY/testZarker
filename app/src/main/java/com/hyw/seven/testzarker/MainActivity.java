package com.hyw.seven.testzarker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hyw.seven.baseUI.customViewPager;
import com.hyw.seven.baseUI.fixedSpeedScroller;
import com.hyw.seven.baseUI.indicatorView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.layout_main)
public class MainActivity extends Activity
{
    /**
     * 请求更新显示的View。
     */
    protected static final int MSG_UPDATE_IMAGE  = 1;
    /**
     * 请求暂停轮播。
     */
    protected static final int MSG_KEEP_SILENT   = 2;
    /**
     * 请求恢复轮播。
     */
    protected static final int MSG_BREAK_SILENT  = 3;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
     */
    protected static final int MSG_PAGE_CHANGED  = 4;

    //轮播间隔时间
    protected static final long MSG_DELAY = 3000;

    private static final String TAG = "test";
    private int i;

    @ViewById(R.id.viewpager)
    customViewPager mViewPager;

    @ViewById(R.id.gridView)
    GridView mGridView;

    @ViewById(R.id.indicatorContent)
    indicatorView indicatorContent;


    private int[] imageResource = {R.mipmap.largecloudysunny, R.mipmap.largecloudy, R.mipmap.largerain, R.mipmap.largesnow, R.mipmap.largethundershower};
    private viewPagerAdapter mAdapter;
    private ArrayList<ImageView> images;
    private updateHandler mHandler;
    private int currentPageIndex = 0;

    private fixedSpeedScroller mScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void initView()
    {
        Log.i(TAG, "initView");
        mAdapter = new viewPagerAdapter();
        mHandler = new updateHandler(this);


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        images = new ArrayList<ImageView>();
        for (int i = 0; i < imageResource.length; i++)
        {
            ImageView im = new ImageView(this);
            im.setImageResource(imageResource[i]);
            im.setScaleType(ImageView.ScaleType.FIT_XY);
            im.setLayoutParams(lp);
            images.add(im);
        }


        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                indicatorContent.setIndicator(position % imageResource.length);
                mHandler.sendMessage(Message.obtain(mHandler, MSG_PAGE_CHANGED, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                switch (state)
                {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mHandler.sendEmptyMessage(MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });

        try
        {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new fixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
            mScroller.setmDuration(600);

            mField.set(mViewPager, mScroller);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界

        indicatorContent.addIndicator(imageResource.length);
        indicatorContent.setIndicator((Integer.MAX_VALUE / 2) % imageResource.length);

        //开始轮播效果
        mHandler.sendEmptyMessage(MSG_UPDATE_IMAGE);
        Log.i(TAG, "send empty message");
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }

    private class updateHandler extends Handler
    {
        WeakReference<MainActivity> mActivity;

        updateHandler(MainActivity activity)
        {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg)
        {
            MainActivity activity = mActivity.get();
            Log.i(TAG, "handler");
            if (activity == null)
            {
                //Activity已经回收，无需再处理UI了
                Log.i(TAG, "null");
                return ;
            }


            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (activity.mHandler.hasMessages(MSG_UPDATE_IMAGE))
            {
                activity.mHandler.removeMessages(MSG_UPDATE_IMAGE);
                Log.i(TAG, "remove");
            }
            switch (msg.what)
            {
                case MSG_UPDATE_IMAGE:
                    currentPageIndex++;
                    Log.i(TAG, "MSG_UPDATE_IMAGE: " + currentPageIndex);
                    activity.mViewPager.setCurrentItem(currentPageIndex);
                    //准备下次播放
                    activity.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    activity.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentPageIndex = msg.arg1;
                    Log.i(TAG, "PAGE_CHANGED: " + currentPageIndex);
                    break;
                default:
                    break;
            }
        }
    }

    private class viewPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            //对ViewPager页号求模取出View列表中要显示的项
            position %= images.size();

            if (position < 0)
            {
                position = images.size() + position;
            }

            ImageView view = images.get(position);

            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp =view.getParent();
            if (vp != null)
            {
                ViewGroup parent = (ViewGroup)vp;
                parent.removeView(view);
            }
            container.addView(view);
            //add listeners here if necessary
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {

        }
    }
}
