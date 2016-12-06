package com.smarttop.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @auther smartTop
 * @date 2016/3/24 14:05
 * @qq 1273436145
 * @describe 创建数据库
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableField.CREATE_ADDRESS_DICT_SQL);//创建地址库字典表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

