package com.hire.common.web.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeUtil {

    public static String list2Str(List<String>list) {
        if (list == null || list.size() == 0) {
            return "";
        }

        return String.join(",", list);
    };

    public static List<String> str2List(String strs) {
        if (strs == null || strs.isEmpty()) {
            return new ArrayList<String>();
        }

        String str[] = strs.split(",");
        return Arrays.asList(str);
    }
}
