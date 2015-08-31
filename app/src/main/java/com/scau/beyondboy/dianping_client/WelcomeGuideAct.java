package com.scau.beyondboy.dianping_client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;


/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-08-30
 * Time: 10:13
 * 欢迎导航页面
 */
public class WelcomeGuideAct extends AppCompatActivity
{
    private static final String TAG = WelcomeGuideAct.class.getName();
    @Bind(R.id.welcome_pager)
    ViewPager mViewPager;
    @Bind(R.id.welcome_guide_btn)
    Button mClickEnter;
    private List<ImageView> mPagerData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_guide);
        ButterKnife.bind(this);
        initViewPager();
    }
    private void initViewPager()
    {
        mPagerData = new ArrayList<>();
        ImageView imageGuide1=new ImageView(this);
        imageGuide1.setImageBitmap(compressImage(R.drawable.guide_01));
        ImageView imageGuide2=new ImageView(this);
        imageGuide2.setImageBitmap(compressImage(R.drawable.guide_02));
        ImageView imageGuide3=new ImageView(this);
        imageGuide3.setImageBitmap(compressImage(R.drawable.guide_03));
        mPagerData.add(imageGuide1);
        mPagerData.add(imageGuide2);
        mPagerData.add(imageGuide3);
        mViewPager.setAdapter(new MyPagerAdater());
    }

    /**
     * 监听被选中的页面，如果是最后一页则显示点击按钮
     * @param position 页的位置
     */
    @OnPageChange(R.id.welcome_pager)
    public void onPageSelected(int position)
    {
        if (position == 2)
            mClickEnter.setVisibility(View.VISIBLE);
        else
            mClickEnter.setVisibility(View.INVISIBLE);
    }

    /**
     * 按钮监听，一点击跳转
     */
    @OnClick(R.id.welcome_guide_btn)
    public void setClickEnter()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    private class MyPagerAdater extends PagerAdapter
    {
        private  final String TAG = MyPagerAdater.class.getName();

        @Override
        public int getCount()
        {
            return mPagerData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            container.addView(mPagerData.get(position));
            return mPagerData.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView(mPagerData.get(position));
        }
    }

    /**
     * 压缩图片
     * @param imageId 图片资源的
     */
    @NonNull
    private Bitmap compressImage(@DrawableRes int imageId)
    {
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        BitmapFactory.Options imageOptions= new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds=true;
        //获取图片信息,不加载内存
        BitmapFactory.decodeResource(getResources(), imageId, imageOptions);
        imageOptions.inJustDecodeBounds=false;
        //如果手机像素大于160，那么设置该信息后，图片的宽度和高度就不会被加倍
        imageOptions.inDensity=metrics.densityDpi;
        //压缩图片
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(), imageId, imageOptions);
        Log.i(TAG,"图片的内存： "+bitmap.getRowBytes()*bitmap.getHeight()/1024/1024);
        return bitmap;
    }
}
