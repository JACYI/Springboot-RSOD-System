package com.learning.mltds.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ImageinfoDTO {
    private String filename;
    private String satType;
    private String sensorType;
    private Integer imageWidth;
    private Integer imHeight;
    private String path;
    private Boolean isDetected;
    private Integer taskId;
    private String detectedTime;
}
