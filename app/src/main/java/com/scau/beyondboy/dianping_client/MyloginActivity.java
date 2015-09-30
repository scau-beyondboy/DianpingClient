package com.scau.beyondboy.dianping_client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scau.beyondboy.dianping_client.Consts.Consts;
import com.scau.beyondboy.dianping_client.fragment.FragmentMy;
import com.scau.beyondboy.dianping_client.model.ResponseObject;
import com.scau.beyondboy.dianping_client.utils.HttpNetWorkUtils;
import com.scau.beyondboy.dianping_client.utils.ShareUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-28
 * Time: 19:00
 * 登陆界面
 */
public class MyloginActivity extends AppCompatActivity implements PlatformActionListener
{
    public static final String RANDOMS = "1234567890poiuytrewqasdfghjklmnbvcxzQWERTYUIOPASDFGHJKLZXCVBNM";
    public static final String LOGIN = "login";
    private static final String TAG = MyloginActivity.class.getName();
    @Bind(R.id.login_check_random)
    Button checkRandom;
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.login_uname)
    EditText loginName;
    @Bind(R.id.login_pass)
    EditText loginpass;
    @Bind(R.id.login_register)
    TextView register;
    @Bind(R.id.login_by_qq)
    TextView loginByQQ;
    @Bind(R.id.login_by_weixin)
    TextView loginByWeixin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_login_act);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        checkRandom.setText(getRandom(4));//初始化验证码
    }
    /**QQ的第三方登陆*/
    private void loginByQQ()
    {
        Platform qq= ShareSDK.getPlatform(this, QQ.NAME);
        qq.setPlatformActionListener(this);
        qq.SSOSetting(true);
        Log.i(TAG,"QQ登录");
        //3.判断授权是否已经验证（是否正常登录）
        if (qq.isAuthValid())
        {
            String uname = qq.getDb().getUserName();//获取三方的显示名称
            System.out.println("验证通过。。。。。。"+uname);
            //返回我的页面
            loginSuccess(uname);
        }
        else
        {
            //如果没有授权登录
            qq.showUser(null);
        }
    }
    /**微信第三方登陆*/
    private void loginByWeixin()
    {

    }
    @OnClick({R.id.login_check_random,R.id.login_btn,R.id.login_register,R.id.login_by_qq,R.id.login_by_weixin,R.id.login_back})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_check_random:
                checkRandom.setText(getRandom(4));//设置验证码
                break;
            case R.id.login_btn://点击登录
                handleLogin();
                break;
            case R.id.login_register://点击注册
                startActivity(new Intent(this, MyRegisterActivity.class));
                break;
            case R.id.login_by_qq://QQ三方登录
                loginByQQ();
                break;
            case R.id.login_by_weixin://微信三方登录
                loginByWeixin();
                break;
            case R.id.login_back:
                finish();
                break;
        }
    }

    /**
     * 处理登陆
     */
    private void handleLogin()
    {
        final String userName = loginName.getText().toString();
        final String password = loginpass.getText().toString();
        HttpNetWorkUtils.synchroGetwithParam(Consts.HOST + Consts.USER, String.format("username=%s&password=%s&flag=%s", userName, password, LOGIN), new HttpNetWorkUtils.RequestCallBack<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.i(TAG,"数据：  "+result );
                Gson gson = new Gson();
                ResponseObject responseObject=gson.fromJson(result,ResponseObject.class);
                //登录成功
                if(responseObject.getState()==1)
                {
                    ShareUtils.putUserName(MyloginActivity.this, userName);//获取三方的显示名称
                    loginSuccess(loginName.getText().toString());//出来登录信息
                }
                Toast.makeText(MyloginActivity.this, responseObject.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception arg0, String arg1)
            {
                Toast.makeText(MyloginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //登录成功的时候执行的方法
    private void loginSuccess(String username)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("login_name", username);
        setResult(FragmentMy.REQUESTLOGINCODE, intent);
        finish();
    }

    @NonNull
    private String getRandom(int number)
    {
        StringBuilder randomString=new StringBuilder();
        for (int i = 0; i < number; i++)
        {
            randomString.append(RANDOMS.charAt((int)(Math.random()*RANDOMS.length())));
        }
        return randomString.toString();
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap)
    {
        String uname = platform.getDb().getUserName();//获取第三方平台显示的名称
        System.out.println("uname====="+uname);
        //返回我的页面
        loginSuccess(uname);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable)
    {
        Toast.makeText(this, platform.getName()+"授权已失败，请重试", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"错误： "+throwable.getMessage());
    }

    @Override
    public void onCancel(Platform platform, int i)
    {
        Toast.makeText(this, platform.getName()+"授权已取消", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ShareSDK.stopSDK();//销毁ShareSDK
    }
}
