package com.smarttop.addressselector.db;

/**
 * @auther smartTop
 * @date 2016/8/25 14:05
 * @qq 1273436145
 * @describe 表的管理类
 */

public class TableField {
    // 表名
    public static final String TABLE_ADDRESS_DICT = "address_dict";//地址库字典表
    //字段名
    public static final String FIELD_ID = "id"; //公用的id
    //地址库字典表的字段名
    public static final String ADDRESS_DICT_FIELD_PARENTID = "parentId";//父id，自关联id主键
    public static final String ADDRESS_DICT_FIELD_ID = "id";
    public static final String ADDRESS_DICT_FIELD_LEVEL = "level";//等级：1表一级，2表二级，3表三级，4表四级
    public static final String ADDRESS_DICT_FIELD_CODE = "code";//地址编号
    public static final String ADDRESS_DICT_FIELD_NAME = "name";//中文名
    public static final String ADDRESS_DICT_FIELD_SORT = "sort";//排序值
    public static final String ADDRESS_DICT_FIELD_FLAG = "flag";//启用标志：0表停用，1表启用，默认为1
    public static final String ADDRESS_DICT_FIELD_CREATETIME = "createTime";//创建时间
    public static final String ADDRESS_DICT_FIELD_SYNCTIME = "syncTime";//和服务器同步时间，调接口时使用

    //创建地址库字典表sql语句
    public static final String CREATE_ADDRESS_DICT_SQL = "create table " + TABLE_ADDRESS_DICT + "("+ ADDRESS_DICT_FIELD_ID + " integer not null," +  ADDRESS_DICT_FIELD_PARENTID + " integer not null,"+ADDRESS_DICT_FIELD_LEVEL+" integer not null,"+ ADDRESS_DICT_FIELD_CODE + " text," + ADDRESS_DICT_FIELD_NAME + " text," + ADDRESS_DICT_FIELD_SORT + " integer not null," + ADDRESS_DICT_FIELD_FLAG + " integer not null," + ADDRESS_DICT_FIELD_CREATETIME + " integer not null," + ADDRESS_DICT_FIELD_SYNCTIME + " integer not null)";
}
