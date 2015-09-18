package com.scau.beyondboy.dianping_client;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.scau.beyondboy.dianping_client.model.ProductEntity;
import com.scau.beyondboy.dianping_client.model.ShopEntity;
import com.scau.beyondboy.dianping_client.utils.LoadImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-18
 * Time: 15:03
 * 显示商品详细信息
 */
public class GoodsDetailActivity extends AppCompatActivity
{
    static final String TAG = GoodsDetailActivity.class.getName();
    @Bind(R.id.goods_image)
    ImageView goods_image;
    @Bind(R.id.goods_title)
    TextView goods_title;
    @Bind(R.id.goods_desc)
    TextView goods_desc;
    @Bind(R.id.shop_title)
    TextView shop_title;
    @Bind(R.id.shop_phone)
    TextView shop_phone;
    @Bind(R.id.goods_price)
    TextView goods_price;
    @Bind(R.id.goods_old_price)
    TextView goods_old_price;
    @Bind(R.id.tv_more_details_web_view)
    WebView tv_more_details_web_view;
    @Bind(R.id.wv_gn_warm_prompt)
    WebView wv_gn_warm_prompt;
    private ProductEntity mProductEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuan_goods_detail);
        ButterKnife.bind(this);
        // TextView的文字中划线效果
        goods_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        // 让网页自适应屏幕
        WebSettings webSettings = tv_more_details_web_view.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        WebSettings webSettings1 = wv_gn_warm_prompt.getSettings();
        webSettings1.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mProductEntity = (ProductEntity) bundle.get("goods");
        }
        if (mProductEntity != null)
        {
            // 更新页面上所有的内容
            updateTitleImage();
            updateGoodsInfo();
            updateShopInfo();
            updateMoreDetails();
        }
    }
    @OnClick({R.id.shop_call,R.id.goods_detail_goback})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.shop_call:
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mProductEntity.getShop().getShopTel()));
                startActivity(intent);
                break;
            case R.id.goods_detail_goback:
                finish();
            default:
                break;
        }

    }
    // 更新商品的标题图片
    private void updateTitleImage()
    {
        LoadImageUtils.getInstance().loadImage(goods_image,mProductEntity.getProductImage(),this);
    }
    //商品的标题，显示，价钱处理
    private void updateGoodsInfo()
    {
        goods_title.setText(mProductEntity.getProductSortTitle());
        goods_desc.setText(mProductEntity.getProductTip());
        goods_price.setText("￥" + mProductEntity.getProductPrice());
        goods_old_price.setText("￥" + mProductEntity.getProductValue());
    }

    private void updateShopInfo()
    {
        ShopEntity shop = mProductEntity.getShop();
        shop_title.setText(shop.getShopName());
        shop_phone.setText(shop.getShopTel()+"");
    }
    private void updateMoreDetails()
    {
        String data[]=htmlSub(mProductEntity.getProductDetail());
        tv_more_details_web_view.loadDataWithBaseURL("", data[1], "text/html", "utf-8", "");
        wv_gn_warm_prompt.loadDataWithBaseURL("", data[0], "text/html", "utf-8", "");
    }

    private String[] htmlSub(String html)
    {
        char[] str = html.toCharArray();
        int len = str.length;
        Log.i("TAG", "长度是"+len);
        int n = 0;
        String[] data = new String[3];
        int oneIndex = 0;
        int secIndex = 1;
        int ThiIndex = 2;
        for (int i = 0; i < len; i++)
        {
            if (str[i]=='【')
            {
                n++;
                if (n ==1) oneIndex=i;
                if (n ==2) secIndex=i;
                if (n ==3) ThiIndex=i;
            }
        }
        if (oneIndex>0 && secIndex >1 && ThiIndex>2)
        {
            data[0] = html.substring(oneIndex, secIndex);
            data[1] = html.substring(secIndex, ThiIndex);
            data[2] = html.substring(ThiIndex, html.length()-6);
        }
        return data;
    }
}
