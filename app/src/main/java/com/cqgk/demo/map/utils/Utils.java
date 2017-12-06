package com.cqgk.demo.map.utils;

import java.util.Collection;

/**
 * Created by Administrator on 2017/12/2/0002.
 */

public class Utils {

    public static boolean isCollectionEmpty(Collection collections){
        if(null == collections || collections.isEmpty()){
            return true;
        }

        return false;
    }

    public static boolean isCollectionNotEmpty(Collection collections){
        if(null != collections && !collections.isEmpty()){
            return true;
        }

        return false;
    }

    public static boolean isStringEmpty(String src){
        return (null == src || src.isEmpty());
    }
}
