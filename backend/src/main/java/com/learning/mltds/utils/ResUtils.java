package com.learning.mltds.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResUtils {
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

    public static Map<String, Object> makeResponse(String status, String message){
        Map<String, Object> resultsMap = new HashMap<>();
        resultsMap.put("message", message);
        resultsMap.put("status", status);

        return resultsMap;
    }

    public static Map<String, Object> makeResponse(Object result){
        Map<String, Object> resultsMap = new HashMap<>();
        resultsMap.put("result", result);
        resultsMap.put("message", "");
        resultsMap.put("status", "OK");

        return resultsMap;
    }

    public static Map<String, Object> makeResponse(){
        Map<String, Object> resultsMap = new HashMap<>();
        resultsMap.put("message", "");
        resultsMap.put("status", "OK");

        return resultsMap;
    }

    public static Map<String, Object> makeResponse(Object result, String message){
        Map<String, Object> resultsMap = new HashMap<>();
        resultsMap.put("result", result);
        resultsMap.put("message", message);
        resultsMap.put("status", "OK");

        return resultsMap;
    }
}
