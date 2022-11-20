package com.learning.mltds.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.learning.mltds.utils.MapUtils;
import com.learning.mltds.utils.ReqUtils;
import com.learning.mltds.vo.ObjectinfoVO;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DetectionResultDTO {
    private String imageName;
    private ImageinfoDTO imageInfo;
    private List<ObjectinfoVO> objectInfos;

    public DetectionResultDTO(Map<String, Object> imageinfoMap, List<Map<String, Object>> objectinfosMap) {
        MapUtils.removeEmptyMap(imageinfoMap);  // 清除空值
        ReqUtils.toHump(imageinfoMap);          // 下划线转驼峰
        this.imageInfo = ImageinfoDTO.builder()
                .filename((String) imageinfoMap.get("filename"))
                .satType((String) imageinfoMap.get("satType"))
                .sensorType((String) imageinfoMap.get("sensorType"))
                .imageWidth((Integer) imageinfoMap.get("imageWidth"))
                .imageHeight((Integer) imageinfoMap.get("imageHeight"))
                .path((String) imageinfoMap.get("path"))
                .isDetected((Boolean) imageinfoMap.get("isDetected"))
                .taskId((Integer) imageinfoMap.get("taskId"))
                .detectedTime((String) imageinfoMap.get("detectedTime"))
//                    .detectedTime(LocalDateTime.parse((String) imageinfoMap.get("detect_time"), localDateTimeFormatter))
                .geoCenter(new ArrayList<>(Arrays.asList((Double) imageinfoMap.get("longitude"), (Double) imageinfoMap.get("latitude"))))
                .longitude((Double) imageinfoMap.get("longitude"))
                .latitude((Double) imageinfoMap.get("latitude"))
                .build();

        this.imageName = imageInfo.getFilename();
        this.objectInfos = new ArrayList<>();
        for(Map<String, Object> objectInfo : objectinfosMap) {
            // 清除空值
            MapUtils.removeEmptyMap(objectInfo);
            // 下划线转驼峰
            ReqUtils.toHump(objectInfo);
            // 以下属性需要考虑objectinfoMap不存在对应键的情况
            Integer taskId = null;
            List<Integer> imageCenter = null;
            Double length = null;
            Double width = null;
            List<Integer> bbox = null;
            String targetSlicePath = null;
            String fixTargetSlicePath = null;
            String areaSlicePath = null;
            String detectedTime = LocalDateTime.now().toString();
            if(objectInfo.containsKey("taskId"))
                taskId = (Integer) objectInfo.get("taskId");
            if(objectInfo.containsKey("imageCenter"))
                imageCenter = (List<Integer>) objectInfo.get("imageCenter");
            if(objectInfo.containsKey("length"))
                length = ((Number) objectInfo.get("length")).doubleValue();
            if(objectInfo.containsKey("width"))
                width = ((Number) objectInfo.get("width")).doubleValue();
            if(objectInfo.containsKey("bbox"))
                bbox = (List<Integer>) objectInfo.get("bbox");
            if(objectInfo.containsKey("targetSlicePath"))
                targetSlicePath = (String) objectInfo.get("targetSlicePath");
            if(objectInfo.containsKey("fixTargetSlicePath"))
                fixTargetSlicePath = (String) objectInfo.get("fixTargetSlicePath");
            if(objectInfo.containsKey("areaSlicePath"))
                areaSlicePath = (String) objectInfo.get("areaSlicePath");
            if(objectInfo.containsKey("detectedTime"))
                detectedTime = (String) objectInfo.get("detectedTime");

            ObjectinfoVO objectinfoVO = ObjectinfoVO.builder()
                    // 保存到 list 中
                    .taskId(taskId)
                    .confidence((Double) objectInfo.getOrDefault("confidence", null))
                    .classname((String) objectInfo.get("classname"))
                    .typename((String) objectInfo.get("typename"))
                    .shipNumber((String) objectInfo.getOrDefault("shipNumber", null))
                    .isLabeled((Boolean) objectInfo.getOrDefault("isLabeled", null))
//                .isDeleted((Integer) objectInfo.get("isDeleted"))
                    .imageCenter(imageCenter)
                    .geoCenter((List<Double>) objectInfo.get("geoCenter"))
                    .length(length)
                    .width(width)
                    .bbox(bbox)
                    .geoBbox((List<Double>) objectInfo.get("geoBbox"))
                    .targetSlicePath(targetSlicePath)
                    .fixTargetSlicePath(fixTargetSlicePath)
                    .areaSlicePath(areaSlicePath)
//                .detectedTime(LocalDateTime.parse((String) imageinfoMap.get("detect_time"), localDateTimeFormatter))
                    .detectedTime(detectedTime)
                    .build();
            objectInfos.add(objectinfoVO);
        }
    }
}
