package com.smarttop.library.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smartTop on 2016/8/25.
 * banner基类
 */
public class AdressBean {


    /**
     * changeCount : 3
     * changeRecords : [{"createTime":1471603797064,"flag":1,"id":10000,"picSavedPath":"http://jifenshangcheng-test.oss-cn-beijing.aliyuncs.com/mall/image/9610c9b91fbc433499ff8ba5f4054bbb64.png","sort":0,"syncTime":1471603797064,"url":"https://www.baidu.com"},{"createTime":1471603797065,"flag":1,"id":10001,"picSavedPath":"http://jifenshangcheng-test.oss-cn-beijing.aliyuncs.com/mall/image/9610c9b91fbc433499ff8ba5f4054bbb64.png","sort":1,"syncTime":1471603797065,"url":"https://www.baidu.com"},{"createTime":1471603797066,"flag":1,"id":10002,"picSavedPath":"http://jifenshangcheng-test.oss-cn-beijing.aliyuncs.com/mall/image/9610c9b91fbc433499ff8ba5f4054bbb64.png","sort":2,"syncTime":1471603797066,"url":"https://www.baidu.com"}]
     * code : 0
     * message : 调用成功
     */

    public int changeCount;
    public String code;
    public String message;
    /**
     * createTime : 1471603797064
     * flag : 1
     * id : 10000
     * picSavedPath : http://jifenshangcheng-test.oss-cn-beijing.aliyuncs.com/mall/image/9610c9b91fbc433499ff8ba5f4054bbb64.png
     * sort : 0
     * syncTime : 1471603797064
     * url : https://www.baidu.com
     */

    public List<ChangeRecordsBean> changeRecords = new ArrayList<>();

    public static class ChangeRecordsBean {
        /*** id*/
        public int id;
        /*** 地址编号*/
        public String code;
        /*** 中文名*/
        public String name;
        /*** 父id*/
        public int parentId;
    }
}
