package com.scau.beyondboy.dianping_client;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scau.beyondboy.dianping_client.Consts.Consts;
import com.scau.beyondboy.dianping_client.model.ProductEntity;
import com.scau.beyondboy.dianping_client.model.ShopEntity;
import com.scau.beyondboy.dianping_client.utils.HttpNetWorkUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-21
 * Time: 17:22
 * 显示地图和地图周围商品信息
 */
public class NearbyMapActivity extends AppCompatActivity implements LocationSource,AMapLocationListener, AMap.OnMarkerClickListener,AMap.OnInfoWindowClickListener,AMap.InfoWindowAdapter
{
    private static final String TAG = NearbyMapActivity.class.getName();
    public static final int RADIUS = 1000000;
    @Bind(R.id.search_mymap)
    MapView mMapView;
    private AMap mAMap;
    private LocationManagerProxy mAMapLocManager = null;
    private OnLocationChangedListener mListener;
    private double longitude=113.5455;
    private double latitude=23.5223;
    private List<ProductEntity> mProductEntityList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_map_act);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        if(mAMap==null)
        {
            mAMap=mMapView.getMap();
            mAMap.setLocationSource(this);
            mAMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位，默认是false
           // mAMap.setOnMapLoadedListener(this);// 设置aMap加载成功事件的监听
            mAMap.setOnMarkerClickListener(this);// 设置点击marker事件的监听器
            mAMap.setInfoWindowAdapter(this);// 设置自定义的InfoWindow样式
            mAMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow的事件监听器
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        stopLocation();
        mMapView.onDestroy();
    }
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        Log.i(TAG, "定位开始");
        if (mAMapLocManager==null)
        {
            mListener = onLocationChangedListener;
            mAMapLocManager = LocationManagerProxy.getInstance(this);
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法
            //其中如果间隔时间为-1，则定位只定一次
            mAMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork, 5000, 10, this);
        }
    }

    @Override
    public void deactivate()
    {

    }

    //停止定位
    private void stopLocation()
    {
        if (mAMapLocManager != null)
        {
            mAMapLocManager.removeUpdates(this);
            mAMapLocManager.destroy();
        }
        mAMapLocManager = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation)
    {
        if (aMapLocation!=null)
        {
            longitude = aMapLocation.getLongitude();
            latitude = aMapLocation.getLatitude();
            mListener.onLocationChanged(aMapLocation);//显示系统的小蓝点
            Log.i("TAG", "当前的经度和纬度是：" + longitude + "," + latitude);
            loadData(1, 5, String.valueOf(latitude), String.valueOf(longitude), String.valueOf(RADIUS));
            mAMapLocManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {

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

    //当显示的窗体进行点击的时候
    @Override
    public void onInfoWindowClick(Marker marker)
    {
        //获取商店的名称
        String shopName =marker.getTitle();
        //根据商铺名称找到对应的商品
        ProductEntity product = getGoodsByShopName(shopName);
        if (product!=null)
        {
            //跳转到详情页面
            Intent intent = new Intent(this, GoodsDetailActivity.class);
            intent.putExtra("goods", product);
            startActivity(intent);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker)
    {
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        return null;
    }
    //http://localhost:8080/product/nearby?page=1&size=5&lat=23.554&lon=116.3154950&raidus=1000
    // 按照定位的地址和搜索半径加载周边的数据
    private void loadData(int page,int size,String lat, String lon, String radius)
    {
        HttpNetWorkUtils.synchroGetwithParam(Consts.HOST+Consts.PRODUCT_NEARBY, String.format("page=%d&size=%d&lat=%s&lon=%s&raidus=%s", page, size, lat, lon, radius), new HttpNetWorkUtils.RequestCallBack<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                Log.i(TAG,"这里:  "+result);
                mProductEntityList=parseProductDataJson(result);
                Log.i(TAG,"多少条数据： "+mProductEntityList.size());
                // 设置地图的缩放
                /*
				* new LatLng(latitude, longitude) 以当前位置为中心 16 缩放级别
				* 地图缩放级别为4-20级
				* 缩放级别较低时，您可以看到更多地区的地图；缩放级别高时，您可以查看地区更加详细的地图。 0
				* 默认情况下，地图的方向为0度，屏幕正上方指向北方。当您逆时针旋转地图时，
				* 地图正北方向与屏幕正上方的夹角度数为地图方向
				* ，范围是从0度到360度。例如，一个90度的查询结果，在地图上的向上的方向指向正东
				* 地图倾角范围为0-45度。
				*
				* 详情参考：http://lbs.amap.com/api/android-sdk/guide/camera/
				*/
                mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 16, 0,30)));
                addMarker(mProductEntityList);
            }

            @Override
            public void onFailure(Exception arg0, String arg1)
            {
                Toast.makeText(NearbyMapActivity.this, "数据加载失败请重试",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private List<ProductEntity> parseProductDataJson(String productDataJson)
    {
        Gson gson=new Gson();
        return gson.fromJson(productDataJson,new TypeToken<List<ProductEntity>>(){}.getType());
    }
    // 将数据标记到地图上
    private void addMarker(List<ProductEntity> productEntityList)
    {
        for (ProductEntity productEntity:productEntityList)
        {
            //Log.i(TAG,"数据：  "+productEntity.getProductTitle());
            MarkerOptions markerOptions=new MarkerOptions();
            ShopEntity shop=productEntity.getShop();
            // 设置当前的markerOptions对象的经纬度
            markerOptions.position(new LatLng(shop.getShopLat(),shop.getShopLon()));
            //Log.i(TAG,"店铺的纬度和经度：  "+shop.getShopLat()+"      "+shop.getShopLon());
            // 点击每一个图标显示信息 显示内容为商铺名称以及当前的商品价钱
            markerOptions.title(shop.getShopName()).snippet("￥" +productEntity.getProductPrice());
            // 不同类型的商品设置不同的类型图标
            if (productEntity.getCategoryId()==3)
            {
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_landmark_chi));
            } else if (productEntity.getCategoryId()==5)
            {
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_landmark_movie));
            } else if (productEntity.getCategoryId()==8)
            {
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_landmark_hotel));
            } else if (productEntity.getCategoryId()==6)
            {
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_landmark_life));
            } else if (productEntity.getCategoryId()==4)
            {
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_landmark_wan));
            } else
            {
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_landmark_default));
            }
            // 在地图上显示所有的图标
            mAMap.addMarker(markerOptions).setObject(productEntity);
        }
    }
    //根据商店的名称来获取当前的商品信息
    private ProductEntity getGoodsByShopName(String shopName)
    {
        for(ProductEntity goods: mProductEntityList)
        {
            //遍历商品集合进行商铺的匹配
            if (goods.getShop().getShopName().equals(shopName))
            {
                return goods;
            }
        }
        return null;
    }
}
