package com.learning.mltds.utils;

import java.util.*;

import com.learning.mltds.entity.Task;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @version 1.0.0
 */
@Component
public class MapUtils {
    private static final Integer charType = 1;  // 驼峰转下划线模式，1表示全小写，2表示全大写

    /**
     * 实体类属性和属性值的键值对 转为 Map
     * @param object 对象
     * @return object对象的属性键值对 HashMap
     */
    public static <T> Map<String, Object> entityToMap(T object) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);

                // 驼峰转下划线
                String fieldName = field.getName();
                fieldName = camelToUnderline(fieldName, charType);

                map.put(fieldName, o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static <T> List<Map<String, Object>> entitysToMap(List<T> objects) {
        List<Map<String, Object>> res = new ArrayList<>();
        for(T object : objects) {
            res.add(entityToMap(object));
        }
        return res;
    }

    // Map转实体类
    public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
        T t = null;
        try {
            t = entity.newInstance();
            for (Field field : entity.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object object = map.get(field.getName());
                    if (object != null && field.getType().isAssignableFrom(object.getClass())) {
                        field.set(t, object);
                    }
                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static void main(String[] args) {
        Task task = new Task();
        Map res = MapUtils.entityToMap(task);
        System.out.println(res);
    }

    // 清除 Map 空值
    public static void removeEmptyMap(Map<String, Object> map){
        List<String> remove_keys = new ArrayList<>();
        for(Map.Entry<String, Object> entry: map.entrySet()){
            if(entry.getValue() == "" || entry.getValue() == null){
                remove_keys.add(entry.getKey());
            }
        }

        for(String rm_key:remove_keys){
            map.remove(rm_key);
        }
    }

    //下划线转驼峰
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        Boolean flag = false; // "_" 后转大写标志,默认字符前面没有"_"
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_') {
                flag = true;
                continue;   //标志设置为true,跳过
            } else {
                if (flag == true) {
                    //表示当前字符前面是"_" ,当前字符转大写
                    sb.append(Character.toUpperCase(param.charAt(i)));
                    flag = false;  //重置标识
                } else {
                    sb.append(param.charAt(i));
                }
            }
        }
        return sb.toString();
    }

    //下划线转驼峰
    public static void mapUnderlineToCamel(Map<String, Object> map) {

        Set<String> searchMapSet = map.keySet();
        List<String> searchMapKeyList = new ArrayList<>();
        for(String key: searchMapSet) {
            searchMapKeyList.add(key);
        }
        for(String key: searchMapKeyList) {
            String camelKey = underlineToCamel(key);
            // 如果这里是下划线那么就转成驼峰
            if (!map.containsKey(camelKey)) {
                map.put(camelKey, map.get(key));
                map.remove(key);
            }
        }

    }

    // 驼峰下划线
    public static String camelToUnderline(String param, Integer charType) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
            }
            if (charType == 2) {
                sb.append(Character.toUpperCase(c));  //统一都转大写
            } else {
                sb.append(Character.toLowerCase(c));  //统一都转小写
            }


        }
        return sb.toString();
    }

}

