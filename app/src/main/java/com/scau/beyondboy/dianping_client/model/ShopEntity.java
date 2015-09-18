package com.scau.beyondboy.dianping_client.model;

import java.io.Serializable;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015/9/10
 * Time: 9:16
 */
public class ShopEntity implements Serializable
{
    private int shopId;
    private String shopName;
    private int shopTel;
    private String shopAddress;
    private String shopArea;
    private String shopOpenTime;
    private double shopLon;
    private double shopLat;
    private String shopTrafficInfo;
    public int getShopId()
    {
        return shopId;
    }
    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }
    public String getShopName()
    {
        return shopName;
    }
    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }
    public int getShopTel()
    {
        return shopTel;
    }
    public void setShopTel(int shopTel)
    {
        this.shopTel = shopTel;
    }
    public String getShopAddress()
    {
        return shopAddress;
    }
    public void setShopAddress(String shopAddress)
    {
        this.shopAddress = shopAddress;
    }
    public String getShopArea()
    {
        return shopArea;
    }
    public void setShopArea(String shopArea)
    {
        this.shopArea = shopArea;
    }
    public String getShopOpenTime()
    {
        return shopOpenTime;
    }
    public void setShopOpenTime(String shopOpenTime)
    {
        this.shopOpenTime = shopOpenTime;
    }
    public double getShopLon()
    {
        return shopLon;
    }
    public void setShopLon(double shopLon)
    {
        this.shopLon = shopLon;
    }
    public double getShopLat()
    {
        return shopLat;
    }
    public void setShopLat(double shopLat)
    {
        this.shopLat = shopLat;
    }
    public String getShopTrafficInfo()
    {
        return shopTrafficInfo;
    }
    public void setShopTrafficInfo(String shopTrafficInfo)
    {
        this.shopTrafficInfo = shopTrafficInfo;
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShopEntity that = (ShopEntity) o;

        if (shopId != that.shopId) return false;
        if (shopTel != that.shopTel) return false;
        if (Double.compare(that.shopLon, shopLon) != 0) return false;
        if (Double.compare(that.shopLat, shopLat) != 0) return false;
        if (shopName != null ? !shopName.equals(that.shopName) : that.shopName != null) return false;
        if (shopAddress != null ? !shopAddress.equals(that.shopAddress) : that.shopAddress != null) return false;
        if (shopArea != null ? !shopArea.equals(that.shopArea) : that.shopArea != null) return false;
        if (shopOpenTime != null ? !shopOpenTime.equals(that.shopOpenTime) : that.shopOpenTime != null) return false;
        if (shopTrafficInfo != null ? !shopTrafficInfo.equals(that.shopTrafficInfo) : that.shopTrafficInfo != null)
            return false;

        return true;
    }
    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = shopId;
        result = 31 * result + (shopName != null ? shopName.hashCode() : 0);
        result = 31 * result + shopTel;
        result = 31 * result + (shopAddress != null ? shopAddress.hashCode() : 0);
        result = 31 * result + (shopArea != null ? shopArea.hashCode() : 0);
        result = 31 * result + (shopOpenTime != null ? shopOpenTime.hashCode() : 0);
        temp = Double.doubleToLongBits(shopLon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(shopLat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (shopTrafficInfo != null ? shopTrafficInfo.hashCode() : 0);
        return result;
    }
}
