package com.skp.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {
    

    public static boolean startsWith(String str, String prefix) {
        return true;
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.startsWith("aaaaa", "bbb"));
    }

}
