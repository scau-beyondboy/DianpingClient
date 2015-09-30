package com.scau.beyondboy.dianping_client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.beyondboy.dianping_client.MyloginActivity;
import com.scau.beyondboy.dianping_client.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-08-31
 * Time: 10:31
 * 显示设置信息
 */
public class FragmentMy extends Fragment
{
    public static final int REQUESTLOGINCODE = 1;
    @Bind(R.id.my_index_login_text)
    TextView loginText;
    @Bind(R.id.my_index_login_image)
    ImageView loginImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.my_index,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.my_index_login_image,R.id.my_index_login_text})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.my_index_login_image:
            case R.id.my_index_login_text:
                login();//登录
                break;
            default:
                break;
        }
    }
    private void login()
    {
        Intent intent = new Intent(getActivity(),MyloginActivity.class);
        startActivityForResult(intent,REQUESTLOGINCODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUESTLOGINCODE&&resultCode==REQUESTLOGINCODE)
        {
            loginText.setText(data.getStringExtra("login_name"));
            loginImage.setImageResource(R.drawable.profile_default);
        }
    }
}
