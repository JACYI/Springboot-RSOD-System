package com.learning.mltds.dto;

import com.learning.mltds.utils.MapUtils;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ImageinfoDTO {
    private String filename;
    private String satType;
    private String sensorType;
    private Integer imageWidth;
    private Integer imageHeight;
    private String path;
    private Boolean isDetected;
    private Integer taskId;
    private String detectedTime;
//    有builder不能有显式构造函数
//    public ImageinfoDTO(Map<String, Object> imageinfoMap) {
//        MapUtils.removeEmptyMap(imageinfoMap);  // 清除空值
//
//        this.filename = (String) imageinfoMap.get("filename");
//        this.satType = (String) imageinfoMap.get("sat_type");
//        this.sensorType = (String) imageinfoMap.get("sensor_type");
//        this.imageWidth = (Integer) imageinfoMap.get("image_width");
//        this.imageHeight = (Integer) imageinfoMap.get("im_height");
//        this.path = (String) imageinfoMap.get("path");
//        this.isDetected = (Boolean) imageinfoMap.get("is_detected");
//        this.taskId = (Integer) imageinfoMap.get("task_id");
//        this.detectedTime = (String) imageinfoMap.get("detect_time");
////                    .detectedTime(LocalDateTime.parse((String) imageinfoMap.get("detect_time"), localDateTimeFormatter))
//    }
}
