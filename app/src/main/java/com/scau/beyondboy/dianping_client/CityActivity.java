package com.scau.beyondboy.dianping_client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scau.beyondboy.dianping_client.Consts.Consts;
import com.scau.beyondboy.dianping_client.model.CityEntity;
import com.scau.beyondboy.dianping_client.utils.HttpNetWorkUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-06
 * Time: 19:33
 * 显示城市列表数据
 */
public class CityActivity extends AppCompatActivity
{
    private static final String TAG = CityActivity.class.getName();
    private CityDataCallback mCityDataCallback=new CityDataCallback();
    private static Handler mHandler;
    @Bind(R.id.city_list)
    ListView mListData;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_city_list);
        ButterKnife.bind(this);
        mHandler=new MyHandler(this,mListData);
        View headView= LayoutInflater.from(this).inflate(R.layout.home_city_search,null);
        mListData.addHeaderView(headView);
        //拉去网络数据后，在ListView显示
        try
        {
            HttpNetWorkUtils.get(Consts.HOST+Consts.CITY_DATA,mCityDataCallback);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @OnItemClick(R.id.city_list)
    public void onItemClick(View view)
    {
        Intent intent=new Intent();
        TextView cityNameText=(TextView)view.findViewById(R.id.city_list_item_name);
        intent.putExtra("cityName",cityNameText.getText().toString());
        //回传数据
        setResult(RESULT_OK, intent);
    }
    @OnClick({R.id.index_city_back,R.id.index_city_flushcity})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.index_city_back:
                finish();
                break;
            case R.id.index_city_flushcity:
                try
                {
                    HttpNetWorkUtils.get(Consts.HOST+Consts.CITY_DATA,mCityDataCallback);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
    }
    private class CityAdapter extends ArrayAdapter<CityEntity>
    {
        //用来第一次保存首字母的索引以及对应的城市名
        private Map<String,String> firstList=new LinkedHashMap<>();
        public CityAdapter(List<CityEntity> cityEntities)
        {
            super(CityActivity.this,0, cityEntities);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Holder holder;
            if(convertView==null)
            {
                convertView= getLayoutInflater().inflate(R.layout.home_city_list_item,parent,false);
                holder= new Holder(convertView);
                convertView.setTag(holder);
            }
            else
            {
                holder=(Holder)convertView.getTag();
            }
            CityEntity cityEntity=getItem(position);
            //如果字母对应的城市名和cityEntity对象一样，则显示字母，否则不显示
            if(firstList.containsKey(cityEntity.getCitySortkey()))
            {
                if(firstList.get(cityEntity.getCitySortkey()).equals(cityEntity.getCityName()))
                {
                    holder.keySort.setVisibility(View.VISIBLE);
                    holder.keySort.setText(cityEntity.getCitySortkey());
                }
                else
                {
                    holder.keySort.setVisibility(View.GONE);
                }
            }
            //字母第一次显示时候会进入这里
            else
            {
                Log.i(TAG,"这里2");
                firstList.put(cityEntity.getCitySortkey(),cityEntity.getCityName());
                holder.keySort.setText(cityEntity.getCitySortkey());
            }
            holder.cityName.setText(cityEntity.getCityName());
            return convertView;
        }
    }
    class Holder
    {
        Holder(View convertView)
        {
            ButterKnife.bind(this,convertView);
        }
        @Bind(R.id.city_list_item_sort)
        public TextView keySort;
        @Bind(R.id.city_list_item_name)
        public TextView cityName;
    }

    /**
     * 网络拉取回调接口
     */
    private class CityDataCallback implements Callback
    {

        @Override
        public void onFailure(Request request, IOException e)
        {
            Log.e(TAG,"拉取数据失败",e);
        }

        @Override
        public void onResponse(Response response) throws IOException
        {
            if(response.isSuccessful())
            {
                CityAdapter cityAdapter=new CityAdapter(parseCityDataJson(response.body().string()));
                mHandler.obtainMessage(0x123,cityAdapter).sendToTarget();
            }
        }
        private List<CityEntity> parseCityDataJson(String cityDataJson)
        {
            Gson gson=new Gson();
            return gson.fromJson(cityDataJson,new TypeToken<List<CityEntity>>(){}.getType());
        }
    }
    private static class MyHandler extends Handler
    {
        private final WeakReference<CityActivity> mActivityWeakReference;
        private ListView mListData;

        public MyHandler(CityActivity activity,ListView listData)
        {
            mActivityWeakReference=new WeakReference<>(activity);
            this.mListData=listData;
        }
        @Override
        public void handleMessage(Message msg)
        {
            CityActivity activity=mActivityWeakReference.get();
            if(activity!=null)
                if(msg.what==0x123)
                {
                    Log.i(TAG,"结果");
                    mListData.setAdapter((CityAdapter) msg.obj);
                }
        }
    }
}
