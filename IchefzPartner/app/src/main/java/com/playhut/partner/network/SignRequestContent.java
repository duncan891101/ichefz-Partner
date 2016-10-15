package com.playhut.partner.network;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 生成request_content
 *
 * @author zhangbh
 */
public class SignRequestContent {

    public static String getRequestContent(Map<String, String> map) {
        String result = null;
        Map<String, String> resultMap = sortMapByKey(map);
        if (resultMap != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                sb.append(entry.getKey());
                sb.append(entry.getValue());
            }
            result = sb.toString();
        }
        return result;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        return str1.compareTo(str2);
                    }
                });
        sortMap.putAll(map);
        return sortMap;
    }
}

