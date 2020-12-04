package com.example.addresspickerlib.utils;

import java.util.List;

public class ListUtil {
    private ListUtil() {

    }

    private static ListUtil instance;

    public static ListUtil getInstance() {
        if (instance == null) {
            synchronized (ListUtil.class) {
                if (instance == null) {
                    instance = new ListUtil();
                }
            }
        }
        return instance;
    }

    public <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }
}
