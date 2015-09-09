package com.scau.beyondboy.dianping_client.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.scau.beyondboy.dianping_client.R;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-08
 * Time: 10:20
 */
public class SiderBar extends View
{
    private static final String TAG = SiderBar.class.getName();
    private Paint paint = new Paint();//画笔
    // 26个字母
    public final static String[] sideBar =
            { "热门","A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"
            };
    /**字母滚动条回调监听接口*/
    private OnTouchingLetterChangedListener letterChangedListener;

    public SiderBar(Context context)
    {
        super(context);
    }

    public SiderBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    //定义监听回调接口
    public interface OnTouchingLetterChangedListener
    {
        public void onTouchingLetterChanged(String s);//根据滑动位置的索引做出处理
    }
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener)
    {
        this.letterChangedListener = onTouchingLetterChangedListener;
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);//设置字体
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体颜色
        paint.setTextSize(35);
        //获取自定义View的宽和高
        int height = getHeight();
        int width = getWidth();
       // Log.i(TAG,"高度：  "+height+"    宽度：   "+width );
        //设定每一个字母所在控件的高度
        int each_height = height/sideBar.length;
        for (int i = 0; i < sideBar.length; i++)
        {
            //字体所在区域在x轴的偏移量
            float x=width/2-paint.measureText(sideBar[i])/2;
            //绘制文字的baseline高度，即相当于文字的y轴偏移量
            float y=(i+1)*each_height;
            canvas.drawText(sideBar[i],x,y,paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        final int action=event.getAction();
        final float y=event.getY();
        //根据点击坐标求出对应字母的索引
        int index=(int)(y/getHeight()*sideBar.length);
        switch (action)
        {
            case MotionEvent.ACTION_UP:
                setBackgroundResource(android.R.color.transparent);
                invalidate();
                break;
            default:
                setBackgroundResource(R.drawable.sidebar_background);
                Log.i(TAG,"索引：  "+index);
                //回调字母滚动条监听接口
                if(index>0&&sideBar.length>0)
                    if(letterChangedListener!=null)
                        letterChangedListener.onTouchingLetterChanged(sideBar[index]);
                invalidate();
                break;
        }
        return true;
    }
}
