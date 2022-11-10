package com.learning.mltds.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ObjectinfoVO {
    private Double confidence;
    private String classname;
    private String typename;
    private String shipNumber;
    private String detectedTime;
    private String imageCenter;
    private String geoCenter;
    private String bboxP1;
    private String bboxP2;
    private String bboxP3;
    private String bboxP4;
    private String geoBboxP1;
    private String geoBboxP2;
    private String geoBboxP3;
    private String geoBboxP4;
    private String areaSlicePath;
    private String targetSlicePath;

//    private String candidateShipNumbers;
}
