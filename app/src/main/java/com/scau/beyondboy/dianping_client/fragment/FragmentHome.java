package com.scau.beyondboy.dianping_client.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scau.beyondboy.dianping_client.CityActivity;
import com.scau.beyondboy.dianping_client.Consts.Consts;
import com.scau.beyondboy.dianping_client.R;
import com.scau.beyondboy.dianping_client.utils.ShareUtils;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-08-31
 * Time: 10:31
 */
public class FragmentHome extends Fragment implements LocationListener
{
    private static final String TAG = FragmentHome.class.getName();
    @Bind(R.id.index_top_city)
    TextView mTopCity;
    private LocationManager mLocationManager;
    private String cityName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_index, container, false);
        ButterKnife.bind(this, view);
        mTopCity.setText(ShareUtils.getCityName(getActivity()));
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        checkGPSIsOpen();
    }

    private void checkGPSIsOpen()
    {
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isOpenGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //开启GPS
        if (!isOpenGPS)
        {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //将目标压入新的栈
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 0);
        }
        //开始定位
        startLocation();
    }
    @OnClick(R.id.index_top_city)
    public void onClick()
    {
        startActivityForResult(new Intent(getActivity(), CityActivity.class), Consts.REQUESTCITYCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==Consts.REQUESTCITYCODE&&resultCode== AppCompatActivity.RESULT_OK)
        {
            mTopCity.setText(data.getStringExtra("cityName"));
        }
    }

    /**
     * 定位
     */
    private void startLocation()
    {
        if (getActivity().getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", getActivity().getPackageName()) == PackageManager.PERMISSION_GRANTED )
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        updateWithNewLocation(location);
        mTopCity.setText(cityName);
    }

    /**
     * 获取城市名
     * @param location 定位类
     */
    private void updateWithNewLocation(Location location)
    {
        double lat=0.0,lng=0.0;
        Log.i(TAG,"哈哈");
        if(location!=null)
        {
            //经度
            lat=location.getLatitude();
            //纬度
            lng=location.getLongitude();
            Log.i(TAG,"经度:  "+lat+"    纬度： "+lng);
        }
        else
        {
            cityName="否则无法获取城市信息";
        }
        try
        {
            List<Address> addresses;
            Geocoder geocoder=new Geocoder(getActivity());
            //获取地址
            addresses=geocoder.getFromLocation(lat,lng,2);
            if(addresses!=null&&addresses.size()>0)
                for(int i=0;i<addresses.size();i++)
                {
                    Address address=addresses.get(i);
                    //获取城市名
                    cityName=address.getLocality();
                    Log.i(TAG,"城市名： "+cityName );
                }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.i(TAG,"结束");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onStop()
    {
        super.onStop();
        //删除监听
        if(getActivity().getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION", getActivity().getPackageName()) == PackageManager.PERMISSION_GRANTED )
        {
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ShareUtils.putCityName(getActivity(),cityName);
    }
}
