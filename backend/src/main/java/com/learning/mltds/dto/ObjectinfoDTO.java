package com.learning.mltds.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ObjectinfoDTO {
    private Integer id;
    private Integer imageId;
    private Integer taskId;
    private Double confidence;
    private String classname;
    private String typename;
    private String shipNumber;
    private Integer isDeleted;

    private Integer imageCenterX;

    private Integer imageCenterY;

    private Double geoCenterLongitude;

    private Double geoCenterLatitude;

    private Integer bboxP1X;

    private Integer bboxP1Y;

    private Integer bboxP2X;

    private Integer bboxP2Y;

    private Integer bboxP3X;

    private Integer bboxP3Y;

    private Integer bboxP4X;

    private Integer bboxP4Y;

    private Double geoBboxP1X;

    private Double geoBboxP1Y;

    private Double geoBboxP2X;

    private Double geoBboxP2Y;

    private Double geoBboxP3X;

    private Double geoBboxP3Y;

    private Double geoBboxP4X;

    private Double geoBboxP4Y;

    private String targetSlicePath;

    private String fixTargetSlicePath;

    private String areaSlicePath;

    private String detectedTime;

}
