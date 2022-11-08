package com.learning.mltds.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SearchConditionDTO {
    private String filename;

    private Double confidenceGte;
    private Double confidenceLte;

    private String classname;
    private String typename;

    private String detectedTimeGte;
    private String detectedTimeLte;

    private Integer imageCenterXGte;
    private Integer imageCenterXLte;
    private Integer imageCenterYGte;
    private Integer imageCenterYLte;

    private Double geoCenterLongitudeGte;
    private Double geoCenterLongitudeLte;
    private Double geoCenterLatitudeGte;
    private Double geoCenterLatitudeLte;

    private String identifier;
}
