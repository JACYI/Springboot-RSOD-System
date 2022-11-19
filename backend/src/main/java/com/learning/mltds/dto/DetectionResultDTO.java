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
//            System.out.println(objectInfo.toString());
            // 拆除字典中的嵌套字典
//        ReqUtils.turnObjectInfoSepcialValue(objectInfo);
//
            ObjectinfoVO objectinfoVO = ObjectinfoVO.builder()
                    // 保存到 list 中
                    .taskId((Integer) objectInfo.get("taskId"))
                    .confidence((Double) objectInfo.get("confidence"))
                    .classname((String) objectInfo.get("classname"))
                    .typename((String) objectInfo.get("typename"))
                    .shipNumber((String) objectInfo.getOrDefault("shipNumber", null))
                    .isLabeled((Boolean) objectInfo.getOrDefault("isLabeled", null))
//                .isDeleted((Integer) objectInfo.get("isDeleted"))
                    .imageCenter((List<Integer>) objectInfo.get("imageCenter"))
                    .geoCenter((List<Double>) objectInfo.get("geoCenter"))
                    .geoCenter((List<Double>) objectInfo.get("geoCenter"))
                    .length(((Number) objectInfo.get("length")).doubleValue())
                    .width(((Number) objectInfo.get("width")).doubleValue())
                    .bbox((List<Integer>) objectInfo.get("bbox"))
                    .geoBbox((List<Double>) objectInfo.get("geoBbox"))
                    .targetSlicePath((String) objectInfo.get("targetSlicePath"))
                    .fixTargetSlicePath((String) objectInfo.get("fixTargetSlicePath"))
                    .areaSlicePath((String) objectInfo.get("areaSlicePath"))
//                .detectedTime(LocalDateTime.parse((String) imageinfoMap.get("detect_time"), localDateTimeFormatter))
                    .detectedTime((String) objectInfo.get("detectedTime"))
                    .build();
            objectInfos.add(objectinfoVO);
        }
    }
}
