package com.learning.mltds.utils;

import com.learning.mltds.dto.DetectionResultDTO;
import com.learning.mltds.dto.ImageinfoDTO;
import com.learning.mltds.dto.ObjectinfoDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 前端解析工具
public class ReqUtils {
//    private static final DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static List<DetectionResultDTO> detectionResultsConvert(Map<String, Object> detectionResults) {
        // 返回值初始化
        List<DetectionResultDTO> detectionResultDTOS = new ArrayList<>();


        for(String imageName : detectionResults.keySet()) {
            Map<String, Object> detectionResult = (Map<String, Object>) detectionResults.get(imageName);

            // 读取 image_info 对象 map
            Map<String, Object> imageinfoMap = (Map<String, Object>)detectionResult.get("image_info");
            MapUtils.removeEmptyMap(imageinfoMap);  // 清除空值
            ImageinfoDTO imageinfoDTO = ImageinfoDTO.builder()
                    .filename((String) imageinfoMap.get("filename"))
                    .satType((String) imageinfoMap.get("sat_type"))
                    .sensorType((String) imageinfoMap.get("sensor_type"))
                    .imageWidth((Integer) imageinfoMap.get("image_width"))
                    .imHeight((Integer) imageinfoMap.get("im_height"))
                    .path((String) imageinfoMap.get("path"))
                    .isDetected((Boolean) imageinfoMap.get("is_detected"))
                    .taskId((Integer) imageinfoMap.get("task_id"))
                    .detectedTime((String) imageinfoMap.get("detect_time"))
//                    .detectedTime(LocalDateTime.parse((String) imageinfoMap.get("detect_time"), localDateTimeFormatter))
                    .build();

            // 读取 object_info map
            List<Map<String, Object>> objectInfos = (List<Map<String, Object>>) detectionResult.get("object_infos");
            List<ObjectinfoDTO> objectinfoDTOS = new ArrayList<>();
            for (Map<String, Object> objectInfo : objectInfos) {
                // 清除空值
                MapUtils.removeEmptyMap(objectInfo);
                // 下划线转驼峰
                toHump(objectInfo);
                // 拆除字典中的嵌套字典
                turnObjectInfoSepcialValue(objectInfo);

                // 保存到 list 中
                ObjectinfoDTO objectinfoDTO = ObjectinfoDTO.builder()
                        .taskId((Integer) objectInfo.get("taskId"))
                        .confidence((Double) objectInfo.get("confidence"))
                        .classname((String) objectInfo.get("classname"))
                        .typename((String) objectInfo.get("typename"))
                        .shipNumber((String) objectInfo.getOrDefault("shipNumber", null))
                        .isDeleted((Integer) objectInfo.get("isDeleted"))
                        .imageCenterX((Integer) objectInfo.get("imageCenterX"))
                        .imageCenterY((Integer) objectInfo.get("imageCenterY"))
                        .geoCenterLongitude((Double) objectInfo.get("geoCenterLongitude"))
                        .geoCenterLatitude((Double) objectInfo.get("geoCenterLatitude"))
                        .bboxP1X((Integer) objectInfo.get("bboxP1X"))
                        .bboxP1Y((Integer) objectInfo.get("bboxP1Y"))
                        .bboxP2X((Integer) objectInfo.get("bboxP2X"))
                        .bboxP2Y((Integer) objectInfo.get("bboxP2Y"))
                        .bboxP3X((Integer) objectInfo.get("bboxP3X"))
                        .bboxP3Y((Integer) objectInfo.get("bboxP3Y"))
                        .bboxP4X((Integer) objectInfo.get("bboxP4X"))
                        .bboxP4Y((Integer) objectInfo.get("bboxP4Y"))
                        .geoBboxP1X((Double) objectInfo.get("geoBboxP1X"))
                        .geoBboxP1Y((Double) objectInfo.get("geoBboxP1Y"))
                        .geoBboxP2X((Double) objectInfo.get("geoBboxP2X"))
                        .geoBboxP2Y((Double) objectInfo.get("geoBboxP2Y"))
                        .geoBboxP3X((Double) objectInfo.get("geoBboxP3X"))
                        .geoBboxP3Y((Double) objectInfo.get("geoBboxP3Y"))
                        .geoBboxP4X((Double) objectInfo.get("geoBboxP4X"))
                        .geoBboxP4Y((Double) objectInfo.get("geoBboxP4Y"))
                        .targetSlicePath((String) objectInfo.get("targetSlicePath"))
                        .fixTargetSlicePath((String) objectInfo.get("fixTargetSlicePath"))
                        .areaSlicePath((String) objectInfo.get("areaSlicePath"))
//                        .detectedTime(LocalDateTime.parse((String) imageinfoMap.get("detect_time"), localDateTimeFormatter))
                        .detectedTime((String) objectInfo.get("detectedTime"))
                        .build();
                objectinfoDTOS.add(objectinfoDTO);
            }


//            map转化为detectionResultDTO对象
            DetectionResultDTO detectionResultDTO = DetectionResultDTO.builder()
                    .imageName(imageName)
                    .imageinfoDTO(imageinfoDTO)
                    .objectinfoList(objectinfoDTOS)
                    .build();
            detectionResultDTOS.add(detectionResultDTO);
        }

        return detectionResultDTOS;
    }

    // 转换工具
    public static void turnObjectInfoSepcialValue(Map<String, Object> objectInfo) {
        if(objectInfo.containsKey("bbox")){
            List<Integer> bbox = (List<Integer>) objectInfo.get("bbox");
            objectInfo.put("bboxP1X", bbox.get(0));
            objectInfo.put("bboxP1Y", bbox.get(1));
            objectInfo.put("bboxP2X", bbox.get(2));
            objectInfo.put("bboxP2Y", bbox.get(3));
            objectInfo.put("bboxP3X", bbox.get(4));
            objectInfo.put("bboxP3Y", bbox.get(5));
            objectInfo.put("bboxP4X", bbox.get(6));
            objectInfo.put("bboxP4Y", bbox.get(7));
            objectInfo.remove("bbox");
        }
        if(objectInfo.containsKey("geoBbox")){
            List<Double> geoBbox = (List<Double>) objectInfo.get("geoBbox");
            objectInfo.put("geoBboxP1X", geoBbox.get(0));
            objectInfo.put("geoBboxP1Y", geoBbox.get(1));
            objectInfo.put("geoBboxP2X", geoBbox.get(2));
            objectInfo.put("geoBboxP2Y", geoBbox.get(3));
            objectInfo.put("geoBboxP3X", geoBbox.get(4));
            objectInfo.put("geoBboxP3Y", geoBbox.get(5));
            objectInfo.put("geoBboxP4X", geoBbox.get(6));
            objectInfo.put("geoBboxP4Y", geoBbox.get(7));
            objectInfo.remove("geoBbox");
        }
        if(objectInfo.containsKey("imageCenter")){
            List<Integer> imageCenter = (List<Integer>) objectInfo.get("imageCenter");
            objectInfo.put("imageCenterX", imageCenter.get(0));
            objectInfo.put("imageCenterY", imageCenter.get(1));
            objectInfo.remove("imageCenter");
        }
        if(objectInfo.containsKey("geoCenter")){
            List<Double> geoCenter = (List<Double>) objectInfo.get("geoCenter");
            objectInfo.put("geoCenterLongitude", geoCenter.get(0));
            objectInfo.put("geoCenterLatitude", geoCenter.get(1));
            objectInfo.remove("geoCenter");
        }
    }

    // 下划线转驼峰
    public static void toHump(Map<String, Object> target) {
        List<String> objectInfoKeyList = new ArrayList<>(target.keySet());
        for (String key : objectInfoKeyList) {
            String camelKey = MapUtils.underlineToCamel(key);
            // 如果这里是下划线那么就转成驼峰
            if (!target.containsKey(camelKey)) {
                target.put(camelKey, target.get(key));
                target.remove(key);
            }
        }
    }
}