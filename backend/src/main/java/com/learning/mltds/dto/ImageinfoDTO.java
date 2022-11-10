package com.learning.mltds.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

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
}
