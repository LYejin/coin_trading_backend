package com.project.coinTrade.utils;

public class StringUtil {
    // 간단한 null/empty 체크
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
