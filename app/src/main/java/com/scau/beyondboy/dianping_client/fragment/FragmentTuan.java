package com.scau.beyondboy.dianping_client.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.scau.beyondboy.dianping_client.Consts.Consts;
import com.scau.beyondboy.dianping_client.GoodsDetailActivity;
import com.scau.beyondboy.dianping_client.R;
import com.scau.beyondboy.dianping_client.model.ProductEntity;
import com.scau.beyondboy.dianping_client.utils.HttpNetWorkUtils;
import com.scau.beyondboy.dianping_client.utils.LoadImageUtils;

import java.io.IOException;
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
    PullToRefreshListView goodsListView;
    private static List<ProductEntity> sProductEntityList;
    /**记录是否第一次加载数据*/
    private boolean mFirstLoadData=false;
    private ProductAdapter mProductAdapter;
    private  int page=1;
    private final int size=5;
    /**记录信息条目数量*/
    private int count=0;
    /**标记上下滑动距离*/
    private int mScrollY=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.tuan_index, container, false);
        ButterKnife.bind(this,view);
        goodsListView.setMode(PullToRefreshBase.Mode.BOTH);//支持上拉也支持下拉
        goodsListView.setScrollingWhileRefreshingEnabled(true);//滚动的时候不加载数据
        goodsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
        {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase)
            {
                mScrollY = goodsListView.getScrollY();
                Log.i(TAG, "移动距离：  " + mScrollY);
                if (mScrollY > 0)
                    page++;
                else
                    page = 1;
                new AsyProductDataTask().execute();
            }
        });
        new AsyProductDataTask().execute();
        //当商品列表点击的时候显示详情
        goodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.i(TAG, "点击位置： " + position);
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("goods", sProductEntityList.get(position-1));
                startActivity(intent);
            }
        });
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

    private class AsyProductDataTask extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            String prouductDataJson;
            try
            {
                prouductDataJson=HttpNetWorkUtils.geResponseStringtWithparam(Consts.HOST + Consts.PRODUCT_DATA, "page=" + page + "&size=" + size);
                if(!mFirstLoadData)
                {
                    count=Integer.valueOf(HttpNetWorkUtils.getResponseString(Consts.HOST + Consts.PRODUCT_TOTAL));
                    mFirstLoadData=true;
                }
            } catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
            return prouductDataJson;
        }
        @Override
        protected void onPostExecute(String s)
        {
            if(s!=null)
            {
                if(mScrollY<=0)
                {
                    sProductEntityList=parseProductDataJson(s);
                    mProductAdapter =new ProductAdapter();
                    goodsListView.setAdapter(mProductAdapter);
                }
                else
                {
                    Log.i(TAG,"数量统计："+count);
                    sProductEntityList.addAll(parseProductDataJson(s));
                    mProductAdapter.notifyDataSetChanged();
                }
                Log.i(TAG,"刚好更新");
            }
            goodsListView.onRefreshComplete();
            //数据加载完全部
            if(page*size==count)
                goodsListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//只能上拉刷新
            else
                goodsListView.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }
    private List<ProductEntity> parseProductDataJson(String categoryDataJson)
    {
        Gson gson=new Gson();
        return gson.fromJson(categoryDataJson,new TypeToken<List<ProductEntity>>(){}.getType());
    }
}
