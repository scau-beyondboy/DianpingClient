package com.scau.beyondboy.dianping_client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.scau.beyondboy.dianping_client.utils.ShareUtils;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-08-29
 * Time: 21:28
 * 欢迎页面
 */
public class WelcomeStartAct extends AppCompatActivity
{

    private ObjectAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setBackgroundResource(R.drawable.guide_welcome);
        addContentView(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //设置动画
        mAnimator = ObjectAnimator.ofFloat(linearLayout, "alpha", 0, 1.0f).setDuration(3000);
        mAnimator.start();
        init();
    }

    private void init()
    {
        mAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                //判断应用是否是第一次安装
                if(!ShareUtils.getWelcomeEnterFlag(WelcomeStartAct.this))
                {
                    //跳转到欢迎导航页面
                    startActivity(new Intent(WelcomeStartAct.this, WelcomeGuideAct.class));
                    ShareUtils.putWelcomeEnterFlag(WelcomeStartAct.this,true);
                }
                //跳转到主页面
                startActivity(new Intent(WelcomeStartAct.this,MainActivity.class));
                finish();
            }
        });
    }
}
