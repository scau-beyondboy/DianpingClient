package com.scau.beyondboy.dianping_client;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-30
 * Time: 20:26
 * 注册界面显示
 */
public class MyRegisterActivity extends AppCompatActivity
{
    @Bind(R.id.register_get_check_pass)
    Button checkPassButton;
    @Bind(R.id.register_back)
    ImageView backImageView;
    private CountTimer countTimer;
    private EventHandler smssdkEventHandler;
    @Bind(R.id.register_phone)
    EditText phone;
    @Bind(R.id.register_check_upass)
    EditText phoneRandom;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_register_act);
        ButterKnife.bind(this);
        countTimer  = new CountTimer(60000, 1000);
    }
    @OnClick({R.id.register_get_check_pass,R.id.register_back,R.id.register_btn})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.register_get_check_pass://点击了获取验证码
                //开启倒计时
                countTimer.start();
                sendSMSRandom();
                break;
            case R.id.register_back://点击了返回按钮
                finish();
                break;
            case R.id.register_btn:// 点击了提交按钮
                // 验证输入的验证码
                SMSSDK.submitVerificationCode("86", phone.getText().toString(),phoneRandom.getText().toString());
                break;
            default:
                break;
        }
    }

    private void sendSMSRandom()
    {
        SMSSDK.initSDK(this,"ad9033fcaa33","01d9581c609b59f6b05972dd41b3c9ff");
        smssdkEventHandler =new EventHandler()
        {
            @Override
            public void afterEvent(int event, int result, Object data)
            {
                if (result == SMSSDK.RESULT_COMPLETE)
                {
                    // 回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                    {
                        // 提交验证码成功
                        System.out.println("验证码校验成功");
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE)
                    {
                        // 获取验证码成功
                        System.out.println("验证码发送成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES)
                    {
                        // 返回支持发送验证码的国家列表
                    }
                } else
                {
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(smssdkEventHandler); // 注册短信回调
        String phoneName = phone.getText().toString();
        SMSSDK.getVerificationCode("86", phoneName.toString());
    }

    //每隔一分钟可点击一次验证码
    public class CountTimer extends CountDownTimer
    {
        /**
         * @param millisInFuture 时间间隔是多长的时间
         * @param countDownInterval 回调onTick方法，没隔多久执行一次
         */
        public CountTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }
        //间隔时间结束的时候调用的方法
        @Override
        public void onFinish()
        {
            //更新页面的组件
            checkPassButton.setText(R.string.register_get_check_num);
            checkPassButton.setBackgroundResource(R.drawable.my_register_get_check_pass);
            checkPassButton.setClickable(true);
        }
        //间隔时间内执行的操作
        @Override
        public void onTick(long millisUntilFinished)
        {
            //更新页面的组件
            checkPassButton.setText(millisUntilFinished/1000+"秒后发送");
            checkPassButton.setBackgroundResource(R.drawable.btn_light_press);
            checkPassButton.setClickable(false);
        }

    }
}
