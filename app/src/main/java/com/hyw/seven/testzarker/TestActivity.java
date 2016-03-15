package com.hyw.seven.testzarker;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyw.seven.baseUI.customGridView;
import com.hyw.seven.baseUI.customViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;


@EActivity(R.layout.activity_test)
public class TestActivity extends Activity
{
    @ViewById(R.id.test_gridview)
    customGridView mGridView;

    @ViewById(R.id.scrollView)
    ScrollView mScrollView;

    @ViewById(R.id.test_viewpager)
    customViewPager mViewPager;

    private static final String TAG = "test";

    private int[] imageResource = {R.mipmap.largecloudysunny, R.mipmap.largecloudy, R.mipmap.largerain, R.mipmap.largesnow, R.mipmap.largethundershower};
    private viewPagerAdapter mAdapter;
    private ArrayList<ImageView> images;

    private ArrayList<String> gridViewData;
    private gridViewAdapter mGridViewAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    void init()
    {
        context = this;

        gridViewData = new ArrayList<String>();
        for(int i = 0; i < 50; i++)
        {
            String tmp = new String("test: ");
            tmp += i;
            gridViewData.add(tmp);
        }
        mGridViewAdapter = new gridViewAdapter();
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(context, "Click: " + position + " id: " + id, Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter = new viewPagerAdapter();

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
                Log.i(TAG, "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                Log.i(TAG, "onPageScrollStateChanged Select: " + state);
            }
        });
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);

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

    private class gridViewAdapter extends BaseAdapter
    {
        Holder holder;

        @Override
        public int getCount()
        {
            return gridViewData.size();
        }

        @Override
        public Object getItem(int position)
        {
            return gridViewData.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                holder = new Holder();

                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.gridview_item, null);
                holder.im = (ImageView) convertView.findViewById(R.id.gridview_item_imageview);
                holder.tx = (TextView) convertView.findViewById(R.id.gridview_item_textview);
                convertView.setTag(holder);
            }
            else
            {
                holder = (Holder) convertView.getTag();
            }

            holder.tx.setText(gridViewData.get(position));

            return convertView;
        }
    }

    private class Holder
    {
        ImageView im;
        TextView tx;
    }
}
