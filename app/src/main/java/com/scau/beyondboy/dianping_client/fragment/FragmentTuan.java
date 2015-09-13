package com.scau.beyondboy.dianping_client.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
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
import com.scau.beyondboy.dianping_client.R;
import com.scau.beyondboy.dianping_client.model.ProductEntity;
import com.scau.beyondboy.dianping_client.utils.HttpNetWorkUtils;
import com.scau.beyondboy.dianping_client.utils.LoadImageUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-08-31
 * Time: 10:31
 */
public class FragmentTuan extends Fragment
{
    private static final String TAG = FragmentTuan.class.getName();
    @Bind(R.id.index_listGoods)
    ListView goodsListView;
    private static List<ProductEntity> sProductEntityList;
    private final int page=1,size=5;
    private MyHandler mHandler;
    private ProductDataCallback mProductDataCallback=new ProductDataCallback();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.tuan_index, container, false);
        ButterKnife.bind(this,view);
        mHandler=new MyHandler(this,goodsListView);
        try
        {
            HttpNetWorkUtils.AsygetWithparam(Consts.HOST + Consts.PRODUCT_DATA, "page=" + page + "&size=" + size, mProductDataCallback);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return view;
    }
    private class ProductAdapter extends ArrayAdapter<ProductEntity>
    {
        public ProductAdapter()
        {
            super(getActivity(), 0, sProductEntityList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            Holder holder;
            if(convertView==null)
            {
                convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.tuan_goods_list_item,parent,false);
                holder=new Holder();
                ButterKnife.bind(holder,convertView);
                convertView.setTag(holder);
            }
            else
            {
                holder=(Holder)convertView.getTag();
            }
            ProductEntity productEntity= sProductEntityList.get(position);
            String imageUrl=productEntity.getProductImage();
            LoadImageUtils.getInstance().loadImage(holder.image, productEntity.getProductImage(), parent.getContext());
            String value="￥"+productEntity.getProductValue();
            //添加中划线
            SpannableString spannable = new SpannableString(value);
            spannable.setSpan(new StrikethroughSpan(), 0, value.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            holder.value.setText(spannable);
            holder.count.setText(productEntity.getProductBought() + "份");
            holder.price.setText("￥" + productEntity.getProductPrice());
            holder.title.setText(productEntity.getProductSortTitle());
            holder.titleContent.setText(productEntity.getProductTitle());
            return convertView;
        }
    }
    /**定义Handler对象防止内存泄露*/
    private  class MyHandler extends Handler
    {
        private final WeakReference<FragmentTuan> mFragmentWeakReference;
        private ListView goodsListView;
        public MyHandler(FragmentTuan fragmentTuan,ListView goodsListView)
        {
            mFragmentWeakReference =new WeakReference<>(fragmentTuan);
            this.goodsListView=goodsListView;
        }
        @Override
        public void handleMessage(Message msg)
        {
            FragmentTuan fragmentTuan = mFragmentWeakReference.get();
            if(fragmentTuan!=null)
                if(msg.what==0x123)
                {
                    sProductEntityList =(List<ProductEntity>)msg.obj;
                    goodsListView.setAdapter(new ProductAdapter());
                }
        }
    }
    class Holder
    {
        @Bind(R.id.index_gl_item_image)
        public ImageView image;
        @Bind(R.id.index_gl_item_title)
        public TextView title;
        @Bind(R.id.index_gl_item_titlecontent)
        public TextView titleContent;
        @Bind(R.id.index_gl_item_price)
        public TextView price;
        @Bind(R.id.index_gl_item_value)
        public TextView value;
        @Bind(R.id.index_gl_item_count)
        public TextView count;
    }
    /**
     * 网络拉取回调接口
     */
    private class ProductDataCallback implements Callback
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
                mHandler.obtainMessage(0x123, parseProductDataJson(response.body().string())).sendToTarget();
            }
        }
        private List<ProductEntity> parseProductDataJson(String categoryDataJson)
        {
            Gson gson=new Gson();
            sProductEntityList=gson.fromJson(categoryDataJson,new TypeToken<List<ProductEntity>>(){}.getType());
            return sProductEntityList;
        }
    }
}
