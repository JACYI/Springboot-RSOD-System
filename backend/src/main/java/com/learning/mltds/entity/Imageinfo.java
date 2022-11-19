package com.learning.mltds.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import com.learning.mltds.dto.ImageinfoDTO;
import com.learning.mltds.vo.ImageinfoVO;
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
@Getter
@Setter
  @TableName("mltds_imageinfo")
public class Imageinfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String filename;

//    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Double latitude;
//    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Double longitude;

    private Integer imageWidth;

    private Integer imageHeight;

//    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Double spatialResolution;
//    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Integer imageBands;
//    @TableField(insertStrategy = FieldStrategy.NEVER)
    private String imageType;

    private String satType;

    private String sensorType;

    private String path;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private LocalDateTime observationTime;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private LocalDateTime productTime;

    private String detectedTime;

//    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Double projectedCoordinateX;
//    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Double projectedCoordinateY;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    private Boolean isDetected;

    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Boolean isProcessed;
    @TableField(insertStrategy = FieldStrategy.NEVER)
    private Integer isDeleted;

    private Integer taskId;

    public ImageinfoDTO convert2DTO(){
        return ImageinfoDTO.builder()
                .filename(filename)
                .satType(satType)
                .sensorType(sensorType)
                .imageWidth(imageWidth)
                .imageHeight(imageHeight)
                .path(path)
                .taskId(taskId)
                .isDetected(isDetected)
                .detectedTime(detectedTime)
                .build();
    }
    public ImageinfoVO convert2VO() {
        return ImageinfoVO.builder()

                .filename(filename)
                .latitude(latitude)
                .longitude(longitude)
                .imageWidth(imageWidth)
                .imageHeight(imageHeight)
                .spatialResolution(spatialResolution)
                .imageBands(imageBands)
                .geoCenter(new ArrayList<>(Arrays.asList(longitude, latitude)))
                .imageType(imageType)
                .satType(satType)
                .sensorType(sensorType)
                .path(path)
                .observationTime(observationTime)
                .productTime(productTime)
                .detectedTime(detectedTime)
                .projectedCoordinateX(projectedCoordinateX)
                .projectedCoordinateY(projectedCoordinateY)
                .updateTime(updateTime)
                .createTime(createTime)
                .isDetected(isDetected)
                .isProcessed(isProcessed)
                .isDeleted(isDeleted)
                .taskId(taskId)
                .build();
    }
}
