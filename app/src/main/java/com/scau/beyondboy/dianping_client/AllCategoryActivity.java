package com.scau.beyondboy.dianping_client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scau.beyondboy.dianping_client.Consts.Consts;
import com.scau.beyondboy.dianping_client.Consts.ResourcesConsts;
import com.scau.beyondboy.dianping_client.model.Category;
import com.scau.beyondboy.dianping_client.utils.HttpNetWorkUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-09
 * Time: 00:07
 */
public class AllCategoryActivity extends AppCompatActivity
{
    private static final String TAG = AllCategoryActivity.class.getName();
    private static List<Category> mCategoryData;
    private CategoryDataCallback mCategoryDataCallback=new CategoryDataCallback();
    private static MyHandler mHandler;
    /**保存categoryData的索引信息*/
    private long mCategoryIndex[]=new long[ResourcesConsts.allCategray.length+5];
    @Bind(R.id.home_nav_all_categray)
    ListView mAllCategoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_index_nav_all);
        ButterKnife.bind(this);
        mHandler=new MyHandler(this,mAllCategoryListView,new AllCategoryListAdapter());
        //mAllCategoryListView.setAdapter(new AllCategoryListAdapter());
        //拉去网络数据后，在ListView显示
        try
        {
            HttpNetWorkUtils.get(Consts.HOST + Consts.CATEGORY_DATA,mCategoryDataCallback);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private class AllCategoryListAdapter extends ArrayAdapter<String>
    {
        //categoryData索引指针
        private int index=0;
        public AllCategoryListAdapter()
        {
            super(AllCategoryActivity.this,0, ResourcesConsts.allCategray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Holder holder;
            if(convertView==null)
            {
                convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_index_nav_all_item,parent,false);
                holder=new Holder(convertView);
                convertView.setTag(holder);
            }
            else
            {
                holder=(Holder)convertView.getTag();
            }
           // 赋值
			holder.textDesc.setText(ResourcesConsts.allCategray[position]);
            holder.imageView.setImageResource(ResourcesConsts.allCategrayImages[position]);
            Category category=mCategoryData.get(index);
            holder.textNumber.setText(mCategoryIndex[position]+"");
            return convertView;
        }
    }
    @OnClick(R.id.home_nav_all_back)
    public void onClick()
    {
        finish();
    }
    class Holder
    {
        @Bind(R.id.home_nav_all_item_number)
         TextView textNumber;
        @Bind(R.id.home_nav_all_item_desc)
         TextView textDesc;
        @Bind(R.id.home_nav_all_item_image)
         ImageView imageView;
        public Holder(View view)
        {
            ButterKnife.bind(this,view);
        }
    }
    /**定义静态Handler对象防止内存泄露*/
    private static class MyHandler extends Handler
    {
        private final WeakReference<AllCategoryActivity> mActivityWeakReference;
        private ListView allCategoryListView;
        private AllCategoryListAdapter allCategoryListAdapter;
        public MyHandler(AllCategoryActivity activity,ListView allCategoryListView,AllCategoryListAdapter allCategoryListAdapter)
        {
            mActivityWeakReference=new WeakReference<>(activity);
            this.allCategoryListView=allCategoryListView;
            this.allCategoryListAdapter=allCategoryListAdapter;
        }
        @Override
        public void handleMessage(Message msg)
        {
            AllCategoryActivity activity=mActivityWeakReference.get();
            if(activity!=null)
                if(msg.what==0x123)
                {
                    mCategoryData=(List<Category>)msg.obj;
                    allCategoryListView.setAdapter(allCategoryListAdapter);
                }
        }
    }
    /**
     * 网络拉取回调接口
     */
    private class CategoryDataCallback implements Callback
    {

        @Override
        public void onFailure(Request request, IOException e)
        {
            Log.e(TAG, "拉取数据失败", e);
        }

        @Override
        public void onResponse(Response response) throws IOException
        {
            if(response.isSuccessful())
            {
                mHandler.obtainMessage(0x123, parseCategoryDataJson(response.body().string())).sendToTarget();
            }
        }
        private List<Category> parseCategoryDataJson(String categoryDataJson)
        {
            Gson gson=new Gson();
            mCategoryData=gson.fromJson(categoryDataJson,new TypeToken<List<Category>>(){}.getType());
            for (Category category:mCategoryData)
            {
                mCategoryIndex[category.getCategoryId()]=category.getCategoryNumber();
            }
            return mCategoryData;
        }
    }
}
