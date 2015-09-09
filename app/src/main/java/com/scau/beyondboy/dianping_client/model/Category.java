package com.scau.beyondboy.dianping_client.model;
/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015/9/9
 * Time: 9:34
 * 存储首页列表的分类条目信息
 */
public class Category
{
    private int categoryId;
    private long categoryNumber;
    public Category(int categoryId, long categoryNumber)
    {
        this.categoryId = categoryId;
        this.categoryNumber = categoryNumber;
    }
        public int getCategoryId()
    {
        return categoryId;
    }
    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }
    public long getCategoryNumber()
    {
        return categoryNumber;
    }
    public void setCategoryNumber(long categoryNumber)
    {
        this.categoryNumber = categoryNumber;
    }

    @Override
    public String toString()
    {
        return categoryId+" --> "+categoryNumber;
    }
}
