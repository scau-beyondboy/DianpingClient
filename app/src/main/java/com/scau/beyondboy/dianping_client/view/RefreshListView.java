package com.scau.beyondboy.dianping_client.view;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scau.beyondboy.dianping_client.R;
import com.scau.beyondboy.dianping_client.utils.DisplayUtil;
import com.scau.beyondboy.dianping_client.utils.ShareUtils;
import com.scau.beyondboy.dianping_client.utils.TimeUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-14
 * Time: 20:33
 * 自定义上下拉更新的ListView
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener
{
    @Nullable
    @Bind(R.id.pull_to_refresh_arrow)
    ImageView mArrowView;
    @Nullable
    @Bind(R.id.pull_to_refresh_date)
    TextView mDateTextView;
    @Nullable
    @Bind(R.id.pull_to_refresh_progressbar)
    ProgressBar mProgressBar;
    @Nullable
    @Bind(R.id.pull_to_refresh_tip)
    TextView tipTextView;
    private RefreshListener mRefreshListener;
    private static final String TAG = RefreshListView.class.getName();
    private int mFirstVisibleItem;
    private int mTotalItemCount;
    private int mHeaderHeight;
    private int mFooterHeight;
    private boolean isPullEnable = false;
    /**标记箭头方向*/
    private boolean mArrowIsDown=true;
    /**控制滑动坐标改变比例*/
    private float mScrollSpeed=0.8f;
    private boolean isRefreshEable=true;
    private boolean isRefreshing=false;
    private State mState;
  //  private int mScrollState;
    private Animation mAnimation;

    public RefreshListView(Context context)
    {
        this(context,null);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }
    /**标记ListView下拉时的头部动作状态*/
    static enum State
    {
        NONE,PULL,REFRESHING,RELEASE
    }
    private void init()
    {
        View footer=LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_footer,null);
        View header= LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_header,null);
        ButterKnife.bind(this,header);
        mDateTextView.setText(ShareUtils.getRefreshData(getContext()));
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pull_to_refresh_progressbar);
        measureViewHeight(header);
        measureViewHeight(footer);
        mHeaderHeight=header.getMeasuredHeight();
        mFooterHeight=footer.getMeasuredHeight();
        //隐藏头部和尾部
        setPadding(-header.getMeasuredHeight(), -footer.getMeasuredHeight());
        addHeaderView(header);
        addFooterView(footer);
    }
    private int startY=0;

    public void setIsRefreshEable(boolean isRefreshEable)
    {
        this.isRefreshEable = isRefreshEable;
    }
    public void setRefreshListener(RefreshListener refreshListener)
    {
        mRefreshListener = refreshListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        //获取坐标移动距离
        int scrollY=(int)((ev.getY()-startY)*mScrollSpeed);
        //当大移动距离大于头部件30dp则改变提示信息和箭头方向
        if(isPullEnable&&mState==State.PULL&&scrollY>=mHeaderHeight+DisplayUtil.dip2px(getContext(),30)&&mArrowIsDown)
        {
            //Log.i(TAG,"开头30");
            mArrowIsDown=false;
            tipTextView.setText("松开刷新");
            mArrowView.setRotation(180);
        }
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //判断是否是ListView第一项
                if (!isPullEnable&&mFirstVisibleItem == 0)
                {
                    if(!isRefreshing)
                    {
                        isPullEnable = true;
                        tipTextView.setText("下拉更新");
                        mArrowView.setVisibility(VISIBLE);
                    }
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mFirstVisibleItem==0&&isRefreshing&&mState==State.REFRESHING&&scrollY>0)
                {
                    return true;
                }
                //是否能下拉刷新
                if(isPullEnable&&isRefreshEable)
                {
                    //当下拉高度达到一定程度时，更新状态为释放状态
                    if(mState==State.PULL&&scrollY>mHeaderHeight+ DisplayUtil.dip2px(getContext(),80))
                    {
                        //Log.i(TAG,"80高度");
                        isPullEnable=false;
                        mState=State.RELEASE;
                    }
                    //下拉
                    else if(scrollY>0)
                    {
                        mState=State.PULL;
                        //当下拉一段距离，又会往上回滚时，通过判断来改变相应的箭头方向和提示信息
                        if(scrollY<mHeaderHeight+DisplayUtil.dip2px(getContext(),30))
                        {
                           // Log.i(TAG,"移动下拉");
                            if(!mArrowIsDown)
                            {
                                mArrowIsDown=true;
                                mArrowView.setRotation(360);
                            }
                            tipTextView.setText("下拉更新");
                        }
                        else
                        {
                            tipTextView.setText("松开刷新");
                            mArrowView.setRotation(180);
                        }
                        setHeaderPadding(scrollY - mHeaderHeight);
                    }
                     //上拉
                    else
                    {
                       // Log.i(TAG,"移动上拉");
                        mState=State.NONE;
                        isPullEnable=false;
                    }
                }
                else if(!isPullEnable&&mState==State.RELEASE)
                {
                   // Log.i(TAG,"移动释放");
                    startY=0;
                    setHeaderPadding(0);
                    mArrowView.setVisibility(INVISIBLE);
                    mProgressBar.setVisibility(VISIBLE);
                    mProgressBar.startAnimation(mAnimation);
                    mState=State.REFRESHING;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!mArrowIsDown)
                {
                    mArrowIsDown=true;
                    mArrowView.setRotation(360);
                }
                switch (mState)
                {
                    case PULL:
                        if(scrollY>=mHeaderHeight+DisplayUtil.dip2px(getContext(),30))
                        {
                           // Log.i(TAG,"抬起下拉");
                            mState=State.RELEASE;
                        }
                        else
                        {
                           // Log.i(TAG,"抬起恢复");
                            mState = State.NONE;
                            setHeaderPadding(-mHeaderHeight);
                            break;
                        }
                    case RELEASE:
                        startY=0;
                       // Log.i(TAG,"抬起释放");
                        setHeaderPadding(0);
                        mArrowView.setVisibility(INVISIBLE);
                        mProgressBar.setVisibility(VISIBLE);
                        mProgressBar.startAnimation(mAnimation);
                        mState=State.REFRESHING;
                    case REFRESHING:
                        if(!isRefreshing)
                        {
                            tipTextView.setText("正在更新");
                            //由于系统绘制回滚需要一定的时间，故这里循坏等待，直到getPaddingTop==0.
                            while (getPaddingTop()!=0);
                            new RefreshingTask().execute();
                        }
                        break;
                }
                //Log.i(TAG,"抬起");
                isPullEnable=false;
                return true;
        }
        return super.onTouchEvent(ev);
    }
    private void setHeaderPadding(int toppadding)
    {
        setPadding(toppadding,getPaddingBottom());
    }
    private void setFooterPadding(int bottompadding)
    {
        setPadding(getPaddingTop(),bottompadding);
    }
    //测量该View在父容器占有的高度
    private void measureViewHeight(View child)
    {
        ViewGroup.LayoutParams params=child.getLayoutParams();
        if(params==null)
        {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWitdthSpec=ViewGroup.getChildMeasureSpec(MeasureSpec.UNSPECIFIED,0,params.width);
        int realHeight=params.height;
        int childHeightSpec;
        if(realHeight>0)
        {
            childHeightSpec=MeasureSpec.makeMeasureSpec(realHeight,MeasureSpec.EXACTLY);
        }
        else
        {
            childHeightSpec=MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        }
        //绘制子View的宽度和高度
        child.measure(childWitdthSpec, childHeightSpec);
    }
    private void setPadding(int topPadding, int bottomPadding)
    {
        setPadding(getPaddingLeft(), topPadding, getPaddingRight(), bottomPadding);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        mFirstVisibleItem = firstVisibleItem;
        mTotalItemCount = totalItemCount;
    }

    /**
     * 更新完成时
     */
    private void onCompleteRefresh()
    {
        mState = State.NONE;
        isRefreshing=false;
        setHeaderPadding(-mHeaderHeight);
        mProgressBar.setVisibility(INVISIBLE);
        ShareUtils.putRefreshData(getContext(), TimeUtils.converTime(new Date().getTime()));
    }

    /**
     * 更新监听回调接口
     */
    public interface RefreshListener
    {
        public void onRefresh();
    }

    private class RefreshingTask extends AsyncTask<Void,Void,Integer>
    {

        @Override
        protected Integer doInBackground(Void... params)
        {
            if(mRefreshListener!=null&&!isRefreshing)
            {
                isRefreshing=true;
                mRefreshListener.onRefresh();
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer)
        {
            onCompleteRefresh();
        }
    }
}
