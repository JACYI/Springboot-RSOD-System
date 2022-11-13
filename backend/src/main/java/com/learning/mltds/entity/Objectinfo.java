package com.learning.mltds.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.learning.mltds.vo.ObjectinfoVO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author root
 * @since 2022-10-26
 */
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
  @TableName("mltds_objectinfo")
@Builder
public class Objectinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private Double confidence;

    private String classname;

    private String typename;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private String shipNumber;  //弦号

    private String detectedTime;

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

    @TableField(insertStrategy = FieldStrategy.NEVER)
    private String createTime;
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.DEFAULT)
    private String updateTime;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Integer isDeleted;

    private Integer imageId;

    private Integer taskId;

    public ObjectinfoVO convert2VO() {
        return ObjectinfoVO.builder()
                .id(id)
                .confidence(confidence)
                .classname(classname)
                .typename(typename)
                .shipNumber(shipNumber)
                .detectedTime(detectedTime)
                .createTime(createTime)

                .imageCenter(coord2String(imageCenterX, imageCenterY))
                .geoCenter(coord2String(geoCenterLongitude, geoCenterLatitude))

                .bboxP1(coord2String(bboxP1X, bboxP1Y))
                .bboxP2(coord2String(bboxP2X, bboxP2Y))
                .bboxP3(coord2String(bboxP3X, bboxP3Y))
                .bboxP4(coord2String(bboxP4X, bboxP4Y))

                .geoBboxP1(coord2String(geoBboxP1X, geoBboxP1Y))
                .geoBboxP2(coord2String(geoBboxP2X, geoBboxP2Y))
                .geoBboxP3(coord2String(geoBboxP3X, geoBboxP3Y))
                .geoBboxP4(coord2String(geoBboxP4X, geoBboxP4Y))

                .targetSlicePath(targetSlicePath)
                .areaSlicePath(areaSlicePath)
                .fixTargetSlicePath(fixTargetSlicePath)

                .isDeleted(isDeleted)
                .imageId(imageId)
                .taskId(taskId)
                .build();
    }

    public <T> String coord2String(T coordX, T coordY) {
        return '(' + coordX.toString() + ',' + coordY.toString() + ')';
    }
}
