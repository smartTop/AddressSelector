package com.smarttop.addressselector.db.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smarttop.addressselector.App;
import com.smarttop.addressselector.bean.AdressBean;
import com.smarttop.addressselector.bean.City;
import com.smarttop.addressselector.bean.County;
import com.smarttop.addressselector.bean.Province;
import com.smarttop.addressselector.bean.Street;
import com.smarttop.addressselector.db.TableField;

import java.util.ArrayList;
import java.util.List;

import static com.smarttop.addressselector.db.TableField.ADDRESS_DICT_FIELD_ID;
import static com.smarttop.addressselector.db.TableField.ADDRESS_DICT_FIELD_PARENTID;

/**
 * Created by huanghaojie on 2016/10/19.
 * 对地址库的增删改查
 */

public class AddressDictManager {
    private static final String TAG = "AddressDictManager";
    private SQLiteDatabase db;
    public AddressDictManager(Context context){

        db = App.getInstance().getAddressDB();
    }

    /**
     * 增加一个地址
     * @param adress
     */
    public void inserddress(AdressBean.ChangeRecordsBean adress){
        if(adress !=null){
            db.beginTransaction();//手动设置开启事务
            try{
                ContentValues values = new ContentValues();
                values.put(TableField.ADDRESS_DICT_FIELD_LEVEL,adress.level);
                values.put(TableField.ADDRESS_DICT_FIELD_CODE,adress.code);
                values.put(TableField.ADDRESS_DICT_FIELD_NAME,adress.name);
                values.put(TableField.ADDRESS_DICT_FIELD_CREATETIME,adress.createTime);
                values.put(ADDRESS_DICT_FIELD_PARENTID,adress.parentId);
                values.put(TableField.ADDRESS_DICT_FIELD_SORT,adress.sort);
                values.put(TableField.ADDRESS_DICT_FIELD_FLAG,adress.flag);
                values.put(TableField.ADDRESS_DICT_FIELD_SYNCTIME,adress.syncTime);
                db.insert(TableField.TABLE_ADDRESS_DICT,null,values);
                db.setTransactionSuccessful(); //设置事务处理成功
            }catch (Exception e){
            }finally {
                db.endTransaction(); //事务终止
            }
        }
    }
    /**
     * 增加地址集合
     * @param list
     */
    public void insertAddress(List<AdressBean.ChangeRecordsBean> list){
        if(list !=null){
            db.beginTransaction();//手动设置开启事务
            try{
                for (AdressBean.ChangeRecordsBean adress:list) {
                    ContentValues values = new ContentValues();
                    values.put(TableField.ADDRESS_DICT_FIELD_LEVEL,adress.level);
                    values.put(TableField.ADDRESS_DICT_FIELD_CODE,adress.code);
                    values.put(TableField.ADDRESS_DICT_FIELD_NAME,adress.name);
                    values.put(TableField.ADDRESS_DICT_FIELD_CREATETIME,adress.createTime);
                    values.put(ADDRESS_DICT_FIELD_PARENTID,adress.parentId);
                    values.put(TableField.ADDRESS_DICT_FIELD_SORT,adress.sort);
                    values.put(TableField.ADDRESS_DICT_FIELD_FLAG,adress.flag);
                    values.put(TableField.ADDRESS_DICT_FIELD_SYNCTIME,adress.syncTime);
                    db.insert(TableField.TABLE_ADDRESS_DICT,null,values);
                }
                db.setTransactionSuccessful(); //设置事务处理成功
            }catch (Exception e){
            }finally {
                db.endTransaction(); //事务终止
            }
        }
    }
    //更新地址
    public void updateAddressInfo(AdressBean.ChangeRecordsBean adress){
        if(adress !=null){
            db.beginTransaction();//手动设置开启事务
            try{
                ContentValues values = new ContentValues();
                values.put(TableField.ADDRESS_DICT_FIELD_LEVEL,adress.level);
                values.put(TableField.ADDRESS_DICT_FIELD_CODE,adress.code);
                values.put(TableField.ADDRESS_DICT_FIELD_NAME,adress.name);
                values.put(TableField.ADDRESS_DICT_FIELD_CREATETIME,adress.createTime);
                values.put(ADDRESS_DICT_FIELD_PARENTID,adress.parentId);
                values.put(TableField.ADDRESS_DICT_FIELD_SORT,adress.sort);
                values.put(TableField.ADDRESS_DICT_FIELD_FLAG,adress.flag);
                values.put(TableField.ADDRESS_DICT_FIELD_SYNCTIME,adress.syncTime);
                String[] args = {String.valueOf(adress.id)};
                db.update(TableField.TABLE_ADDRESS_DICT,values,TableField.FIELD_ID+ "=?",args);
                db.setTransactionSuccessful(); //设置事务处理成功
            }catch (Exception e){
            }finally {
                db.endTransaction(); //事务终止
            }
        }
    }

    /**
     * 查找 地址 数据
     * @return
     */
    public List<AdressBean.ChangeRecordsBean> getAddressList(){
        List<AdressBean.ChangeRecordsBean> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+ TableField.TABLE_ADDRESS_DICT+" order by sort asc", null);
        while (cursor.moveToNext()){
            AdressBean.ChangeRecordsBean adressInfo =  new AdressBean.ChangeRecordsBean();
            adressInfo.id = cursor.getInt(cursor.getColumnIndex(TableField.FIELD_ID));
            adressInfo.parentId = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_PARENTID));
            adressInfo.code = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_CODE));
            adressInfo.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            adressInfo.level = cursor.getInt(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_LEVEL));
            adressInfo.createTime = cursor.getLong(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_CREATETIME));
            adressInfo.sort = cursor.getInt(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_SORT));
            adressInfo.flag = cursor.getInt(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_FLAG));
            adressInfo.syncTime = cursor.getLong(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_SYNCTIME));
            list.add(adressInfo);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取省份列表
     * @return
     */
    public List<Province> getProvinceList(){
        List<Province> provinceList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT+" where "+ ADDRESS_DICT_FIELD_PARENTID+"=?", new String[]{String.valueOf(0)});
        while (cursor.moveToNext()){
            Province province =  new Province();
            province.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            province.code = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_CODE));
            province.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            provinceList.add(province);
        }
        cursor.close();

        return provinceList;
    }
    /**
     * 获取省份对应的城市列表
     * @return
     */
    public List<City> getCityList(int  provinceId){
        List<City> cityList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT+" where "+ ADDRESS_DICT_FIELD_PARENTID+"=?", new String[]{String.valueOf(provinceId)});
        while (cursor.moveToNext()){
            City city =  new City();
            city.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            city.code = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_CODE));
            city.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            cityList.add(city);
        }
        cursor.close();
        return cityList;
    }
    /**
     * 获取城市对应的区，乡镇列表
     * @return
     */
    public List<County> getCountyList(int cityId){
        List<County> countyList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT+" where "+ ADDRESS_DICT_FIELD_PARENTID+"=?", new String[]{String.valueOf(cityId)});
        while (cursor.moveToNext()){
            County county = new County();
            county.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            county.code = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_CODE));
            county.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            countyList.add(county);
        }
        cursor.close();
        return countyList;
    }
    /**
     * 获取区，乡镇对应的街道列表
     * @return
     */
    public List<Street> getStreetList(int countyId){
        List<Street> streetList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TableField.TABLE_ADDRESS_DICT+" where "+ ADDRESS_DICT_FIELD_PARENTID+"=?", new String[]{String.valueOf(countyId)});
        while (cursor.moveToNext()){
            Street street = new Street();
            street.id = cursor.getInt(cursor.getColumnIndex(ADDRESS_DICT_FIELD_ID));
            street.code = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_CODE));
            street.name = cursor.getString(cursor.getColumnIndex(TableField.ADDRESS_DICT_FIELD_NAME));
            streetList.add(street);
        }
        cursor.close();
        return streetList;
    }

    /**
     * 查找消息临时列表中是否存在这一条记录
     * @param bannerInfo banner数据
     * @return
     */
    public int isExist(AdressBean.ChangeRecordsBean bannerInfo){
        int count = 0;
        Cursor cursor = db.rawQuery("select count(*) from " + TableField.TABLE_ADDRESS_DICT+" where "+TableField.FIELD_ID+"=?", new String[]{String.valueOf(bannerInfo.id)});
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    //获取最大的时间，用户调取用户增量信息
    public long getMaxSyncTime(){
        long time = 0L;
        Cursor cursor = db.rawQuery("select MAX(syncTime) from " + TableField.TABLE_ADDRESS_DICT, null);
        if (cursor.moveToFirst()) {
            time = cursor.getLong(0);
        }
        cursor.close();
        return time;
    }
}
