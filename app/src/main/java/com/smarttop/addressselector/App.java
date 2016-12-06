package com.smarttop.addressselector;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.smarttop.addressselector.db.AssetsDatabaseManager;

/**
 * Created by smartTop on 2016/12/6.
 */

public class App extends Application {
    private static App instance;
    private SQLiteDatabase addressDB;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 初始化，只需要调用一次
        AssetsDatabaseManager.initManager(getApplicationContext());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        addressDB = mg.getDatabase("adress.db");
    }
    public static App getInstance() {
        return instance;
    }

    /**
     * 获取地址库的DB
     *
     * @return
     */
    public SQLiteDatabase getAddressDB() {
        return addressDB;
    }

}
