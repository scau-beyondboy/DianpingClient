package com.scau.beyondboy.dianping_client.model;

import java.io.Serializable;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015/9/9
 * Time: 11:12
 */
public class ProductEntity implements Serializable
{
    private int productId;
    private int categoryId;
    private Integer shopId;
    private Integer cityId;
    private String productTitle;
    private String productSortTitle;
    private String productImage;
    private String productStartTime;
    private String productValue;
    private String productPrice;
    private String productRibat;
    private String productBought;
    private int productMinquota;
    private int productMaxquota;
    private String productPost;
    private String productSoldout;
    private String productTip;
    private long productEndTime;
    private String productDetail;
    private ShopEntity shop;
    public int getProductId()
    {
        return productId;
    }
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    public int getCategoryId()
    {
        return categoryId;
    }
    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductEntity that = (ProductEntity) o;

        if (productId != that.productId) return false;
        if (categoryId != that.categoryId) return false;

        return true;
    }
    @Override
    public int hashCode()
    {
        int result = productId;
        result = 31 * result + categoryId;
        return result;
    }
    public Integer getShopId()
    {
        return shopId;
    }
    public void setShopId(Integer shopId)
    {
        this.shopId = shopId;
    }
    public Integer getCityId()
    {
        return cityId;
    }
    public void setCityId(Integer cityId)
    {
        this.cityId = cityId;
    }
    public String getProductTitle()
    {
        return productTitle;
    }
    public void setProductTitle(String productTitle)
    {
        this.productTitle = productTitle;
    }
    public String getProductSortTitle()
    {
        return productSortTitle;
    }
    public void setProductSortTitle(String productSortTitle)
    {
        this.productSortTitle = productSortTitle;
    }
    public String getProductImage()
    {
        return productImage;
    }
    public void setProductImage(String productImage)
    {
        this.productImage = productImage;
    }
    public String getProductStartTime()
    {
        return productStartTime;
    }
    public void setProductStartTime(String productStartTime)
    {
        this.productStartTime = productStartTime;
    }
    public String getProductValue()
    {
        return productValue;
    }
    public void setProductValue(String productValue)
    {
        this.productValue = productValue;
    }
    public String getProductPrice()
    {
        return productPrice;
    }
    public void setProductPrice(String productPrice)
    {
        this.productPrice = productPrice;
    }
    public String getProductRibat()
    {
        return productRibat;
    }
    public void setProductRibat(String productRibat)
    {
        this.productRibat = productRibat;
    }
    public String getProductBought()
    {
        return productBought;
    }
    public void setProductBought(String productBought)
    {
        this.productBought = productBought;
    }
    public int getProductMinquota()
    {
        return productMinquota;
    }
    public void setProductMinquota(int productMinquota)
    {
        this.productMinquota = productMinquota;
    }
    public int getProductMaxquota()
    {
        return productMaxquota;
    }
    public void setProductMaxquota(int productMaxquota)
    {
        this.productMaxquota = productMaxquota;
    }
    public String getProductPost()
    {
        return productPost;
    }
    public void setProductPost(String productPost)
    {
        this.productPost = productPost;
    }
    public String getProductSoldout()
    {
        return productSoldout;
    }
    public void setProductSoldout(String productSoldout)
    {
        this.productSoldout = productSoldout;
    }
    public String getProductTip()
    {
        return productTip;
    }
    public void setProductTip(String productTip)
    {
        this.productTip = productTip;
    }
    public long getProductEndTime()
    {
        return productEndTime;
    }
    public void setProductEndTime(long productEndTime)
    {
        this.productEndTime = productEndTime;
    }
    public String getProductDetail()
    {
        return productDetail;
    }
    public void setProductDetail(String productDetail)
    {
        this.productDetail = productDetail;
    }
    public ShopEntity getShop()
    {
        return shop;
    }
    public void setShop(ShopEntity shop)
    {
        this.shop = shop;
    }
}
