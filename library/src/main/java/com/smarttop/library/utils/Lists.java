package com.smarttop.library.utils;

import java.util.List;

/**
 * Created by smartTop on 2016/10/19.
 * list的工具类
 */

public class Lists {
    public Lists() {
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }
}
