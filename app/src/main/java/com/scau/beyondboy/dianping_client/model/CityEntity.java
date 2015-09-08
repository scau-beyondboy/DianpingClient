package com.scau.beyondboy.dianping_client.model;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015/9/4
 * Time: 17:49
 */
public class CityEntity
{
    private int id;
    private String cityName;
    private String citySortkey;
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getCityName()
    {
        return cityName;
    }
    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }
    public String getCitySortkey()
    {
        return citySortkey;
    }
    public void setCitySortkey(String citySortkey)
    {
        this.citySortkey = citySortkey;
    }
    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityEntity that = (CityEntity) o;
        if (id != that.id) return false;
        if (cityName != null ? !cityName.equals(that.cityName) : that.cityName != null) return false;
        if (citySortkey != null ? !citySortkey.equals(that.citySortkey) : that.citySortkey != null) return false;

        return true;
    }
    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (citySortkey != null ? citySortkey.hashCode() : 0);
        return result;
    }
}
